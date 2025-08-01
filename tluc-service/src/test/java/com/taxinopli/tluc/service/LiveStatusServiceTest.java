package com.taxinopli.tluc.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.taxinopli.tluc.dto.LiveStatusRequestDto;
import com.taxinopli.tluc.enumeration.DriverStatus;
import com.taxinopli.tluc.mapper.LiveStatusMapper;
import com.taxinopli.tluc.model.LiveStatusModel;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class LiveStatusServiceTest {

    private static final String KEY_PREFIX = "driver:status:";

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOps;

    @InjectMocks
    private LiveStatusService service;

    @BeforeEach
    void setUp() {
        lenient().when(redisTemplate.opsForValue()).thenReturn(valueOps);
    }

    @Nested
    @DisplayName("updateDriverStatus")
    class UpdateDriverStatusTests {

        @Test
        @DisplayName("writes correct key, payload and TTL when rideId is non-null")
        void writesCorrectKeyPayloadAndTtlWithNonNullRideId() {
            Long driverId = 42L;
            DriverStatus status = DriverStatus.AVAILABLE;
            Long rideId = 123L;
            long before = System.currentTimeMillis();

            service.updateDriverStatus(driverId, status, rideId);

            ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
            @SuppressWarnings("unchecked")
            ArgumentCaptor<Map<String, Object>> mapCaptor = ArgumentCaptor.forClass(Map.class);
            ArgumentCaptor<Duration> ttlCaptor = ArgumentCaptor.forClass(Duration.class);

            verify(valueOps).set(
                keyCaptor.capture(),
                mapCaptor.capture(),
                ttlCaptor.capture()
            );

            assertThat(keyCaptor.getValue()).isEqualTo(KEY_PREFIX + driverId);
            Map<String, Object> payload = mapCaptor.getValue();
            assertThat(payload)
                .containsEntry("status", status.toString())
                .containsEntry("rideId", rideId)
                .containsKey("updatedAt");
            long updatedAt = (Long) payload.get("updatedAt");
            long after = System.currentTimeMillis();
            assertThat(updatedAt).isBetween(before, after);
            assertThat(ttlCaptor.getValue()).isEqualTo(Duration.ofHours(24));
        }

        @Test
        @DisplayName("handles null rideId correctly")
        void handlesNullRideIdCorrectly() {
            Long driverId = 43L;
            DriverStatus status = DriverStatus.BUSY;

            service.updateDriverStatus(driverId, status, null);

            @SuppressWarnings("unchecked")
            ArgumentCaptor<Map<String, Object>> mapCaptor = ArgumentCaptor.forClass(Map.class);
            verify(valueOps).set(anyString(), mapCaptor.capture(), any());

            assertThat(mapCaptor.getValue()).containsEntry("rideId", null);
        }

        @Test
        @DisplayName("swallows exceptions from Redis operations")
        void swallowsExceptionsFromRedisOperations() {
            doThrow(new RuntimeException("fail"))
                .when(valueOps).set(anyString(), any(), any(Duration.class));

            assertThatCode(() ->
                service.updateDriverStatus(1L, DriverStatus.AVAILABLE, null)
            ).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("dto overload delegates through mapper")
        void dtoOverloadDelegatesThroughMapper() {
            Long driverId = 7L;
            LiveStatusRequestDto dto = new LiveStatusRequestDto();
            dto.setDriverStatus(DriverStatus.OCCUPIED);
            dto.setDriverRideId(777L);

            LiveStatusModel model = new LiveStatusModel(driverId, DriverStatus.OCCUPIED, 777L);

            try (MockedStatic<LiveStatusMapper> mapper = mockStatic(LiveStatusMapper.class)) {
                mapper.when(() ->
                    LiveStatusMapper.liveStatusRequestDtoToLiveStatusModel(driverId, dto)
                ).thenReturn(model);

                service.updateDriverStatus(driverId, dto);

                verify(valueOps).set(
                    eq(KEY_PREFIX + driverId),
                    argThat(m -> DriverStatus.OCCUPIED.toString()
                        .equals(((Map<?, ?>) m).get("status"))),
                    eq(Duration.ofHours(24))
                );
            }
        }
    }

    @Nested
    @DisplayName("getDriverStatus")
    class GetDriverStatusTests {

        @Test
        @DisplayName("returns empty when key is missing")
        void returnsEmptyWhenKeyIsMissing() {
            Long driverId = 5L;
            when(valueOps.get(KEY_PREFIX + driverId)).thenReturn(null);

            assertThat(service.getDriverStatus(driverId)).isEmpty();
        }

        @Test
        @DisplayName("returns info when a map is stored")
        void returnsInfoWhenMapIsStored() {
            Long driverId = 6L;
            Map<String, Object> stored = new HashMap<>();
            stored.put("status", DriverStatus.BUSY.toString());
            stored.put("rideId", 555L);
            when(valueOps.get(KEY_PREFIX + driverId)).thenReturn(stored);

            Optional<LiveStatusService.DriverStatusInfo> result =
                service.getDriverStatus(driverId);

            assertThat(result)
                .map(LiveStatusService.DriverStatusInfo::status)
                .contains(DriverStatus.BUSY);
            assertThat(result)
                .map(LiveStatusService.DriverStatusInfo::rideId)
                .contains(555L);
        }

        @ParameterizedTest(name = "returns empty for wrong type: {0}")
        @NullSource
        @ValueSource(strings = {"not a map"})
        void returnsEmptyForWrongType(Object value) {
            Long driverId = 7L;
            when(valueOps.get(KEY_PREFIX + driverId)).thenReturn(value);

            assertThat(service.getDriverStatus(driverId)).isEmpty();
        }
    }

    @Nested
    @DisplayName("driverExists")
    class DriverExistsTests {

        @ParameterizedTest(name = "hasKey={0} => exists={1}")
        @CsvSource({
            "true, true",
            "false, false"
        })
        void returnsCorrectExistence(boolean hasKey, boolean expected) {
            Long driverId = 8L;
            when(redisTemplate.hasKey(KEY_PREFIX + driverId)).thenReturn(hasKey);

            assertThat(service.driverExists(driverId)).isEqualTo(expected);
        }

        @Test
        @DisplayName("returns false when Redis throws exception")
        void returnsFalseOnException() {
            Long driverId = 9L;
            when(redisTemplate.hasKey(anyString()))
                .thenThrow(new RuntimeException("boom"));

            assertThat(service.driverExists(driverId)).isFalse();
        }
    }
}

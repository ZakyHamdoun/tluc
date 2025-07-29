package com.taxinopli.tluc.entity;

import com.taxinopli.tluc.enumeration.DriverStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "live_statuses", schema = "tluc")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public final class LiveStatusEntity {
    @Id
    @Column(name = "driver_id")
    private Long driverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "driver_status", nullable = false)
    private DriverStatus driverStatus;
}
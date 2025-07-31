import websocket
import json
import threading
import time
from datetime import datetime
import geocoder

WS_URL = "ws://localhost:8080/ws/websocket"
DESTINATION = "/update/driver-update"
SEND_INTERVAL_SECONDS = 5  # adjust as needed

def build_frame(command, headers=None, body=""):
    lines = [command]
    if headers:
        lines += [f"{k}:{v}" for k, v in headers.items()]
    lines.append("")
    lines.append(body)
    return "\n".join(lines) + "\x00"

def get_current_coordinates():
    g = geocoder.ip('me')
    if g.ok and g.latlng:
        return g.lng, g.lat  # note: longitude first
    else:
        return None, None

def on_open(ws):
    print("🟢 Connected.")

    # Send CONNECT frame
    ws.send(build_frame("CONNECT", {
        "accept-version": "1.2",
        "host": "localhost"
    }))
    print("📨 Sent CONNECT")

    def send_loop():
        while True:
            longitude, latitude = get_current_coordinates()
            if longitude is None or latitude is None:
                print("❌ Unable to get GPS coordinates. Retrying...")
                time.sleep(SEND_INTERVAL_SECONDS)
                continue

            payload = {
                "timestamp": datetime.utcnow().isoformat() + "Z",
                "driverId": 1,
                "coordinate": {
                    "longitude": longitude,
                    "latitude": latitude
                },
                "speed": 30  # optional: add dynamic speed simulation
            }
            ws.send(build_frame("SEND", {
                "destination": DESTINATION,
                "content-type": "application/json"
            }, json.dumps(payload)))
            print(f"📨 Sent DTO with lat/lng = ({latitude}, {longitude}) at {payload['timestamp']}")

            time.sleep(SEND_INTERVAL_SECONDS)

    thread = threading.Thread(target=send_loop)
    thread.daemon = True
    thread.start()

def on_message(ws, message):
    print("📩 Received:", message)

def on_error(ws, error):
    print("❌ Error:", error)

def on_close(ws, code, msg):
    print("🔴 Closed:", code, msg)

if __name__ == "__main__":
    ws = websocket.WebSocketApp(
        WS_URL,
        on_open=on_open,
        on_message=on_message,
        on_error=on_error,
        on_close=on_close,
        subprotocols=["v10.stomp", "v11.stomp", "v12.stomp"]
    )
    ws.run_forever()

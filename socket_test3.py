import websocket
import json
import threading
import time
from datetime import datetime

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

def on_open(ws):
    print("🟢 Connected.")

    # Send CONNECT frame
    ws.send(build_frame("CONNECT", {
        "accept-version": "1.2",
        "host": "localhost"
    }))
    print("📨 Sent CONNECT")

    longitude = 2.294481  # initial longitude

    def send_loop():
        nonlocal longitude
        while True:
            payload = {
                "timestamp": datetime.utcnow().isoformat() + "Z",
                "driverId": 2,
                "coordinate": {
                    "longitude": longitude,
                    "latitude": 48.858370
                },
                "speed": 30
            }
            ws.send(build_frame("SEND", {
                "destination": DESTINATION,
                "content-type": "application/json"
            }, json.dumps(payload)))
            print(f"📨 Sent DTO at {payload['timestamp']} with longitude {longitude}")

            longitude += 0.1  # increase longitude by 0.1 for next send
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


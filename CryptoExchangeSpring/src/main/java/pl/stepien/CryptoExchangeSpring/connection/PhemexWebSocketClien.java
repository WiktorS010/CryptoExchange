package pl.stepien.CryptoExchangeSpring.connection;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;


public class PhemexWebSocketClien extends WebSocketClient {

    // using onOpenListener beacouse of method sendMessage executing before connection establishing
    private Runnable onOpenListener;

    public PhemexWebSocketClien(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected to Phemex WebSocket");
        // onOpenListener in onOpen method couse here connection will be already established
        if (onOpenListener != null) {
            onOpenListener.run();
        }
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed, reason: " + reason);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }
    public void setOnOpenListener(Runnable onOpenListener) {
        this.onOpenListener = onOpenListener;
    }
    public void sendMessage(String message) {
        this.send(message);
    }

}

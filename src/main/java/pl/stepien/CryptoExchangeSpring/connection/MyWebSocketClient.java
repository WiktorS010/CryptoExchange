package pl.stepien.CryptoExchangeSpring.connection;


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import pl.stepien.CryptoExchangeSpring.model.JSONMessages;
import pl.stepien.CryptoExchangeSpring.service.OrderBookService;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Setter
public class MyWebSocketClient extends WebSocketClient {

    private Runnable onOpenListener;
    private ScheduledExecutorService pingScheduler;
    private OrderBookService orderBookService;

    public MyWebSocketClient(URI serverUri, OrderBookService orderBookService) {
        super(serverUri);
        this.orderBookService = orderBookService;
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("Connected to Phemex WebSocket");
        startPing();
        if (onOpenListener != null) {
            onOpenListener.run();
        }

    }

    @Override
    public void onMessage(String message) {
        log.info("Received message from Phemex: " + message);
        orderBookService.processMessage(message);
        if (message.contains("pong")) {
            log.info("Pong received from Phemex");
        }
        orderBookService.printOrderBook();

    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info("Phemex connection closed, reason: " + reason);
        stopPing();

    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }


    public void sendMessage(String message) {
        log.info("My message to Phemex " + message);
        this.send(message);
    }

    public void startPing() {
        pingScheduler = Executors.newSingleThreadScheduledExecutor();
        pingScheduler.scheduleAtFixedRate(() -> {
            try {
                log.info("Sending Ping to phemex");
                this.send(JSONMessages.createPhemexPingMessage().toString());
            } catch (Exception e) {
                log.error("Failed to send Ping", e);
            }
        }, 0, 30, TimeUnit.SECONDS);
    }

    public void stopPing() {
        if (pingScheduler != null && !pingScheduler.isShutdown()) {
            pingScheduler.shutdown();
            log.info("Ping scheduler stopped");
        }
    }

}

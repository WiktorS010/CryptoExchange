package pl.stepien.CryptoExchangeSpring.connection;


import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import pl.stepien.CryptoExchangeSpring.model.ExchangeType;
import pl.stepien.CryptoExchangeSpring.service.JSONMessagesService;
import pl.stepien.CryptoExchangeSpring.service.ExchangeOrderBookService;

import java.net.URI;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Setter
@Getter
public class MyWebSocketClient extends WebSocketClient {

    private Runnable onOpenListener;
    private ScheduledExecutorService pingScheduler;
    private ExchangeOrderBookService exchangeOrderBookService;
    private ExchangeType exchangeType;

    public MyWebSocketClient(URI serverUri, ExchangeOrderBookService exchangeOrderBookService) {
        super(serverUri);
        this.exchangeOrderBookService = exchangeOrderBookService;

        //Checking which serverUri
        if (serverUri.toString().contains("phemex")) {
            this.exchangeType = ExchangeType.PHEMEX;
        } else if (serverUri.toString().contains("bybit")) {
            this.exchangeType = ExchangeType.BYBIT;
        } else if (serverUri.toString().contains("binance")) {
            this.exchangeType = ExchangeType.BINANCE;
        } else {
            throw new IllegalArgumentException("Unsupported WebSocket URI: " + serverUri);
        }
    }


    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("Connected to " + exchangeType + " WebSocket");
        startPing();
        if (onOpenListener != null) {
            onOpenListener.run();
        }
    }


    @Override
    public void onMessage(String message) {
        log.info("Received message from " + exchangeType + ": " + message);

        if (message.contains("pong") || message.contains("200")) {
            log.info("Pong received from: " + exchangeType);
        }

        switch (exchangeType) {
            case PHEMEX:
                exchangeOrderBookService.processPhemexMessageToMap(message, exchangeType);
                exchangeOrderBookService.printOrderBook(exchangeType);
                break;
            case BYBIT:
                exchangeOrderBookService.processBybitMessageToMap(message, exchangeType);
                exchangeOrderBookService.printOrderBook(exchangeType);
                break;
            case BINANCE:
                exchangeOrderBookService.processBinanceMessageToMap(message, exchangeType);
                exchangeOrderBookService.printOrderBook(exchangeType);
                break;
            default:
                throw new IllegalArgumentException("Unknown exchange: " + exchangeType);
        }
    }


    @Override
    public void onClose(int code, String reason, boolean remote) {
        log.info(exchangeType + " connection closed, reason: " + reason);
        stopPing();
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }


    public void sendMessage(String message) {
        log.info("My message to exchange " + message);
        this.send(message);
    }

    public void startPing() {
        pingScheduler = Executors.newSingleThreadScheduledExecutor();
        pingScheduler.scheduleAtFixedRate(() -> {
            try {
                if (exchangeType == ExchangeType.PHEMEX) {
                    this.send(JSONMessagesService.createPhemexPingMessage().toString());
                    log.info("Sending Ping to " + exchangeType);
                } else if (exchangeType == ExchangeType.BYBIT) {
                    this.send(JSONMessagesService.createBybitPingMessage().toString());
                    log.info("Sending Ping to " + exchangeType);
                } else if (exchangeType == ExchangeType.BINANCE) {
                    this.send(JSONMessagesService.createBinancePingMessage().toString());
                    log.info("Sending Ping to " + exchangeType);
                }
            } catch (Exception e) {
                log.error("Failed to send Ping", e);
            }
        }, 15, 30, TimeUnit.SECONDS);
    }

    public void stopPing() {
        if (pingScheduler != null && !pingScheduler.isShutdown()) {
            pingScheduler.shutdown();
            log.info("Ping scheduler stopped");
        }
    }
}

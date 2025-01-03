package pl.stepien.CryptoExchangeSpring;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import pl.stepien.CryptoExchangeSpring.service.ConnectionService;

import java.net.URI;

@SpringBootApplication
public class CryptoExchangeSpringApplication {

    private final ConnectionService connectionService;
    private final URI phemexWebSocketUri;
    private final URI bybitWebSocketUri;
    private final URI binanceWebSocketUri;

    public CryptoExchangeSpringApplication(ConnectionService connectionService,
                                           @Qualifier("phemexWebSocketUri") URI phemexWebSocketUri,
                                           @Qualifier("bybitWebSocketUri") URI bybitWebSocketUri,
                                           @Qualifier("binanceWebSocketUri") URI binanceWebSocketUri)
    {
        this.connectionService = connectionService;
        this.phemexWebSocketUri = phemexWebSocketUri;
        this.bybitWebSocketUri = bybitWebSocketUri;
        this.binanceWebSocketUri = binanceWebSocketUri;
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CryptoExchangeSpringApplication.class, args);
    }

    @PostConstruct
    public void startConnections(){
        // Connect and subscribe to Phemex
        connectionService.connectAndSubscribe("BTCUSD", phemexWebSocketUri);

        // Connect and subscribe to Bybit
//        connectionService.connectAndSubscribe("BTCUSDT", bybitWebSocketUri);

        //Connect and subscribe to Binance
//        connectionService.connectAndSubscribe("BTCUSDT", binanceWebSocketUri);
    }
}



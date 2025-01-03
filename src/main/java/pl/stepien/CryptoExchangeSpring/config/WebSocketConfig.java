package pl.stepien.CryptoExchangeSpring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.net.URI;

@Configuration
public class WebSocketConfig {
    @Bean
    public URI phemexWebSocketUri() {
        return URI.create("wss://testnet-api.phemex.com/ws");
    }

    @Bean
    public URI bybitWebSocketUri() {
        return URI.create("wss://stream.bybit.com/v5/public/spot");
    }

    @Bean
    public URI binanceWebSocketUri() {
        return URI.create("wss://stream.binance.com:9443/ws");
    }
}

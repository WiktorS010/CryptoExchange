package pl.stepien.CryptoExchangeSpring.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.stepien.CryptoExchangeSpring.model.ExchangeType;
import pl.stepien.CryptoExchangeSpring.model.OrderBook;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Slf4j
public class ExchangeOrderBookServiceTest {

    private ExchangeOrderBookService exchangeOrderBookService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        exchangeOrderBookService = new ExchangeOrderBookService();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testProcessBinanceMessageToMap_snapshot() throws Exception {
        // Initial snapshot message from the exchange
        String snapshotMessage = """
                {
                    "e": "depthUpdate",
                    "b": [
                        ["101.5", "5.0"],
                        ["101.0", "10.0"]
                    ],
                    "a": [
                        ["102.0", "8.0"],
                        ["102.5", "15.0"]
                    ]
                }
                """;
        // Simulate processing the snapshot
        exchangeOrderBookService.processBinanceMessageToMap(snapshotMessage, ExchangeType.BINANCE);

        // Verify the initial snapshot
        OrderBook orderBook = exchangeOrderBookService.getOrderBooks().get(ExchangeType.BINANCE);
        assertEquals(2, orderBook.getBids().size(), "Bids size should be 2 after snapshot");
        assertEquals(2, orderBook.getAsks().size(), "Asks size should be 2 after snapshot");

        assertEquals(5.0, orderBook.getBids().get(101.5), "Bid at 101.5 should have quantity 5.0");
        assertEquals(8.0, orderBook.getAsks().get(102.0), "Ask at 102.0 should have quantity 8.0");
    }

    @Test
    void testProcessBinanceMessageToMap_update() throws Exception {
        // Initial snapshot
        String snapshotMessage = """
                {
                    "e": "depthUpdate",
                    "b": [
                        ["101.5", "5.0"],
                        ["101.0", "10.0"]
                    ],
                    "a": [
                        ["102.0", "8.0"],
                        ["102.5", "15.0"]
                    ]
                }
                """;

        // Process snapshot
        exchangeOrderBookService.processBinanceMessageToMap(snapshotMessage, ExchangeType.BINANCE);

        // Update message
        String updateMessage = """
                {
                    "e": "depthUpdate",
                    "b": [
                        ["101.0", "12.0"],
                        ["100.5", "6.0"]
                    ],
                    "a": [
                        ["102.0", "0.0"],
                        ["103.0", "10.0"]
                    ]
                }
                """;
        // Process update
        exchangeOrderBookService.processBinanceMessageToMap(updateMessage, ExchangeType.BINANCE);

        // Verify after the update
        OrderBook orderBook = exchangeOrderBookService.getOrderBooks().get(ExchangeType.BINANCE);
        assertEquals(3, orderBook.getBids().size(), "Bids size should be 3 after update");
        assertEquals(2, orderBook.getAsks().size(), "Asks size should be 2 after update");

        // Check specific values
        assertEquals(12.0, orderBook.getBids().get(101.0), "Bid at 101.0 should have updated quantity 12.0");
        assertEquals(6.0, orderBook.getBids().get(100.5), "Bid at 100.5 should have quantity 6.0");
        assertNull(orderBook.getAsks().get(102.0), "Ask at 102.0 should be removed");
        assertEquals(10.0, orderBook.getAsks().get(103.0), "Ask at 103.0 should have quantity 10.0");
    }

    @Test
    void testProcessBinanceMessageToMap_updateV2() throws Exception {
        // Initial snapshot
        String snapshotMessage = """
                {
                    "e": "depthUpdate",
                    "b": [
                        ["100.5", "5.0"],
                        ["101.0", "10.0"]
                    ],
                    "a": [
                        ["100.0", "8.0"],
                        ["101.5", "7.0"]
                    ]
                }
                """;

        // Process snapshot
        exchangeOrderBookService.processBinanceMessageToMap(snapshotMessage, ExchangeType.BINANCE);

        // Update message
        String updateMessage = """
                {
                    "e": "depthUpdate",
                    "b": [
                        ["101.0", "12.0"],
                        ["101.5", "6.0"],
                        ["102.0", "4.0"],
                        ["103.0", "12.0"]
                    ],
                    "a": [
                        ["102.0", "1.0"],
                        ["103.0", "10.0"],
                        ["104.0", "2.0"],
                        ["104.5", "11.0"],
                        ["105.0", "4.0"],
                        ["105.5", "20.0"]
                    ]
                }
                """;
        // Process update
        exchangeOrderBookService.processBinanceMessageToMap(updateMessage, ExchangeType.BINANCE);

        // Verify after the update
        OrderBook orderBook = exchangeOrderBookService.getOrderBooks().get(ExchangeType.BINANCE);
        assertEquals(5, orderBook.getBids().size(), "Bids size should be 5 after update");
        assertEquals(8, orderBook.getAsks().size(), "Asks size should be 8 after update");

        // Check specific values
        assertEquals(12.0, orderBook.getBids().get(101.0), "Bid at 101.0 should have updated quantity 12.0");
    }

    @Test
    void testProcessBybitMessageToMap() throws Exception {
        // Initial snapshot
        String snapshotMessage = """
                {
                    "type": "snapshot",
                    "data":{
                    "b": [
                        ["100.0", "0"],
                        ["101.0", "10.0"],
                        ["102.0", "10.0"]
                    ],
                    "a": [
                        ["100.0", "8.0"],
                        ["101.0", "7.0"],
                        ["102.0", "7.0"]
                    ]
                   }
                }
                """;

        // Process snapshot
        exchangeOrderBookService.processBybitMessageToMap(snapshotMessage, ExchangeType.BYBIT);

        // Update message
        String updateMessage = """
                {
                    "type": "delta",
                    "data":{
                    "b": [
                        ["102.0", "12.0"],
                        ["103.0", "6.0"],
                        ["104.0", "4.0"],
                        ["105.0", "12.0"]
                    ],
                    "a": [
                        ["102.0", "0"],
                        ["103.0", "0"],
                        ["104.0", "2.0"],
                        ["105.0", "11.0"],
                        ["106.0", "4.0"],
                        ["107.0", "20.0"]
                    ]
                   }
                }
                """;
        // Process update
        exchangeOrderBookService.processBybitMessageToMap(updateMessage, ExchangeType.BYBIT);

        // Verify after the update
        OrderBook orderBook = exchangeOrderBookService.getOrderBooks().get(ExchangeType.BYBIT);
        assertEquals(5, orderBook.getBids().size(), "Bids size should be 5 after update");
        assertEquals(6, orderBook.getAsks().size(), "Asks size should be 8 after update");

        // Check specific values
        assertEquals(10.0, orderBook.getBids().get(101.0), "Bid at 101.0 should have quantity 10.0");
        assertNull(orderBook.getBids().get(100.0), "Bid at 101.0 should have quantity 10.0");
        assertNull(orderBook.getAsks().get(102.0), "Bid at 101.0 should have quantity 10.0");
        assertNull(orderBook.getAsks().get(103.0), "Bid at 101.0 should have quantity 10.0");
        assertEquals(11.0, orderBook.getAsks().get(105.0), "Bid at 101.0 should have quantity 10.0");
        assertEquals(4.0, orderBook.getAsks().get(106.0), "Bid at 101.0 should have quantity 10.0");
    }
}
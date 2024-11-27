package pl.stepien.CryptoExchangeSpring.util;

import pl.stepien.CryptoExchangeSpring.model.Order;
import pl.stepien.CryptoExchangeSpring.model.OrderBook;

import java.util.List;

public interface OrderBookHandler {
void updateOrderBook(OrderBook orderBook);
void onOrderAddad(Order order, String bidOrAsk);
void onOrderRemoved(Order order);
OrderBook getCurrentOrders();
}

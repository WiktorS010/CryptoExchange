package pl.stepien.CryptoExchangeSpring.util;

import pl.stepien.CryptoExchangeSpring.model.MyOrder;
import pl.stepien.CryptoExchangeSpring.model.MyOrderBook;

import java.util.List;

public interface OrderBookHandler {
void updateOrderBook(MyOrderBook orderBook);
void onOrderAddad(MyOrder order);
void onOrderRemoved(MyOrder order);
List<MyOrder> getCurrentOrders();
}

package pl.stepien.CryptoExchangeSpring.util;

import pl.stepien.CryptoExchangeSpring.model.Order;
import pl.stepien.CryptoExchangeSpring.model.OrderBook;

import java.util.ArrayList;
import java.util.List;

public class OrderBookManager implements OrderBookHandler {
    private final OrderBook orderBook;

    public OrderBookManager(OrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void updateOrderBook(OrderBook nweOrderBook) {
        orderBook.getOrders().clear();
        orderBook.getOrders().addAll(nweOrderBook.getOrders());
    }

    @Override
    public void onOrderAddad(Order order) {
        orderBook.getOrders().add(order);

    }

    @Override
    public void onOrderRemoved(Order order) {
        orderBook.getOrders().remove(order);
    }

    @Override
    public List<Order> getCurrentOrders() {
        return new ArrayList<>(orderBook.getOrders());
    }
}

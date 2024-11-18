package pl.stepien.CryptoExchangeSpring.util;


import pl.stepien.CryptoExchangeSpring.model.MyOrderBook;

import java.util.ArrayList;

public class OrderBookManager implements OrderBookHandler {
    private final MyOrderBook orderBook;

    public OrderBookManager(MyOrderBook orderBook) {
        this.orderBook = orderBook;
    }

    @Override
    public void updateOrderBook(MyOrderBook nweOrderBook) {
        orderBook.getOrders().clear();
        orderBook.getOrders().addAll(nweOrderBook.getOrders());
    }

    @Override
    public void onOrderAddad(MyOrder order) {
        orderBook.getOrders().add(order);

    }

    @Override
    public void onOrderRemoved(MyOrder order) {
        orderBook.getOrders().remove(order);
    }

    @Override
    public List<MyOrder> getCurrentOrders() {
        return new ArrayList<>(orderBook.getOrders());
    }
}

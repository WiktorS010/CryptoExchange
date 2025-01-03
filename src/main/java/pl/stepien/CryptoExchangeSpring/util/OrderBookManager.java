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
    public void updateOrderBook(OrderBook newOrderBook) {
    }

    @Override
    public void onOrderAddad(Order order, String bidOrAsk) {
    }

    @Override
    public void onOrderRemoved(Order order) {
    }

    @Override
    public OrderBook getCurrentOrders() {
        return orderBook;
    }
}

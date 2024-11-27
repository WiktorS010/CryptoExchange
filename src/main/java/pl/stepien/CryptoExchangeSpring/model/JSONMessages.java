package pl.stepien.CryptoExchangeSpring.model;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONMessages {

    public static JSONObject createPhemexPingMessage(){
        JSONObject pingMessage = new JSONObject();
        JSONArray paramsArray = new JSONArray();
        pingMessage.put("id", 0);
        pingMessage.put("method", "server.ping");
        pingMessage.put("params", paramsArray);
        return pingMessage;
    }
    public static JSONObject createPhemexSubscribeMessage(String symbol) {
        JSONObject subscribeMessage = new JSONObject();
        subscribeMessage.put("id", 1);
        subscribeMessage.put("method", "orderbook.subscribe");
        JSONArray paramsArray = new JSONArray();
        paramsArray.put(symbol);
        subscribeMessage.put("params", paramsArray);
        return subscribeMessage;
    }

    public static JSONObject createCoinbaseSubscribeMessage(String symbol) {
        JSONObject subscribeMessage = new JSONObject();
        return subscribeMessage;
    }

}

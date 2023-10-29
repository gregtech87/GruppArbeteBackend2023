package com.Grupparbete.API;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class CurrencyConverter {

    public CurrencyConverter() {
    }

    public static double SekToRequestedCurrency(double sek, String requestedCurrency) throws IOException {

        String url_str = "https://open.er-api.com/v6/latest/SEK";

        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        Map map = new Gson().fromJson(jsonobj.get("rates"), Map.class);
        double currency = (double) map.get(requestedCurrency);
        double calculatedCurrency = sek * currency;

        return Math.round(calculatedCurrency);
    }
}

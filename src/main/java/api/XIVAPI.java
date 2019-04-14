package api;

import com.google.common.base.Joiner;
import models.MarketData;
import models.MarketResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utilities.Mapper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class XIVAPI {

    private static final List<String> API_KEYS = Arrays.asList(
            "ecff380e759d4a048b95c75ef069f1953893e2cca6ea48269c0cc808cf54f0ad",
            "b1452611432f4b8eb9bfbf38",
            "cbd02351c9d84595b5be1b07992e1820a8b046f7ef5f49e4b95a801762461d77");

    private static final Random rng = new Random();

    private static final String API_KEY = "?key=";
    private static final String BASE_URL = "https://www.xivapi.com";
    private static final String MARKET_ENDPOINT = "/market";
    private static final String ITEM_ENDPOINT = "/items";
    private static final String ITEM_IDS = "&ids=";
    private static final String SERVER_LIST_ENDPOINT = "servers=";
    private static final OkHttpClient client = new OkHttpClient();

    private static final long CLIENT_COOLDOWN_IN_MS = 100;

    private static long timeSinceLastCall = 0;

    public static MarketData getMarketResponse(long itemId, String server) throws Exception {
        String url = BASE_URL + MARKET_ENDPOINT + "/" + server + ITEM_ENDPOINT + "/" + itemId + API_KEY + API_KEYS.get(rng.nextInt(API_KEYS.size()));
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = callAPI(request);
        return Mapper.getSingleItemMarketDataFromString(response.body().string());
    }

    public static List<MarketResponse> getMarketResponse(List<Long> itemIds, List<String> servers) throws Exception{
        String url = BASE_URL + MARKET_ENDPOINT + ITEM_ENDPOINT + "?" + SERVER_LIST_ENDPOINT + Joiner.on(",").join(servers) + ITEM_IDS + Joiner.on(",").join(itemIds) + API_KEY + API_KEYS.get(rng.nextInt(API_KEYS.size()));
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = callAPI(request);
        return Mapper.getMultipleItemMarketDataFromString(response.body().string());
    }

    private static Response callAPI (Request request) throws Exception{
        while((System.currentTimeMillis() - timeSinceLastCall) < CLIENT_COOLDOWN_IN_MS) { }
        System.out.println(request.url());
        timeSinceLastCall = System.currentTimeMillis();
        return client.newCall(request).execute();
    }
}

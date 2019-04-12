package api;

import com.google.common.base.Joiner;
import models.MarketData;
import models.MarketResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utilities.Mapper;

import java.util.List;

public class XIVAPI {

    private static final String API_KEY = "?key=b1452611432f4b8eb9bfbf38";
    private static final String BASE_URL = "https://www.xivapi.com";
    private static final String MARKET_ENDPOINT = "/market";
    private static final String ITEM_ENDPOINT = "/items";
    private static final String ITEM_IDS = "&ids=";
    private static final String SERVER_LIST_ENDPOINT = "servers=";
    private static final OkHttpClient client = new OkHttpClient();

    private static final long CLIENT_COOLDOWN_IN_MS = 500;

    private static long timeSinceLastCall = 0;

    public static MarketData getMarketResponse(long itemId, String server) throws Exception {
        String url = BASE_URL + MARKET_ENDPOINT + "/" + server + ITEM_ENDPOINT + "/" + itemId + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = callAPI(request);
        return Mapper.getSingleItemMarketDataFromString(response.body().string());
    }

    public static List<MarketResponse> getMarketResponse(List<Long> itemIds, List<String> servers) throws Exception{
        String url = BASE_URL + MARKET_ENDPOINT + ITEM_ENDPOINT + "?" + SERVER_LIST_ENDPOINT + Joiner.on(",").join(servers) + ITEM_IDS + Joiner.on(",").join(itemIds) + API_KEY;
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

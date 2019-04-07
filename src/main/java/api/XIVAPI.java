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

    public static MarketData getMarketResponse(long itemId, String server) throws Exception {
        String url = BASE_URL + MARKET_ENDPOINT + "/" + server + ITEM_ENDPOINT + "/" + itemId + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return Mapper.getSingleItemMarketDataFromString(response.body().string());
    }

    //https://www.xivapi.com/market/items?servers=faerie&ids=24250,24252,19943,24256,14,17&key=b1452611432f4b8eb9bfbf38
    public static List<MarketResponse> getMarketResponse(List<Long> itemIds, List<String> servers) throws Exception{
        String url = BASE_URL + MARKET_ENDPOINT + ITEM_ENDPOINT + "?" + SERVER_LIST_ENDPOINT + Joiner.on(",").join(servers) + ITEM_IDS + Joiner.on(",").join(itemIds) + API_KEY;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        return Mapper.getMultipleItemMarketDataFromString(response.body().string());
    }
}

import api.XIVAPI;
import database.ItemDatabase;
import database.RecipeDatabase;
import models.*;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utilities.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Driver {

    public static void main(String[] args) {
        String itemToQuery = "shakshouka";

        MarketData marketData = new MarketData();
        List<MarketResponse> marketResponseList = new ArrayList<>();
        ItemData itemData = new ItemData();
        RecipeData recipeData = new RecipeData();
        List<String> servers = Arrays.asList("Faerie");

        try {
            itemData = ItemDatabase.get(itemToQuery);
            recipeData = RecipeDatabase.get(itemData.getItemId());
            List<Long> ingredientList = new ArrayList<Long>();
            recipeData.getIngredientList().stream().forEach(i -> ingredientList.add(i.getItemId()));
            marketResponseList = XIVAPI.getMarketResponse(ingredientList, servers);
            marketData = XIVAPI.getMarketResponse(itemData.getItemId(), "faerie");
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String server : servers) {
            long totalCost = 0;
            for (MarketResponse marketResponse : marketResponseList) {
                if (server.equals(marketResponse.getServerAndMarketDataHolder().getServer())) {
                    long amountNeeded = 0;
                    for (Ingredient ingredient : recipeData.getIngredientList()) {
                        if (ingredient.getItemId() == marketResponse.getServerAndMarketDataHolder().getMarketData().getItemID()) {
                            amountNeeded = ingredient.getAmount();
                        }
                    }
                    totalCost += marketResponse.getServerAndMarketDataHolder().getMarketData().getPrices().get(0).getPricePerUnit() * amountNeeded;
                }
            }
            System.out.println("It costs " + marketData.getPrices().get(0).getPricePerUnit() + " to buy a " + itemData.getSingularName() + ". It costs " + totalCost + " to craft " + recipeData.getAmount());
        }
    }
}

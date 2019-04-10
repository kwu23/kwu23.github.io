import api.XIVAPI;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import database.RecipeDatabase;
import models.*;
import rendering.Model;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

public class Driver {

    private static final NumberFormat nf = NumberFormat.getInstance(Locale.US);

    private static final Map<String, Long> profits = new HashMap<>();

    public static void main(String[] args) {
        try {
            File htmlOutput = new File(".");

            HashMap<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("name", "Mustache");
            scopes.put("model", new Model("Perfect!"));

            PrintWriter writer = new PrintWriter("index.html");
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile("index.mustache");
            mustache.execute(writer, scopes);
            writer.flush();


            //writer.close();
            //getCraftingPrice("oasis half partition");
            //getCraftingPrices(Arrays.asList("molybdenum pliers", "molybdenum war axe", "molybdenum tassets of fending", "molybdenum plate belt of maiming"));
            //getCraftingPrices(Stream.concat(ItemDatabase.getAllContaining("obi").stream(), ItemDatabase.getAllContaining("craftsing").stream()).collect(Collectors.toList()));

            Map<String, Long> sortedProfits = profits.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            //System.out.println("PROFITS");
            for (String key : sortedProfits.keySet()) {
                System.out.println(key);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void getCraftingPrices(List<ItemData> itemsToQuery) {
        for (ItemData itemData : itemsToQuery) {
            getCraftingPrice(itemData);
        }
    }

    static void getCraftingPrice(ItemData itemData) {
        MarketData marketData = new MarketData();
        List<MarketResponse> marketResponseList = new ArrayList<>();
        RecipeData recipeData = new RecipeData();
        List<String> servers = Arrays.asList("Faerie");

        try {
            recipeData = RecipeDatabase.get(itemData.getItemId());
            List<Long> ingredientList = new ArrayList<>();
            if (recipeData == null || recipeData.getIngredientList() == null || recipeData.getIngredientList().isEmpty()) {
                return;
            }
            recipeData.getIngredientList().stream().forEach(i -> ingredientList.add(i.getItemId()));
            marketResponseList = XIVAPI.getMarketResponse(ingredientList, servers);
            marketData = XIVAPI.getMarketResponse(itemData.getItemId(), "faerie");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (marketData == null || marketData.getPrices() == null || marketData.getPrices().isEmpty()) {
            return;
        }

        System.out.println("--------------------------------------------");
        for (String server : servers) {
            long totalCost = 0;
            for (MarketResponse marketResponse : marketResponseList) {
                if (marketResponse == null || marketResponse.getServerAndMarketDataHolder() == null || marketResponse.getServerAndMarketDataHolder().getMarketData() == null || marketResponse.getServerAndMarketDataHolder().getMarketData().getPrices() == null || marketResponse.getServerAndMarketDataHolder().getMarketData().getPrices().isEmpty()) {
                    return;
                }
                if (server.equals(marketResponse.getServerAndMarketDataHolder().getServer())) {
                    long amountNeeded = 0;
                    for (Ingredient ingredient : recipeData.getIngredientList()) {
                        if (ingredient.getItemId() == marketResponse.getServerAndMarketDataHolder().getMarketData().getItemID()) {
                            amountNeeded = ingredient.getAmount();
                        }
                    }
                    long ingredientCost = marketResponse.getServerAndMarketDataHolder().getMarketData().getPrices().get(0).getPricePerUnit() * amountNeeded;
                    System.out.println(amountNeeded + "x " + marketResponse.getServerAndMarketDataHolder().getMarketData().getItem().getName() + " will cost " + nf.format(ingredientCost));
                    totalCost += ingredientCost;
                }
            }
            long nqCost = marketData.getPrices().get(0).getPricePerUnit();
            if (marketData.getPrices().stream().anyMatch(e -> e.isIsHQ())) {
                long hqCost = marketData.getPrices().stream().filter(e -> e.isIsHQ()).findFirst().get().getPricePerUnit();
                long profit = (hqCost * recipeData.getAmount()) - (totalCost);
                long historyProfit = (marketData.getAverageHQHistory() * recipeData.getAmount()) - totalCost;
                String textToPrint = "It costs " + nf.format(nqCost) + " to buy the cheapest " + itemData.getSingularName() + " and " + nf.format(hqCost) + " for HQ. It costs " + nf.format(totalCost) + " to craft " + nf.format(recipeData.getAmount()) + " for a profit of " + profit + ". On average, this item has been sold for " + nf.format(marketData.getAverageHQHistory()) + " and was sold " + (marketData.getAmountSoldLastWeek() >= 100 ? "at least " : "") + marketData.getAmountSoldLastWeek() + " time(s) in the last week which would be a profit of " + historyProfit;
                profits.put(textToPrint, historyProfit);
                System.out.println(textToPrint);
            } else {
                long profit = (nqCost * recipeData.getAmount()) - (totalCost);
                long historyProfit = (marketData.getAverageHistory() * recipeData.getAmount()) - totalCost;
                String textToPrint = "It costs " + nf.format(nqCost) + " to buy the cheapest " + itemData.getSingularName() + ". It costs " + nf.format(totalCost) + " to craft " + nf.format(recipeData.getAmount()) + " for a profit of " + profit + ". On average, this item has been sold for " + nf.format(marketData.getAverageHistory()) + " and was sold " + (marketData.getAmountSoldLastWeek() >= 100 ? "at least " : "") + marketData.getAmountSoldLastWeek() + " time(s) in the last week which would be a profit of " + historyProfit;
                profits.put(textToPrint, historyProfit);
                System.out.println(textToPrint);
            }

        }
        System.out.println("--------------------------------------------");
    }
}

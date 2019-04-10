import api.XIVAPI;
import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.google.common.collect.Lists;
import database.ItemDatabase;
import database.RecipeDatabase;
import models.*;
import rendering.Model;

import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Driver {

    private static final NumberFormat nf = NumberFormat.getInstance(Locale.US);

    private static final Map<String, Long> profits = new HashMap<>();
    private static final List<Model> models = new ArrayList<>();
    private static final Map<Long, SimpleItemObject> items = new HashMap<>();

    public static void main(String[] args) {
        try {
            File htmlOutput = new File(".");
            //writer.close();
            //getCraftingPrice("oasis half partition");
            //getCraftingPrices(Arrays.asList("molybdenum pliers", "molybdenum war axe", "molybdenum tassets of fending", "molybdenum plate belt of maiming"));
            //getCraftingPrices(Stream.concat(ItemDatabase.getAllContaining("rakshasa").stream(), ItemDatabase.getAllContaining("asfgadccs").stream()).collect(Collectors.toList()));
            //getCraftingPrices(RecipeDatabase.getAllCraftableItems());

            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a z");
            Date dt = new Date();
            String lastUpdatedOn = sdf.format(dt);

            createFaerieItemDatabaseHtml();

            HashMap<String, Object> scopes = new HashMap<String, Object>();
            scopes.put("lastUpdatedOn", lastUpdatedOn);
            scopes.put("models", models);

            PrintWriter writer = new PrintWriter("index.html");
            MustacheFactory mf = new DefaultMustacheFactory();
            Mustache mustache = mf.compile("index.mustache");
            mustache.execute(writer, scopes);
            writer.flush();

            Map<String, Long> sortedProfits = profits.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            //System.out.println("PROFITS");
            for (String key : sortedProfits.keySet()) {
                System.out.println(key);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void createFaerieItemDatabaseHtml() throws Exception {
        Set<Long> idsToQuery = new HashSet<>();
        for (RecipeData recipeData : RecipeDatabase.getDatabase()) {
            if (recipeData != null && recipeData.getIngredientList() != null && !recipeData.getIngredientList().isEmpty() && recipeData.getItemId() > 0) {
                idsToQuery.add(recipeData.getItemId());
                for (Ingredient ingredient : recipeData.getIngredientList()) {
                    idsToQuery.add(ingredient.getItemId());
                }
            }
        }

        List<List<Long>> idBatches = Lists.partition(Lists.newArrayList(idsToQuery), 99);

        int numberOfAPICalls = 0;
        for (List<Long> batch : idBatches) {
            List<MarketResponse> apiMarketResponse = XIVAPI.getMarketResponse(batch, Lists.newArrayList("Faerie"));
            for (MarketResponse marketResponse : apiMarketResponse) {
                if (marketResponse == null || marketResponse.getServerAndMarketDataHolder() == null || marketResponse.getServerAndMarketDataHolder().getMarketData() == null || marketResponse.getServerAndMarketDataHolder().getMarketData().getPrices() == null || marketResponse.getServerAndMarketDataHolder().getMarketData().getPrices().isEmpty()) {
                    continue;
                }
                items.put(marketResponse.getServerAndMarketDataHolder().getMarketData().getItemID(), new SimpleItemObject(
                        marketResponse.getServerAndMarketDataHolder().getMarketData().getItemID(),
                        marketResponse.getServerAndMarketDataHolder().getMarketData().getItem().getName(),
                        marketResponse.getServerAndMarketDataHolder().getMarketData().getHistory(),
                        marketResponse.getServerAndMarketDataHolder().getMarketData().getPrices()));
            }
            System.out.println("Called API " + ++numberOfAPICalls + " times");
        }

        for (RecipeData recipeData : RecipeDatabase.getDatabase()) {
            long itemId = recipeData.getItemId();
            if (items.get(itemId) == null || !items.get(itemId).isValid()) {
                continue;
            }
            String name = ItemDatabase.get(recipeData.getItemId()).getName();
            long cheapest = items.get(itemId).getCheapestAvailable();
            long cheapestHQ = items.get(itemId).getCheapestHQAvailable();
            long priceToCraft = 0;
            boolean ingredientDataMissing = false;
            for (Ingredient ingredient : recipeData.getIngredientList()) {
                if (items.get(ingredient.getItemId()) == null || !items.get(ingredient.getItemId()).isValid()) {
                    ingredientDataMissing = true;
                    continue;
                }
                priceToCraft += items.get(ingredient.getItemId()).getCheapestAvailable() * ingredient.getAmount();
            }
            if (ingredientDataMissing) {
                continue;
            }
            long profit = items.get(itemId).hqAvailableToPurchase() ? cheapestHQ * recipeData.getAmount() - priceToCraft : cheapest * recipeData.getAmount() - priceToCraft;
            long averageHistory = items.get(itemId).hqInHistory() ? items.get(itemId).getAverageHQHistory() : items.get(itemId).getAverageHistory();
            long numSoldInPastWeek = items.get(itemId).getAmountSoldLastWeek();
            long historicalProfit = averageHistory * recipeData.getAmount() - priceToCraft;
            long profitScore = historicalProfit * numSoldInPastWeek;
            models.add(new Model(
                    name,
                    nf.format(cheapest),
                    nf.format(cheapestHQ),
                    nf.format(priceToCraft),
                    nf.format(profit),
                    nf.format(averageHistory),
                    nf.format(numSoldInPastWeek),
                    nf.format(historicalProfit),
                    nf.format(profitScore)
            ));
        }

        System.out.println("Done");
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

        //System.out.println("--------------------------------------------");
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
                    //System.out.println(amountNeeded + "x " + marketResponse.getServerAndMarketDataHolder().getMarketData().getItem().getName() + " will cost " + nf.format(ingredientCost));
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

                //System.out.println(textToPrint);
            } else {
                long profit = (nqCost * recipeData.getAmount()) - (totalCost);
                long historyProfit = (marketData.getAverageHistory() * recipeData.getAmount()) - totalCost;
                String textToPrint = "It costs " + nf.format(nqCost) + " to buy the cheapest " + itemData.getSingularName() + ". It costs " + nf.format(totalCost) + " to craft " + nf.format(recipeData.getAmount()) + " for a profit of " + profit + ". On average, this item has been sold for " + nf.format(marketData.getAverageHistory()) + " and was sold " + (marketData.getAmountSoldLastWeek() >= 100 ? "at least " : "") + marketData.getAmountSoldLastWeek() + " time(s) in the last week which would be a profit of " + historyProfit;
                profits.put(textToPrint, historyProfit);

                //System.out.println(textToPrint);
            }

        }
        //System.out.println("--------------------------------------------");
    }
}

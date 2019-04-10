package database;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import models.RecipeData;

import java.util.ArrayList;
import java.util.List;

public class RecipeDatabase {
    private static boolean databaseInitialized = false;
    private static boolean mapperInitialized = false;
    private static CsvMapper mapper = new CsvMapper();
    private static CsvSchema schema;
    private static List<RecipeData> database = new ArrayList<RecipeData>();

    private static void initializeMapper() {
        if (mapperInitialized) {
            return;
        }
        schema = CsvSchema.builder()
                .addColumn("craftType", CsvSchema.ColumnType.NUMBER)
                .addColumn("recipeLevel", CsvSchema.ColumnType.NUMBER)
                .addColumn("itemId", CsvSchema.ColumnType.NUMBER)
                .addColumn("amount", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId1", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount1", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId2", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount2", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId3", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount3", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId4", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount4", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId5", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount5", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId6", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount6", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId7", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount7", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId8", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount8", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId9", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount9", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientId10", CsvSchema.ColumnType.NUMBER)
                .addColumn("ingredientAmount10", CsvSchema.ColumnType.NUMBER)
                .addColumn("recipeElement", CsvSchema.ColumnType.NUMBER)
                .addColumn("difficultyFactor", CsvSchema.ColumnType.NUMBER)
                .addColumn("qualityFactor", CsvSchema.ColumnType.NUMBER)
                .addColumn("durabilityFactor", CsvSchema.ColumnType.NUMBER)
                .addColumn("canHQ", CsvSchema.ColumnType.STRING)
                .addColumn("expRewarded", CsvSchema.ColumnType.STRING)
                .addColumn("isSpecialization", CsvSchema.ColumnType.STRING).build();
        mapperInitialized = true;
    }

    public static void initializeDatabase() throws Exception{
        if (databaseInitialized) {
            return;
        }
        initializeMapper();
        MappingIterator<RecipeData> it = mapper.readerFor(RecipeData.class).with(schema).readValues(Thread.currentThread().getContextClassLoader().getResourceAsStream("RecipeDatabase.csv"));
        while (it.hasNextValue()) {
            RecipeData recipeData = it.next();
            recipeData.initializeIngredientData();
            database.add(recipeData);
        }
        databaseInitialized = true;
    }

    public static RecipeData get(long id) throws Exception{
        initializeDatabase();
        for (RecipeData recipeData : database) {
            if (recipeData.getItemId() == id) {
                return recipeData;
            }
        }
        return null;
    }

    public static void printDatabase() throws Exception{
        initializeDatabase();
        for (RecipeData recipeData : database) {
            System.out.println(recipeData.getItemId());
        }
    }
}

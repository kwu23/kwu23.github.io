package database;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Strings;
import models.ItemData;
import utilities.Utilities;

import java.util.ArrayList;
import java.util.List;

public class ItemDatabase {
    private static boolean databaseInitialized = false;
    private static boolean mapperInitialized = false;
    private static CsvMapper mapper = new CsvMapper();
    private static CsvSchema schema;
    private static List<ItemData> database = new ArrayList<ItemData>();

    private static void initializeMapper() {
        if (mapperInitialized) {
            return;
        }
        schema = CsvSchema.builder()
                .addColumn("itemId", CsvSchema.ColumnType.NUMBER)
                .addColumn("singularName", CsvSchema.ColumnType.STRING)
                .addColumn("pluralName", CsvSchema.ColumnType.STRING)
                .addColumn("startsWithVowel", CsvSchema.ColumnType.NUMBER)
                .addColumn("description", CsvSchema.ColumnType.STRING)
                .addColumn("name", CsvSchema.ColumnType.STRING)
                .addColumn("imageId", CsvSchema.ColumnType.STRING)
                .addColumn("itemLevel", CsvSchema.ColumnType.NUMBER)
                .addColumn("rarity", CsvSchema.ColumnType.NUMBER).build();

        mapperInitialized = true;
    }

    public static void initializeDatabase() throws Exception{
        if (databaseInitialized) {
            return;
        }
        initializeMapper();
        MappingIterator<ItemData> it = mapper.readerFor(ItemData.class).with(schema).readValues(Thread.currentThread().getContextClassLoader().getResourceAsStream("ItemDatabase.csv"));
        while (it.hasNextValue()) {
            ItemData itemData = it.next();
            if (!Strings.isNullOrEmpty(itemData.getName())) {
                itemData.setName(Utilities.convertToShortName(itemData.getName()));
                database.add(itemData);
            }
        }
        databaseInitialized = true;
    }

    public static ItemData get(String name) throws Exception{
        initializeDatabase();
        name = Utilities.convertToShortName(name);
        for (ItemData itemData : database) {
            if (itemData.getName().contains(name)) {
                return itemData;
            }
        }
        return null;
    }

    public static ItemData get(long id) throws Exception{
        initializeDatabase();
        for (ItemData itemData : database) {
            if (itemData.getItemId() == id) {
                return itemData;
            }
        }
        return null;
    }

    public static void printDatabase() throws Exception{
        initializeDatabase();
        for (ItemData itemData : database) {
            System.out.println(itemData.getSingularName());
        }
    }
}

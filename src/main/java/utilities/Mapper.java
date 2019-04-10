package utilities;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.MarketData;
import models.MarketResponse;

import java.util.List;

public class Mapper {
    private static final ObjectMapper mapper = new ObjectMapper();

    private static boolean mapperConfigured = false;

    private static void configureMapper() {
        if (mapperConfigured) {
            return;
        }
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapperConfigured = true;
    }

    public static synchronized MarketData getSingleItemMarketDataFromString(String marketData) throws Exception {
        configureMapper();
        return mapper.readValue(marketData, MarketData.class);
    }

    public static synchronized List<MarketResponse> getMultipleItemMarketDataFromString(String marketData) throws Exception {
        configureMapper();
        return mapper.readValue(marketData, mapper.getTypeFactory().constructCollectionType(List.class, MarketResponse.class));
    }
}

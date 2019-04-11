package utilities;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class Utilities {

    public static HashMap<Long, String> jobIdToString = new HashMap<Long, String>() {{
        put(0L, "CRP");
        put(1L, "BSM");
        put(2L, "ARM");
        put(3L, "GSM");
        put(4L, "LTW");
        put(5L, "WVR");
        put(6L, "ALC");
        put(7L, "CUL");
    }};

    public static String convertToShortName(String name) {
        return name.toLowerCase().trim().replaceAll(" ", "");
    }
}

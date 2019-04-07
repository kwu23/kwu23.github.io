package utilities;

public class Utilities {
    public static String convertToShortName(String name) {
        return name.toLowerCase().trim().replaceAll(" ", "");
    }
}

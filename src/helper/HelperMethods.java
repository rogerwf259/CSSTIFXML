package helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HelperMethods {
    public static boolean isMatch(String text) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        //String regex = "^(https?)://(\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b)/([a-zA-Z]*)/[0-9\\*]*/([a-zA-Z]*)";
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        } catch (RuntimeException e) {
            return false;
        }
    }
}


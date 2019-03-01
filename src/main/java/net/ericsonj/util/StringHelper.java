package net.ericsonj.util;

import java.util.LinkedList;

/**
 *
 * @author Ericson Joseph <ericsonjoseph@gmail.com>
 *
 * Create on Feb 16, 2019 9:16:55 AM
 */
public class StringHelper {

    public static boolean stringMatches(String str, LinkedList<String> matchesOptions) {
        return stringMatches(str, matchesOptions.toArray(new String[matchesOptions.size()]));
    }

    public static boolean stringMatches(String str, String... matchesOptions) {
        for (String matchesOption : matchesOptions) {
            if (str.matches(matchesOption)) {
                return true;
            }
        }
        return false;
    }
    
    public static String getSpaces(int count) {
        String spaces = "";
        for (int i = 0; i < count; i++) {
            spaces += " ";
        }
        return spaces;
    }

}

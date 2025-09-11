package eu.pickride.utils;

import java.util.Map;

public class FormattingUtils {

    private final static Map<String, String> replacements = Map.ofEntries(
            Map.entry("Ș", "S"), Map.entry("ș", "s"),
            Map.entry("Ț", "T"), Map.entry("ț", "t"),
            Map.entry("Ă", "A"), Map.entry("ă", "a"),
            Map.entry("Î", "I"), Map.entry("î", "i"),
            Map.entry("Â", "A"), Map.entry("â", "a")
    );

    public static String replaceRomanianDiacritics(String text) {

        for (var entry : replacements.entrySet()) {
            text = text.replace(entry.getKey(), entry.getValue());
        }
        return text;
    }
}

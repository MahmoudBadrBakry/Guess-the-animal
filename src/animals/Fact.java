package animals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Fact {
    private String factContent;
    private Map<Animal, Boolean> relatedAnimals;

    public Fact(String factContent) {
        relatedAnimals = new HashMap<>();
        this.factContent = factContent;
    }

    public String getFactStatement(String type) {
        Map<String, String> patternReplace = new HashMap<>();
        String patternBase = type + ".#.pattern";
        String replaceBase = type + ".#.replace";
        String result = "";
        for (int i = 1; Application.rBPatterns.containsKey(patternBase.replaceFirst("#", "" + i)); i++) {
            String pattern = Application.rBPatterns.getString(patternBase.replaceFirst("#", "" + i));
            String replacement = Application.rBPatterns.getString(replaceBase.replaceFirst("#", "" + i));
            patternReplace.put(pattern, replacement);
        }

        for (var entry: patternReplace.entrySet()) {
            Pattern pattern = Pattern.compile(entry.getKey());
            Matcher matcher = pattern.matcher(factContent);
            if (matcher.matches()) {
                result = matcher.replaceFirst(entry.getValue());
            }
        }
        return result;
    }

    public String getFact() {
        return factContent;
    }

    public void setFactByAnimal(Animal animal, Boolean state) {
        relatedAnimals.put(animal, state);
    }

}

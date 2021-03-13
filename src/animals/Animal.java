package animals;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Animal {
    private String name;
    private String article;

    public Animal(String name) {
        this.name = parseAnimalName(name.toLowerCase().trim());
        article = (article == null) ? articleSelect(this.name) : article;
    }

    public String getArticleName() {
        String articleName = article + " " + name;
        return "".equals(article) ? name : articleName;
    }

    public String getName() {
        return name;
    }

    private String parseAnimalName(String animalName) {
        String splitedName = animalName;
        Pattern namePattern = Pattern.compile(Application.rBPatterns.getString("animal.1.pattern"));
        Matcher nameMatcher = namePattern.matcher(animalName);
        if(nameMatcher.find()) {
            splitedName = nameMatcher.group(2);
            article = nameMatcher.group(1).toLowerCase();
        }
        return splitedName;
    }

    private static String articleSelect(String animalName) {
        if (Application.rBPatterns.containsKey("animal.2.pattern")) {
            return (animalName.substring(0, 1).matches("[aAeEIiOoUu]") ? "an" : "a");
        }
        return "";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((article == null) ? 0 : article.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

}

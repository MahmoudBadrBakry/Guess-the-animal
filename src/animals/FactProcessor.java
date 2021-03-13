package animals;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FactProcessor {

    private static Pattern pattern;
    private static Matcher matcher;

    static {
        pattern = Pattern.compile(Application.rBPatterns.getString("statement.1.pattern"), Pattern.CASE_INSENSITIVE);
    }

    private FactProcessor() {

    }

    public static Fact getFact(Scanner sc) {

        boolean notFact = true;
        while (notFact) {
            try {
                matcher = pattern.matcher(sc.nextLine());
                if (!matcher.find()) throw new Exception("FactFormatException");
                notFact = false;
            } catch (Exception e) {
                System.out.println(Application.rBMessages.getString("statement.error"));
            }
        }

        matcher.reset();
        matcher.find();

        String factContent = matcher.replaceFirst(Application.rBPatterns.getString("statement.1.replace"));
        Fact fact = new Fact(factContent);

        return fact;
    }
    public static Fact getFact(String rawFact) {

        boolean notFact = true;
        while (notFact) {
            try {
                matcher = pattern.matcher(rawFact);
                if (!matcher.find()) throw new Exception("FactFormatException");
                notFact = false;
            } catch (Exception e) {
                System.out.println(Application.rBMessages.getString("statement.error"));
            }
        }

        matcher.reset();
        matcher.find();

        String factContent = matcher.replaceFirst(Application.rBPatterns.getString("statement.1.replace"));
        Fact fact = new Fact(factContent);

        return fact;
    }

    public static Boolean isFact(String rawFact) {

        matcher = pattern.matcher(rawFact);

        return matcher.find();
    }



}

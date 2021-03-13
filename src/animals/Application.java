package animals;

import java.io.IOException;
import java.text.MessageFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.regex.Pattern;

public class Application {

    static Scanner sc;
    static String fileType;
    static ResourceBundle rBMessages;
    static ResourceBundle rBPatterns;


    public static void run(String args[]) {

        rBMessages = ResourceBundle.getBundle("messages");
        rBPatterns = ResourceBundle.getBundle("patterns");

        sc = new Scanner(System.in);
        fileType = getFileType(args);

        String hi = rBMessages.getString(Greeter.getGreeting(LocalTime.now()));
        System.out.println(hi);

        QuestionsTree questionsTree = new QuestionsTree();
        initialize(questionsTree);

        UI(questionsTree);

        bye();
    }

    private static void UI(QuestionsTree questionsTree) {
        boolean exit = false;
        System.out.println(rBMessages.getString("welcome"));

        while (!exit) {
            options();
            switch (sc.nextLine().trim()) {
                case "0": {
                    exit = true;
                    break;
                }
                case "1": {
                    applyQuestioningScenario(questionsTree);
                    break;
                }
                case "2": {
                    listAnimals(questionsTree);
                    break;
                }
                case "3": {
                    findAnimal(questionsTree);
                    break;
                }
                case "4": {
                    calculateStatistics(questionsTree);
                    break;
                }
                case "5": {
                    questionsTree.print();
                    break;
                }
                default: {
                    System.out.println(new MessageFormat(rBMessages.getString("menu.property.error")).format(new Object[]{5}));
                    break;
                }
            }
            System.out.println();
        }
    }

    private static void calculateStatistics(QuestionsTree questionsTree) {
        questionsTree.calculateStatistics();
        System.out.println(rBMessages.getString("tree.stats.title") + "\n");
        System.out.println(new MessageFormat( rBMessages.getString("tree.stats.root"))
                .format(new Object[] {questionsTree.getRoot().getContent()}));
        System.out.println(new MessageFormat(rBMessages.getString("tree.stats.nodes"))
                .format(new Object[] {questionsTree.getNodesCount()}));
        System.out.println(new MessageFormat(rBMessages.getString("tree.stats.animals"))
                .format(new Object[] {questionsTree.getAnimalsCount()}));
        System.out.println(new MessageFormat(rBMessages.getString("tree.stats.statements"))
                .format(new Object[] {questionsTree.getFactsCount()}));
        System.out.println(new MessageFormat(rBMessages.getString("tree.stats.height"))
                .format(new Object[] {questionsTree.getTreeHeight()}));
        System.out.println(new MessageFormat(rBMessages.getString("tree.stats.minimum"))
                .format(new Object[] {questionsTree.getMinDepth()}));
        System.out.println(new MessageFormat(rBMessages.getString("tree.stats.average"))
                .format(new Object[] {questionsTree.getAvgDepth()}));
    }

    private static void findAnimal(QuestionsTree questionsTree) {
        System.out.println(rBMessages.getString("animal.prompt"));
        questionsTree.searchForAnAnimal(sc.nextLine().trim().toLowerCase());
    }

    private static void listAnimals(QuestionsTree questionsTree) {
        questionsTree.printAnimals();
    }

    private static void options() {
        System.out.println(rBMessages.getString("menu.property.title") + "\n");
        System.out.println("1. " + rBMessages.getString("menu.entry.play"));
        System.out.println("2. " + rBMessages.getString("menu.entry.list"));
        System.out.println("3. " + rBMessages.getString("menu.entry.search"));
        System.out.println("4. " + rBMessages.getString("menu.entry.statistics"));
        System.out.println("5. " + rBMessages.getString("menu.entry.print"));
        System.out.println("0. " + rBMessages.getString("menu.property.exit"));
    }

    private static String getFileType(String[] args) {
        return (args.length >= 2 ? args[1] : "");

    }

    private static void initialize(QuestionsTree questionsTree) {
        try {
            questionsTree.setRoot(Loader.readJSON(fileType));
        } catch (IOException e) {}

        if (questionsTree.isEmpty()) {
            System.out.println(rBMessages.getString("animal.wantLearn") + "\n"
                    + rBMessages.getString("animal.askFavorite"));
            Animal firstAnimal = new Animal(sc.nextLine().toLowerCase());

            addFirstAnimal(firstAnimal.getArticleName(), questionsTree);

            String[] wonder = rBMessages.getString("animal.nice").split("\\f");
            System.out.println(wonder[getRandomIndex(wonder.length)] + rBMessages.getString("animal.learnedMuch"));
        }

    }

    private static void addFirstAnimal(String animalName, QuestionsTree questionsTree) {
        questionsTree.setRoot(new TreeNode(animalName, "animal"));
    }

    private static void letsPlay() {

        System.out.println(rBMessages.getString("game.letsPlay"));
        System.out.println(rBMessages.getString("game.think"));
        System.out.println(rBMessages.getString("game.enter"));

        sc.nextLine();
    }

    private static void applyQuestioningScenario(QuestionsTree questionsTree) {

        letsPlay();

        questionsTree.reset();

        boolean playing = true;

        while (playing) {
            if (questionsTree.getCurrentNode().getNodeType().equals("animal")) {
                Animal animal = new Animal(questionsTree.getCurrentNode().getContent());

                System.out.println(animal.getArticleName()
                        .replaceAll(rBPatterns.getString("guessAnimal.1.pattern")
                                , rBPatterns.getString("guessAnimal.1.replace")));

                if (!YesNoAnswer.getAnswer(sc)) {
                    learnFact(animal, questionsTree);
                } else {
                    System.out.println(rBMessages.getString("game.win"));
                }

                var playAgain = rBMessages.getString("game.again").split("\\f");
                System.out.println(playAgain[getRandomIndex(playAgain.length)]);

                playing = YesNoAnswer.getAnswer(sc);

                if (playing) {
                    System.out.println(rBMessages.getString("game.think") +  "\n"
                            + rBMessages.getString("game.enter"));
                    sc.nextLine();
                } else {
                    thanks();
                    save(questionsTree);
                }

                questionsTree.reset();
            } else {
                Fact fact = FactProcessor.getFact(questionsTree.getCurrentNode().getContent());
                System.out.println(fact.getFactStatement("question"));

                if (YesNoAnswer.getAnswer(sc)) {
                    questionsTree.nextTrue();
                } else {
                    questionsTree.nextFalse();
                }

            }
        }
    }

    private static void thanks() {
        String[] thanks = rBMessages.getString("game.thanks").split("\\f");
        System.out.println(thanks[getRandomIndex(thanks.length)]);
    }

    private static void learnFact(Animal animal, QuestionsTree questionsTree) {
        System.out.println(rBMessages.getString("game.giveUp"));
        Animal secondAnimal = new Animal(sc.nextLine());

        System.out.println(new MessageFormat(rBMessages.getString("statement.prompt")).format(new Object[] {
                animal.getArticleName(), secondAnimal.getArticleName()
        }));

        Fact fact;

        fact = FactProcessor.getFact(sc);

        System.out.println(new MessageFormat(rBMessages.getString("game.isCorrect")).format(new Object[] {secondAnimal.getArticleName()}));
        boolean trueForSecond = YesNoAnswer.getAnswer(sc);
        fact.setFactByAnimal(animal, !trueForSecond);
        fact.setFactByAnimal(secondAnimal, trueForSecond);

        String trueAnimal = !trueForSecond ? animal.getArticleName() : secondAnimal.getArticleName();
        String falseAnimal = trueForSecond ? animal.getArticleName() : secondAnimal.getArticleName();

        Animal temp = !trueForSecond ? animal : secondAnimal;
        secondAnimal = trueForSecond ? animal : secondAnimal;
        animal = temp;

        questionsTree.replaceCurrentAnimalWithFact(trueAnimal, falseAnimal, fact);

        System.out.println(rBMessages.getString("game.learned"));

        String definiteAnimal = Pattern.compile(rBPatterns.getString("definite.1.pattern")).matcher(animal.getArticleName())
                .replaceFirst(rBPatterns.getString("definite.1.replace"));
        String definiteSecondAnimal = Pattern.compile(rBPatterns.getString("definite.1.pattern")).matcher(secondAnimal.getArticleName())
                .replaceFirst(rBPatterns.getString("definite.1.replace"));

        System.out.println(String.format(fact.getFact()
                .replaceFirst(rBPatterns
                        .getString("animalFact.1.pattern"), rBPatterns.getString("animalFact.1.replace")) , definiteAnimal));
        System.out.println(String.format(fact.getFactStatement("negative")
                .replaceFirst(rBPatterns.getString("animalFact.1.pattern"),
                        rBPatterns.getString("animalFact.1.replace")) , definiteSecondAnimal));

        System.out.println(rBMessages.getString("game.distinguish"));
        System.out.println(fact.getFactStatement("question"));

    }

    private static void save(QuestionsTree questionsTree) {
        try {
            Loader.writeJSON(questionsTree.getRoot(), fileType);
        } catch (IOException e) {

        }
    }

    private static void bye() {
        System.out.println();
        var farewell = rBMessages.getString("farewell").split("\\f");
        System.out.println(farewell[getRandomIndex(farewell.length)]);
    }

    private static int getRandomIndex(int max) {
        return new Random().nextInt(max);
    }

}

package animals;

import java.util.Random;
import java.util.Scanner;


public class YesNoAnswer {

    static class AnswerChecker {

        public AnswerChecker() {}

        public Boolean checkAnswer(String answer) {
            if (isYes(answer)) {
                return true;
            } else if(isNo(answer)) {
                return false;
            } else {
                return null;
            }
        }

        private boolean isNo(String answer) {
            answer = answer.toLowerCase().trim();
            return answer.matches(Application.rBPatterns.getString("negativeAnswer.isCorrect"));
        }

        private boolean isYes(String answer) {
            answer = answer.toLowerCase().trim();
            return answer.matches(Application.rBPatterns.getString("positiveAnswer.isCorrect"));
        }
    }

    static class ConfusingAnswer {
        private static String[] answers;
        private static Random random;

        static {
            random = new Random();
        }
        public static String getAnswer() {
            answers = Application.rBMessages.getString("ask.again").split("\\f");
            return answers[random.nextInt(answers.length)];
        }

    }

    public static Boolean getAnswer(Scanner sc) {
        AnswerChecker answerChecker = new AnswerChecker();
        Boolean answer = answerChecker.checkAnswer(sc.nextLine());
        while (answer == null) {
            System.out.println(ConfusingAnswer.getAnswer());
            answer = answerChecker.checkAnswer(sc.nextLine());
        }
        return answer;
    }

}


package animals;

import java.util.*;

public class QuestionsTree {
    private TreeNode root = null;
    private TreeNode currentNode = null;

    public QuestionsTree() {
    }

    public void replaceCurrentAnimalWithFact(String trueAnimal, String falseAnimal, Fact fact) {
        currentNode.content = fact.getFact();
        currentNode.nodeType = "fact";
        currentNode.yes = new TreeNode(trueAnimal, "animal");
        currentNode.no = new TreeNode(falseAnimal, "animal");
    }

    public TreeNode getRoot() {
        return this.root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public TreeNode getCurrentNode() {
        return this.currentNode;
    }

    public void nextTrue() {
        currentNode = currentNode.yes;
    }

    public void nextFalse() {
        currentNode = currentNode.no;
    }

    public void reset() {
        currentNode = root;
    }

    public void print() {
        this.reset();
        System.out.print("└ ");
        printNode(getCurrentNode());
        String prefix = " ";
        recPrint(prefix, getCurrentNode());
    }

    private void printNode(TreeNode node) {
        if (node.getNodeType().equals("fact")) {
            Fact fact = FactProcessor.getFact(node.getContent());
            System.out.println(fact.getFactStatement("question"));
        } else {
            Animal animal = new Animal(node.getContent());
            System.out.println(animal.getArticleName());
        }
    }

    private void recPrint(String prefix, TreeNode node) {

        if (node.yes != null) {
            System.out.print(prefix + "├ ");
            printNode(node.yes);
            recPrint(prefix + "│ ", node.yes);
        }
        if (node.no != null) {
            System.out.print(prefix + "└ ");
            printNode(node.no);
            recPrint(prefix + " ", node.no);
        }
    }

    public void printAnimals() {
        List<String> animals = new ArrayList<>();

        catchAnimal(root, animals);

        Collections.sort(animals);

        System.out.println(Application.rBMessages.getString("tree.list.animals"));

        for (String animal : animals) {
            System.out.println(" - " + animal);
        }
    }

    private void catchAnimal(TreeNode node, List<String> animals) {
        if (node.isLeaf()) {
            animals.add(new Animal(node.content).getName());
            return;
        }
        catchAnimal(node.no, animals);
        catchAnimal(node.yes, animals);
    }

    public void searchForAnAnimal(String animal) {
        List<String> facts = new ArrayList<>();

        boolean found = catchFact(root, facts, animal);

        if (found && !facts.isEmpty()) {
            System.out.println(Application.rBMessages.getString("tree.search.facts")
                    .replaceFirst("\\{0\\}", animal));
            for (int i = facts.size() - 1; i >= 0; --i) {
                System.out.println(" " + facts.get(i));
            }
        } else {
            System.out.println(Application.rBMessages.getString("tree.search.noFacts")
                    .replaceFirst("\\{0\\}", animal));
        }
    }

    private boolean catchFact(TreeNode node, List<String> facts, String animal) {
        boolean answer = false;
        Animal animalA = new Animal(animal);

        if (!node.isLeaf()) {
            answer = catchFact(node.no, facts, animal);
            if (answer) {
                Fact fact = FactProcessor.getFact(node.content);
                fact.setFactByAnimal(animalA, false);
                facts.add(fact.getFactStatement("negative"));
            } else {
                answer = catchFact(node.yes, facts, animal);
                if (answer) {
                    Fact fact = FactProcessor.getFact(node.content);
                    fact.setFactByAnimal(animalA, true);
                    facts.add(fact.getFact());
                }
            }
        }
        if (new Animal(node.content).getName().equals(animal))
            answer = true;

        return answer;
    }

    private int nodesCount;
    private int animalsCount;
    private int factsCount;
    private int treeHeight;
    private int minDepth;
    private double avgDepth;
    public void calculateStatistics() {
        nodesCount = countNodes(root);
        animalsCount = countAnimals(root);
        factsCount = nodesCount - animalsCount;
        treeHeight = getTreeHeight(root);
        minDepth = getMinDepth(root);
        avgDepth = getAvgDepth(root);
    }

    public int getNodesCount() {
        return nodesCount;
    }

    public void setNodesCount(int nodesCount) {
        this.nodesCount = nodesCount;
    }

    public int getAnimalsCount() {
        return animalsCount;
    }

    public void setAnimalsCount(int animalsCount) {
        this.animalsCount = animalsCount;
    }

    public int getFactsCount() {
        return factsCount;
    }

    public void setFactsCount(int factsCount) {
        this.factsCount = factsCount;
    }

    public int getTreeHeight() {
        return treeHeight;
    }

    public void setTreeHeight(int treeHeight) {
        this.treeHeight = treeHeight;
    }

    public int getMinDepth() {
        return minDepth;
    }

    public void setMinDepth(int minDepth) {
        this.minDepth = minDepth;
    }

    public double getAvgDepth() {
        return avgDepth;
    }

    public void setAvgDepth(double avgDepth) {
        this.avgDepth = avgDepth;
    }

    public void setCurrentNode(TreeNode currentNode) {
        this.currentNode = currentNode;
    }

    private double getAvgDepth(TreeNode root) {

        List<Integer> depths = new ArrayList<>();
        double sum = 0;

        getDepths(root, depths, 0);

        for (Integer depth : depths) {
            sum += depth;
        }

        return sum / depths.size();
    }

    private void getDepths(TreeNode node, List<Integer> depths, int depth) {
        if (node.isLeaf()) {
            depths.add(depth);
            return ;
        }
        if (node.yes != null) {
            getDepths(node.yes, depths, depth + 1);
        }
        if (node.no != null) {
            getDepths(node.no, depths, depth + 1);
        }

    }

    private int getMinDepth(TreeNode node) {
        if (node.isLeaf()) {
            return 0;
        }
        int yesCount = (int) (2E31 - 1);
        int noCount = (int) (2E31 - 1);
        if (node.yes != null) {
            yesCount = getMinDepth(node.yes);
        }
        if (node.no != null) {
            noCount = getMinDepth(node.no);
        }
        return Math.min(yesCount, noCount) + 1;
    }

    private int getTreeHeight(TreeNode node) {
        if (node.isLeaf()) {
            return 0;
        }

        int yesCount = 0;
        int noCount = 0;

        if (node.yes != null) {
            yesCount = getTreeHeight(node.yes);
        }

        if (node.no != null) {
            noCount = getTreeHeight(node.no);
        }
        return Math.max(yesCount, noCount) + 1;
    }

    private int countAnimals(TreeNode node) {
        if (node.isLeaf()) {
            return 1;
        }
        int yesCount = 0;
        int noCount = 0;
        if (node.yes != null) {
            yesCount = countAnimals(node.yes);
        }
        if (node.no != null) {
            noCount = countAnimals(node.no);
        }
        return noCount + yesCount;
    }

    private int countNodes(TreeNode node) {
        if (node.isLeaf()) {
            return 1;
        }
        int yesCount = 0;
        int noCount = 0;
        if (node.yes != null) {
            yesCount += countNodes(node.yes);
        }
        if (node.no != null) {
            noCount += countNodes(node.no);
        }
        return noCount + yesCount + 1;
    }

    public boolean isEmpty() {
        return getRoot() == null;
    }

}

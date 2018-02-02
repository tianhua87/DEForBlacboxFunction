import algorithm.CommonDEInitializer;
import algorithm.DE;
import algorithm.DEInitializer;
import problem.Ackley;
import problem.BlackBoxProblem;

public class Main {

    public static void main(String[] args) {

        DEInitializer deInitializer = new CommonDEInitializer();
        BlackBoxProblem blackBoxProblem = new Ackley();
        DE de = new DE(10, 50, 0.5, 0.5, -30, 30,  deInitializer, blackBoxProblem);
        de.optimize();
    }
}

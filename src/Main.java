import algorithm.CommonDEInitializer;
import algorithm.DE;
import algorithm.DEInitializer;
import problem.Ackley;
import problem.BlackBoxProblem;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        args = new String[]{
                "svmfile/train/test_train.txt",
                "svmfile/model/test_train.txt.model"
        };
        svm_train.main(args);

    }

    public static void testDE(){
        DEInitializer deInitializer = new CommonDEInitializer();
        BlackBoxProblem blackBoxProblem = new Ackley();
        DE de = new DE(10, 50, 0.5, 0.5, -30, 30,  deInitializer, blackBoxProblem);
        de.optimize();
    }
}

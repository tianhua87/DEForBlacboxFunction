import algorithm.CommonDEInitializer;
import algorithm.DE;
import algorithm.DEInitializer;
import problem.Ackley;
import problem.BlackBoxProblem;
import train_file_generate.TrainFileGenerator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        args = new String[]{
//                "svmfile/train/test_train.txt",
//                "svmfile/model/test_train.txt.model"
//        };
//        svm_train.main(args);
//        String[] args1 = new String[]{
//                "svmfile/predict/test",
//                "svmfile/model/test_train.txt.model",
//                "svmfile/predict/test.out"
//        };
//        svm_predict.main(args1);

//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate("Ackley");

        TrainFileGenerator tfg = new TrainFileGenerator();
        tfg.trainFileGenerate("Beale");

    }

    public static void testDE() {
        DEInitializer deInitializer = new CommonDEInitializer();
        BlackBoxProblem blackBoxProblem = new Ackley();
        DE de = new DE(10, 50, 0.5, 0.5,  deInitializer, blackBoxProblem);
        de.optimize();
    }
}

import algorithm.CommonDEInitializer;
import algorithm.DE;
import algorithm.DEInitializer;
import file_generate.ScaleFileGenerator;
import problem.Ackley;
import problem.BlackBoxProblem;
import file_generate.TrainFileGenerator;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate("Ackley");

//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate("Beale");

        ScaleFileGenerator sfg = new ScaleFileGenerator();
        //sfg.trainFileScale("Ackley");
        sfg.predictFileScale("Ackley");

    }

    public static void testDE() {
        DEInitializer deInitializer = new CommonDEInitializer();
        BlackBoxProblem blackBoxProblem = new Ackley();
        DE de = new DE(10, 50, 0.5, 0.5,  deInitializer, blackBoxProblem);
        de.optimize();
    }
}

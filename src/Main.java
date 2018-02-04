import algorithm.CommonDEInitializer;
import algorithm.DE;
import algorithm.DEInitializer;
import file_generate.*;
import problem.Ackley;
import problem.BlackBoxProblem;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate("Ackley");

//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate("Beale");

//        ScaleFileGenerator sfg = new ScaleFileGenerator();
//        sfg.trainFileScale("Ackley");
//        sfg.predictFileScale("Ackley");
//
//        ParametersFinder pf = new ParametersFinder();
//        pf.findRegTrainParameters("Ackley");
//        ModelGenerator mg = new ModelGenerator();
//        mg.generateModel("Ackley");

        Predictor predictor = new Predictor();
        predictor.predict("Ackley");

    }

    public static void testDE() {
        DEInitializer deInitializer = new CommonDEInitializer();
        BlackBoxProblem blackBoxProblem = new Ackley();
        DE de = new DE(10, 50, 0.5, 0.5,  deInitializer, blackBoxProblem);
        de.optimize();
    }
}

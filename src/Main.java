import algorithm.CommonDEInitializer;
import algorithm.DE;
import algorithm.DEInitializer;
import file_generate.*;
import problem.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate("Ackley");

//        ScaleFileGenerator sfg = new ScaleFileGenerator();
//        sfg.trainFileScale("Ackley");
//        sfg.predictFileScale("Ackley");
//
//        ParametersFinder pf = new ParametersFinder();
//        pf.findRegTrainParameters("Ackley");
//        ModelGenerator mg = new ModelGenerator();
//        mg.generateModel("Ackley");
//
//        Predictor predictor = new Predictor();
//        predictor.predict("Ackley");
        //18：35-19：19

        String pro = "Beale";
//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate(pro);

        ScaleFileGenerator sfg = new ScaleFileGenerator();
//        sfg.trainFileScale(pro);
        sfg.predictFileScale(pro);
//
//        ParametersFinder pf = new ParametersFinder();
//        pf.findRegTrainParameters(pro);
//        ModelGenerator mg = new ModelGenerator();
//        mg.generateModel(pro);

        Predictor predictor = new Predictor();
        predictor.predict(pro);

        //testSVM();
    }

    public static void testDE() {
        DEInitializer deInitializer = new CommonDEInitializer();
        BlackBoxProblem blackBoxProblem = new Ackley();
        DE de = new DE(10, 50, 0.5, 0.5,  deInitializer, blackBoxProblem);
        de.optimize();

    }

    public static void testSVM(){
        BlackBoxProblem blackBoxProblem = new Beale_SVM();
        double X[]=new double[]{-3.5531411069213337 ,0.3727889003046929};
        blackBoxProblem.evaluate(X,2);
        BlackBoxProblem blackBoxProblem1 = new Beale();
        double res = blackBoxProblem1.evaluate(X,2);
        System.out.println("理论结果:" + res);
    }
}

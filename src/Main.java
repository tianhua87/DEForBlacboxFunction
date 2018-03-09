import algorithm.CommonDEInitializer;
import algorithm.DE;
import algorithm.DEInitializer;
import file_generate.*;
import problem.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //Ackley,Beale,Bohachevsky1,Branin,Rastrigin
        String problemName = "Beale";
        testSVMProblem(problemName);
        //testDEProblem(problemName);
    }

    public static void testDE() {
        DEInitializer deInitializer = new CommonDEInitializer();
        //BlackBoxProblem blackBoxProblem = new Ackley();
        //BlackBoxProblem blackBoxProblem = new Beale();
        BlackBoxProblem blackBoxProblem = new Beale_SVM();
        DE de = new DE(10, 50, 0.5, 0.5,  deInitializer, blackBoxProblem);
        de.optimize();

    }

    public static void testNoscale(){
        String pro = "Beale";
//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate(pro);
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithoutScale(pro);
    }

    public static void testSVM(){
        BlackBoxProblem blackBoxProblem = new Beale_SVM();
        double X[]=new double[]{-3.5531411069213337 ,0.3727889003046929};
        double res1 = blackBoxProblem.evaluate(X,2);
        System.out.println("实际结果:"+res1);
        BlackBoxProblem blackBoxProblem1 = new Beale();
        double res = blackBoxProblem1.evaluate(X,2);
        System.out.println("理论结果:" + res);
    }

    public static void testSVMProblem(String problemName) {
        TrainFileGenerator tfg = new TrainFileGenerator();
        tfg.trainFileGenerate(problemName);
        //ScaleFileGenerator sfg = new ScaleFileGenerator();
        //sfg.trainFileScale(problemName);
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithoutScale(problemName);
        BlackBoxProblem bbProblem = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problemName + "_SVM").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DEInitializer deInitializer = new CommonDEInitializer();
        DE de = new DE(10, 50, 0.5, 0.5,  deInitializer, bbProblem);
        de.optimize();
    }

    public static void testDEProblem(String problemName) {
        BlackBoxProblem bbProblem = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problemName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DEInitializer deInitializer = new CommonDEInitializer();
        DE de = new DE(10, 50, 0.5, 0.5,  deInitializer, bbProblem);
        de.optimize();
    }
}

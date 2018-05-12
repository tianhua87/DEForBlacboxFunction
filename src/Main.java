import algorithm.*;
import file_generate.*;
import problem.*;
import test.RegAndClassfyCampared;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        //Ackley,Beale,Bohachevsky1,Branin,Rastrigin,Shekel2,Kowalik,SixHumpCamel

        String problemName = "Rosenbrock";
        //testSVMProblem(problemName);

        //testDEProblem(problemName);

        //statistic();

        //testOneToOne();

        //testSVM();

        testUpdate();


    }


    public static void testUpdate() {
        //Ackley,Beale,Bohachevsky1,Branin,Rastrigin,Shekel2,Kowalik,SixHumpCamel',Matyas,Exp2,GoldsteinPrice,Booth
        String problem = "Beale";
        DEOptimizer deOptimizer = new DEOptimizer();
        deOptimizer.optimizeModel(problem);
    }

    public static void testDE() {
        DEInitializer deInitializer = new CommonDEInitializer();
        //BlackBoxProblem blackBoxProblem = new Ackley();
        //BlackBoxProblem blackBoxProblem = new Beale();
        BlackBoxProblem blackBoxProblem = new Beale_SVM();
        DE de = new DE(10, 50, 0.8, 0.7,  deInitializer, blackBoxProblem);
        de.optimize();
    }

    public static void testOneToOne(){
        //Ackley,Beale,Bohachevsky1,Branin,Rastrigin,Shekel2,Kowalik,SixHumpCamel
        String problemName = "Kowalik";
        TrainFileGenerator tfg = new TrainFileGenerator();
        //tfg.trainFileGenerate(problemName,true);
        ModelGenerator mg = new ModelGenerator();
        mg.generateModel(problemName,true);
        BlackBoxProblem bbProblem = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problemName + "_SVM").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DEInitializer deInitializer = new EvenlyDEInitializer();
        DE de = new DE(10, 500, 0.5, 0.5,  deInitializer, bbProblem);
        de.optimize();

    }


    public static void testNoscale(){
        String pro = "Beale";
//        TrainFileGenerator tfg = new TrainFileGenerator();
//        tfg.trainFileGenerate(pro);
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithoutScale(pro);
    }

    public static void testSVM() {

        //Ackley,Beale,Bohachevsky1,Branin,Rastrigin,Shekel2,Kowalik,SixHumpCamel
        String problemName = "Ackley";
        TrainFileGenerator tfg = new TrainFileGenerator();
        tfg.trainFileGenerate(problemName);
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithoutScale(problemName);
        BlackBoxProblem bbProblem = null;
        BlackBoxProblem blackBoxProblem1 = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problemName + "_SVM").newInstance();
            blackBoxProblem1 = (BlackBoxProblem) Class.forName("problem."+problemName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int dim =1;
        double X[]=new double[]{-0.036729037291331805};
        double res1 = bbProblem.evaluate(X,dim);
        System.out.println("实际结果:"+res1);
        double res = blackBoxProblem1.evaluate(X,dim);
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
        DEInitializer deInitializer = new EvenlyDEInitializer();
        DE de = new DE(10, 500, 0.5, 0.5,  deInitializer, bbProblem);
        de.optimize();
    }

    public static void testDEProblem(String problemName) {
        BlackBoxProblem bbProblem = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problemName).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        DEInitializer deInitializer = new EvenlyDEInitializer();
        DE de = new DE(10, 50, 0.5, 0.5,  deInitializer, bbProblem);
        de.optimize();
    }
}

package test;

import file_generate.ModelGenerator;
import file_generate.TrainFileGenerator;
import problem.BlackBoxProblem;

import java.io.*;
import java.nio.Buffer;

public class SVMTest {

    public static void main(String[] args) {

        SVMTest svmTest = new SVMTest();
        svmTest.testSVMAccuracy();

    }

    public void testSVMAccuracy() {
        //Ackley,Beale,Bohachevsky1,Branin,Rastrigin,Shekel2,Kowalik,SixHumpCamel'
        String problem = "Ackley";
        int count = 10;
        BlackBoxProblem bbp = generateBBProblem(problem);
        TrainFileGenerator tfg = new TrainFileGenerator();
        TrainFileGenerator.COUNT = count;
        tfg.trainFileGenerate(problem);

        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithoutScale(problem);

        double[][] g = getTrainSets(problem,count,bbp.dim);

//        for(int i = 0; i < count ;i++) {
//            System.out.print(g[i][0]);
//            for(int j = 1; j <= bbp.dim ; j++) {
//                System.out.print(" "+g[i][j]);
//            }
//            System.out.println();
//        }

        BlackBoxProblem bbpSVM = generateSVMProblem(problem);

        double[] t = new double[bbp.dim];
        double sum = 0;
        for (int i=0;i<count;i++) {
            for(int j=0;j<bbp.dim;j++){
                t[j] = g[i][j+1];
            }
            double ySVM = bbpSVM.evaluate(t,bbp.dim);
            sum+=(ySVM-g[i][0])*(ySVM-g[i][0]);
        }
        System.out.println(sum/count);


    }

    public BlackBoxProblem generateBBProblem(String problem){
        BlackBoxProblem bbProblem = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problem).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bbProblem;
    }

    public BlackBoxProblem generateSVMProblem(String problem){
        BlackBoxProblem bbProblem = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problem + "_SVM").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bbProblem;
    }


    public double[][] getTrainSets(String problem,int count,int dim) {
        try {
            FileInputStream fis = new FileInputStream("svmfile/train/"+problem+"_train");

            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String line;
            double [][]g = new double[count][dim+1];
            int k = 0;
            while((line = br.readLine())!=null && line.length() != 0){
                String xStrs[] = line.split(" ");
                g[k][0] = Double.parseDouble(xStrs[0]);
                for(int i=1;i<=dim;i++){
                    g[k][i] = Double.parseDouble(xStrs[i].split(":")[1]);
                }
                k++;
            }
            return g;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

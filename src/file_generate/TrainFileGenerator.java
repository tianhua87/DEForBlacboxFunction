package file_generate;

import problem.BlackBoxProblem;
import random.DERandom;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class TrainFileGenerator {

    final int COUNT = 1000;
    DERandom deRandom = new DERandom();
    public void trainFileGenerate(String problemName){
        try {
            Class C = Class.forName("problem."+problemName);
            BlackBoxProblem blackBoxProblem = (BlackBoxProblem) C.newInstance();
            double[] x = new double[blackBoxProblem.dim];
            FileOutputStream fos = new FileOutputStream("svmfile/train/"+problemName+"_train");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            StringBuilder line = new StringBuilder();
            double res;
            for (int i = 0; i < COUNT; i++){
                createRandomVector(x,blackBoxProblem);
                res = blackBoxProblem.evaluate(x,blackBoxProblem.dim);
                line.append(res);
                for (int j = 0 ; j < blackBoxProblem.dim; j++){
                    line.append(" ").append(j+1).append(":").append(x[j]);
                }
                line.append("\r\n");
                bw.write(line.toString());
                bw.flush();//重要，重要，重要
                line.delete(0,line.length());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     void createRandomVector(double[] x, BlackBoxProblem blackBoxProblem){
        for (int i = 0;i < blackBoxProblem.dim;i++){
            x[i] = deRandom.nextDouble(blackBoxProblem.lowLimit,blackBoxProblem.highLimit);
        }
    }
}

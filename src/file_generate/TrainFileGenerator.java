package file_generate;

import problem.BlackBoxProblem;
import random.DERandom;
import utilities.NumberUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class TrainFileGenerator {

    final int COUNT = 10000;
    DERandom deRandom = new DERandom();
//    public void trainFileGenerate(String problemName){
//        try {
//            Class C = Class.forName("problem."+problemName);
//            BlackBoxProblem blackBoxProblem = (BlackBoxProblem) C.newInstance();
//            double[] x = new double[blackBoxProblem.dim];
//            FileOutputStream fos = new FileOutputStream("svmfile/train/"+problemName+"_train");
//            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
//            StringBuilder line = new StringBuilder();
//            double res;
//            for (int i = 0; i < COUNT; i++){
//                createRandomVector(x,blackBoxProblem);//随机生成个体
//                res = blackBoxProblem.evaluate(x,blackBoxProblem.dim); //评价个体
//                line.append(res);
//                for (int j = 0 ; j < blackBoxProblem.dim; j++){
//                    line.append(" ").append(j+1).append(":").append(x[j]);
//                }
//                line.append("\r\n");
//                bw.write(line.toString());
//                bw.flush();//重要，重要，重要
//                line.delete(0,line.length());
//            }
//            if (bw != null)bw.close();
//            if(fos != null)fos.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("--------------------"+problemName+"训练文件生成结束--------------------");
//    }

    public void trainFileGenerate(String problemName){
        try {
            Class C = Class.forName("problem."+problemName);
            BlackBoxProblem blackBoxProblem = (BlackBoxProblem) C.newInstance();
            FileOutputStream fos = new FileOutputStream("svmfile/train/"+problemName+"_train");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            StringBuilder line = new StringBuilder();

            int dim = blackBoxProblem.dim;
            double p[][] = new double[COUNT][dim];
            double xmin[] = new double[dim+1];
            double xmax[] = new double[dim+1];
            for(int i = 0; i <= dim; i++) {
                xmin[i] = blackBoxProblem.lowLimit;
                xmax[i] = blackBoxProblem.highLimit;
            }
            NumberUtil.latin_hyp(p,COUNT,dim,xmin,xmax);
            double res[] = new double[COUNT];
            for (int i = 0; i < COUNT; i++){

                res[i] = blackBoxProblem.evaluate(p[i],dim); //评价个体
                line.append(res[i]);
                for (int j = 0 ; j < dim; j++){
                    line.append(" ").append(j+1).append(":").append(p[i][j]);
                }
                line.append("\r\n");
                bw.write(line.toString());
                bw.flush();//重要，重要，重要
                line.delete(0,line.length());
            }
            if (bw != null)bw.close();
            if(fos != null)fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------------------"+problemName+"训练文件生成结束--------------------");
    }

     void createRandomVector(double[] x, BlackBoxProblem blackBoxProblem){
        for (int i = 0;i < blackBoxProblem.dim;i++){
            x[i] = deRandom.nextDouble(blackBoxProblem.lowLimit,blackBoxProblem.highLimit);
        }
    }
}

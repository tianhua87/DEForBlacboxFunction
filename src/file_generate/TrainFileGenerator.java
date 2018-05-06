package file_generate;

import algorithm.DEInitializer;
import algorithm.DEOptimizer;
import algorithm.EvenlyDEInitializer;
import problem.BlackBoxProblem;
import random.DERandom;
import utilities.NumberUtil;
import utilities.ProblemGenerator;
import utilities.SortUtil;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class TrainFileGenerator {

    public static void main(String[] args){
        //初始数据
        int NP = 50;
        double Cr = 0.2;
        double F = 0.3;
        String problem = "Ackley";
        BlackBoxProblem blackBoxProblem = ProblemGenerator.generateSVMProblem(problem);
        int dim = blackBoxProblem.dim;
        TrainFileGenerator.COUNT = dim*11+1;

        //生成初始种群
        double[][] p = new double[NP][dim];
        double[] cost = new double[NP];
        DEInitializer deInitializer = new EvenlyDEInitializer();
        deInitializer.init(p,blackBoxProblem.lowLimit,blackBoxProblem.highLimit,NP,F,Cr,dim);
        DEOptimizer deOptimizer = new DEOptimizer();
        deOptimizer.caculateCost(problem,p,cost);

        TrainFileGenerator tfg = new TrainFileGenerator();
        tfg.trainFileGenerate(problem,p,cost,true);
    }



    public static int COUNT = 10000;
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

            BlackBoxProblem blackBoxProblem = ProblemGenerator.generateBBProblem(problemName);
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

    //排序，然后两两组合，生成训练文件，for 分类
    public void trainFileGenerate(String problemName,double[][]p,double[] cost,boolean campared){
        try {
            BlackBoxProblem blackBoxProblem = ProblemGenerator.generateBBProblem(problemName);
            FileOutputStream fos = new FileOutputStream("svmfile/train/"+problemName+"_train");
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            StringBuilder line = new StringBuilder();
            int dim = blackBoxProblem.dim;
            int NP = p.length;
            SortUtil.spaceSort(p,cost,NP);
            for(int i=0;i<NP;i++){
                for(int j=0;j<i;j++){
                    line.append(1).append(" ");
                    for(int k=0;k<dim;k++){
                        line.append(k+1).append(':').append(p[i][k]).append(' ');
                    }
                    for(int k=0;k<dim;k++){
                        line.append(k+1+dim).append(':').append(p[j][k]).append(" ");
                    }
                    line.append("\r\n");
                    bw.write(line.toString());
                    bw.flush();
                    line.delete(0,line.length());
                }
                for(int j=i+1;j<NP;j++){
                    line.append(-1).append(" ");
                    for(int k=0;k<dim;k++){
                        line.append(k+1).append(':').append(p[i][k]).append(' ');
                    }
                    for(int k=0;k<dim;k++){
                        line.append(k+1+dim).append(':').append(p[j][k]).append(" ");
                    }
                    line.append("\r\n");
                    bw.write(line.toString());
                    bw.flush();
                    line.delete(0,line.length());
                }
            }

            if (bw != null)bw.close();
            if(fos != null)fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("--------------------"+problemName+"训练文件生成结束--------------------");
    }

}

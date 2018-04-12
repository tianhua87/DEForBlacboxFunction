package algorithm;

import file_generate.ModelGenerator;
import file_generate.TrainFileGenerator;
import problem.BlackBoxProblem;
import random.DERandom;
import utilities.ProblemGenerator;
import utilities.SortUtil;

import java.io.*;
import java.util.ArrayList;

public class DEOptimizer {

    public static final int MAX_COUNT = 200;
    public static int oldestIndex = 0;

    //初始数据
    int initPopSize ;
    int NP = 50;
    double Cr = 0.5;
    double F = 0.8;
    double[][] p ;
    double[] cost ;

    public void optimizeModel(String problem){


        BlackBoxProblem blackBoxProblem = ProblemGenerator.generateSVMProblem(problem);
        int dim = blackBoxProblem.dim;
        TrainFileGenerator.COUNT = dim*11+1;

        //生成初始种群
        initPopSize = dim*11+1;
        p = new double[initPopSize][dim];
        cost = new double[initPopSize];
        //DEInitializer deInitializer = new EvenlyDEInitializer();
        DEInitializer deInitializer = new CommonDEInitializer();
        deInitializer.init(p,blackBoxProblem.lowLimit,blackBoxProblem.highLimit,initPopSize,F,Cr,dim);
        caculateCost(problem,p,cost);


        TrainFileGenerator tfg = new TrainFileGenerator();
        //tfg.trainFileGenerate(problem);
        tfg.trainFileGenerate(problem,p,cost,true);
        ModelGenerator mg = new ModelGenerator();
        //mg.generateModelWithoutScale(problem);
        mg.generateModel(problem,true);

        DE de = new DE(dim, NP, F, Cr,  deInitializer, blackBoxProblem);
        double minicost = 0;
        int counter = 0;
        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        while (counter++<MAX_COUNT) {
            de.optimize();
            minicost = bbp.evaluate(de.best,dim);
            //addTrainFile(problem, minicost,de.best,dim);
            //replaceTrainFile(problem, minicost,de.best,dim,bbp);
            //replaceMinTrainFile(problem,minicost,de.best,dim);
            replaceTrainFile(problem, minicost,de.best,dim,true);
            //replaceOldestTrainFile(problem, minicost,de.best,dim);
            //updateModel(problem);
            //updateModel(problem,true);
            updateModelWithBestPara(problem,true);
            de = new DE(dim, NP, F, Cr,  deInitializer, blackBoxProblem);

            System.out.println("使用真实评价的次数： "+(counter+dim*11+1));
        }

        System.out.println("-------------结果------------------------");
        System.out.println(minicost);
        for (int i=0; i<dim;i++) {
            System.out.print(de.best[i]+" ");
        }
        System.out.println();
    }


    //计算每个个体的适应度
    public void caculateCost(String problem,double[][] p,double[] cost) {
        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        for(int i=0;i<p.length;i++) {
            cost[i]=bbp.evaluate(p[i],bbp.dim);
        }
    }

    //更新回归模型，使用默认参数
    public void updateModel(String problem) {
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithoutScale(problem);
    }
    //更新二分类模型，使用默认参数
    public void updateModel(String problem,boolean cc) {
        ModelGenerator mg = new ModelGenerator();
        mg.generateModel(problem,true);
    }

    //更新模型，使用最优参数,二分类模型
    public void updateModelWithBestPara(String problem,boolean cc) {
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithBestPara(problem,true);
    }

    public void addTrainFile(String problem,double minicost,double best[], int dim) {

        String filePath = "svmfile/train/"+problem+"_train";
        try {
            FileWriter fw = new FileWriter(filePath,true);

            StringBuilder sb = new StringBuilder();
            sb.append(minicost).append(" ");
            for (int i = 0; i < dim; i++)
                sb.append(i + 1).append(":").append(best[i]).append(" ");
            sb.append("\r\n");
            fw.write(sb.toString());
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void replaceTrainFile(String problem,double minicost,double best[], int dim, BlackBoxProblem bbp) {

        int counter = 0;
        DERandom deRandom = new DERandom();
        double Cr = 0.6;
        double F = 0.5;
        double x[] = new double[dim];
        for(int i =0 ;i<dim ;i++) {

            x[i] = best[i] * (deRandom.nextDouble(1-Cr,1+Cr));
            System.out.println("++++++++++++++++++++++++++++++++++++++++++++");

        }
        double xcost = bbp.evaluate(x,dim);
        if(xcost < minicost) {
            for(int i=0;i<dim;i++){
                best[i] = x[i];
            }
            minicost = xcost;
        }

        minicost = bbp.evaluate(best,dim);

        String filePath = "svmfile/train/"+problem+"_train";
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            ArrayList<String> list = new ArrayList<>();
            String line;
            while((line = br.readLine())!=null){
                list.add(line);
            }
            int maxIndex = -1;
            //double maxValue = Double.parseDouble(list.get(0).split(" ")[0]);
            double maxValue = Double.MIN_VALUE;
            for(int i=0;i<list.size();i++){
                double v = Double.parseDouble(list.get(i).split(" ")[0]);
                if(v>maxValue){
                    maxValue = v;
                    maxIndex = i;
                }
            }
            if (maxValue > minicost ) {
                StringBuilder sb = new StringBuilder();
                sb.append(minicost).append(" ");
                for (int i = 0; i < dim; i++)
                    sb.append(i + 1).append(":").append(best[i]).append(" ");
                list.remove(maxIndex);
                list.add(maxIndex, sb.toString());
            }

            try {
                FileWriter fw = new FileWriter(filePath);

                for(String str:list){
                    fw.write(str);
                    fw.write("\r\n");
                }
                fw.flush();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void replaceMinTrainFile(String problem,double minicost,double best[], int dim) {

        String filePath = "svmfile/train/"+problem+"_train";
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            ArrayList<String> list = new ArrayList<>();
            String line;
            while((line = br.readLine())!=null){
                list.add(line);
            }
            int minIndex = -1;
            //double maxValue = Double.parseDouble(list.get(0).split(" ")[0]);
            double minValue = Double.MAX_VALUE;
            for(int i=0;i<list.size();i++){
                double v = Double.parseDouble(list.get(i).split(" ")[0]);
                if(v<minValue){
                    minValue = v;
                    minIndex = i;
                }
            }
            if (minValue > minicost ) {
                StringBuilder sb = new StringBuilder();
                sb.append(minicost).append(" ");
                for (int i = 0; i < dim; i++)
                    sb.append(i + 1).append(":").append(best[i]).append(" ");
                list.remove(minIndex);
                list.add(minIndex, sb.toString());
            }

            try {
                FileWriter fw = new FileWriter(filePath);

                for(String str:list){
                    fw.write(str);
                    fw.write("\r\n");
                }
                fw.flush();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void replaceTrainFile(String problem,double minicost,double best[], int dim, boolean cc) {

        String filePath = "svmfile/train/"+problem+"_train";
        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        double realCost = bbp.evaluate(best,dim);

        if(realCost < cost[initPopSize-1]) {
            cost[initPopSize-1]=realCost;
            p[initPopSize-1]=best;
        }
        //SortUtil.spaceSort(p,cost,initPopSize);
        TrainFileGenerator tfg = new TrainFileGenerator();
        tfg.trainFileGenerate(problem,p,cost,true);
    }



    public void replaceOldestTrainFile(String problem,double minicost,double best[], int dim) {

        String filePath = "svmfile/train/"+problem+"_train";
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            ArrayList<String> list = new ArrayList<>();
            String line;
            int count = 0;
            while((line = br.readLine())!=null) {
                list.add(line);
                count++;
            }

            StringBuilder sb = new StringBuilder();
            sb.append(minicost).append(" ");
            for (int i = 0; i < dim; i++)
                sb.append(i + 1).append(":").append(best[i]).append(" ");
            list.remove(oldestIndex%count);
            list.add(oldestIndex%count, sb.toString());
            oldestIndex++;

            try {
                FileWriter fw = new FileWriter(filePath);

                for(String str:list){
                    fw.write(str);
                    fw.write("\r\n");
                }
                fw.flush();
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

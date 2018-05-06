package algorithm;

import file_generate.ModelGenerator;
import file_generate.ParametersFinder;
import file_generate.TrainFileGenerator;
import problem.BlackBoxProblem;
import random.DERandom;
import utilities.ProblemGenerator;
import utilities.SortUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;

public class DEOptimizer {


    public static final int MAX_COUNT = 100;
    public static int oldestIndex = 0;
    public static final String UPDATE_MODEL_ADD = "add";
    public static final String UPDATE_MODEL_OLDEST = "replaceOldest";
    public static final String UPDATE_MODEL_WORST = "replaceWorst";
    public static String updateModel = UPDATE_MODEL_WORST;

    //初始数据
    int initPopSize ;
    int NP = 50;
    double Cr = 0.5;
    double F = 0.8;
    double[][] p ;
    double[] cost ;
    double minCost = Double.MAX_VALUE;
    double globalBest;

    public void optimizeModel(String problem){


        BlackBoxProblem blackBoxProblem = ProblemGenerator.generateSVMProblem(problem);
        int dim = blackBoxProblem.dim;
        TrainFileGenerator.COUNT = dim*11+1;

        //生成初始种群
        initPopSize = dim*11+1;
        p = new double[initPopSize][dim];
        cost = new double[initPopSize];
        DEInitializer deInitializer = new CommonDEInitializer();
        deInitializer.init(p,blackBoxProblem.lowLimit,blackBoxProblem.highLimit,initPopSize,F,Cr,dim);
        caculateCost(problem,p,cost);

        TrainFileGenerator tfg = new TrainFileGenerator();
        ModelGenerator mg = new ModelGenerator();

        if(DE.isReg==false) {
            tfg.trainFileGenerate(problem, p, cost, true);
            mg.generateModelWithBestPara(problem, true);
        }else {
            tfg.trainFileGenerate(problem);
            mg.generateModelWithBestPara(problem);
        }

        double minicost = Double.MAX_VALUE;
        int counter = 0;
        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        DE de = null;
        while (counter++<MAX_COUNT) {
            //开始差分进化算法，求解当前最优解
            de = new DE(dim, NP, F, Cr,  deInitializer, blackBoxProblem);
            long start = Calendar.getInstance().getTimeInMillis();
            de.optimize();
            long end = Calendar.getInstance().getTimeInMillis();
            saveDETime(problem,end-start,counter+dim*11+1);
            //将当前最优解做真实评价，添加到训练文件中
            if(DE.isReg == false) {
                if(updateModel.equals(UPDATE_MODEL_ADD)) {
                    replaceTrainFile(problem, minicost, de.best, dim, true);
                }else if(updateModel.equals(UPDATE_MODEL_WORST)) {
                    replaceWorstTrainFile(problem, minicost, de.best, dim, true);
                }
                //使用最优参数，训练模型
                updateModelWithBestPara(problem, true);
            }else {
                addTrainFile(problem,de.best,dim);
                updateModelWithBestPara(problem);
            }

            globalBest = cost[0];
            System.err.println("当前最优解: "+globalBest);
            writeBestSolution(problem,bbp.evaluate(de.best,dim),globalBest,counter+dim*11+1);
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

    public void saveDETime(String problem,long time,int count) {
        File f = new File("svmfile/time/"+problem+"_de");

        try {
            if (!f.exists())
                f.createNewFile();
            FileWriter fw=new FileWriter(f,true);
            fw.write(count+" "+time+" "+"\r\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveFindParaTime(String problem,long time,int count) {
        File f = new File("svmfile/time/"+problem+"_para");

        try {
            if (!f.exists())
                f.createNewFile();
            FileWriter fw=new FileWriter(f,true);
            fw.write(count+" "+time+" "+"\r\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeBestSolution(String problem,double deBest,double globalBest,int count) {
        File f = new File("svmfile/best/"+problem);

        try {
            if (!f.exists())
                f.createNewFile();
            FileWriter fw=new FileWriter(f,true);
            fw.write(count+" "+deBest+" "+globalBest+"\r\n");
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
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
    //更新模型，使用最优参数,回归模型
    public void updateModelWithBestPara(String problem) {
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithBestPara(problem);
    }

    //回归模型，直接将DE优化出来的最优解加入到训练集中
    public void addTrainFile(String problem,double best[], int dim) {

        String filePath = "svmfile/train/"+problem+"_train";
        try {
            FileWriter fw = new FileWriter(filePath,true);
            BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
            StringBuilder sb = new StringBuilder();
            sb.append(bbp.evaluate(best,dim)).append(" ");
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

    //分类模型，直接将DE优化出来的最优解加入到训练集中
    public void replaceTrainFile(String problem,double minicost,double best[], int dim, boolean cc) {
        String filePath = "svmfile/train/"+problem+"_train";
        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        double realCost = bbp.evaluate(best,dim);

        initPopSize++;
        double[][] pp = new double[initPopSize][dim];
        double []ccost = new double[initPopSize];
        for(int ll=0;ll<initPopSize-1;ll++) {
            pp[ll]=p[ll];
            ccost[ll]=cost[ll];
        }
        pp[initPopSize-1]=best;
        ccost[initPopSize-1]=realCost;
        p=pp;
        cost=ccost;
//        if(realCost < cost[initPopSize-1]) {
//            cost[initPopSize-1]=realCost;
//            p[initPopSize-1]=best;
//        }
        //SortUtil.spaceSort(p,cost,initPopSize);
        TrainFileGenerator tfg = new TrainFileGenerator();
        tfg.trainFileGenerate(problem,p,cost,true);
    }

    //分类模型，替换最坏的解
    public void replaceWorstTrainFile(String problem,double minicost,double best[], int dim, boolean cc) {
        String filePath = "svmfile/train/"+problem+"_train";
        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        double realCost = bbp.evaluate(best,dim);

        if(realCost < cost[initPopSize-1]) {
            p[initPopSize-1]=best;
            cost[initPopSize-1] = realCost;
        }
        TrainFileGenerator tfg = new TrainFileGenerator();
        tfg.trainFileGenerate(problem,p,cost,true);
    }


}

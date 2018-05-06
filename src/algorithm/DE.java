package algorithm;

import file_generate.TrainFileGenerator;
import problem.BlackBoxProblem;
import problem.SVMProblem;
import random.DERandom;
import strategy.DEStrategy;
import strategy.DEStrategyConst;
import utilities.ProblemGenerator;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DE {

    public static  boolean isReg =false;

    public int generation  = 0;
    public double mincost  = Double.MAX_VALUE;
    public double lastMincost = Double.MAX_VALUE ;

    public int     dim;
    public int     NP;
    public double  F;
    public double  Cr;
    public double lowLimit;
    public double highLimit;
    public DEInitializer initializer;
    public DERandom deRandom;

    public DEStrategy Strategem[];
    public int current_strategy = 1;

    //如果 MAX_COUNTER 次后当前最优解依然没有改变 ，则终止进化
    private int counter = 0;
    private static final int MAX_COUNTER = 100;

    public BlackBoxProblem blackBoxProblem;

    int MaxD          = 30;   //maximum number of parameters
    int MaxN          = 500;  //maximum for NP
    int MaxR          = 10;   //maximum number of random choices
    int MaxGeneration = 200;

    double trial[]       = new double [MaxD]; // the trial vector
    public double best[] = new double [MaxD]; // the best vector so far
    double genbest[]     = new double [MaxD]; // the best vector of the current generation
    double cost[]        = new double [MaxN];

    double p1[][]     = new double [MaxN][MaxD]; // array of vectors
    double p2[][]     = new double [MaxN][MaxD];

    double rvec[][]   = new double [MaxR][MaxD]; // array of randomly chosen vectors
    int rnd[]         = new int [MaxR];		   // array of random indices

    double g0[][];	// just some pointers (placeholders)
    double g1[][];

    int    min_index   = 0;

    int all = 0,right = 0;
    BlackBoxProblem bbpT ;

    public void initStrategy(){
        String S[] = DEStrategyConst.DEStrategyName;
        Strategem = new DEStrategy [S.length];
        for (int i = 0;  i < S.length;  i++) {
            try {
                Class C = Class.forName ("strategy." + S[i]);
                Strategem[i] = (DEStrategy)C.newInstance();
                Strategem[i].init (deRandom);
            }
            catch (Exception e) {
                System.err.println (e);
            };
        }
    }

    public void init(){
        initStrategy();
        initializer.init(p1,lowLimit,highLimit,NP,F,Cr,dim);
        dim = blackBoxProblem.dim;

        //回归
        if(isReg) {
            for (int i = 0; i < NP; i++) {
                cost[i] = blackBoxProblem.evaluate(p1[i], dim);
            }
            mincost = cost[0];
            min_index = 0;
            for (int i = 0; i < NP; i++) {
                double x = cost[i];
                if (x < mincost) {
                    mincost = x;
                    min_index = i;
                }
            }
        }

        //二分类
        if (!isReg) {
            if (blackBoxProblem.evaluate(concat(p1[0], dim, p1[1], dim), 2 * dim) == -1) {
                min_index = 0;
                if (isBetter(p1[0], p1[1], dim)) {
                    right++;
                }
            } else {
                min_index = 1;
                if (isBetter(p1[0], p1[1], dim) == false) {
                    right++;
                }
            }
            all++;

            for (int i = 2; i < NP; i++) {
                if (blackBoxProblem.evaluate(concat(p1[i], dim, p1[min_index], dim), 2 * dim) == -1) {
                    min_index = i;
                    if (isBetter(p1[i], p1[min_index], dim)) {
                        right++;
                    }
                }
                all++;
            }
        }
        //System.out.println(mincost+" "+min_index);
        assign (best, p1[min_index]);
        assign (genbest, best);
        g0 = p1;   // generation t
        g1 = p2;   // generation t+1

    }

    public DE(int dim, int NP, double f, double cr, DEInitializer initializer, BlackBoxProblem blackBoxProblem) {
        this.dim = dim;
        this.NP = NP;
        F = f;
        Cr = cr;
        this.lowLimit = blackBoxProblem.lowLimit;
        this.highLimit = blackBoxProblem.highLimit;
        this.initializer = initializer;
        deRandom = new DERandom();
        this.blackBoxProblem = blackBoxProblem;
        if(blackBoxProblem instanceof SVMProblem)
            bbpT = ProblemGenerator.generateBBProblem(((SVMProblem)blackBoxProblem).getPROBLEM_NMAE());
        else
            bbpT = blackBoxProblem;
        init();

    }

    public double optimize () {

        ArrayList<String> evolution = new ArrayList<>();



        while (!isCompleted()) {
            for (int i = 0;  i < NP;  i++) {
                assign (trial, g0[i]);
                //从种群中随机选取6个不同个体，用做变异使用
                do rnd[0] = deRandom.nextInt (NP);
                while (rnd[0] == i);
                do rnd[1] = deRandom.nextInt (NP);
                while ((rnd[1] == i) || (rnd[1] == rnd[0]));
                do rnd[2] = deRandom.nextInt (NP);
                while ((rnd[2] == i) || (rnd[2] == rnd[1]) || (rnd[2] == rnd[0]));
                do rnd[3] = deRandom.nextInt (NP);
                while ((rnd[3] == i) || (rnd[3] == rnd[2]) || (rnd[3] == rnd[1]) || (rnd[3] == rnd[0]));
                do rnd[4] = deRandom.nextInt (NP);
                while ((rnd[4] == i) || (rnd[4] == rnd[3]) || (rnd[4] == rnd[2]) || (rnd[4] == rnd[1]) || (rnd[4] == rnd[0]));
                do rnd[5] = deRandom.nextInt (NP);
                while ((rnd[5] == i) || (rnd[5] == rnd[4]) || (rnd[5] == rnd[3]) || (rnd[5] == rnd[2])
                        || (rnd[5] == rnd[1]) || (rnd[5] == rnd[0]));

                for (int k=0; k<6; k++) {
                    rvec[k] = g0[rnd[k]];
                }

                //变异与交叉
                Strategem[current_strategy].apply (F, Cr, dim, trial, genbest, rvec);
                //若变异后超出了定义域，修正个体
                for(int k =0;k<dim;k++){
                    if(trial[k]<blackBoxProblem.lowLimit || trial[k]>blackBoxProblem.highLimit){
                        trial[k] = deRandom.nextDouble(blackBoxProblem.lowLimit,blackBoxProblem.highLimit);
                    }
                }

                //回归
                if (isReg) {
                    double testcost = blackBoxProblem.evaluate(trial, dim);
                    if (testcost <= cost[i]) {
                        all++;
                        if(isBetter(trial,g0[i],dim)){
                            right++;
                        }else {
                            //System.err.println(bbpT.evaluate(trial,dim)+" "+bbpT.evaluate(g0[i],dim));
                        }
                        assign(g1[i], trial);
                        cost[i] = testcost;
                        if (testcost < mincost) {
                            all++;
                            if(isBetter(trial,best,dim)){
                                right++;
                            }else {
                                //System.err.println(bbpT.evaluate(trial,dim)+" "+bbpT.evaluate(best,dim));
                            }
                            mincost = testcost;
                            assign(best, trial);
                            min_index = i;
                        }
                    } else {
                        assign(g1[i], g0[i]);
                    }
                }

                //二分类
                if (!isReg) {
                    all++;
                    double testcost = blackBoxProblem.evaluate(concat(trial, dim, g0[i], dim), 2 * dim);
                    if (testcost == -1) {
                        if (isBetter(trial, g0[i], dim)) {
                            right++;
                        }else {
                            //System.err.println(bbpT.evaluate(trial,dim)+" "+bbpT.evaluate(g0[i],dim));
                        }
                        assign(g1[i], trial);
                        cost[i] = testcost;
                        all++;
                        if (blackBoxProblem.evaluate(concat(trial, dim, best, dim), 2 * dim) == -1) {
                            if (isBetter(trial, best, dim)) {
                                right++;
                            }else {
                                //System.err.println(bbpT.evaluate(trial,dim)+" "+bbpT.evaluate(best,dim));
                            }

                            mincost = testcost;
                            assign(best, trial);
                            min_index = i;
                        }
                    } else {
                        assign(g1[i], g0[i]);
                        if (isBetter(trial, g0[i], dim) == false) {
                            right++;
                        }else {
                            //System.err.println(bbpT.evaluate(trial,dim)+" "+bbpT.evaluate(g0[i],dim));
                        }
                    }
                }

        }
            if (lastMincost != mincost) {
                counter = 0;
                lastMincost = mincost;
            } else {
                counter++;
            }

            assign (genbest, best);

            double gx[][] = g0;
            g0 = g1;
            g1 = gx;
            //System.out.println("代数：" + generation);
            generation++;
            evolution.add(bbpT.evaluate(best,dim)+"");
        }
        for (int i = 0 ;i < dim;i++)
            System.out.print(best[i]+" ");
        System.out.println();
        System.out.println(mincost);
        //如果是svm预测的结果，计算其真实结果
        if (blackBoxProblem instanceof SVMProblem) {
            String problemName = ((SVMProblem)blackBoxProblem).getPROBLEM_NMAE();
            try {
                BlackBoxProblem bbProblem = (BlackBoxProblem) Class.forName("problem."+problemName).newInstance();
                double realResult = bbProblem.evaluate(best,dim);
                System.out.println("真实结果：" + realResult);
                writeResult(problemName,best,dim,mincost,realResult);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(blackBoxProblem instanceof SVMProblem)
            saveEvolutionProcess(((SVMProblem)blackBoxProblem).getPROBLEM_NMAE(),evolution);
        System.err.println("*********预测准确率："+1.0*right/all+"  "+right+"  "+all);

        return mincost;
    }

    private void assign (double[] to, double[] from) {
        int i;
        for (i=0; i<dim; i++) {
            to[i] = from[i];
        }
    }

    //判断个体x1是否比个体x2适应度更好
    public boolean isBetter(double[]x1,double[] x2,int dim) {
        double res1 = bbpT.evaluate(x1,dim);
        double res2 = bbpT.evaluate(x2,dim);
        if(res1<res2)
            return true;
        else
            return false;
    }

    private void saveEvolutionProcess(String problem,ArrayList<String> evolution) {
        String filePath = "svmfile/evolution/"+problem;
        File file =new File(filePath);
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter fw = new FileWriter(file);
            for(String s:evolution){
                fw.write(s+"\r\n");
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isCompleted(){
        return counter >= MAX_COUNTER;
    }

    private void writeResult(String problem, double[] best, int dim,double predictResult, double realResult){

        try {
            File file = new File("statistic/"+problem);
            if(!file.exists())
                file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file,true);
            BufferedWriter br = new BufferedWriter(new OutputStreamWriter(fos));
            StringBuilder sb = new StringBuilder();
            sb.append(problem).append(" ");
            sb.append(dim).append(" ");
            sb.append(TrainFileGenerator.COUNT).append(" ");
            for (int i=0;i<dim;i++) {
                sb.append(best[i]).append(" ");
            }
            sb.append(predictResult).append(" ");
            sb.append(realResult).append(" ");
            sb.append("\r\n");
            br.write(sb.toString());
            br.flush();
            br.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    double[] concat(double[] A, int aLen,double[] B,int bLen) {
        double[] C= new double[aLen+bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        //System.out.println("--------------------------------"+Arrays.toString(C)+"---------------------------------------");
        return C;
    }


    public void recordLowAccuracyModel(String problem, double accuracy) {
        String filepathAc = "svmfile/accuracy/"+problem+accuracy;
        String filepathModel = "svmfile/model/"+problem+"_model";
        File file = new File(filepathAc);

        try {
            if (!file.exists()) file.createNewFile();
            FileInputStream fis = new FileInputStream(filepathModel);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            FileWriter fos = new FileWriter(file);
            String line="";
            while((line=br.readLine())!=null) {
                fos.write(line);
                fos.write("\r\n");
            }

            fos.flush();
            fos.close();
            br.close();
            fis.close();


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

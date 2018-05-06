package test;

import algorithm.EvenlyDEInitializer;
import file_generate.ModelGenerator;
import file_generate.ParametersFinder;
import file_generate.TrainFileGenerator;
import problem.BlackBoxProblem;
import random.DERandom;
import svm.svm_predict;
import utilities.NumberUtil;
import utilities.ProblemGenerator;

import java.io.*;

public class SVMTest {

    public static void main(String[] args) {

        SVMTest svmTest = new SVMTest();
        svmTest.testSVMAccuracy();
//        double sum=0;
//        int count=1;
//        for(int i=0;i<count;i++) {
//            sum+=svmTest.testBinarySVMAccuracy("Branin");
//        }
//        System.out.print(sum/count);
//        String problem = "Branin";

        //svmTest.testModel(problem,"Branin_model");
    }

    public void testModelAccuracy() {
        String problem = "Branin";
        EvenlyDEInitializer initializer = new EvenlyDEInitializer();
        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        int NP = 100;
        double F = 0.5;
        double Cr = 0.2;
        int dim =bbp.dim;
        double [][] p=new double[NP][bbp.dim];
        double[] cost=new double[NP];
        initializer.init(p,bbp.lowLimit,bbp.highLimit,NP,F,Cr,dim);
        for (int i=0;i<NP;i++) {
            cost[i]=bbp.evaluate(p[i],dim);
        }
        TrainFileGenerator tfg = new TrainFileGenerator();
        tfg.trainFileGenerate(problem,p,cost,true);
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithBestPara(problem,true);
    }

    //测试回归模型均方误差
    public void testSVMAccuracy() {
        //Ackley,Beale,Bohachevsky1,Branin,Rastrigin,Shekel2,Kowalik,SixHumpCamel'
        String problem = "Branin";

        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        int count = bbp.dim*11+1;
        TrainFileGenerator tfg = new TrainFileGenerator();
        TrainFileGenerator.COUNT = count;
        tfg.trainFileGenerate(problem);

        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithBestPara(problem);

        BlackBoxProblem bbpSVM = ProblemGenerator.generateSVMProblem(problem);

        double[] t = new double[bbp.dim];
        double sum = 0;
        int testCount = 1000;
        DERandom deRandom = new DERandom();
        double x[] = new double[bbp.dim];
        double[] real = new double[testCount];
        double[] predict = new double[testCount];
        for (int i=0;i<testCount;i++) {
            generateRandomX(x,bbp.dim,bbp.lowLimit,bbp.highLimit);
            real[i] = bbp.evaluate(x,bbp.dim);
            predict[i] = bbpSVM.evaluate(x,bbp.dim);
            sum+=(real[i]-predict[i])*(real[i]-predict[i]);
            System.out.println(real[i]+" "+predict[i]);
        }
        int right=0;
        int all =0;
        for(int i=0;i<testCount;i++){
            for(int j=i+1;j<testCount;j++){
                all++;
                if(real[i]>real[j]&&predict[i]>predict[j] || real[i]<real[j]&&predict[i]<predict[j]){
                    right++;
                }
            }
        }
        System.out.println(1.0*right/all);
        //System.out.println(sum/testCount);
    }

    public void generateRandomX(double[] x,int dim,double low,double high) {
        DERandom deRandom = new DERandom();
        for(int i=0;i<dim;i++){
            x[i]=deRandom.nextDouble(low,high);
        }
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

    public double testBinarySVMAccuracy(String problem) {
        TrainFileGenerator tfg = new TrainFileGenerator();
        EvenlyDEInitializer initializer = new EvenlyDEInitializer();
        BlackBoxProblem blackBoxProblem = ProblemGenerator.generateBBProblem(problem);
        int dim = blackBoxProblem.dim;
        int NP = 11*dim+1;
        double[][] p1 = new double[NP][dim];
        double[] cost = new double[NP];

        initializer.init(p1,blackBoxProblem.lowLimit,blackBoxProblem.highLimit,NP,0.5,0.5,dim);

        for(int i=0;i<11*dim+1;i++) {
            cost[i] = blackBoxProblem.evaluate(p1[i],dim);
        }
        tfg.trainFileGenerate(problem,p1,cost,true);
        ModelGenerator mg = new ModelGenerator();
        mg.generateModelWithBestPara(problem,true);

        BlackBoxProblem bbpSVM = ProblemGenerator.generateSVMProblem(problem);

        int testCount = 1000;

        double[] x1 = new double[dim];
        double[] x2 = new double[dim];
        int right =0;
        for (int i=0;i<testCount;i++) {
            generateTextVector(blackBoxProblem,x1,dim);
            generateTextVector(blackBoxProblem,x2,dim);
            double res1 = blackBoxProblem.evaluate(x1,dim);
            double res2 = blackBoxProblem.evaluate(x2,dim);
            double pre = bbpSVM.evaluate(NumberUtil.concat(x1,dim,x2,dim),2*dim);
            if((pre == -1) && res1<res2) {
                right++;
            }else if((pre == 1) && res1>res2) {
                right++;
            }

            System.out.println((res1<res2?-1:1)+" "+pre);

        }

        System.out.println("准确率："+1.0*right/testCount);
        return 1.0*right/testCount;
    }

    public void generateTextVector(BlackBoxProblem bbp,double[] x,int dim) {
        DERandom deRandom = new DERandom();
        for(int i=0;i<dim;i++){
            x[i]=deRandom.nextDouble(bbp.lowLimit,bbp.highLimit);
        }
    }

    public void testModel(String problem,String modelName) {

        BlackBoxProblem blackBoxProblem = ProblemGenerator.generateBBProblem(problem);
        int dim = blackBoxProblem.dim;
        int testCount = 1000;
        double[] x1 = new double[dim];
        double[] x2 = new double[dim];
        int right =0;
        for (int i=0;i<testCount;i++) {
            generateTextVector(blackBoxProblem,x1,dim);
            generateTextVector(blackBoxProblem,x2,dim);
            double res1 = blackBoxProblem.evaluate(x1,dim);
            double res2 = blackBoxProblem.evaluate(x2,dim);
            if((evaluate(modelName,problem,NumberUtil.concat(x1,dim,x2,dim),2*dim) == -1) && res1<res2) {
                right++;
            }else if((evaluate(modelName,problem,NumberUtil.concat(x1,dim,x2,dim),2*dim) == 1) && res1>res2) {
                right++;
            }
        }

        System.out.println("准确率："+1.0*right/testCount);
    }


    public void predict(String problemName,String model){
        String modelPath = "svmfile/accuracy/"+model;
        String resultPath = "svmfile/result/"+problemName + "_result";
        String predictPath = "svmfile/predict/"+problemName + "_predict";
        String args = predictPath + " " + modelPath + " " + resultPath;
        try {
            svm_predict.main(args.split(" "));
            //System.out.println("-------------预测结束---------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public double evaluate(String model,String problem,double[] X, int dim) {
        generatePredictFile(problem,X,dim);  //将X整理成符合svm的格式写入预测文件中

        predict(problem,model); //预测结果，结果保存在文件中
        double res = readResult(problem);

        return res;
    }


    void generatePredictFile(String problem,double[] X, int dim){
        String predictPath = "svmfile/predict/"+problem+"_predict";
        String vector = "1";
        for (int i=0;i<dim;i++){
            vector+=" "+(i+1)+":"+X[i];
        }
        //System.out.println(vector);
        try {
            FileOutputStream fos = new FileOutputStream(predictPath);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(vector);
            bw.flush();
            bw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    double readResult(String problem){
        String predictPath = "svmfile/result/"+problem+"_result";
        String res ="";
        try {
            FileInputStream fis = new FileInputStream(predictPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            res = br.readLine();
            br.close();
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Double.parseDouble(res);
    }

}

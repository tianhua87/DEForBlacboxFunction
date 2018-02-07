package problem;

import file_generate.Predictor;
import file_generate.ScaleFileGenerator;

import java.io.*;

public class SVMProblem extends BlackBoxProblem {

    double max = 1;
    double min = -1;
    double upper = 1;
    double lower = -1;

    protected String PROBLEM_NMAE ;
    ScaleFileGenerator sfg = new ScaleFileGenerator();
    Predictor predictor = new Predictor();

    public SVMProblem(double lowLimit, double highLimit, String problemName) {
        super(lowLimit, highLimit);
        this.PROBLEM_NMAE = problemName;
        setBoundary();
    }

    @Override
    public double evaluate(double[] X, int dim) {
        generatePredictFile(X,dim);  //将X整理成符合svm的格式写入预测文件中
        sfg.predictFileScale(PROBLEM_NMAE); //对预测文件进行归一化
        predictor.predict(PROBLEM_NMAE); //预测结果，结果保存在文件中
        double res = readResult();
        //System.out.println(res);
        res =(res-lower)*(max-min)/(upper-lower)  + min; //对结果进行反归一化
        System.out.println("实际结果:"+res);
        return res;
    }

    void generatePredictFile(double[] X, int dim){
        String predictPath = "svmfile/predict/"+PROBLEM_NMAE+"_predict";
        String vector = "666";
        for (int i=0;i<dim;i++){
            vector+=" "+(i+1)+":"+X[i];
        }
        System.out.println(vector);
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

    double readResult(){
        String predictPath = "svmfile/result/"+PROBLEM_NMAE+"_result";
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

    protected void setBoundary(){
        String scaleParaPath = "svmfile/scale/" + PROBLEM_NMAE + "_scale_parameters";
        try {
            FileInputStream fis = new FileInputStream(scaleParaPath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            br.readLine();//读第一行
            String UL[] = br.readLine().split(" ");
            String MN[] = br.readLine().split(" ");
            upper = Double.parseDouble(UL[1]);
            lower = Double.parseDouble(UL[0]);
            max = Double.parseDouble(MN[1]);
            min = Double.parseDouble(MN[0]);
            //System.out.println(lower + " " + upper + " " + min + " " +max);
            br.close();
            fis.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

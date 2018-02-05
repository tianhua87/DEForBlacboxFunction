package problem;

import file_generate.Predictor;
import file_generate.ScaleFileGenerator;

import java.io.*;

public class Beale_SVM extends BlackBoxProblem {
    double max = 120677.3310349569;
    double min = 0.1678246891654736;
    double upper = 10;
    double lower = -10;
    public Beale_SVM() {
        super(-4.5, 4.5);
    }

    public static final String PROBLEM_NMAE= "Beale";
    ScaleFileGenerator sfg = new ScaleFileGenerator();
    Predictor predictor = new Predictor();
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
}

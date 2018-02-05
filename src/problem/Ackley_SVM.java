package problem;

import file_generate.Predictor;
import file_generate.ScaleFileGenerator;

import java.io.*;

public class Ackley_SVM extends BlackBoxProblem {
    double max = 21.89534720155972;
    double min = 17.57112251059377;
    double upper = 10;
    double lower = -10;
    public Ackley_SVM() {
        super(-30, 30);
    }

    public static final String PROBLEM_NMAE= "Ackley";
    ScaleFileGenerator sfg = new ScaleFileGenerator();
    Predictor predictor = new Predictor();
    @Override
    public double evaluate(double[] X, int dim) {

        generatePredictFile(X,dim);
        sfg.predictFileScale(PROBLEM_NMAE);
        predictor.predict("Ackley");
        double res = readResult();
        res =(res-lower)/(upper-lower) *(max-min) + min;
        System.out.println(res);
        return res;
    }

    void generatePredictFile(double[] X, int dim){
        String predictPath = "svmfile/predict/"+PROBLEM_NMAE+"_predict";
        String vector = "20";
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

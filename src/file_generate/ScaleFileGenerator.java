package file_generate;


import svm.svm_scale;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ScaleFileGenerator {

    public static final String SVM_FILE_PATH = "svmfile/";

    public double lower = -10;
    public double upper = 10;

    public ScaleFileGenerator(){}

    public ScaleFileGenerator(double lower, double upper) {
        this.lower = lower;
        this.upper = upper;
    }

    public void trainFileScale(String problemName){
        String parametersFilePath = SVM_FILE_PATH + "scale/"+problemName+"_scale_parameters";
        String trainFilePath = SVM_FILE_PATH + "train/"+problemName+"_train";
        String scaleFilePath = SVM_FILE_PATH + "scale/"+problemName+"_scale";
        String[] args = new String[]{
                "-y",
                lower+"",
                upper+"",
                "-s",
                parametersFilePath,
                trainFilePath
        };
        try {
            String res = svm_scale.main(args);
            FileOutputStream fos = new FileOutputStream(scaleFilePath);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(res);
            bw.flush();
            //System.out.println(res);
            bw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("--------------------"+problemName+"训练文件归一化结束--------------------");
    }

    public void predictFileScale(String problemName){
        String parametersFilePath = SVM_FILE_PATH + "scale/"+problemName+"_scale_parameters";
        String predictFilePath = SVM_FILE_PATH + "predict/"+problemName+"_predict";
        String predictScaledFilePath = SVM_FILE_PATH + "predict/"+problemName+"_predict_scaled";
        String[] args = new String[]{
                "-y",
                lower+"",
                upper+"",
                "-r",
                parametersFilePath,
                predictFilePath
        };
        try {
            String res = svm_scale.main(args);
            FileOutputStream fos = new FileOutputStream(predictScaledFilePath);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(res);
            bw.flush();
            bw.close();
            fos.close();
            //System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //System.out.println("--------------------"+problemName+"预测文件归一化结束--------------------");
    }
}

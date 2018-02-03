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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void predictFileScale(String problemName){

    }
}

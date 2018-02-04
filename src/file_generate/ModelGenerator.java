package file_generate;

import svm.svm_train;

import java.io.*;

public class ModelGenerator {

    public void generateModel(String problemName){
        String parameter = readParameters(problemName);
        String scaleTrainFilePath = "svmfile/scale/"+problemName+"_scale";
        String modelFilrPath = "svmfile/model/"+problemName+"_model";
        //System.out.println(parameter);
        String args = "-s 3 -t 2 "+parameter+" "+scaleTrainFilePath+" "+modelFilrPath;
        try {
            svm_train.main(args.split(" "));
            System.out.println("--------------------"+problemName+"模型生成结束--------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readParameters(String problemName){
        try {
            FileInputStream fis =new FileInputStream("svmfile/parameters/"+problemName+"_para_reg");
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String res = br.readLine();
            br.close();
            fis.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

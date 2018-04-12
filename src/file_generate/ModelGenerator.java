package file_generate;

import svm.svm_train;

import java.io.*;

public class ModelGenerator {

    //归一化，使用最优参数,训练模型，回归模型
    public void generateModel(String problemName){
        String parameter = readParameters(problemName,true);
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

    //不归一化，使用默认参数，训练模型，二分类模型
    public void generateModel(String problemName, boolean campared){
        String trainFilePath = "svmfile/train/"+problemName+"_train";
        String modelFilrPath = "svmfile/model/"+problemName+"_model";
        //System.out.println(parameter);
        String args = trainFilePath+" "+modelFilrPath;
        try {
            svm_train.main(args.split(" "));
            System.out.println("--------------------"+problemName+"模型生成结束--------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //不归一化，使用最优参数，训练模型，二分类模型
    public void generateModelWithBestPara(String problemName, boolean campared){
        String trainFilePath = "svmfile/train/"+problemName+"_train";
        String modelFilrPath = "svmfile/model/"+problemName+"_model";
        generateBestPara(problemName);
        String parameters = readParameters(problemName,false);
        //System.out.println(parameter);
        String args = parameters+" "+trainFilePath+" "+modelFilrPath;
        try {
            svm_train.main(args.split(" "));
            System.out.println("--------------------"+problemName+"模型生成结束--------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //训练集不归一化，使用默认参数，训练模型，回归模型
    public void generateModelWithoutScale(String problemName){
        String trainFilePath = "svmfile/train/"+problemName+"_train";
        String modelFilrPath = "svmfile/model/"+problemName+"_model";
        //System.out.println(parameter);
        String args = "-s 3 -t 2 "+trainFilePath+" "+modelFilrPath;
        try {
            svm_train.main(args.split(" "));
            System.out.println("--------------------"+problemName+"模型生成结束--------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readParameters(String problemName, boolean isReg){
        try {
            String filePath;
            if(isReg) {
                filePath = "svmfile/parameters/"+problemName+"_para_reg";
            }else {
                filePath = "svmfile/parameters/"+problemName+"_para_binary";
            }
            FileInputStream fis =new FileInputStream(filePath);
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

    //寻找二分类的最优参数
    public void generateBestPara(String problem) {
        ParametersFinder pf = new ParametersFinder();
        pf.findBinaryTrainParameters(problem);
    }
}

package file_generate;

import svm.svm_predict;

import java.io.IOException;

public class Predictor {

    public void predict(String problemName){
        String modelPath = "svmfile/model/"+problemName+"_model";
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

//    public void predict(String problemName){
//        String modelPath = "svmfile/model/"+problemName+"_model";
//        String resultPath = "svmfile/result/"+problemName + "_result";
//        String predictPath = "svmfile/predict/"+problemName + "_predict_scaled";
//        String args = predictPath + " " + modelPath + " " + resultPath;
//        try {
//            svm_predict.main(args.split(" "));
//            //System.out.println("-------------预测结束---------------------------------");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public void predictNoScale(String problemName){
        String modelPath = "svmfile/model/"+problemName+"_model_noscale";
        String resultPath = "svmfile/result/"+problemName + "_result";
        String predictPath = "svmfile/predict/"+problemName + "_predict_noscale";
        String args = predictPath + " " + modelPath + " " + resultPath;
        try {
            svm_predict.main(args.split(" "));
            //System.out.println("-------------预测结束---------------------------------");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

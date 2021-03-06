package file_generate;

import java.io.*;

public class ParametersFinder {

    public static final String regScriptFile = "gridregression.py";
    public static final String binaryScriptFile = "grid.py";


    //寻找回归的最优参数
    public void findRegTrainParameters(String problemName){
        File scaleFile = new File("svmfile/train/"+problemName+"_train");
        String scaleFileAbsoluPath = scaleFile.getAbsolutePath();
        File scriptFile = new File("svmfile/script/"+regScriptFile);
        String scriptFileAbsoluPath = scriptFile.getAbsolutePath();

        String parameters = execPythonScript(scriptFileAbsoluPath,scaleFileAbsoluPath);
        if(parameters.length()!=0){

            String []p = parameters.split(" ");
            String ps = "-c "+p[0]+" -g "+p[1]+" -p "+p[2];

            String parameterFilePath = "svmfile/parameters/"+problemName+"_para_reg";
            writeFile(parameterFilePath,ps);
        }
        System.out.println("--------------------"+problemName+"参数寻优生成结束--------------------");

    }


    public void findBinaryTrainParameters(String problemName){
        File trainFile = new File("svmfile/train/"+problemName+"_train");
        String trainFileAbsoluPath = trainFile.getAbsolutePath();
        File scriptFile = new File("svmfile/script/"+binaryScriptFile);
        String scriptFileAbsoluPath = scriptFile.getAbsolutePath();

        String parameters = execPythonScript(scriptFileAbsoluPath,trainFileAbsoluPath);
        if(parameters.length()!=0){

            String []p = parameters.split(" ");
            String ps = "-c "+p[0]+" -g "+p[1]+" -r "+p[2];

            String parameterFilePath = "svmfile/parameters/"+problemName+"_para_binary";
            writeFile(parameterFilePath,ps);
        }
        System.out.println("--------------------"+problemName+"参数寻优生成结束--------------------");

    }


    public void writeFile(String filePath,String str){
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(str);
            bw.flush();
            bw.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String execPythonScript(String scriptFile, String arg){
        String parameters = "";
        try {
            Process process = Runtime.getRuntime().exec("python "+ scriptFile + " "+arg);
            System.out.println("python "+ scriptFile + " "+arg);
            InputStreamReader ir = new InputStreamReader(process.getInputStream());
            BufferedReader br = new BufferedReader(ir);
            String line;
            while((line = br.readLine())!=null){
                System.out.println(line);
                if(line != null)
                    parameters = line;
            }
            process.waitFor();
            System.out.println("参数："+parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return parameters;
    }

}

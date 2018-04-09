package test;

import problem.BlackBoxProblem;
import random.DERandom;
import utilities.NumberUtil;
import utilities.ProblemGenerator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

public class InitSet {

    public static void main(String[] args) {
        InitSet initSet = new InitSet();
        initSet.generateInitSet("Beale");
        //initSet.generateCommonSet("Beale");
    }

    public void generateCommonSet(String problem) {
        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        int NP = 50;
        double[][] p = new double[NP][bbp.dim];
        DERandom deRandom = new DERandom();
        for(int i=0;i<NP;i++) {
            for(int j=0;j<bbp.dim;j++) {
                p[i][j] = deRandom.nextDouble(bbp.lowLimit,bbp.highLimit);
            }
        }
        generateFile(problem,p);
    }

    public void generateInitSet(String problem) {

        BlackBoxProblem bbp = ProblemGenerator.generateBBProblem(problem);
        double xmin[] = new double[bbp.dim+1];
        double xmax[] = new double[bbp.dim+1];
        for(int i = 0; i <= bbp.dim; i++) {
            xmin[i] = bbp.lowLimit;
            xmax[i] = bbp.highLimit;
        }
        int NP =50;
        double[][] p= new double[NP][bbp.dim];
        NumberUtil.latin_hyp(p,NP,bbp.dim,xmin,xmax);

//        for(int i=0;i<NP;i++){
//            for(int j=0;j<bbp.dim;j++){
//                System.out.print(p[i][j]+" ");
//            }
//            System.out.println();
//        }

        generateFile(problem,p);

    }

    public void generateFile(String problem, double[][] p) {
        String filePath = "svmfile/initSet/"+problem;
        try {
            FileWriter fw = new FileWriter(new File(filePath));
            for(int i=0;i<p.length;i++){
                for(int j=0;j<p[i].length;j++){
                    fw.write(p[i][j]+" ");
                }
                fw.write("\r\n");
            }
            fw.flush();
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

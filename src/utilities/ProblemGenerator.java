package utilities;

import problem.BlackBoxProblem;

public class ProblemGenerator {

    public static BlackBoxProblem generateBBProblem(String problem){
        BlackBoxProblem bbProblem = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problem).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bbProblem;
    }

    public static BlackBoxProblem generateSVMProblem(String problem){
        BlackBoxProblem bbProblem = null;
        try {
            bbProblem = (BlackBoxProblem) Class.forName("problem."+problem + "_SVM").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bbProblem;
    }

}

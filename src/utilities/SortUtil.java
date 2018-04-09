package utilities;

public class SortUtil {

    public static void spaceSort(double[][] p,double[] cost, int NP) {
        for(int i=0;i<NP-1;i++) {
            for(int j=0;j<NP-i-1;j++) {
                if(cost[i]>cost[i+1]) {
                    double t = cost[i];
                    cost[i] = cost[i+1];
                    cost[i+1]=t;
                    double[] tt = p[i];
                    p[i] = p[i+1];
                    p[i+1]=tt;
                }
            }
        }
    }

}

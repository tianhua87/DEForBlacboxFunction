package utilities;

public class SortUtil {

    public static void spaceSort(double[][] p,double[] cost, int NP) {
        for(int i=0;i<NP-1;i++) {
            for(int j=0;j<NP-i-1;j++) {
                if(cost[j]>cost[j+1]) {
                    double t = cost[j];
                    cost[j] = cost[j+1];
                    cost[j+1]=t;
                    double[] tt = p[j];
                    p[j] = p[j+1];
                    p[j+1]=tt;
                }
            }
        }
    }

}

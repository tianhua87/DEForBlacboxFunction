package utilities;

import org.omg.CORBA.INTERNAL;

public class NumberUtil {

    public static final double RN = Math.random();

    public static void  latin_hyp(double[][] ax, int iter,
                          int dim, double[] xmin,
                          double[] xmax)
    {
        int v;
		double [][]L;
        boolean [][]viable;
        L = new double[dim][];
        viable = new boolean[dim][];
        for (int j = 0; j<dim; j++)
        {
            L[j] = new double[iter];
            viable[j] = new boolean[iter];
        }

        //   double L[dim][iter];
        //    bool viable[dim][iter];


        for(int d=0; d<dim; d++)
        {
            for(int i=0;i<iter;i++)
            {
                viable[d][i]=true;

                L[d][i] = xmin[d+1] + i*((xmax[d+1]-xmin[d+1])/(double)(iter));
            }
        }
        //System.out.println(RN+" "+dim + " "+iter);
        for(int i=0; i<iter; i++)
        {
            for(int d = 0; d < dim; d++)
            {
                do
                {
                    double rn = Math.random();
                    v = (int)(rn*iter);
                }
                while(!viable[d][v]);
                viable[d][v]=false;
                double rn = Math.random();
                ax[i][d] = L[d][v]+rn*((xmax[d+1]-xmin[d+1])/(double)(iter));
            }
        }
    }

}

package algorithm;

import problem.BlackBoxProblem;
import random.DERandom;
import strategy.DEStrategy;
import strategy.DEStrategyConst;

public class DE {

    public int generation  = 0;
    public double mincost  = Double.MAX_VALUE;

    public int     dim;
    public int     NP;
    public double  F;
    public double  Cr;
    public double lowLimit;
    public double highLimit;
    public DEInitializer initializer;
    public DERandom deRandom;

    public DEStrategy Strategem[];
    public int current_strategy = 0;

    public BlackBoxProblem blackBoxProblem;

    int MaxD          = 30;   //maximum number of parameters
    int MaxN          = 500;  //maximum for NP
    int MaxR          = 10;   //maximum number of random choices
    int MaxGeneration = 200;

    double trial[]       = new double [MaxD]; // the trial vector
    public double best[] = new double [MaxD]; // the best vector so far
    double genbest[]     = new double [MaxD]; // the best vector of the current generation
    double cost[]        = new double [MaxN];

    double p1[][]     = new double [MaxN][MaxD]; // array of vectors
    double p2[][]     = new double [MaxN][MaxD];

    double rvec[][]   = new double [MaxR][MaxD]; // array of randomly chosen vectors
    int rnd[]         = new int [MaxR];		   // array of random indices

    double g0[][];	// just some pointers (placeholders)
    double g1[][];

    int    min_index   = 0;

    public void initStrategy(){
        String S[] = DEStrategyConst.DEStrategyName;
        Strategem = new DEStrategy [S.length];
        for (int i = 0;  i < S.length;  i++) {
            try {
                Class C = Class.forName ("strategy." + S[i]);
                Strategem[i] = (DEStrategy)C.newInstance();
                Strategem[i].init (deRandom);
            }
            catch (Exception e) {
                System.err.println (e);
            };
        }
    }

    public void init(){
        initStrategy();
        initializer.init(p1,lowLimit,highLimit,NP,F,Cr,dim);

        for(int i = 0;i < NP;i++)
            cost[i]= blackBoxProblem .evaluate(p1[i],dim);
        mincost   = cost[0];
        min_index = 0;
        for (int i=0; i<NP; i++) {
            double x = cost[i];
            if (x < mincost) {
                mincost   = x;
                min_index = i;
            }
        }
        System.out.println(mincost+" "+min_index);
        assign (best, p1[min_index]);
        assign (genbest, best);
        g0 = p1;   // generation t
        g1 = p2;   // generation t+1

    }

    public DE(int dim, int NP, double f, double cr, double lowLimit, double highLimit, DEInitializer initializer,
              BlackBoxProblem blackBoxProblem) {
        this.dim = dim;
        this.NP = NP;
        F = f;
        Cr = cr;
        this.lowLimit = lowLimit;
        this.highLimit = highLimit;
        this.initializer = initializer;
        deRandom = new DERandom();
        this.blackBoxProblem = blackBoxProblem;
        init();
    }

    public double optimize () {


        while (generation <= MaxGeneration) {
            for (int i = 0;  i < NP;  i++) {
                assign (trial, g0[i]);
                do rnd[0] = deRandom.nextInt (NP);
                while (rnd[0] == i);

                do rnd[1] = deRandom.nextInt (NP);
                while ((rnd[1] == i) || (rnd[1] == rnd[0]));

                do rnd[2] = deRandom.nextInt (NP);
                while ((rnd[2] == i) || (rnd[2] == rnd[1]) || (rnd[2] == rnd[0]));

                do rnd[3] = deRandom.nextInt (NP);
                while ((rnd[3] == i) || (rnd[3] == rnd[2]) || (rnd[3] == rnd[1]) || (rnd[3] == rnd[0]));

                do rnd[4] = deRandom.nextInt (NP);
                while ((rnd[4] == i) || (rnd[4] == rnd[3]) || (rnd[4] == rnd[2]) || (rnd[4] == rnd[1]) || (rnd[4] == rnd[0]));

                do rnd[5] = deRandom.nextInt (NP);
                while ((rnd[5] == i) || (rnd[5] == rnd[4]) || (rnd[5] == rnd[3]) || (rnd[5] == rnd[2])
                        || (rnd[5] == rnd[1]) || (rnd[5] == rnd[0]));

                for (int k=0; k<6; k++) {
                    rvec[k] = g0[rnd[k]];
                }

                Strategem[current_strategy].apply (F, Cr, dim, trial, genbest, rvec);

                double testcost = blackBoxProblem.evaluate (trial, dim);
                //System.out.println(DEStrategyConst.DEStrategyName[current_strategy]+" "+testcost);
                if (testcost <= cost[i]) {
                    assign (g1[i], trial);
                    cost[i] = testcost;
                    if (testcost < mincost) {
                        mincost = testcost;
                        assign (best, trial);
                        min_index = i;
                    }
                } else {
                    assign (g1[i], g0[i]);
                }
            }

            assign (genbest, best);

            double gx[][] = g0;
            g0 = g1;
            g1 = gx;

            generation++;
        }
        for (int i = 0 ;i < dim;i++)
            System.out.print(best[i]+" ");
        System.out.println();
        System.out.println(mincost);
        return mincost;
    }

    private void assign (double[] to, double[] from) {
        int i;
        for (i=0; i<dim; i++) {
            to[i] = from[i];
        }
    }
}

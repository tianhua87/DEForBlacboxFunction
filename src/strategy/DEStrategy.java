package strategy;

import random.DERandom;

public abstract class DEStrategy {
   protected DERandom deRandom;

   protected int i, counter;

   abstract public void apply (double F, double Cr, int dim,
	                          double[]x, double[]gen_best,
                              double[][]g0);

   public void init (DERandom deRnd) {
	   deRandom = deRnd;
   }

   protected final void prepare (int dim) {
     i = deRandom.nextInt (dim);
     counter = 0;
   }
}





package strategy;

public class DEBest1Bin extends DEStrategy {
  public void apply (double F, double Cr, int dim, double[]x, double[]gen_best,
  double[][]g0) {
    //初始化i为[0-dim)范围内的一个随机数，counter = 0
    prepare (dim);
    while (counter++ < dim) {
	  if ((deRandom.nextDouble() < Cr) || (counter == dim))
        x[i] = gen_best[i] + F * (g0[0][i] - g0[1][i]);
      i = ++i % dim;
    }
  }
}





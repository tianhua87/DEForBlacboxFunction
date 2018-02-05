package algorithm;

public interface DEInitializer {
    /**
     *
     * @param p  种群
     * @param lowLimit  个体的基因的最小值
     * @param highLimit  个体基因的最大值
     * @param NP   个体数量
     * @param F     缩放因子
     * @param Cr    杂交概率
     * @param dim   基因数量（向量维度）
     */
    void init(double[][] p, double lowLimit, double highLimit, int NP, double F, double Cr, int dim);
}

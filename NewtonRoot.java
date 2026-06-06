/**
 * 牛顿法求根
 */
public class NewtonRoot {

    /**
     * @param f            可微函数
     * @param initialGuess 初始猜测值
     * @param tolerance    容差（|f(x)| < tolerance 时停止）
     * @param maxIter      最大迭代次数
     * @return 近似根
     */
    public static double findRoot(DifferentiableFunction f,
                                  double initialGuess,
                                  double tolerance,
                                  int maxIter) {
        double x = initialGuess;
        for (int i = 0; i < maxIter; i++) {
            double fx = f.eval(x);
            if (Math.abs(fx) < tolerance) {
                return x;
            }
            double dfx = f.diff(x);
            if (Math.abs(dfx) < 1e-12) { // 防止除零
                System.out.println("导数接近零，迭代停止");
                return x;
            }
            x = x - fx / dfx;
        }
        System.out.println("达到最大迭代次数，当前值: " + x);
        return x;
    }

    /** 便捷方法：默认参数 */
    public static double findRoot(DifferentiableFunction f) {
        return findRoot(f, 1.0, 1e-6, 1000);
    }
}
/**
 * 测试类：包含所有要求的测试用例
 */
public class Main1 {
    public static void main(String[] args) {
        System.out.println("========== 数值积分实验测试 ==========\n");

        // 1. 函数类 eval / diff 测试
        System.out.println(">>> 函数类基本测试");
        Linear lin = new Linear(2, 3);
        System.out.printf("Linear f(1)=%.2f, f'(1)=%.2f\n", lin.eval(1), lin.diff(1));

        Quadratic quad = new Quadratic(1, -3, 2);
        System.out.printf("Quadratic f(2)=%.2f, f'(2)=%.2f\n", quad.eval(2), quad.diff(2));

        Sin sinFunc = new Sin(1, 0);
        System.out.printf("Sin f(pi/2)=%.2f, f'(pi/2)=%.2f\n",
                sinFunc.eval(Math.PI / 2), sinFunc.diff(Math.PI / 2));

        NormalPDF norm = new NormalPDF(0, 1);
        System.out.printf("NormalPDF f(0)=%.4f, f'(0)=%.4f\n", norm.eval(0), norm.diff(0));
        System.out.println();

        // 2. 牛顿法求根测试
        System.out.println(">>> 牛顿法求根测试");

        // f(x) = x^2 - 2，根 √2 ≈ 1.41421356
        Quadratic f1 = new Quadratic(1, 0, -2);
        double root1 = NewtonRoot.findRoot(f1, 2.0, 1e-8, 100);
        System.out.printf("x^2-2 的根 (初值2): %.8f (理论√2≈1.41421356)\n", root1);

        // f(x) = sin(x)，根 π ≈ 3.14159265
        Sin f2 = new Sin(1, 0);
        double root2 = NewtonRoot.findRoot(f2, 3.0, 1e-8, 100);
        System.out.printf("sin(x) 的根 (初值3): %.8f (理论π≈3.14159265)\n", root2);

        // 自定义：f(x) = x^3 - 2x - 5
        DifferentiableFunction f3 = new DifferentiableFunction() {
            @Override
            public double eval(double x) { return x*x*x - 2*x - 5; }
            @Override
            public double diff(double x) { return 3*x*x - 2; }
        };
        double root3 = NewtonRoot.findRoot(f3, 2.0, 1e-8, 100);
        System.out.printf("x^3-2x-5 的根 (初值2): %.8f (理论约2.09455148)\n", root3);
        System.out.println();

        // 3. 数值积分测试
        System.out.println(">>> 数值积分测试");

        // ∫0^π sin(x) dx = 2
        double a = 0, b = Math.PI;
        double trap1 = NewtonCotes.trapezoidal(sinFunc, a, b);
        double simp1 = NewtonCotes.simpson(sinFunc, a, b);
        System.out.printf("∫0^π sin(x) dx (理论值2.0):\n");
        System.out.printf("  梯形公式: %.8f (误差 %.8f)\n", trap1, Math.abs(trap1 - 2.0));
        System.out.printf("  Simpson:  %.8f (误差 %.8f)\n", simp1, Math.abs(simp1 - 2.0));

        // ∫0^1 标准正态分布密度 dx ≈ 0.34134
        // NormalPDF.eval 返回的是核，密度需要除以 sqrt(2π)
        NormalPDF normPDF = new NormalPDF(0, 1);
        Function standardNormal = new Function() {
            @Override
            public double eval(double x) {
                return normPDF.eval(x) / Math.sqrt(2 * Math.PI);
            }
        };
        double trap2 = NewtonCotes.trapezoidal(standardNormal, 0, 1);
        double simp2 = NewtonCotes.simpson(standardNormal, 0, 1);
        System.out.printf("\n∫0^1 标准正态密度 dx (理论约0.34134):\n");
        System.out.printf("  梯形公式: %.8f\n", trap2);
        System.out.printf("  Simpson:  %.8f\n", simp2);

        System.out.println("\n========== 测试完成 ==========");
    }
}
/**
 * Newton-Cotes 单区间积分公式
 */
public class NewtonCotes {

    /**
     * 梯形公式 ∫a^b f(x)dx ≈ (b-a)/2 * [f(a)+f(b)]
     */
    public static double trapezoidal(Function f, double a, double b) {
        return (b - a) / 2.0 * (f.eval(a) + f.eval(b));
    }

    /**
     * Simpson 公式 ∫a^b f(x)dx ≈ (b-a)/6 * [f(a) + 4f((a+b)/2) + f(b)]
     */
    public static double simpson(Function f, double a, double b) {
        double mid = (a + b) / 2.0;
        return (b - a) / 6.0 * (f.eval(a) + 4.0 * f.eval(mid) + f.eval(b));
    }
}
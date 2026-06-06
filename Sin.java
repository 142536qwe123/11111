/**
 * 正弦函数 f(x) = sin(ω*x + φ)
 */
public class Sin implements DifferentiableFunction {
    private double omega, phi;

    public Sin(double omega, double phi) {
        this.omega = omega;
        this.phi = phi;
    }

    @Override
    public double eval(double x) {
        return Math.sin(omega * x + phi);
    }

    @Override
    public double diff(double x) {
        return omega * Math.cos(omega * x + phi);
    }
}
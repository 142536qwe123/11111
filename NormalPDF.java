/**
 * 正态分布概率密度函数核 f(x) = exp( -(x-μ)² / (2σ²) )
 */
public class NormalPDF implements DifferentiableFunction {
    private double mu, sigma;

    public NormalPDF(double mu, double sigma) {
        this.mu = mu;
        this.sigma = sigma;
    }

    @Override
    public double eval(double x) {
        double z = (x - mu) / sigma;
        return Math.exp(-0.5 * z * z);
    }

    @Override
    public double diff(double x) {
        double z = (x - mu) / sigma;
        return -z / sigma * Math.exp(-0.5 * z * z);
    }
}

/**
 * 可微函数接口，扩展 Function
 */
public interface DifferentiableFunction extends Function {
    double diff(double x);
}
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * 数值积分驱动程序 —— 比较复化梯形公式与复化 Simpson 公式的精度
 * 积分: ∫0^π sin(x) dx = 2
 */
public class IntegrationDriver {

    /** 一元函数接口 */
    interface Function {
        double eval(double x);
    }

    /** sin(ωx+φ) 函数，此处使用 ω=1, φ=0，即 sin(x) */
    static class Sin implements Function {
        private double omega, phi;

        public Sin(double omega, double phi) {
            this.omega = omega;
            this.phi = phi;
        }

        @Override
        public double eval(double x) {
            return Math.sin(omega * x + phi);
        }
    }

    /**
     * 复化梯形公式
     * @param f 被积函数
     * @param a 下限
     * @param b 上限
     * @param n 等分数
     * @return 积分近似值
     */
    public static double compositeTrapezoidal(Function f, double a, double b, int n) {
        double h = (b - a) / n;
        double sum = 0.5 * (f.eval(a) + f.eval(b));
        for (int i = 1; i < n; i++) {
            double x = a + i * h;
            sum += f.eval(x);
        }
        return sum * h;
    }

    /**
     * 复化 Simpson 公式（要求 n 为偶数）
     * @param f 被积函数
     * @param a 下限
     * @param b 上限
     * @param n 等分数（必须为偶数）
     * @return 积分近似值
     */
    public static double compositeSimpson(Function f, double a, double b, int n) {
        if (n % 2 != 0) {
            throw new IllegalArgumentException("n 必须为偶数，当前 n = " + n);
        }
        double h = (b - a) / n;
        double sum = f.eval(a) + f.eval(b);

        // 奇数项 (i = 1,3,5,...,n-1) 权重 4
        for (int i = 1; i < n; i += 2) {
            double x = a + i * h;
            sum += 4.0 * f.eval(x);
        }

        // 偶数项 (i = 2,4,6,...,n-2) 权重 2
        for (int i = 2; i < n; i += 2) {
            double x = a + i * h;
            sum += 2.0 * f.eval(x);
        }

        return sum * h / 3.0;
    }

    /**
     * 绘制“积分近似值 vs n”的收敛曲线
     */
    private static void showConvergencePlot(int[] nValues, double[] trapValues,
                                            double[] simpsonValues, double exact) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("积分收敛曲线: 积分值 vs n");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(900, 560);
            frame.setLocationRelativeTo(null);
            frame.add(new ConvergencePanel(nValues, trapValues, simpsonValues, exact));
            frame.setVisible(true);
        });
    }

    /**
     * 简单折线图面板：梯形、Simpson 与理论值三条曲线
     */
    static class ConvergencePanel extends JPanel {
        private final int[] nValues;
        private final double[] trapValues;
        private final double[] simpsonValues;
        private final double exact;

        private static final int LEFT = 80;
        private static final int RIGHT = 30;
        private static final int TOP = 40;
        private static final int BOTTOM = 70;

        public ConvergencePanel(int[] nValues, double[] trapValues, double[] simpsonValues, double exact) {
            this.nValues = Arrays.copyOf(nValues, nValues.length);
            this.trapValues = Arrays.copyOf(trapValues, trapValues.length);
            this.simpsonValues = Arrays.copyOf(simpsonValues, simpsonValues.length);
            this.exact = exact;
            setBackground(Color.WHITE);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int plotW = w - LEFT - RIGHT;
            int plotH = h - TOP - BOTTOM;

            if (plotW <= 0 || plotH <= 0 || nValues.length == 0) {
                return;
            }

            double yMin = exact;
            double yMax = exact;
            for (double v : trapValues) {
                yMin = Math.min(yMin, v);
                yMax = Math.max(yMax, v);
            }
            for (double v : simpsonValues) {
                yMin = Math.min(yMin, v);
                yMax = Math.max(yMax, v);
            }

            // 给 y 轴留出上下边距，防止曲线贴边
            double pad = Math.max((yMax - yMin) * 0.15, 1e-6);
            yMin -= pad;
            yMax += pad;

            // 坐标轴
            g2.setColor(Color.DARK_GRAY);
            g2.drawLine(LEFT, TOP, LEFT, TOP + plotH);
            g2.drawLine(LEFT, TOP + plotH, LEFT + plotW, TOP + plotH);

            // 标题与坐标标签
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString("积分计算值 vs n (收敛过程)", LEFT, 24);
            g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
            g2.drawString("n", LEFT + plotW / 2, h - 25);
            g2.drawString("积分值", 20, TOP + plotH / 2);

            int nMin = nValues[0];
            int nMax = nValues[nValues.length - 1];

            // x 轴刻度
            for (int n : nValues) {
                int x = mapX(n, nMin, nMax, LEFT, plotW);
                g2.drawLine(x, TOP + plotH, x, TOP + plotH + 5);
                g2.drawString(Integer.toString(n), x - 14, TOP + plotH + 22);
            }

            // y 轴刻度（5段）
            for (int i = 0; i <= 5; i++) {
                double yVal = yMin + i * (yMax - yMin) / 5.0;
                int y = mapY(yVal, yMin, yMax, TOP, plotH);
                g2.setColor(new Color(220, 220, 220));
                g2.drawLine(LEFT, y, LEFT + plotW, y);
                g2.setColor(Color.DARK_GRAY);
                g2.drawLine(LEFT - 5, y, LEFT, y);
                g2.drawString(String.format("%.6f", yVal), 8, y + 4);
            }

            // 理论值水平线
            int yExact = mapY(exact, yMin, yMax, TOP, plotH);
            g2.setColor(new Color(100, 100, 100));
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                    10, new float[]{6, 6}, 0));
            g2.drawLine(LEFT, yExact, LEFT + plotW, yExact);

            // 梯形法曲线
            g2.setStroke(new BasicStroke(2.0f));
            g2.setColor(new Color(200, 60, 60));
            drawSeries(g2, nValues, trapValues, nMin, nMax, yMin, yMax, LEFT, TOP, plotW, plotH);

            // Simpson 曲线
            g2.setColor(new Color(30, 120, 220));
            drawSeries(g2, nValues, simpsonValues, nMin, nMax, yMin, yMax, LEFT, TOP, plotW, plotH);

            drawLegend(g2, w - 255, 20);
        }

        private static void drawSeries(Graphics2D g2, int[] nVals, double[] yVals,
                                       int nMin, int nMax, double yMin, double yMax,
                                       int left, int top, int plotW, int plotH) {
            for (int i = 1; i < nVals.length; i++) {
                int x1 = mapX(nVals[i - 1], nMin, nMax, left, plotW);
                int y1 = mapY(yVals[i - 1], yMin, yMax, top, plotH);
                int x2 = mapX(nVals[i], nMin, nMax, left, plotW);
                int y2 = mapY(yVals[i], yMin, yMax, top, plotH);
                g2.drawLine(x1, y1, x2, y2);
            }

            for (int i = 0; i < nVals.length; i++) {
                int x = mapX(nVals[i], nMin, nMax, left, plotW);
                int y = mapY(yVals[i], yMin, yMax, top, plotH);
                g2.fillOval(x - 3, y - 3, 6, 6);
            }
        }

        private static void drawLegend(Graphics2D g2, int x, int y) {
            g2.setColor(new Color(245, 245, 245));
            g2.fillRoundRect(x, y, 220, 82, 10, 10);
            g2.setColor(Color.GRAY);
            g2.drawRoundRect(x, y, 220, 82, 10, 10);

            g2.setStroke(new BasicStroke(2.0f));
            g2.setColor(new Color(200, 60, 60));
            g2.drawLine(x + 14, y + 22, x + 44, y + 22);
            g2.drawString("Trapezoidal", x + 52, y + 26);

            g2.setColor(new Color(30, 120, 220));
            g2.drawLine(x + 14, y + 44, x + 44, y + 44);
            g2.drawString("Simpson", x + 52, y + 48);

            g2.setColor(new Color(100, 100, 100));
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND,
                    10, new float[]{6, 6}, 0));
            g2.drawLine(x + 14, y + 66, x + 44, y + 66);
            g2.setStroke(new BasicStroke(1.0f));
            g2.drawString("Exact = 2.0", x + 52, y + 70);
        }

        private static int mapX(int n, int nMin, int nMax, int left, int plotW) {
            if (nMax == nMin) return left;
            double t = (n - nMin) / (double) (nMax - nMin);
            return left + (int) Math.round(t * plotW);
        }

        private static int mapY(double y, double yMin, double yMax, int top, int plotH) {
            if (Math.abs(yMax - yMin) < 1e-15) return top + plotH / 2;
            double t = (y - yMin) / (yMax - yMin);
            return top + plotH - (int) Math.round(t * plotH);
        }
    }

    public static void main(String[] args) {
        // 积分区间与被积函数
        double a = 0.0;
        double b = Math.PI;
        double exact = 2.0;
        Function f = new Sin(1.0, 0.0); // sin(x)

        // n 序列: 10, 20, 40, 80, 160, 320, 640, 1000
        int[] nValues = {10, 20, 40, 80, 160, 320, 640, 1000};

        System.out.println("∫0^π sin(x) dx 理论值 = " + exact);
        System.out.println("--------------------------------------------------------------");
        System.out.printf("%-8s %-18s %-16s %-18s %-16s\n",
                "n", "Trapezoidal", "Trap Error", "Simpson", "Simp Error");
        System.out.println("--------------------------------------------------------------");

        double[] trapValues = new double[nValues.length];
        double[] simpValues = new double[nValues.length];

        for (int i = 0; i < nValues.length; i++) {
            int n = nValues[i];
            double trap = compositeTrapezoidal(f, a, b, n);
            double simp = compositeSimpson(f, a, b, n);
            double trapErr = Math.abs(trap - exact);
            double simpErr = Math.abs(simp - exact);

            trapValues[i] = trap;
            simpValues[i] = simp;

            System.out.printf("%-8d %-18.12f %-16.2e %-18.12f %-16.2e\n",
                    n, trap, trapErr, simp, simpErr);
        }
        System.out.println("--------------------------------------------------------------");

        showConvergencePlot(nValues, trapValues, simpValues, exact);
    }
}
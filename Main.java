/**
 * EcoFarm 1.0 场景测试入口。
 */
public class Main {
    public static void main(String[] args) {
        FarmManager manager = new FarmManager();

        DairyCow bessie = new DairyCow("Bessie", 2000, 500);
        SmartReaper reaper = new SmartReaper("Reaper-V1", 10000, 0.8, 2);
        AppleTree appleTree = new AppleTree("Apple-01", 500, 5);
        WoodenFence fence = new WoodenFence("Fence-01", 30, 100);

        manager.addAsset(bessie);
        manager.addAsset(reaper);
        manager.addAsset(appleTree);
        manager.addAsset(fence);

        manager.generateReport();

        manager.dailyProcess();

        appleTree.water();

        manager.dailyProcess();

        manager.generateReport();
    }
}
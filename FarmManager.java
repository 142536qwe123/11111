import java.util.ArrayList;

/**
 * 农场总控系统：统一管理资产、生成报表并执行每日流程。
 */
public class FarmManager {
    private final ArrayList<FarmAsset> assets = new ArrayList<>();

    /**
     * 添加一项农场资产。
     * @param asset 资产对象
     */
    public void addAsset(FarmAsset asset) {
        assets.add(asset);
    }

    /**
     * 打印所有资产的基础信息与当前价值。
     */
    public void generateReport() {
        System.out.println("===== Farm Report =====");
        for (FarmAsset asset : assets) {
            System.out.println(asset.getAssetInfo());
            System.out.println("Current Value: $" + asset.getCurrentValue());
        }
    }

    /**
     * 执行一次每日流程：先维护，再检查是否可收获。
     */
    public void dailyProcess() {
        System.out.println("===== Daily Process =====");
        for (FarmAsset asset : assets) {
            if (asset instanceof IMaintainable) {
                IMaintainable maintainable = (IMaintainable) asset;
                double cost = maintainable.performMaintenance();
                System.out.println("维护: " + getAssetName(asset) + " - " + maintainable.getMaintenanceDetail() + ", 费用: " + cost);
            }

            if (asset instanceof IHarvestable) {
                IHarvestable harvestable = (IHarvestable) asset;
                if (harvestable.isHarvestable()) {
                    String product = harvestable.harvest();
                    System.out.println("收获: " + getAssetName(asset) + " 产出了 " + product);
                }
            }
        }
    }

    private String getAssetName(FarmAsset asset) {
        return asset.name;
    }
}
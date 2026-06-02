package com.ditur.crops;

public enum CropType {
    CARROT(12),
    POTATO(18),
    WHEAT(25),
    NONE(0);

    private final int baseGrowthTime;

    CropType(int baseGrowthTime) {
        this.baseGrowthTime = baseGrowthTime;
    }

    public int getBaseGrowthTime() {
        return baseGrowthTime;
    }
}

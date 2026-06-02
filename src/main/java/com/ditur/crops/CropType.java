package com.ditur.crops;

public enum CropType {
    CARROT(40),
    POTATO(60),
    WHEAT(70),
    NONE(0);

    private final int baseGrowthTime;

    CropType(int baseGrowthTime) {
        this.baseGrowthTime = baseGrowthTime;
    }

    public int getBaseGrowthTime() {
        return baseGrowthTime;
    }
}

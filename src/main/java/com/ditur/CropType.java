package com.ditur;

public enum CropType {
    CARROT(5),
    POTATO(10),
    WHEAT(15),
    NONE(0);

    private final int baseGrowthTime;

    CropType(int baseGrowthTime) {
        this.baseGrowthTime = baseGrowthTime;
    }

    public int getBaseGrowthTime() {
        return baseGrowthTime;
    }
}

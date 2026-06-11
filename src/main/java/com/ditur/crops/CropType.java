package com.ditur.crops;

import com.ditur.Settings;

public enum CropType {
    CARROT(Settings.CARROT_GROWTH_TIME),
    POTATO(Settings.POTATO_GROWTH_TIME),
    WHEAT(Settings.WHEAT_GROWTH_TIME),
    NONE(Settings.NONE);

    private final int baseGrowthTime;

    CropType(int baseGrowthTime) {
        this.baseGrowthTime = baseGrowthTime;
    }

    public int getBaseGrowthTime() {
        return baseGrowthTime;
    }
}

package com.ditur.crops;

import com.ditur.Settings;

// Definiuje dostpene typy upraw i nadaje im domyslny czas wzsotu
// Zastosowanie Enum zabezpiecza przed pojawieniem sie nenznaych roslin
public enum CropType {
    CARROT(Settings.CARROT_GROWTH_TIME),
    POTATO(Settings.POTATO_GROWTH_TIME),
    WHEAT(Settings.WHEAT_GROWTH_TIME),
    NONE(Settings.NONE); // Puste pole

    private final int baseGrowthTime;

    CropType(int baseGrowthTime) {
        this.baseGrowthTime = baseGrowthTime;
    }

    public int getBaseGrowthTime() {
        return baseGrowthTime;
    }
}

package com.ditur.crops;

import com.ditur.Settings;

/**
 * Definiuje dostepne typy upraw i nadaje im domyslny czas wzrostu.
 * Zastosowanie Enum zabezpiecza przed pojawieniem sie nieznanych roslin.
 */
public enum CropType {
    /** Typ uprawy: Marchewka. */
    CARROT(Settings.CARROT_GROWTH_TIME),

    /** Typ uprawy: Ziemniak. */
    POTATO(Settings.POTATO_GROWTH_TIME),

    /** Typ uprawy: Pszenica. */
    WHEAT(Settings.WHEAT_GROWTH_TIME),

    /** Brak uprawy (puste pole). */
    NONE(Settings.NONE); // Puste pole

    /** Bazowy czas wzrostu dla danego typu uprawy. */
    private final int baseGrowthTime;

    /**
     * Konstruktor typu wyliczeniowego CropType.
     * @param baseGrowthTime podstawowy czas wzrostu rosliny wyrazony w tickach
     */
    CropType(int baseGrowthTime) {
        this.baseGrowthTime = baseGrowthTime;
    }

    /**
     * Pobiera bazowy czas wzrostu przypisany do danego typu uprawy.
     * @return podstawowy czas wzrostu
     */
    public int getBaseGrowthTime() {
        return baseGrowthTime;
    }
}
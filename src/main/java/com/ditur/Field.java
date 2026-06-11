package com.ditur;

import com.ditur.crops.CropType;

public class Field {
    private final int x;
    private final int y;

    private int hydrationLevel;
    private String fieldState; // empty, growing, maturely
    private int growthTime;

    private boolean isPesticideActive;
    private int pesticideDuration;
    private CropType cropType;

    private int pesticideTicksLeft = 0;
    private int pesticideMaxDuration; // czas trwania

    // Maximum constant values for field logic
    private static final int MAX_HYDRATION = 100;

    public Field(int x, int y) {
        this.x = x;
        this.y = y;
        this.hydrationLevel = MAX_HYDRATION;
        this.fieldState = "empty";
        this.growthTime = 0;
        this.isPesticideActive = false;
        this.pesticideDuration = 0;
        this.cropType = CropType.NONE;
    }

    // Main field state update method called on each simulation tick
    public void updateState() {
        // 1. Water degradation
//        if (hydrationLevel > 0) {
//            hydrationLevel--;
//        }

        // 2. Pesticide Logic
        if (isPesticideActive) {
            pesticideDuration--;
            if (pesticideDuration <= 0) {
                isPesticideActive = false;
            }
        }

        // 3. Logic of yield growth
        if (fieldState.equals("growing")) {
            if (hydrationLevel > 0) { // The plant only grows when there is water
                if (growthTime > 0) {
                    growthTime--;
                }
                if (growthTime == 0) {
                    fieldState = "maturely";
                }
            } else {
                // If the field dries out completely, the plant dies after some time.
                consumeCrop();
            }
        }
    }

    // The method called by the farmer when planting
    public void setCrop(CropType type) {
        if (type == CropType.NONE) {
            consumeCrop();
        } else {
            this.cropType = type;
            this.fieldState = "growing";
            this.growthTime = type.getBaseGrowthTime();
        }
    }

    // The method used for watering
    public void waterField() {
        this.hydrationLevel = MAX_HYDRATION;
    }

    // Cleaning a field after it has been eaten, harvested, or died
    public void consumeCrop() {
        this.cropType = CropType.NONE;
        this.fieldState = "empty";
        this.growthTime = 0;
    }

    public void applyPesticide(int maxDuration) {
        this.pesticideMaxDuration = maxDuration;
        this.pesticideTicksLeft = maxDuration;
    }

    public boolean hasPesticide() {
        return pesticideTicksLeft > 0;
    }

    public int getPesticideTicksLeft() {
        return pesticideTicksLeft;
    }

    public int getPesticideMaxDuration() {
        return pesticideMaxDuration;
    }

    public void updatePesticide() {
        if (pesticideTicksLeft > 0) {
            pesticideTicksLeft--;
        }
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getHydrationLevel() { return hydrationLevel; }
    public String getFieldState() { return fieldState; }
    public int getGrowthTime() { return growthTime; }
    public boolean isPesticideActive() { return isPesticideActive; }
    public CropType getCropType() { return cropType; }
}

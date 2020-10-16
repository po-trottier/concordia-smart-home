package com.concordia.smarthomesimulator.dataModels;

public enum Permissions {
    /**
     * Parent permissions.
     */
    PARENT(7),
    /**
     * Child permissions.
     */
    CHILD(3),
    /**
     * Visitor permissions.
     */
    VISITOR(1);

    private int bitVal;

    Permissions(int bitVal) {
        this.bitVal = bitVal;
    }

    /**
     * Gets bit value, which can be used with bitwise operations to determine if a user has the right permissions to do an action
     *
     * @return the bit val
     */
    public int getBitVal() {
        return bitVal;
    }
}

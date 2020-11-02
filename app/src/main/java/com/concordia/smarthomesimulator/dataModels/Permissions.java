package com.concordia.smarthomesimulator.dataModels;

public enum Permissions {
    /**
     * Parent permissions.
     */
    PARENT(15),
    /**
     * Child permissions.
     */
    CHILD(7),
    /**
     * Guest permissions.
     */
    GUEST(3),
    /**
     * Stranger permissions.
     */
    STRANGER(1);

    private final int bitValue;

    Permissions(int bitValue) {
        this.bitValue = bitValue;
    }

    /**
     * Gets bit value, which can be used with bitwise operations to determine if a user has the right permissions to do an action
     *
     * @return the bit val
     */
    public int getBitValue() {
        return bitValue;
    }

    /**
     * Converts a string representing a permission to a permission
     *
     * @param permissions the permissions string
     * @return the permissions
     */
    public static Permissions fromString(String permissions){
        switch (permissions.toLowerCase()) {
            case "parent": return PARENT;
            case "child": return CHILD;
            case "guest": return GUEST;
            default: return STRANGER;
        }
    }

    public static Permissions fromInteger(int permissions){
        switch (permissions){
            case 15: return PARENT;
            case 7: return CHILD;
            case 3: return GUEST;
            default: return STRANGER;
        }
    }
}

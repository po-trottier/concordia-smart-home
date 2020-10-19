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

    public static Permissions toPermissions(String permissionsStr){
        if (permissionsStr.equalsIgnoreCase("parent")){
            return PARENT;
        } else if (permissionsStr.equalsIgnoreCase("child")){
            return CHILD;
        } else if (permissionsStr.equalsIgnoreCase("guest")){
            return GUEST;
        } else{
            return STRANGER;
        }

    }
}

package com.concordia.smarthomesimulator.dataModels;

public class User {
    private String username;
    private String password;
    private Permissions permission;

    /**
     * Instantiates a new User.
     *
     * @param username   the username
     * @param password   the password
     * @param permission the permission
     */
    public User(String username, String password, Permissions permission) {
        this.username = username;
        this.password = password;
        this.permission = permission;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets permission.
     *
     * @return the permission
     */
    public Permissions getPermission() {
        return permission;
    }

    /**
     * Sets permission.
     *
     * @param permission the permission
     */
    public void setPermission(Permissions permission) {
        this.permission = permission;
    }
}

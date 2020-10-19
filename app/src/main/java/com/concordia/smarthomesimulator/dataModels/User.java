package com.concordia.smarthomesimulator.dataModels;

public class User {
    private final String username;
    private final String password;
    private final Permissions permission;

    /**
     * Instantiates a new User.
     * <p>
     * Users cannot be modified, new ones must be created and appended to the userbase. So users don't have
     * identical usernames or passwords.
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
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
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
     * A user is similar to another if they have the same username or password
     *
     * @param user the user
     * @return the boolean
     */
    public boolean isSimilar(User user){
        return this.username.equals(user.username) || this.password.equals(user.password);
    }

}

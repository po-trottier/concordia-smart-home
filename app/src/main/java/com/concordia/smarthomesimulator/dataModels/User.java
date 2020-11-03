package com.concordia.smarthomesimulator.dataModels;

public class User {
    private final String username;
    private final String password;
    private final Permissions permission;
    private UserPreferences userPreferences;

    /**
     * Instantiates a new User.
     * <p>
     * Users cannot be modified except for preferences, new ones must be created and appended to the userbase. So users don't have
     * identical usernames or passwords.
     *
     * @param username        the username
     * @param password        the password
     * @param permission      the permission
     * @param userPreferences the user preferences
     */
    public User(String username, String password, Permissions permission, UserPreferences userPreferences) {
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.userPreferences = userPreferences;
    }

    /**
     * Instantiates a new User. Default preferences will be assigned.
     *
     * @param username   the username
     * @param password   the password
     * @param permission the permission
     */
    public User(String username, String password, Permissions permission) {
        this.username = username;
        this.password = password;
        this.permission = permission;
        this.userPreferences = new UserPreferences(username, password, permission);
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
     * A user is similar to another if they have the same username
     *
     * @param user the user
     * @return the boolean
     */
    public boolean isSimilar(User user) {
        return this.username.equalsIgnoreCase(user.username);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return username.equalsIgnoreCase(user.username)
                && password.equals(user.password)
                && permission.equals(user.permission);
    }

    /**
     * Gets user preferences.
     *
     * @return the user preferences
     */
    public UserPreferences getUserPreferences() {
        return userPreferences;
    }

    /**
     * Sets user preferences.
     *
     * @param userPreferences the user preferences
     */
    public void setUserPreferences(UserPreferences userPreferences) {
        this.userPreferences = userPreferences;
    }
}

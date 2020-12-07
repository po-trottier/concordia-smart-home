package com.concordia.smarthomesimulator.exceptions;

// Since permissions are a concept introduced specifically in our application
// it makes sense to create a custom exception to insure that the reason behind
// the error given when this specific exception occurs is clear.
public class PermissionNotFoundException extends Exception {
}

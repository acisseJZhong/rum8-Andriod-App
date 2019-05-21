package com.example.rum8.controllers;


import android.util.Log;

import com.example.rum8.listeners.LoginControllerListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class LoginController {

    private LoginControllerListener controllerListener;
    private FirebaseAuth auth;

    public LoginController(final LoginControllerListener controllerListener) {
        this.controllerListener = controllerListener;
        auth = FirebaseAuth.getInstance();
    }

    public void onSubmit(final String email, final String password) {
        if (!isValidEmail(email)) {
            final String message = "Please use your UCSD email (i.e. abc@ucsd.edu)";
            controllerListener.showToast(message);
        } else if (!isValidPassword(password)) {
            final String message = "Your password need to be more than 6 characters";
            controllerListener.showToast(message);
        } else {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener(authResult -> {

                        // check user verified their email
                        if (!auth.getCurrentUser().isEmailVerified()) {
                            final String message = "Please verify your email!";
                            controllerListener.showToast(message);
                        } else {
                            onLoginSuccessful();
                            Log.d("Success", "signInWithEmail:success");
                        }
                    }).addOnFailureListener(e -> {
                        final String message;
                        if (e instanceof FirebaseAuthInvalidUserException) {
                            message = "Account does not exist";
                        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Incorrect password";
                        } else {
                            message = "Network error";
                        }
                        controllerListener.showToast(message);
                        Log.d("Error", "signInWithEmail:failure", e);
                    });
        }
    }

    private static boolean isValidEmail(final String email) {
        if (email == null) {
            return false;
        }

        final int minimumEmailLength = 10;
        return email.length() >= minimumEmailLength && email.endsWith("@ucsd.edu");
    }

    private static boolean isValidPassword(final String password) {
        final int minimumPasswordLength = 6;
        return password != null && password.length() >= minimumPasswordLength;
    }

    public void onGoToRegistrationButtonClicked() {
        controllerListener.goToRegistration();
    }

    public void onGoToPasswordRecoverClicked() {
        controllerListener.goToPasswordRecovery();
    }

    private void onLoginSuccessful() {
        controllerListener.goToMainPage();
    }

}


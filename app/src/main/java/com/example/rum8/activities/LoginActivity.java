package com.example.rum8.activities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rum8.R;
import com.example.rum8.controllers.LoginController;
import com.example.rum8.listeners.LoginControllerListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.SignInMethodQueryResult;

public class LoginActivity extends AppCompatActivity implements LoginControllerListener {

  //member variables for text field
  private TextInputEditText emailField;
  private TextInputEditText passwordField;
  private Button buttonLogin;

  // [START declare_auth]
  private FirebaseAuth mAuth;
  // [END declare_auth]
  private LoginController controller;

  private String message;
  private boolean isRegistered;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // Initialize Firebase Auth
    mAuth = FirebaseAuth.getInstance();
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    initViews();
    initController();

    // views
    emailField = (TextInputEditText) findViewById(R.id.user_email);
    passwordField = (TextInputEditText) findViewById(R.id.user_password);
    buttonLogin = (Button) findViewById(R.id.button_login);

    findViewById(R.id.button_login);

    FirebaseApp.initializeApp(this);

    // Initialize Firebase Auth
    mAuth = FirebaseAuth.getInstance();


    buttonLogin.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {

        final String email = emailField.getText().toString();
        String pw = passwordField.getText().toString();

        // check empty emails
        if (TextUtils.isEmpty(email)) {
          emailField.setError("Required");
          return;
        }
        //check empty pw
        if (TextUtils.isEmpty(pw)) {
          passwordField.setError("Required");
          return;
        }
        mAuth.signInWithEmailAndPassword(email, pw).addOnCompleteListener
                (LoginActivity.this, new OnCompleteListener<AuthResult>() {
                  @Override
                  public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                      // Sign in success, update UI with the signed-in user's information
                      Log.d("Success", "signInWithEmail:success");
                      FirebaseUser user = mAuth.getCurrentUser();

                      //TODO: updateUI
                      controller.onLoginSuccessful();

                    } else {
                      //TODO: updateUI
                      // If sign in fails, display a message to the user.

                      passwordField.setText("");
                      if (!checkIfRegistered(email)) {
                        message = "Account does not exist! Please register first!";
                        emailField.setText("");
                        
                      } else {
                        message = "Wrong password!";
                      }

                      Log.w("Error:", "signInWithEmail:failure", task.getException());
                      Toast.makeText(LoginActivity.this, message,
                              Toast.LENGTH_SHORT).show();
                    }
                  }
                });
      }
    });
  }

  /*
  * This function checks if the user email is already registered
  * */
  private boolean checkIfRegistered(String email){
    mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<SignInMethodQueryResult>() {
      @Override
      public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
        if(task.getResult().getSignInMethods().isEmpty()) {
          isRegistered = false;
        }
        else{
          isRegistered = true;
        }
      }
    });
    return isRegistered;
  }

  @Override
  public void goToPasswordRecover() {
    final Intent intent = new Intent(LoginActivity.this, PasswordRecoveryActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void goToRegistration() {
    final Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void goToMainPage() {
    final Intent intent = new Intent(LoginActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
  }

  private void initViews() {


    final Button button_goToPasswordRecovery = findViewById(R.id.button_password_recovery);
    button_goToPasswordRecovery.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(final View v) {
        controller.onGoToPasswordRecoverClicked();
      }
    });
    final Button button_goToRegistration = findViewById(R.id.button_register);
    button_goToRegistration.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        controller.onGoToRegistrationButtonClicked();
      }
    });
  }

  // [START on_start_check_user]
  @Override
  public void onStart() {
    super.onStart();
    // Check if user is signed in (non-null) and update UI accordingly.
    FirebaseUser currentUser = mAuth.getCurrentUser();
    if (currentUser != null) {
      //TODO: update UI if the user is already log-in
    }
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  private void initController() {
    controller = new LoginController(this);
  }

}
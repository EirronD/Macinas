package com.example.bahi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class LogIn extends AppCompatActivity {
    TextInputEditText email,password;
    Button loginClick;
    TextView gotoRegister,gotoRecoverAcc;
    boolean valid = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // Initialize UI elements
        email = findViewById(R.id.logInEmailInput);
        password = findViewById(R.id.logInPasswordInput);
        loginClick = findViewById(R.id.logIn_Click);
        gotoRegister = findViewById(R.id.gotoRegister);
        gotoRecoverAcc = findViewById(R.id.gotoRecover);

        loginClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_email = email.getText().toString().trim();
                String login_pass = password.getText().toString().trim();

                // Validate email and password fields
                if (login_email.isEmpty()) {
                    email.setError("Please enter your email");
                    email.requestFocus();
                    return;
                }

                if (!login_email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|org|net|edu|gov)$")) {
                    email.setError("Enter a valid email address");
                    email.requestFocus();
                    return;
                }

                if (login_pass.isEmpty()) {
                    password.setError("Please enter your password");
                    password.requestFocus();
                    return;
                }

                // Proceed with Firebase Authentication
                fAuth.signInWithEmailAndPassword(login_email, login_pass)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                FirebaseUser firebaseUser = fAuth.getCurrentUser();

                                // Check if email is verified before user can access their profile
                                if (firebaseUser != null && firebaseUser.isEmailVerified()) {
                                    Toast.makeText(LogIn.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                                    checkUserAccessLevel(authResult.getUser().getUid());
                                } else if (firebaseUser != null) {
                                    firebaseUser.sendEmailVerification();
                                    fAuth.signOut();
                                    showAlertDialog();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Handle specific Firebase errors
                                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(LogIn.this, "Incorrect username or password.", Toast.LENGTH_SHORT).show();
                                } else if (e instanceof FirebaseAuthInvalidUserException) {
                                    Toast.makeText(LogIn.this, "No account found with this email", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(LogIn.this, "Authentication failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });



        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        gotoRecoverAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Recover_Acc.class));
            }
        });
    }

    private void showAlertDialog() {
        // Set up AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(LogIn.this);
        builder.setTitle("Email Not Verified");
        builder.setMessage("Please verify your email now. You cannot log in without email verification.");

        // Open Email Apps if User clicks Continue button
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        // Create the DialogBox
        AlertDialog alertDialog = builder.create();

        // Show the AlertDialog
        alertDialog.show();
    }


    private void checkUserAccessLevel(String uid) {
        DocumentReference df = fStore.collection("Users").document(uid);
        // Extract the data from the document
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Log.d("TAG", "onSuccess: " + documentSnapshot.getData());
                // Identify the user level
                String accountType = documentSnapshot.getString("AccountType");

                if ("Landlord".equalsIgnoreCase(accountType)) {
                    // Navigate to Admin page
                    String accType = "Landlord";
                    Intent intent = new Intent(getApplicationContext(), Admin.class);
                    intent.putExtra("accountType", accType);
                    startActivity(intent);
                    finish();
                } else if ("Tenant".equalsIgnoreCase(accountType)) {
                    // Navigate to MainActivity
                    String accType = "Tenant";
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("accountType", accType);
                    startActivity(intent);
                    finish();
                } else {
                    // Default case (optional)
                    Toast.makeText(LogIn.this, "Unknown account type", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("TAG", "Failed to fetch user data: " + e.getMessage());
                Toast.makeText(LogIn.this, "Error fetching user details", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            DocumentReference df = FirebaseFirestore.getInstance().collection("Users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
            df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.getString("Landlord") != null) {
                        startActivity(new Intent(getApplicationContext(), Admin.class));
                        finish();
                    }

                    if(documentSnapshot.getString("Tenant") != null) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getApplicationContext(), LogIn.class));
                    finish();
                }
            });
        }
    }

}
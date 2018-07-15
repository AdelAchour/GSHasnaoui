package com.prod.adelachour.gshasnaoui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextemail, editTextpassword, editTextNom, editTextPrenom, editTextTel, editTextDateN;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextemail = (EditText)findViewById(R.id.editTextEmailUp);
        editTextpassword = (EditText)findViewById(R.id.editTextPasswordUp);
        editTextDateN = (EditText)findViewById(R.id.editTextDateN);
        editTextNom = (EditText)findViewById(R.id.editTextNom);
        editTextPrenom = (EditText)findViewById(R.id.editTextPrenom);
        editTextTel = (EditText)findViewById(R.id.editTextTelephone);

        progressBar = (ProgressBar)findViewById(R.id.progressbarUp);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        findViewById(R.id.textViewLogin).setOnClickListener(this);
        findViewById(R.id.buttonSignUp).setOnClickListener(this);


    }





    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonSignUp:
                registerUser();
                break;

            case R.id.textViewLogin:
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                break;
        }
    }

    private void registerUser() {
        String email = editTextemail.getText().toString().trim();
        String password = editTextpassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextemail.setError("Email nécessaire");
            editTextemail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextemail.setError("Email non valide");
            editTextemail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextpassword.setError("Mot de passe nécessaire");
            editTextpassword.requestFocus();
            return;
        }

        if (password.length() < 4) {
            editTextpassword.setError("Longueur doit être supérieur à 4");
            editTextpassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                String nom  = editTextNom.getText().toString().trim();
                String prenom = editTextPrenom.getText().toString().trim();
                String tel = editTextTel.getText().toString().trim();
                String date = editTextDateN.getText().toString().trim();
                String email = editTextemail.getText().toString().trim();

                if (task.isSuccessful()){

                    //Create new employee
                    newUser(nom, prenom, tel, date, email);

                    finish();
                    startActivity(new Intent(SignUpActivity.this, Accueil.class));
                }
                else{
                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "Vous avez déjà un compte", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }

    private void newUser(String nom, String prenom, String tel, String date, String email) {
        NewEmploye employe = new NewEmploye(nom, prenom, tel, email, date, "DG", true);

        mDatabase.child("employe").child("filiale006").child("dep009").child(mAuth.getUid()).setValue(employe);
    }
}

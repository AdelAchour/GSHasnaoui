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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    EditText editTextEmail, editTextPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        editTextEmail = (EditText)findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        progressBar = (ProgressBar)findViewById(R.id.progressbar);

        findViewById(R.id.textViewSignup).setOnClickListener(this);
        findViewById(R.id.buttonLogin).setOnClickListener(this);




    }

    @Override
    public void onStart() {
        super.onStart();

        if(mAuth.getCurrentUser()!= null){ //if authenticated

            String UID = mAuth.getCurrentUser().getUid().toString();
            System.out.println("UID = "+UID);

            final DatabaseReference emp = FirebaseDatabase.getInstance().getReference().child("Employe").child(UID).child("Profile");

            emp.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String isAdmin = dataSnapshot.child("isAdmin").getValue().toString();

                    if(isAdmin.equals("true")){
                        finish();
                        startActivity(new Intent(getApplicationContext(), AccueilAdmin.class));
                    }
                    else{
                        finish();
                        startActivity(new Intent(getApplicationContext(), Accueil.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            //finish();
            //mAuth.signOut();
            //startActivity(new Intent(this, ProfileActivity.class));


        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLogin:
                userLogin();
                break;

            case R.id.textViewSignup:
                finish();
                startActivity(new Intent(this, SignUpActivity.class));
                break;

        }
    }

    private void userLogin() {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        //Testing
        if (email.isEmpty()){
            editTextEmail.setError("Email nécessaire");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Email non valide");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Mot de passe nécessaire");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 4) {
            editTextPassword.setError("Longueur doit être supérieur à 4");
            editTextPassword.requestFocus();
            return;
        }


        //It's okay
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    //finish();
                    //Intent intent = new Intent(MainActivity.this, Accueil.class);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //startActivity(intent);
                    String UID = mAuth.getCurrentUser().getUid().toString();
                    System.out.println("UID = "+UID);

                    final DatabaseReference emp = FirebaseDatabase.getInstance().getReference().child("Employe").child(UID).child("Profile");

                    emp.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String isAdmin = dataSnapshot.child("isAdmin").getValue().toString();

                            if(isAdmin.equals("true")){
                                finish();
                                startActivity(new Intent(getApplicationContext(), AccueilAdmin.class));
                            }
                            else{
                                finish();
                                startActivity(new Intent(getApplicationContext(), Accueil.class));
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });




                }
                else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}

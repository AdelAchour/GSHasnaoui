package com.prod.adelachour.gshasnaoui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AccueilAdmin extends AppCompatActivity implements View.OnClickListener {

    Button newDepartement, newFiliale, newEmploye;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil_admin);

        newDepartement = (Button)findViewById(R.id.newDepButton);
        newFiliale = (Button)findViewById(R.id.newFilialeButton);
        newEmploye = (Button)findViewById(R.id.newEmploye);

        findViewById(R.id.signOutClick).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.signOutClick:
                mAuth.signOut();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;

            case R.id.newDepButton:
                startActivity(new Intent(getApplicationContext(), NewDepartement.class));
                break;

                
        }
    }
}

package com.example.domenger.runsport;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class ConnexionProfile extends AppCompatActivity {
    protected DbHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        db = new DbHandler(this);
    }

    public void clickConnection(View view){

        EditText user = findViewById(R.id.login);
        EditText mdp = findViewById(R.id.mdp);

        String login = user.getText().toString();
        String password = mdp.getText().toString();


        ArrayList<HashMap<String, String>> userList;
        userList = db.GetUsers();

        for(int i=0; i<userList.size(); i++){
            if (login.equals(userList.get(i).get("login"))) {
                if (password.equals(userList.get(i).get("mdp"))){
                    AlertDialog alertDialog = new AlertDialog.Builder(ConnexionProfile.this).create();
                    alertDialog.setTitle("Succes");
                    alertDialog.setMessage("Bienvenue sur votre profil");
                    int finalI = i;
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            (dialog, which) -> {
                                dialog.dismiss();
                                Intent intent = new Intent(ConnexionProfile.this, Acceuil.class);
                                intent.putExtra("id_profil", Integer.parseInt(userList.get(finalI).get("id")));
                                startActivity(intent);
                                finish();
                            });
                    alertDialog.show();
                    return;
                }
            }
        }
        AlertDialog alertDialog = new AlertDialog.Builder(ConnexionProfile.this).create();
        alertDialog.setTitle("Échec");
        alertDialog.setMessage("Aucun profil n'a été trouvé");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                (dialog, which) -> dialog.dismiss());
        alertDialog.show();

    }
}

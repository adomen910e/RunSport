package com.example.domenger.runsport;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CreationProfile extends AppCompatActivity {
    protected DbHandler dbHandler;

    private EditText nom;
    private EditText prenom;
    private EditText login;
    private EditText mdp;
    private EditText mdpconf;
    private EditText datejj;
    private EditText datemm;
    private EditText dateaaaa;
    private int lenghtedittext = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creation);

        nom = (EditText) findViewById(R.id.txtnom);
        prenom = (EditText)findViewById(R.id.txtprenom);
        login = (EditText)findViewById(R.id.txtlogin);
        mdp = (EditText)findViewById(R.id.txtmdp);
        mdpconf = (EditText)findViewById(R.id.txtmdpconf);
        datejj = (EditText)findViewById(R.id.txtdatejj);
        datemm = (EditText)findViewById(R.id.txtdatemm);
        dateaaaa = (EditText)findViewById(R.id.txtdateaaaa);

        datejj.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length()==2) {
                    datemm.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });

        datemm.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length()==2) {
                    dateaaaa.requestFocus();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });

        dateaaaa.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs.length() > 4) {
                    dateaaaa.setText(cs.subSequence(0, cs.length()-1));
                    dateaaaa.setSelection(dateaaaa.getText().length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2, int arg3) {

            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }

        });

        dbHandler = new DbHandler(CreationProfile.this);
    }

    private boolean validBasic(Boolean b, String text, String message, EditText e){
        if( text.length() == 0 ) {
            e.setError(message + " exigé");
            return false;
        }
        String regex = "(.)*(\\d)(.)*";
        Pattern pattern = Pattern.compile(regex);

        if( pattern.matcher(text).matches()){
            e.setError("Attention votre "+ message +" contient des chiffres");
            return false;
        }
        return b;
    }

    private boolean validLogin(Boolean b, String text){
        if(0 == text.length()) {
            login.setError("Nom d\'utilisateur exigé");
            return false;
        }

        ArrayList<HashMap<String, String>> userList;
        userList = dbHandler.GetUsers();

        for(int i=0; i<userList.size(); i++){
            if (text.equals(userList.get(i).get("login"))) {
                login.setError("Nom d\'utilisateur prit");
                return false;
            }
        }

        return b;
    }

    private boolean validMdp(Boolean b, String textmdp, String textconf){
        if(0 == textmdp.length() || 0 == textconf.length()) {
            if(0 == textmdp.length()) {
                mdp.setError("Mot de passe exigé");
            }
            if(0 == textconf.length()) {
                mdpconf.setError("Mot de passe exigé");

            }
            return false;
        }

        if(!textmdp.equals(textconf)){
            mdp.setError("Mots de passe non égaux");
            mdpconf.setError("Mots de passe non égaux");
            return false;
        }

        return b;
    }

    private boolean validDate(Boolean b, String date){

        String error_message = "";
        if ( Integer.parseInt(datejj.getText().toString()) < 1 || Integer.parseInt(datejj.getText().toString()) > 31){
            this.datejj.setError("Jour non conforme");
            return false;
        }

        if ( Integer.parseInt(datemm.getText().toString()) < 1 || Integer.parseInt(datemm.getText().toString()) > 12){
            this.datemm.setError("Mois non conforme");
            return false;
        }

        if ( dateaaaa.getText().toString().length() != 4){
            this.dateaaaa.setError("Année non conforme");
            return false;
        }

        return b;
    }


    public void clickCreation(View view){
        boolean success = true;

        String txtNom = nom.getText().toString();
        success = validBasic(success, txtNom,"Nom", nom);

        String txtPrenom = prenom.getText().toString();
        success = validBasic(success, txtNom,"Prénom", prenom);

        String txtLogin = login.getText().toString();
        success = validLogin(success, txtLogin);

        String txtMdp = mdp.getText().toString();
        String txtMdpconf = mdpconf.getText().toString();
        success = validMdp(success, txtMdp, txtMdpconf);

        String txtDate = datejj.getText().toString() + "/" + datemm.getText().toString() + "/" + dateaaaa.getText().toString();
        success = validDate(success, txtDate);

        if (success){
            long id = dbHandler.insertUserDetails(txtLogin,txtNom,txtPrenom,txtMdp,txtDate);

            AlertDialog alertDialog = new AlertDialog.Builder(CreationProfile.this).create();
            alertDialog.setTitle("Validation");
            alertDialog.setMessage("Votre profil a bien été créé!");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(CreationProfile.this, ConnexionProfile.class);
                            startActivity(intent);
                            finish();
                        }
                    });
            alertDialog.show();
        }
    }
}

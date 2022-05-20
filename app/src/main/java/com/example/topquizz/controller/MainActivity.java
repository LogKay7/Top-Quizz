package com.example.topquizz.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.topquizz.R;
import com.example.topquizz.model.User;

public class MainActivity extends AppCompatActivity {

    private TextView mGreentingText;
    private EditText mNameInput;
    private Button mPlayButton;
    private User mUser;     //Utilisateur
    public static final int GAME_ACTIVITY_REQUEST_CODE = 42;
    private SharedPreferences mPreferences;

    public static final String PREF_KEY_SCORE = "PREF_KEY_SCORE";
    public static final String PREF_KEY_FIRSTNAME = "PREF_KEY_FIRSTNAME";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUser = new User(); //Initialisation

        mPreferences = getPreferences(MODE_PRIVATE);

        mGreentingText = (TextView) findViewById(R.id.activity_main_greeting_txt);
        mNameInput = (EditText) findViewById(R.id.activity_main_name_input);
        mPlayButton = (Button) findViewById(R.id.activity_main_play_btn);

        mPlayButton.setEnabled(false);

        greetUser();

        mNameInput.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                mPlayButton.setEnabled(s.toString().length() != 0);  //Vérifie qu'un caractère soit entré et active le bouton
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstname = mNameInput.getText().toString();
                mUser.setFirstname(firstname);

                mPreferences.edit().putString(PREF_KEY_FIRSTNAME,mUser.getFirstname()).apply();
                // L'utilisateur vient de cliquer

                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
        {   //Récupération du score de l'Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            mPreferences.edit().putInt(PREF_KEY_SCORE, score).apply();

            greetUser();
        }

    }

    private void greetUser()
    {
        String firstname = mPreferences.getString(PREF_KEY_FIRSTNAME,null);

        if(firstname != null)
        {
            int score = mPreferences.getInt(PREF_KEY_SCORE, 0);

            String fulltext = "Bon retour parmi nous, " + firstname
                    + "!\nTon dernier score était de " +score
                    + " points, feras-tu mieux cette fois ?";

            mGreentingText.setText(fulltext);
            mNameInput.setText(firstname);
            mNameInput.setSelection(firstname.length());
            mPlayButton.setEnabled(true);

        }
    }







}

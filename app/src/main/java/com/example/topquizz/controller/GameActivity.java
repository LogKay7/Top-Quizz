package com.example.topquizz.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquizz.R;
import com.example.topquizz.model.Question;
import com.example.topquizz.model.QuestionBank;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity implements View.OnClickListener
{

    private TextView mquestionText;
    private Button manswerButton1;
    private Button manswerButton2;
    private Button manswerButton3;
    private Button manswerButton4;

    private QuestionBank mQuestionBank;
    private Question mCurrentQuestion;

    private int mScore;
    private int mNumberOfQuestions;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";
    public static final String BUNDLE_STATE_SCORE = "CurrentScore";
    public static final String BUNDLE_STATE_QUESTION = "currentQuestion";

    private boolean mEnableTouchEvents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mQuestionBank = this.generateQuestions();


        if (savedInstanceState != null)
        {
            mScore = savedInstanceState.getInt(BUNDLE_EXTRA_SCORE);
            mNumberOfQuestions = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mNumberOfQuestions = 4;
        }

        mEnableTouchEvents = true;

            //Liaison du contrôleur à la vue
        mquestionText = findViewById(R.id.activity_game_question_text);
        manswerButton1 = findViewById(R.id.activity_game_answer1_btn);
        manswerButton2 = findViewById(R.id.activity_game_answer2_btn);
        manswerButton3 = findViewById(R.id.activity_game_answer3_btn);
        manswerButton4 = findViewById(R.id.activity_game_answer4_btn);

            //Affectation d'un identifiant à chaque bouton
        manswerButton1.setTag(0);
        manswerButton2.setTag(1);
        manswerButton3.setTag(2);
        manswerButton4.setTag(3);

            //Détection de l'appui sur les boutons
        manswerButton1.setOnClickListener(this);
        manswerButton2.setOnClickListener(this);
        manswerButton3.setOnClickListener(this);
        manswerButton4.setOnClickListener(this);



        mCurrentQuestion = mQuestionBank.getQuestion();
        this.displayQuestion(mCurrentQuestion);

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(BUNDLE_EXTRA_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION, mNumberOfQuestions);
        
        super.onSaveInstanceState(outState);
    }

    private void displayQuestion(final Question question)
    {   //Affiche la question dans la vue et les boutons

        mquestionText.setText(question.getQuestion());
        manswerButton1.setText(question.getChoiceList().get(0));
        manswerButton2.setText(question.getChoiceList().get(1));
        manswerButton3.setText(question.getChoiceList().get(2));
        manswerButton4.setText(question.getChoiceList().get(3));
    }


    private QuestionBank generateQuestions()
    {
        Question question1 = new Question("Quel est le nom du président actuel de la France ?",
                Arrays.asList("François Hollande", "Emmanuel Macron", "Jacques Chirac", "Homer Simpsons"),1);

        Question question2 = new Question("Quelle est la capitale de la Roumanie ?",
                Arrays.asList("Bucarest", "Vienne", "San Antonio", "Kinshasa"),0);

        Question question3 = new Question("Qui a peint la Mona Lisa ?",
                Arrays.asList("Michelangelo", "Carravagio", "Léonard De Vinci", "Mona Lisa"),2);

        Question question4 = new Question("Quelle est le numéro de maison des Simpsons ?",
                Arrays.asList("42", "101", "666", "742"),3);

        Question question5 = new Question("Quelle console de jeux vidéos s'est le plus vendue dans le monde ?",
                Arrays.asList("La Playstation 2", "La Nintendo DS", "La Playstation 4", "La Wii"),0);

        Question question6 = new Question("Dans Harry Potter, comment s'appelle le chien à 3 têtes de Hagrid ?",
                Arrays.asList("Buck", "Touffu", "Toutouffe", "Il n'a pas de nom"),1);

        Question question7 = new Question("Quelle est la marque la plus riche au monde ?",
                Arrays.asList("Apple", "Amazon", "Google", "Samsung"),0);

        Question question8 = new Question("A quelle heure a été créée cette question ?",
                Arrays.asList("10h26", "16h59", "01h06", "23h53"),3);

        Question question9 = new Question("Comment s'appelle le créateur d'Amazon ?",
                Arrays.asList("Mark Zuckerberg", "Jeff Bezos", "Steve Jobs", "Michel Krieger"),1);

        return new QuestionBank(Arrays.asList(question1, question2, question3, question4, question5, question6, question7, question8, question9));
    }


    @Override
    public void onClick(View v)
    {
        int responseIndex = (int) v.getTag();

        if (responseIndex == mCurrentQuestion.getAnswerIndex())
        {       //Bonne réponse
            Toast.makeText(this,"Bonne réponse !",Toast.LENGTH_SHORT).show();
            mScore++;
        } else {
                //Mauvaise réponse
            Toast.makeText(this,"Mauvaise réponse...",Toast.LENGTH_SHORT).show();
        }

        mEnableTouchEvents = false;

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                    mEnableTouchEvents = true;

                if (--mNumberOfQuestions == 0)
                {   //Pas de questions restantes, partie terminée
                    endGame();

                } else {    //Affichage question suivante
                    mCurrentQuestion = mQuestionBank.getQuestion();
                    displayQuestion(mCurrentQuestion);
                }
             }
        }, 2000);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
    }

    private void endGame()
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle("Bien joué !")
                    .setMessage("Ton score est de " + mScore + " points.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .create()
                    .show();
        }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

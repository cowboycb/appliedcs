package com.cowboydadas.scarnesdice;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int userTotalScore;
    private int userLastTotalScore;
    private int userLastScore;
    private int computerTotalScore;
    private int computerLastScore;
    private ImageView imgView;
    private TextView txtScore;
    private Button btnHold;
    private Button btnRoll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgView = findViewById(R.id.imgViewDice);
        btnRoll = findViewById(R.id.btnRoll);
        btnRoll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserScore();
            }
        });
        txtScore = findViewById(R.id.txtScore);

        Button btnReset = findViewById(R.id.btnReset);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetAll();
            }
        });

        btnHold = findViewById(R.id.btnHold);
        btnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateScorePano(true);
                computerTurn();
            }
        });

    }

    private void resetAll() {
        userTotalScore = userLastTotalScore = userLastScore = computerLastScore = computerTotalScore = 0;
        updateScorePano(true);
    }

    private int rollDice() {
        Random rnd = new Random();
        int value = rnd.nextInt(6) + 1;
        Log.d("DICE", "Dice is rolled and value = " + value);
        return value;
    }

    private void setImageView(int diceValue){
        int imageId = getResourseId(this, "dice"+diceValue, "drawable", getPackageName());
        imgView.setImageResource(imageId);
    }

    private void setUserScore(){
        Log.d("DICE", "Rolling Dice");
        int diceValue = rollDice();
        setImageView(diceValue);
        if (diceValue != 1) {
            userLastScore = diceValue;
            userTotalScore += userLastScore;
            updateScorePano(false);
        }else{
            updateScorePano(true);
            computerTurn();
        }
    }

    private void setComputerScore(){
        Log.d("DICE", "Rolling Dice");
        int diceValue = rollDice();
        setImageView(diceValue);
        if (diceValue != 1) {
            computerLastScore = diceValue;
            computerTotalScore += computerLastScore;
            updateScorePano(false);
        }else{
            computerLastScore = 1;
            updateScorePano(true);
            enableButtons(true);
        }
    }

    private void updateScorePano(boolean updateTotal){
        if (updateTotal){
            userLastTotalScore = userTotalScore;
            userLastScore = 0;
        }
        String scoreText = String.format("Your score: %d computer score: %d your turn score: %d", userLastTotalScore, computerTotalScore, userLastScore);
        txtScore.setText(scoreText);
    }

    public int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
    }

    public void computerTurn(){
        enableButtons(false);
        do{
            setComputerScore();
        }while (computerLastScore != 1);
    }

    private void enableButtons(boolean value){
        btnRoll.setEnabled(value);
        btnHold.setEnabled(value);
    }


}

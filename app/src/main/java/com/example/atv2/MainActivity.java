package com.example.atv2;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextWatcher {
    private double result = 0;
    private  DecimalFormat df = new DecimalFormat("0.00");


    EditText totalVal;
    EditText numPeople;
    TextView valuePerPerson;

    FloatingActionButton shareButton;
    FloatingActionButton voiceButton;

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Objects.requireNonNull(getSupportActionBar()).hide();

        totalVal = findViewById(R.id.totalValue);
        numPeople = findViewById(R.id.numPeople);
        valuePerPerson = findViewById(R.id.valuePerPerson);

        shareButton = findViewById(R.id.shareButton);
        voiceButton = findViewById(R.id.voiceButton);


        valuePerPerson.setText("$ 0.00");
        totalVal.addTextChangedListener(this);
        numPeople.addTextChangedListener(this);

        tts = new TextToSpeech(getApplicationContext(), status -> {
            if (status != TextToSpeech.ERROR) {
                Locale localeBR = new Locale("pt", "BR");
                tts.setLanguage(localeBR);
            }
        });

        voiceButton.setOnClickListener(v -> {
            String text = String.valueOf((int) result);
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        });


        shareButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_message) + df.format(result));
            startActivity(intent);
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            double value = Double.parseDouble(totalVal.getText().toString());
            int people = Integer.parseInt(numPeople.getText().toString());

            if ((value != 0) && (people != 0)) {
                BigDecimal bd = new BigDecimal(value / people).setScale(2, RoundingMode.HALF_UP);
                result = bd.doubleValue();

                valuePerPerson.setText("$ " + df.format(result));
            }
        } catch (Exception e) {
            valuePerPerson.setText("$ 0.00");
            result = 0;
        }
    }
}

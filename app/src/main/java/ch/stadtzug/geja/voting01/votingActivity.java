package ch.stadtzug.geja.voting01;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class votingActivity extends AppCompatActivity {

    private String vote = null;
    CheckBox kandidat1, kandidat2, kandidat3;
    TextView abstimmungstext;
    String tAbstimmungsText = "Lorem Ipsum";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        kandidat1 = (CheckBox)findViewById(R.id.checkbox_1);
        kandidat2 = (CheckBox)findViewById(R.id.checkbox_2);
        abstimmungstext = (TextView)findViewById(R.id.abstimmungstext);

        tAbstimmungsText = getIntent().getStringExtra("abstimmungsText");

        try {abstimmungstext.setText(tAbstimmungsText);}
            catch (Exception e) {
                System.out.println(e.getMessage());
            }

        //Erster Kandidat
        kandidat1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(kandidat1.isChecked())
                {
                    vote = "Test";
                }
            }
        });

        //Zweiter Kandidat
        kandidat2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(kandidat2.isChecked())
                {
                    vote = "Peter Müller";
                }
            }
        });

    }


    public void checkStimme(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Sie haben gestimmt für: " + vote;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

}

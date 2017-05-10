package ch.stadtzug.geja.voting01;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

public class votingActivity extends AppCompatActivity {

    private String vote = null;
    CheckBox option1, option2;
    TextView abstimmungstext;
    String authkey;
    String tAbstimmungsText;
    String tOption1;
    String tOption2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        option1 = (CheckBox)findViewById(R.id.checkbox_1);
        option2 = (CheckBox)findViewById(R.id.checkbox_2);
        abstimmungstext = (TextView)findViewById(R.id.abstimmungstext);

        //Daten von Intent der MainActivity holen
        tAbstimmungsText = getIntent().getStringExtra("abstimmungsText");
        tOption1 = getIntent().getStringExtra("option1");
        tOption2 = getIntent().getStringExtra("option2");
        authkey = getIntent().getStringExtra("authkey");

        try {
            abstimmungstext.setText(tAbstimmungsText);
            option1.setText(tOption1);
            option2.setText(tOption2);

        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //OnClickListener für ersten Kandidaten erstellen. Beim Klicken der Option wird "vote" gesetzt
        option1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(option1.isChecked())
                {
                    vote = (String) option1.getText();
                }
            }
        });

        //OnClickListener für zweiten Kandidaten erstellen. Beim Klicken der Option wird "vote" gesetzt
        option2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(option2.isChecked())
                {
                    vote = (String) option2.getText();
                }
            }
        });

    }

    //Diese Methode zeigt einen Toast mit der abgegebenen Stimme an. (für Testzwecke implementiert.)
    public void vote(View view) {
        Context context = getApplicationContext();
        CharSequence text = "Sie haben gestimmt für: " + vote;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();


        //Stimme an Backendserver senden


    }

}

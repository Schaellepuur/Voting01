package ch.stadtzug.geja.voting01;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class votingActivity extends AppCompatActivity {

    private String vote = null;
    CheckBox kandidat1, kandidat2, kandidat3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        kandidat1 = (CheckBox)findViewById(R.id.checkbox_MaxMuster);
        kandidat2 = (CheckBox)findViewById(R.id.checkbox_PeterMuller);

        //Erster Kandidat
        kandidat1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(kandidat1.isChecked())
                {
                    vote = "Max Muster";
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

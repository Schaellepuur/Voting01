package ch.stadtzug.geja.voting01;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class votingActivity extends AppCompatActivity {

    private String vote = null;
    CheckBox option1, option2;
    TextView abstimmungstext;
    String stringAuthkey;
    int authkey;
    String voteID;
    String tAbstimmungsText;
    String tOption1;
    String tOption2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        option1 = (CheckBox) findViewById(R.id.checkbox_1);
        option2 = (CheckBox) findViewById(R.id.checkbox_2);
        abstimmungstext = (TextView) findViewById(R.id.abstimmungstext);

        //Daten von Intent der MainActivity holen
        tAbstimmungsText = getIntent().getStringExtra("abstimmungsText");
        tOption1 = getIntent().getStringExtra("option1");
        tOption2 = getIntent().getStringExtra("option2");
        stringAuthkey = getIntent().getStringExtra("authkey");
        authkey = Integer.parseInt(stringAuthkey);
        voteID = getIntent().getStringExtra("voteID");

        try {
            abstimmungstext.setText(tAbstimmungsText);
            option1.setText(tOption1);
            option2.setText(tOption2);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        //OnClickListener für ersten Kandidaten erstellen. Beim Klicken der Option wird "vote" gesetzt
        option1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (option1.isChecked()) {
                    vote = (String) option1.getText();
                }
            }
        });

        //OnClickListener für zweiten Kandidaten erstellen. Beim Klicken der Option wird "vote" gesetzt
        option2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (option2.isChecked()) {
                    vote = (String) option2.getText();
                }
            }
        });

    }

    //Diese Methode implementiert den Abstimmungsvorgang. Es wird ein JSON Objekt der Stimme erstellt und anschliessend an den Backendserver geschickt.
    public void vote(View view) {

        final String url = "http://212.4.70.74/stimmeAbgeben";

        //JSON Objekt der Stimme erstellen
        final JSONObject stimme = new JSONObject();

        try {
            stimme.put("voteid", voteID);
            stimme.put("authkey", authkey);
            stimme.put("option", vote);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        //Stimme an Backendserver senden
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL connectionUrl = new URL(url);
                    HttpURLConnection connection = (HttpURLConnection) connectionUrl.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);

                    Log.i("JSON", stimme.toString());

                    DataOutputStream os = new DataOutputStream(connection.getOutputStream());

                    os.writeBytes(stimme.toString());
                    os.flush();
                    os.close();

                    Log.i("STATUS", String.valueOf(connection.getResponseCode()));
                    Log.i("MSG" , connection.getResponseMessage());

                    connection.disconnect();
                    Toast.makeText(votingActivity.this, "Sie haben gestimmt für: " + vote, Toast.LENGTH_LONG).show();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }
}

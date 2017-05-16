package ch.stadtzug.geja.voting01;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class votingActivity extends AppCompatActivity {

    private String vote = null;
    RadioButton option1, option2;
    TextView abstimmungstext;
    String stringAuthkey;
    int authkey;
    String voteID;
    String tAbstimmungsText;
    String tOption1;
    String tOption2;

    String votingHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        option1 = (RadioButton) findViewById(R.id.radiobutton_1);
        option2 = (RadioButton) findViewById(R.id.radiobutton_2);
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

                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null){
                        stringBuilder.append(line+"\n");
                    }
                    bufferedReader.close();
                    String response = stringBuilder.toString();
                    Log.i("Response", response);

                    if (response.indexOf("locked") != -1){

                        String[] separated = response.split(":");
                        String tempString = separated[2];
                        separated = tempString.split(",");
                        votingHash = separated[0];
                        votingHash = votingHash.replace("\"", "");

                        Log.i("VotingHash ist", votingHash );

                        Toast.makeText(votingActivity.this, "Sie haben bereits abgestimmt. ", Toast.LENGTH_LONG).show();
                        startVotingLockedActivity();

                    }

                    else {

                        Toast.makeText(votingActivity.this, "Sie haben gestimmt für: " + vote, Toast.LENGTH_LONG).show();

                    }
                    connection.disconnect();


                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public void startVotingLockedActivity() {
        Intent i = new Intent(votingActivity.this, votingLocked.class);
        i.putExtra("votingHash", votingHash);
        startActivity(i);
    }
}

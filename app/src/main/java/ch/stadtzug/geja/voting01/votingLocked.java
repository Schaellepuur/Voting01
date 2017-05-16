package ch.stadtzug.geja.voting01;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class votingLocked extends AppCompatActivity {

    String votingHash;
    EditText editTextHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting_locked);

        editTextHash = (EditText)findViewById(R.id.editText);

        votingHash = getIntent().getStringExtra("votingHash");
        editTextHash.setText(votingHash);
    }

    @Override
    public void onBackPressed() {
        finish();

    }

    public void checkVote(){
    }

}

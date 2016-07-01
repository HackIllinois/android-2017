package org.hackillinois.app2017.HelpQ;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.hackillinois.app2017.R;

/**
 * Created by tommypacker for HackIllinois' 2016 Clue Hunt
 */
public class NewTicketActivity extends AppCompatActivity {

    private Button submitTicketButton;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_ticket_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.genericToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("New Ticket");

        submitTicketButton = (Button) findViewById(R.id.submitTicket);
        submitTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

package uk.ac.solent.layoutweight;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;


public class ReportProblems extends AppCompatActivity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportproblems);
        Button b1 = (Button)findViewById(R.id.btn1);
        b1.setOnClickListener(this);
        Button b2 = (Button)findViewById(R.id.btn2);
        b2.setOnClickListener(this);
    }

package uk.ac.solent.textfileeditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.io.IOException;

import android.os.Environment;
import android.app.AlertDialog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.FileWriter;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ]
        try {
            EditText et = (EditText) findViewById(R.id.et);
            BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/textedit.txt"));
            String line = "";
            et.setText("");
            while ((line = reader.readLine()) != null) {
                et.setText(et.getText() + line + "\n");
            }

        } catch (IOException e) {
            new AlertDialog.Builder(this).setPositiveButton("OK", null).
                    setMessage("ERROR: " + e).show();
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.saveFile) {
            try {
                EditText et = (EditText) findViewById(R.id.et);
                String content = et.getText().toString();
                PrintWriter pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/textedit.txt"));
                pw.print(content);
                pw.close();

            } catch (IOException e) {
                new AlertDialog.Builder(this).setPositiveButton("OK", null).
                        setMessage("ERROR: " + e).show();
            }
            return true;
        } else if (item.getItemId() == R.id.loadFile) {


            try {
                EditText et = (EditText) findViewById(R.id.et);
                BufferedReader reader = new BufferedReader(new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/textedit.txt"));
                String line = "";
                et.setText("");
                while ((line = reader.readLine()) != null) {
                    et.setText(et.getText() + line + "\n");
                }

            } catch (IOException e) {
                new AlertDialog.Builder(this).setPositiveButton("OK", null).
                        setMessage("ERROR: " + e).show();
            }
            return true;
        }
        return false;
    }

}

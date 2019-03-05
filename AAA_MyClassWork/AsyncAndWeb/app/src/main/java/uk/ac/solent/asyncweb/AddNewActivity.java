package uk.ac.solent.asyncweb;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.os.AsyncTask;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import android.app.AlertDialog;

public class AddNewActivity extends AppCompatActivity implements View.OnClickListener{

    class MyTask extends AsyncTask<String,Void,String>
    {
        public String doInBackground(String... input)
        {
            HttpURLConnection conn = null;
            try
            {
                URL url = new URL("http://www.free-map.org.uk/course/ws/addhit.php");
                conn = (HttpURLConnection) url.openConnection();



                String postData = "song="+input[0]+"&artist="+input[1]+"&year="+input[2];
                // For POST
                conn.setDoOutput(true);
                conn.setFixedLengthStreamingMode(postData.length());

                OutputStream out = null;
                out = conn.getOutputStream();
                out.write(postData.getBytes());
                if(conn.getResponseCode() == 200)
                {
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String all = "", line;
                    while((line = br.readLine()) !=null)
                        all += line;
                    return all;
                }
                else
                {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }
            }
            catch(IOException e)
            {
                return e.toString();
            }
            finally
            {
                if(conn!=null)
                {
                    conn.disconnect();
                }
            }
        }

        public void onPostExecute(String result) {
            if(result == ""){
                new AlertDialog.Builder(AddNewActivity.this).
                        setMessage("Song added successfully").
                        setPositiveButton("OK", null).show();

            }else{
                new AlertDialog.Builder(AddNewActivity.this).
                        setMessage("Server sent back: " + result).
                        setPositiveButton("OK", null).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnew);
        Button go = findViewById(R.id.btn1);
        go.setOnClickListener(this);
    }

    public void onClick(View v)
    {
        EditText song= findViewById(R.id.etSong);
        EditText artist = findViewById(R.id.etArtist);
        EditText year = findViewById(R.id.etYear);

        String strSong = song.getText().toString();
        String strArtist = artist.getText().toString();
        String strYear = year.getText().toString();

        MyTask t = new MyTask();
        t.execute(strSong, strArtist, strYear);
    }
}



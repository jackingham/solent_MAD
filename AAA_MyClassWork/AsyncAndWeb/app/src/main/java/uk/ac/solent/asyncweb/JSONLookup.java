package uk.ac.solent.asyncweb;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import android.app.AlertDialog;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

public class JSONLookup extends AppCompatActivity implements View.OnClickListener {

    class MyTask extends AsyncTask<String, Void, String> {

        public String doInBackground(String... input) {
            HttpURLConnection conn = null;

            try {
                URL url = new URL("http://www.free-map.org.uk/course/ws/hits.php?artist=" + (URLEncoder.encode(input[0], "UTF-8")) + "&format=json");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                System.out.print(in);
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuffer result = new StringBuffer();
                    String line;
                    while ((line = br.readLine()) != null) {
                        result.append(line);
                    }
                    String inArray = result.toString();
                    String text = "";
                    try {
                        JSONArray jsonArr = new JSONArray(inArray);


                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject curObj = jsonArr.getJSONObject(i);
                            String title = curObj.getString("title");
                            String artist = curObj.getString("artist");

                            text += title + " by " + artist + "\n";
                        }


                    }

                    catch (JSONException e) {
                        return "Error: " + e.toString();
                    }
                    return text;
                }

                else {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }

            }
            catch (IOException e) {
                return e.toString();
            }
            finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }


        public void onPostExecute(String text) {
            TextView tv1 = findViewById(R.id.tv1);
            tv1.setText(text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);
        Button go = (Button) findViewById(R.id.btn1);
        go.setOnClickListener(this);
    }

    public void onClick(View v) {
        EditText artist = findViewById(R.id.et1);
        JSONLookup.MyTask t = new JSONLookup.MyTask();
        t.execute(artist.getText().toString());
    }


}


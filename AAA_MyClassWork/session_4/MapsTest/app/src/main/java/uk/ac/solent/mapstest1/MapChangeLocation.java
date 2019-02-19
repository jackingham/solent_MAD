package uk.ac.solent.mapstest1; // or whatever it is in your case

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.support.v7.app.AlertDialog;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import org.osmdroid.util.GeoPoint;

public class MapChangeLocation extends AppCompatActivity implements View.OnClickListener{

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_location);

        Button locate = (Button)findViewById(R.id.btn1);
        locate.setOnClickListener(this);

    }


    public void onClick(View v)
    {
        Intent intent = new Intent();
        Bundle bundle=new Bundle();

        EditText et_lat = (EditText) findViewById(R.id.et1);
        EditText et_long = (EditText) findViewById(R.id.et2);

        try {
            String LatString = et_lat.getText().toString();
            double latitude = Double.parseDouble(LatString);
            String LongString = et_long.getText().toString();
            double longitude = Double.parseDouble(LongString);

            if (latitude >= -90 && latitude <= 90) {

                if (longitude >= -180 && longitude <= 180) {

                    bundle.putDouble("long_out",longitude);
                    bundle.putDouble("lat_out",latitude);
                    intent.putExtras(bundle);
                    setResult(RESULT_OK,intent);
                    finish();
                } else {
                    String message = "Invalid longitude";
                    popupMessage(message);
                }
            } else {
                String message = "Invalid latitude";
                popupMessage(message);
            }

        } catch (Exception e) {
            String message = "Invalid value entered";
            popupMessage(message);
        }




    }

    private void popupMessage(String message) {
        new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage(message).show();
    }
}


package uk.ac.solent.mapstest1;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.EditText;
import android.support.v7.app.AlertDialog;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    MapView mv;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);
        mv.getController().setZoom(16);
        mv.getController().setCenter(new GeoPoint(50.939652, -0.438075));
        Button l = (Button) findViewById(R.id.btn1);
        l.setOnClickListener(this);
        Button r = (Button) findViewById(R.id.btn2);
        r.setOnClickListener(this);
    }

    private void popupMessage(String message) {
        new AlertDialog.Builder(this).setPositiveButton("OK", null).setMessage(message).show();
    }

    public void onClick(View view) {
        mv = (MapView) findViewById(R.id.map1);
        EditText et_lat = (EditText) findViewById(R.id.et1);
        EditText et_long = (EditText) findViewById(R.id.et2);

        switch (view.getId()) {
            case R.id.btn1: //locate button
                try {
                    String LatString = et_lat.getText().toString();
                    double latitude = Double.parseDouble(LatString);
                    String LongString = et_long.getText().toString();
                    double longitude = Double.parseDouble(LongString);

                    if (latitude >= -90 && latitude <= 90) {

                        if (longitude >= -180 && longitude <= 180) {
                            mv.setBuiltInZoomControls(true);
                            mv.getController().setZoom(16);
                            mv.getController().setCenter(new GeoPoint(latitude, longitude));
                        } else {
                            String message = "Invalid longitude";
                            popupMessage(message);
                        }
                    } else {
                        String message = "Invalid latitude";
                        popupMessage(message);
                    }

                } catch (Exception e) {
                    String message = "Invalid value entered entry";
                    popupMessage(message);
                }
                System.out.println("DEBUG MESSAGE 1*********** locate pressed");
                break;

            case R.id.btn2: // reset default
                System.out.println("DEBUG MESSAGE 1*********** reset pressed");
                mv.setBuiltInZoomControls(true);
                mv.getController().setZoom(16);
                mv.getController().setCenter(new GeoPoint(50.939652, -0.438075));
                break;

            default:
                break;
        }


    }

}
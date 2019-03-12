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

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.content.Intent;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;


import android.app.Activity;
import android.os.Bundle;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.Toast;

import android.preference.PreferenceManager;
import android.widget.Toast;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LocationListener {
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;


    /*** Called when the activity is first created.*/
    @Override


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        // This line sets the user agent, a requirement to download OSM maps
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));

        setContentView(R.layout.activity_main);
        mv = (MapView) findViewById(R.id.map1);
        mv.setBuiltInZoomControls(true);


        LocationManager mgr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 1, this);

        mv.getController().setZoom(16);

        ItemizedIconOverlay.OnItemGestureListener<OverlayItem> markerGestureListener;
        markerGestureListener = new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
            public boolean onItemLongPress(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getTitle() + ":" + item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }

            public boolean onItemSingleTapUp(int i, OverlayItem item) {
                Toast.makeText(MainActivity.this, item.getTitle() + ":" + item.getSnippet(), Toast.LENGTH_SHORT).show();
                return true;
            }
        };

        items = new ItemizedIconOverlay<OverlayItem>(this, new ArrayList<OverlayItem>(), markerGestureListener);
        OverlayItem fernhurst = new OverlayItem("Fernhurst", "Village in West Sussex", new GeoPoint(51.05, -0.72));
        OverlayItem blackdown = new OverlayItem("Blackdown", "highest point in West Sussex", new GeoPoint(51.0581, -0.6897));
        items.addItem(fernhurst);
        items.addItem(blackdown);
        mv.getOverlays().add(items);


    }

    public void onLocationChanged(Location newLoc) {

        Double newLat = newLoc.getLatitude();
        Double newLong = newLoc.getLongitude();
        mv.getController().setCenter(new GeoPoint(newLat, newLong));
        OverlayItem currentLocation = new OverlayItem("You are here", "", new GeoPoint(newLat, newLong));
        items.addItem(currentLocation);
        currentLocation.setMarker(getResources().getDrawable(R.drawable.marker));

    }

    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " disabled", Toast.LENGTH_LONG).show();
    }

    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Provider " + provider +
                " enabled", Toast.LENGTH_LONG).show();
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

        Toast.makeText(this, "Status changed: " + status,
                Toast.LENGTH_LONG).show();
    }


    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.newpoi) {
            // react to the menu item being selected...
            Intent intent = new Intent(this, AddNewPOI.class);
            startActivityForResult(intent, 0);
            return true;

        }
        return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                String name_in = extras.getString("name_out");
                String type_in = extras.getString("name_out");
                String description_in = extras.getString("name_out");

                OverlayItem name = new OverlayItem(name_in, description_in, new GeoPoint(51.0581, -0.6897));
                items.addItem(name);



            }
        }

    }
}
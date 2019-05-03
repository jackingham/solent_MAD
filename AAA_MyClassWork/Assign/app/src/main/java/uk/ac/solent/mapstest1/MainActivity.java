package uk.ac.solent.mapstest1;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v7.app.AlertDialog;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import android.location.LocationManager;
import android.location.LocationListener;
import android.location.Location;
import android.content.Context;
import android.widget.Toast;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationListener {
    MapView mv;
    ItemizedIconOverlay<OverlayItem> items;
    OverlayItem currentLocation = null;


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
        mv.getOverlays().add(items);


    }

    public void onLocationChanged(Location newLoc) {

        if (currentLocation != null) {
            items.removeItem(currentLocation);
        }
        Double newLat = newLoc.getLatitude();
        Double newLong = newLoc.getLongitude();
        mv.getController().setCenter(new GeoPoint(newLat, newLong));
        currentLocation = new OverlayItem("You are here", "", new GeoPoint(newLat, newLong));
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.newpoi) {
            // react to the menu item being selected...
            Intent intent = new Intent(this, AddNewPOI.class);
            startActivityForResult(intent, 0);
            return true;

        } else if (item.getItemId() == R.id.prefs) {
            Intent intent = new Intent(this, MyPrefsActivity.class);
            startActivity(intent);
            return true;

        } else if (item.getItemId() == R.id.loadfile) {
            double currLat = currentLocation.getPoint().getLatitude();
            double currLong = currentLocation.getPoint().getLongitude();

            boolean loadSuccess = POIList.load();
            if (loadSuccess) {
                Toast.makeText(this, "Loading items, this will take a few seconds", Toast.LENGTH_LONG).show();
                items.removeAllItems();
                currentLocation = new OverlayItem("You are here", "", new GeoPoint(currLat, currLong));
                items.addItem(currentLocation);
                currentLocation.setMarker(getResources().getDrawable(R.drawable.marker));

                for (POI poi : POIList.getPOIList()) {
                    OverlayItem newItem = new OverlayItem(poi.getName(), (poi.getType() + ": " + poi.getDescription()), new GeoPoint(poi.getLatitude(), poi.getLongitude()));
                    items.addItem(newItem);
                }
            } else {
                new AlertDialog.Builder(MainActivity.this).setPositiveButton("OK", null).setMessage("Error loading file - you may not have saved any POIs").show();
            }
            return true;

        } else if (item.getItemId() == R.id.savefile) {
            boolean saveSuccess = POIList.save();
            if (saveSuccess) {
                Toast.makeText(this, "Points of interest saved to file", Toast.LENGTH_LONG).show();
            } else {
                new AlertDialog.Builder(MainActivity.this).setPositiveButton("OK", null).setMessage("Error saving file").show();
            }

        } else if (item.getItemId() == R.id.loadweb) {
            DownloadTask t = new DownloadTask();
            t.execute();
            return true;

        } else if (item.getItemId() == R.id.list) {
            Intent intent = new Intent(this, ViewPOIList.class);
            startActivityForResult(intent,1);
            return true;
        }

    return false;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == 0) {

            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                String name_in = extras.getString("name_out");
                String type_in = extras.getString("type_out");
                String description_in = extras.getString("description_out");
                double currLat = currentLocation.getPoint().getLatitude();
                double currLong = currentLocation.getPoint().getLongitude();

                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                boolean autoupload = prefs.getBoolean("autoupload", false);
                if (autoupload){
                    String strLat = Double.toString(currLat);
                    String strLong = Double.toString(currLong);
                    UploadTask t = new UploadTask();
                    t.execute(name_in,type_in,description_in,strLat,strLong);

                }

                OverlayItem name = new OverlayItem(name_in,(" "+type_in+" : "+description_in ), new GeoPoint(currLat,currLong));
                items.addItem(name);
                POI newPOI = new POI(name_in, type_in, description_in, currLong, currLat);
                POIList.addPOI(newPOI);
                Toast.makeText(this, "Marker added at current position.", Toast.LENGTH_LONG).show();


            }
        } else if (requestCode == 1){

            if (resultCode == RESULT_OK) {
                Bundle extras = intent.getExtras();
                int index = extras.getInt("index");
                List<POI> list_in = POIList.getPOIList();
                POI chosen_poi = list_in.get(index);
                mv.getController().setCenter(new GeoPoint(chosen_poi.getLatitude(), chosen_poi.getLongitude()));
                mv.getController().setZoom(16);

            }
        }

    }

    public void onDestroy(){
        POIList.save();
        super.onDestroy();
    }


    class DownloadTask extends AsyncTask<Void, Void, String> {
        public String doInBackground(Void... unused) {
            HttpURLConnection  conn = null;
            try {
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/get.php?year=19&username=user005&format=csv");
                conn = (HttpURLConnection) url.openConnection();
                InputStream in = conn.getInputStream();
                if (conn.getResponseCode() == 200) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] components = line.split(",");
                        if (components.length == 5) {
                            String newName = components[0];
                            String newType = components[1];
                            String newDesc = components[2];
                            double newLong = Double.parseDouble(components[3]);
                            double newLat = Double.parseDouble(components[4]);
                            POI newPOI = new POI(newName, newType, newDesc, newLat, newLong);
                            POIList.addPOI(newPOI);
                        }
                    }
                    double currLat = currentLocation.getPoint().getLatitude();
                    double currLong = currentLocation.getPoint().getLongitude();
                    items.removeAllItems();
                    currentLocation = new OverlayItem("You are here", ":", new GeoPoint(currLat, currLong));
                    items.addItem(currentLocation);
                    currentLocation.setMarker(getResources().getDrawable(R.drawable.marker));

                    for (POI poi : POIList.getPOIList()) {
                        OverlayItem newItem = new OverlayItem(poi.getName(), (" "+poi.getType() + " : " + poi.getDescription()), new GeoPoint(poi.getLatitude(), poi.getLongitude()));
                        items.addItem(newItem);
                    }

                    return "Downloading POIs, please wait";
                } else {
                    return "HTTP ERROR: " + conn.getResponseCode();
                }

            } catch (IOException e) {
                return e.toString();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        public void onPostExecute(String result){
            new AlertDialog.Builder(MainActivity.this).setPositiveButton("OK", null).
                    setMessage(result).show();
        }


    }

    class UploadTask extends AsyncTask<String,Void,String> {
        public String doInBackground(String... input)
        {
            HttpURLConnection conn = null;
            try
            {
                URL url = new URL("http://www.free-map.org.uk/course/mad/ws/add.php");
                conn = (HttpURLConnection) url.openConnection();



                String postData = "username=user005&name="+(URLEncoder.encode(input[0], "UTF-8"))+"&type="+(URLEncoder.encode(input[1], "UTF-8"))+"&description="+(URLEncoder.encode(input[3], "UTF-8"))+"&lat="+input[3]+"&lon="+input[4]+"&year=19";
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
            if(result.equals("OK")){
                Toast.makeText(MainActivity.this, "Point uploaded", Toast.LENGTH_LONG).show();
            }else{
                new AlertDialog.Builder(MainActivity.this).
                        setMessage("Server sent back: " + result).
                        setPositiveButton("OK", null).show();
            }
        }
    }
}
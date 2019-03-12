package uk.ac.solent.mapstest1;

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

public class AddNewPOI extends AppCompatActivity implements View.OnClickListener {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_poi);

        Button add = (Button) findViewById(R.id.btn1);
        add.setOnClickListener(this);

    }
    public void onClick(View v)
    {
        Intent intent = new Intent();
        Bundle bundle=new Bundle();

        EditText et_name = (EditText) findViewById(R.id.et1);
        EditText et_type = (EditText) findViewById(R.id.et2);
        EditText et_description = (EditText) findViewById(R.id.et3);


        String name = et_name.getText().toString();
        String type = et_type.getText().toString();
        String description = et_description.getText().toString();

        bundle.putString("name_out",name);
        bundle.putString("type_out",type);
        bundle.putString("description_out",description);
        intent.putExtras(bundle);


        setResult(RESULT_OK,intent);
        finish();
    }
}

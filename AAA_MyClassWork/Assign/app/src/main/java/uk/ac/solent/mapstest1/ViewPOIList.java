package uk.ac.solent.mapstest1;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.content.Context;
import java.util.ArrayList;
import java.util.List;


public class ViewPOIList extends ListActivity {
    List<String> names = new ArrayList<String>();
    List<String> descs = new ArrayList<String>();
    String[] names_a;
    String[] descs_a;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        for (POI poi : POIList.getPOIList()) {
            names.add(poi.getName());
            descs.add(poi.getDescription());
        }

        names_a = names.toArray(new String[0]);
        descs_a = descs.toArray(new String[0]);
       //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names_a);
        MyAdapter adapter = new MyAdapter();
        setListAdapter(adapter);

    }

    public void onListItemClick(ListView lv, View view, int index, long id) {
        Intent intent = new Intent();
        Bundle bundle=new Bundle();
        bundle.putInt("index", index);
        intent.putExtras(bundle);
        setResult(RESULT_OK,intent);
        finish();



    }


    public class MyAdapter extends ArrayAdapter<String>
    {
       public MyAdapter()
       {
            super(ViewPOIList.this, android.R.layout.simple_list_item_1, names_a);
        }

       public View getView(int index, View convertView, ViewGroup parent) {
           View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity_list, parent, false);
            }
            TextView title = (TextView) view.findViewById(R.id.poi_name);
            TextView detail = (TextView) view.findViewById(R.id.poi_type);
            title.setText(names_a[index]);
            detail.setText(descs_a[index]);
            return view;
        }
    }
}







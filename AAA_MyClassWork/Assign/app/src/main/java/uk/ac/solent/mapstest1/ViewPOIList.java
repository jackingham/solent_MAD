package uk.ac.solent.mapstest1;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.ViewGroup;
import android.content.Context;
import java.util.ArrayList;

public class ViewPOIList extends ListActivity {
    ArrayList<String> names, details = new ArrayList<String>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (POI poi : POIList.getPOIList()) {
            names.add(poi.getName());
            details.add(poi.getName());
        }
        MyAdapter adapter = new MyAdapter();
        setListAdapter(adapter);
    }

    public void onListItemClick(ListView lv, View view, int index, long id) {

    }


    public class MyAdapter extends ArrayAdapter<String>
    {
       public MyAdapter()
       {
            super(ViewPOIList.this, android.R.layout.simple_list_item_1, names);
        }

       public View getView(int index, View convertView, ViewGroup parent) {
           View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.activity_list, parent, false);
            }
            TextView title = (TextView) view.findViewById(R.id.poi_name);
            TextView detail = (TextView) view.findViewById(R.id.poi_type);
            title.setText(names.get(index));
            detail.setText(details.get(index));
            return view;
        }
    }
}







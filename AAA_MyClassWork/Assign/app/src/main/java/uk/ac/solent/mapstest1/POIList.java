package uk.ac.solent.mapstest1;

import android.app.AlertDialog;
import android.os.Environment;
import uk.ac.solent.mapstest1.POI;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class POIList {

    public static  List<POI> POIList = new ArrayList<POI>();

    public static boolean load(){
        try {
            FileReader fr = new FileReader(Environment.getExternalStorageDirectory().getAbsolutePath() + "/POIS.csv");
            BufferedReader reader = new BufferedReader(fr);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] components = line.split(",");
                if (components.length == 5){
                    String newName = components[0];
                    String newType = components[1];
                    String newDesc = components[2];
                    double newLong = Double.parseDouble(components[4]);
                    double newLat = Double.parseDouble(components[3]);
                    POI newPOI = new POI(newName, newType, newDesc, newLat, newLong);
                    addPOI(newPOI);


                }
            }
            return true;
        } catch (IOException e) {
            return false;

        }


    }

    public static boolean save(){
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(Environment.getExternalStorageDirectory().getAbsolutePath() + "/POIS.csv"));
            for (POI poi : getPOIList()){
               String newLine = poi.getName()+','+poi.getType()+','+poi.getDescription()+','+poi.getLatitude()+','+poi.getLongitude()+'\n';
               pw.print(newLine);
            }
            pw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static List<POI> getPOIList() {
        return POIList;
    }

    public static void addPOI(POI poiIn) {
        POIList.add(poiIn);
    }

    public static void removePOI(POI poiIn){
        POIList.remove(poiIn);
    }
}

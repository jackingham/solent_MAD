package uk.ac.solent.mapstest1;

public class POI {
    private String name;
    private String type;
    private String description;
    private double latitude;
    private double longitude;

    public POI(String nameIn, String typeIn, String descriptionIn, double latitiudeIn, double longitudeIn){
        name = nameIn;
        type = typeIn;
        description =  descriptionIn;
        latitude = latitiudeIn;
        longitude = longitudeIn;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}

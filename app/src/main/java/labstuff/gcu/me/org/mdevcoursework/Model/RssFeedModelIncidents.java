package labstuff.gcu.me.org.mdevcoursework.Model;

import java.util.Date;

public class RssFeedModelIncidents {

    public String title;
    public String description;
    public Float Lat;
    public Float Long;


    public RssFeedModelIncidents() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Float getLat() {
        return Lat;
    }

    public void setLat(Float lat) {
        Lat = lat;
    }

    public Float getLong() {
        return Long;
    }

    public void setLong(Float aLong) {
        Long = aLong;
    }

}

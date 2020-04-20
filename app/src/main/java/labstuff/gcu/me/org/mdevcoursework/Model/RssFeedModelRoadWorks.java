package labstuff.gcu.me.org.mdevcoursework.Model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RssFeedModelRoadWorks {

    public String title;
    public String description;
    public Float Lat;
    public Float Long;
    public Date startDate;
    public Date endDate;

    SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");


    public RssFeedModelRoadWorks() {

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

    public void setDescription(String xmldescription){

        //StartDate split

        String [] descriptionSplit = xmldescription.split("<br />");

        String [] startDateSplit = descriptionSplit[0].split(", ");

        String startDate = startDateSplit[1];

        //endDate split
        String [] endDateSplit = descriptionSplit[1].split(", ");

        String  endDate = endDateSplit[1];

        description = "\n" + descriptionSplit[2] + "\n\n" + "Start Date: " + startDate + "\n\n" + "End Date: " + endDate;


        try {
            Date dateStart = formatter.parse(startDate);
            Date dateEnd = formatter.parse(endDate);
            setStartDate(dateStart);
            setEndDate(dateEnd);

        } catch (ParseException e) {
            e.printStackTrace();
        }
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}

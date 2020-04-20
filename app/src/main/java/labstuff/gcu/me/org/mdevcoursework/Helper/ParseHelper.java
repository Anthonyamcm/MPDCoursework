package labstuff.gcu.me.org.mdevcoursework.Helper;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import labstuff.gcu.me.org.mdevcoursework.Model.RssFeedModelIncidents;
import labstuff.gcu.me.org.mdevcoursework.Model.RssFeedModelPlannedRoadWorks;
import labstuff.gcu.me.org.mdevcoursework.Model.RssFeedModelRoadWorks;

import static android.content.ContentValues.TAG;


public class ParseHelper {

    public static ArrayList<RssFeedModelIncidents> parseIncidents(String xml){
        ArrayList<RssFeedModelIncidents> IncidentsList = new ArrayList<>();
        try{
            boolean parsingComplete = false;
            RssFeedModelIncidents Incidents = null;
            XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlParser.setInput(new StringReader(xml));
            int eventType = xmlParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && !parsingComplete) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item")) {
                            Incidents = new RssFeedModelIncidents();
                        } else if (Incidents != null) {
                            if (name.equalsIgnoreCase("Title")) {
                                Incidents.setTitle(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Description")) {
                                Incidents.setDescription(xmlParser.nextText());
                            }
                            else if (name.equalsIgnoreCase("Georss:point")) {
                                String[] latlong = xmlParser.nextText().split(" ");
                                Float lat = Float.parseFloat(latlong[0]);
                                Float longitude = Float.parseFloat(latlong[1]);
                                Incidents.setLat(lat);
                                Incidents.setLong(longitude);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item") && Incidents != null) {
                            IncidentsList.add(Incidents);
                        } else if (name.equalsIgnoreCase("Channel")) {
                            parsingComplete = true;
                        }
                        break;
                }
                eventType = xmlParser.next();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return IncidentsList;
    }


    public static ArrayList<RssFeedModelRoadWorks> parseRoadworks(String xml){
        ArrayList<RssFeedModelRoadWorks> roadworksList = new ArrayList<>();
        try{
            boolean parsingComplete = false;
            RssFeedModelRoadWorks roadwork = null;
            XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlParser.setInput(new StringReader(xml));
            int eventType = xmlParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && !parsingComplete) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item")) {
                            roadwork = new RssFeedModelRoadWorks();
                        } else if (roadwork != null) {
                            if (name.equalsIgnoreCase("Title")) {
                                roadwork.setTitle(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Description")) {
                                roadwork.setDescription(xmlParser.nextText());
                            }
                            else if (name.equalsIgnoreCase("Georss:point")) {
                                String[] latlong = xmlParser.nextText().split(" ");
                                Float lat = Float.parseFloat(latlong[0]);
                                Float longitude = Float.parseFloat(latlong[1]);
                                roadwork.setLat(lat);
                                roadwork.setLong(longitude);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item") && roadwork != null) {
                            roadworksList.add(roadwork);
                        } else if (name.equalsIgnoreCase("Channel")) {
                            parsingComplete = true;
                        }
                        break;
                }
                eventType = xmlParser.next();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return roadworksList;
    }

    public static ArrayList<RssFeedModelPlannedRoadWorks> parsePlannedRoadworks(String xml){
        ArrayList<RssFeedModelPlannedRoadWorks> roadworksList = new ArrayList<>();
        try{
            boolean parsingComplete = false;
            RssFeedModelPlannedRoadWorks roadwork = null;
            XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
            xmlParser.setInput(new StringReader(xml));
            int eventType = xmlParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT && !parsingComplete) {
                String name = null;
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item")) {
                            roadwork = new RssFeedModelPlannedRoadWorks();
                        } else if (roadwork != null) {
                            if (name.equalsIgnoreCase("Title")) {
                                roadwork.setTitle(xmlParser.nextText());
                            } else if (name.equalsIgnoreCase("Description")) {
                                roadwork.setDescription(xmlParser.nextText());
                            }
                            else if (name.equalsIgnoreCase("Georss:point")) {
                                String[] latlong = xmlParser.nextText().split(" ");
                                Float lat = Float.parseFloat(latlong[0]);
                                Float longitude = Float.parseFloat(latlong[1]);
                                roadwork.setLat(lat);
                                roadwork.setLong(longitude);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        name = xmlParser.getName();
                        if (name.equalsIgnoreCase("Item") && roadwork != null) {
                            roadworksList.add(roadwork);
                        } else if (name.equalsIgnoreCase("Channel")) {
                            parsingComplete = true;
                        }
                        break;
                }
                eventType = xmlParser.next();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        return roadworksList;
    }

}

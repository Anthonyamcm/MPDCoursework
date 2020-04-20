package labstuff.gcu.me.org.mdevcoursework;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;

import org.joda.time.DateTime;
import org.joda.time.Days;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import labstuff.gcu.me.org.mdevcoursework.Helper.ParseHelper;
import labstuff.gcu.me.org.mdevcoursework.Model.RssFeedModelIncidents;
import labstuff.gcu.me.org.mdevcoursework.Model.RssFeedModelPlannedRoadWorks;
import labstuff.gcu.me.org.mdevcoursework.Model.RssFeedModelRoadWorks;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback, PlaceSelectionListener, GoogleMap.InfoWindowAdapter{


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    public GoogleMap mMap;

    private static final int overview = 0;

    private String endpoint;

    private Calendar calendar = Calendar.getInstance();

    final Calendar myCalendar = Calendar.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        Button IncidentsButton = findViewById(R.id.IncidentsButton);

        Button RoadWorksButton = findViewById(R.id.RoadWorksButton);

        Button PlannedRoadWorks = findViewById(R.id.PlannedRoadWorksButton);

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyDQTPMjZW6d1VardWROgKKJhRjVdUHC6MU");
        // Create a new Places client instance.
        Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);


        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragment.setCountry("UK");

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            }

        };



        IncidentsButton
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        endpoint = "Incidents";
                        startProgress("https://trafficscotland.org/rss/feeds/currentincidents.aspx");
                    }
                });

        RoadWorksButton
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      final DatePickerDialog datePickerDialog =  new DatePickerDialog(MapsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                        Button Okbutton = datePickerDialog.getButton(datePickerDialog.BUTTON_POSITIVE);
                        Okbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                                int month = datePickerDialog.getDatePicker().getMonth();
                                int year = datePickerDialog.getDatePicker().getYear();
                                calendar.set(year, month, day);
                                datePickerDialog.dismiss();
                                endpoint = "RoadWorks";
                                startProgress("https://trafficscotland.org/rss/feeds/roadworks.aspx");
                            }
                        });
                    }
                });


       PlannedRoadWorks
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                         final DatePickerDialog datePickerDialog =  new DatePickerDialog(MapsActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH));
                        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                        datePickerDialog.show();
                        Button Okbutton = datePickerDialog.getButton(datePickerDialog.BUTTON_POSITIVE);
                        Okbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int day = datePickerDialog.getDatePicker().getDayOfMonth();
                                int month = datePickerDialog.getDatePicker().getMonth();
                                int year = datePickerDialog.getDatePicker().getYear();
                                calendar.set(year, month, day);
                                datePickerDialog.dismiss();
                                endpoint = "PlanedRoadWorks";
                                startProgress("https://trafficscotland.org/rss/feeds/plannedroadworks.aspx");
                            }
                        });

                    }
                });



        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener()
        {

            @Override
            public void onPlaceSelected(Place place) {
                try {
                    String origin = getCurrentLocation() +",UK";
                    String destination = place.getName() + ",UK";
                    DirectionsResult results = getDirectionsDetails(origin,destination,TravelMode.DRIVING);
                    if (results != null) {
                        addPolyline(results, mMap);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (com.google.maps.errors.ApiException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Status status) {
            }
        });

        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setInfoWindowAdapter(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true);
            zoomToLocation();
        }

    }

        public void zoomToLocation(){
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

            if (location != null) {
                Log.d("LOCATION", "onMapReady: worked");
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(location.getLatitude(), location.getLongitude()))      // Sets the center of the map to location user
                        .zoom(8)                   // Sets the zoom// Sets the orientation of the camera to east
                        .tilt(0)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }

        }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    zoomToLocation();
                } else {
                    // Permission Denied
                    Toast.makeText( this,"your message" , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public String getCurrentLocation() throws IOException {

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        if (location != null) {
            Log.d("LOCATION", "onMapReady: worked");
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14));

            Geocoder geo = new Geocoder(this.getApplicationContext(), Locale.getDefault());
            List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            Address nameAddress = addresses.get(0);

            return nameAddress.getAddressLine(0).toString();

        } else {

            return "No address";
        }

    }

    private DirectionsResult getDirectionsDetails(String origin,String destination,TravelMode mode) throws InterruptedException, com.google.maps.errors.ApiException, IOException {
        DateTime now = new DateTime();

            return DirectionsApi.newRequest(getGeoContext())
                    .mode(mode)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
    }

    public void addMarkersToMapRSSIncidents(ArrayList<RssFeedModelIncidents> list){

        mMap.clear();

        if (list.size() > 0){
            for(Integer i = 0; i < list.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(new LatLng(list.get(i).Lat, list.get(i).Long)).title(list.get(i).title).snippet(list.get(i).description));
            }
        }
    }


    public void addMarkersToMapRSSRoadWorks(ArrayList<RssFeedModelRoadWorks> list){

        mMap.clear();


        Calendar workDateStart = Calendar.getInstance();
        Calendar workDateEnd = Calendar.getInstance();

        if (list.size() > 0){
            for(Integer i = 0; i < list.size(); i++) {
                workDateEnd.setTime(list.get(i).endDate);
                workDateStart.setTime(list.get(i).startDate);

                long end = workDateEnd.getTimeInMillis();
                long start = workDateStart.getTimeInMillis();

                long days = TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));

                if(calendar.before(workDateEnd)) {
                    if(days < 31) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(list.get(i).Lat, list.get(i).Long)).title(list.get(i).title).snippet(list.get(i).description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    }
                    else if (days < 90){
                        mMap.addMarker(new MarkerOptions().position(new LatLng(list.get(i).Lat, list.get(i).Long)).title(list.get(i).title).snippet(list.get(i).description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    }
                    else{
                        mMap.addMarker(new MarkerOptions().position(new LatLng(list.get(i).Lat, list.get(i).Long)).title(list.get(i).title).snippet(list.get(i).description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                }
            }
        }
    }

    public void addMarkersToMapRSSPlannedRoadWorks(ArrayList<RssFeedModelPlannedRoadWorks> list){

        mMap.clear();

        Calendar workDateStart = Calendar.getInstance();
        Calendar workDateEnd = Calendar.getInstance();

        if (list.size() > 0){
            for(Integer i = 0; i < list.size(); i++) {
                workDateEnd.setTime(list.get(i).endDate);
                workDateStart.setTime(list.get(i).startDate);

                long end = workDateEnd.getTimeInMillis();
                long start = workDateStart.getTimeInMillis();

                long days = TimeUnit.MILLISECONDS.toDays(Math.abs(end - start));
                if(calendar.before(workDateEnd)) {
                    if (days < 31) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(list.get(i).Lat, list.get(i).Long)).title(list.get(i).title).snippet(list.get(i).description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    } else if (days < 90) {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(list.get(i).Lat, list.get(i).Long)).title(list.get(i).title).snippet(list.get(i).description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    } else {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(list.get(i).Lat, list.get(i).Long)).title(list.get(i).title).snippet(list.get(i).description).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                    }
                }
            }
        }
    }



    private void addPolyline(DirectionsResult results, GoogleMap mMap) {
        List<LatLng> decodedPath = PolyUtil.decode(results.routes[overview].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }


    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey("AIzaSyDQTPMjZW6d1VardWROgKKJhRjVdUHC6MU")
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }



    @Override
    public void onPlaceSelected(@NonNull Place place) {

    }

    @Override
    public void onError(@NonNull Status status) {

    }


    public void startProgress(String URL)
    {
        // Run network access on a separate thread;
        new Thread(new Task(URL)).start();


    } //

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = getLayoutInflater().inflate(R.layout.infowindowlayout, null);

        TextView titleTextView = (TextView) view.findViewById(R.id.Title);
        TextView snippetTextView = (TextView) view.findViewById(R.id.Description);

        titleTextView.setText(marker.getTitle());
        snippetTextView.setText(marker.getSnippet());

        return view;

    }


    private class Task implements Runnable
    {
        private String url;

        private String result = "";

        Task(String aurl)
        {
            url = aurl;
        }
        @Override
        public void run()
        {

            URL aurl;
            URLConnection yc;
            BufferedReader in;
            String inputLine;


            try
            {
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

                while ((inputLine = in.readLine()) != null)
                {
                    result = result + inputLine;

                }
                in.close();
            }
            catch (IOException ae)
            {
                Log.e("MyTag", "ioexception");
            }


            MapsActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    if(endpoint.equals("Incidents")) {
                        ArrayList<RssFeedModelIncidents> data = ParseHelper.parseIncidents(result);
                        addMarkersToMapRSSIncidents(data);
                    }
                    else if(endpoint.equals("RoadWorks")){
                        ArrayList<RssFeedModelRoadWorks> data = ParseHelper.parseRoadworks(result);
                        addMarkersToMapRSSRoadWorks(data);
                    }
                    else if(endpoint.equals("PlanedRoadWorks")){
                        ArrayList<RssFeedModelPlannedRoadWorks> data = ParseHelper.parsePlannedRoadworks(result);
                        addMarkersToMapRSSPlannedRoadWorks(data);
                    }
                }
            });
        }

    }
}

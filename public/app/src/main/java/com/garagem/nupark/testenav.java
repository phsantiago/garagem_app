package com.garagem.nupark;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.garagem.nupark.dto.GaragemDto;
import com.garagem.nupark.gps.DirectionsJSONParser;
import com.garagem.nupark.service.GaragemService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.koushikdutta.async.future.FutureCallback;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;





public class testenav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback,
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int ID_FILIAL = 1;
    private static final String LATITUDE = "-23.7377419";
    private static final String LONGITUDE = "-46.6927231";
    private static final String LOGO = "logo";
    private static final String UNIDADE = "unidade";
    private static final String NOME_FANTASIA = "nome_fantasia";
    private static final String TELEFONE = "telefone";
    private static final String ENDERECO = "endereco";
    private static final String CEP = "cep";
    private static final String TAG = "ActivityMap";
    private int id_filial;
    public float latitude;
    public float longitude;
    private String logoPath;
    private String unidade;
    private String nome_fantasia;
    private String telefone;
    private String endereco;
    private String cep;
    private GoogleMap GMap;
    private MapView mapView;
    private Boolean mapReady;

    private GaragemService garagemService = new GaragemService();
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testenav);

        permissionCheck();

        // GoogleMaps
        mapView = (MapView) findViewById(R.id.map);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(testenav.this);

        this.latitude = (float) -23.7365769;
        this.longitude = (float) -46.6942962;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("OpenGar");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        init();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.testenav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            Intent it = new Intent(testenav.this, testenav.class);
            startActivity(it);
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {
            Intent it = new Intent(testenav.this, historico.class);
            startActivity(it);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void init() {
        buildGoogleApiClient();
    }


    /***********************************************************************************************
     * Map Methods
     ************* **********************************************************************************/

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        stopLocationUpdates();
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(TAG, "Location update resumed .....................");
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private ImageView logo_info_balloon;

    @Override
    public void onMapReady(GoogleMap map) {
        GMap = map;
        mapReady = true;
        if (ActivityCompat.checkSelfPermission(testenav.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(testenav.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        GMap.setMyLocationEnabled(true);
        //moveCamera(-23.543628, -46.658199);
        //GMap.moveCamera(CameraUpdateFactory.zoomTo(15));

        createMarker();
    /*
        GMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                LayoutInflater layoutInflater = ActivityMap.this.getLayoutInflater();
                View info_balloon = layoutInflater.inflate(R.layout.info_window_layout, null);

                // get logo
                logo_info_balloon = (ImageView) info_balloon.findViewById(R.id.logo);

                changeBalloonLogo(logoPath);

                // Getting reference to the TextView to set latitude
                TextView textTitle = (TextView) info_balloon.findViewById(R.id.title);

                // Getting reference to the TextView to set longitude
                TextView textSubtitle = (TextView) info_balloon.findViewById(R.id.subtitle);
                TextView textTelefone = (TextView) info_balloon.findViewById(R.id.telefone);
                TextView textEndereco = (TextView) info_balloon.findViewById(R.id.endereco);
                TextView textCep = (TextView) info_balloon.findViewById(R.id.cep);

                // Setting the latitude
                //textTitle.setText("Latitude:" + latLng.latitude);
                textTitle.setText(nome_fantasia);
                textTelefone.setText(telefone);
                textEndereco.setText(endereco);
                textCep.setText("CEP: " + cep);

                textSubtitle.setText(unidade);

                // Returning the info_ballooniew containing InfoWindow contents
                return info_balloon;

            }
        });*/

    }
/*
    private void changeBalloonLogo(String logoPath){
        if(logoPath != null) {
            Uri uri = Uri.parse(logoPath);
            Picasso.with(ActivityMap.this).load(uri).into(logo_info_balloon);
        }
    }*/

    private void permissionCheck() {

        // location updates: at least 1 meter and 200millsecs change
        if (ActivityCompat.checkSelfPermission(testenav.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(testenav.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(testenav.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

    }

    private List<Marker> mMarkers = new ArrayList<Marker>();

    Boolean firstTime = false;

    public void createMarker() {

        garagemService.consultaGaragem(new GaragemDto(),getApplicationContext(), new FutureCallback<JsonArray>() {

            @Override
            public void onCompleted(Exception e, JsonArray result) {
                if (e == null) {

                    Type listType = new TypeToken<List<GaragemDto>>(){}.getType();
                    ArrayList<GaragemDto> listaGaragem = gson.fromJson(result.toString(), listType);

                    for(GaragemDto garagemDto : listaGaragem){

                        LatLng marker = new LatLng(garagemDto.getLatitude(), garagemDto.getLongitude());
                        firstTime = true;
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(marker);
                        markerOptions.title(nome_fantasia);
                        markerOptions.snippet(unidade);
                        //        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker);
                        //        markerOptions.icon(markerIcon);
                        Marker mMarker = GMap.addMarker(markerOptions);
                        mMarkers.add(mMarker);

                    }
                }
            }
        });




    }

//    private List<Branche> mfiliais;

    public void selectMarker() {

        Marker marker = mMarkers.get(0);

        marker.showInfoWindow();

        //trace route usert -> branche
        Double userLat = mCurrentLocation.getLatitude();
        Double userLong = mCurrentLocation.getLongitude();

        LatLng origin = new LatLng(userLat, userLong);
        moveCamera(userLat, userLong);
        LatLng dest = new LatLng(-23.7365769,-46.6942962);

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin, dest);

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);

        moveCamera(latitude  + 0.002, longitude + 0.0012); // 0.002000 & 0.001200 para alinhar o balao de info da loja

    }

    private void moveCamera(Double latitude, Double longitude) {
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(new LatLng(latitude, longitude))
                        .bearing(45)
                        .tilt(0)
                        .zoom(15)
                        .build();

        GMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 1000, null);

    }


    private String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /******************************************************************************************************************

     Android Location Fused Provider
     http://javapapers.com/android/android-location-fused-provider/

     *******************************************************************************************************************/

    private GoogleApiClient mGoogleApiClient;
    private static final long INTERVAL = 1000 * 30;
    private static final long FASTEST_INTERVAL = 1000 * 30;
    LocationRequest mLocationRequest;
    Location mCurrentLocation;
    String mLastUpdateTime;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected synchronized void buildGoogleApiClient() {
        if (!isGooglePlayServicesAvailable()) {
            Log.i("buildGoogleApiClient", "GooglePlayServices is not available");
        }
        createLocationRequest();
        mGoogleApiClient = new GoogleApiClient.Builder(testenav.this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart fired ..............");
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop fired ..............");
        mGoogleApiClient.disconnect();
        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
    }

    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(testenav.this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, testenav.this, 0).show();
            return false;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    private LocationListener locationListener;

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(testenav.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(testenav.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        Log.d(TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    Circle mapCircle;



    private void updateUI() {

        if(!firstTime){
            return;
        }


        if (mCurrentLocation ==null) return;
        Double lat = mCurrentLocation.getLatitude();
        Double lng = mCurrentLocation.getLongitude();

        //Log ---------------------------------------------------------------------
        Log.i(TAG, "At Time: " + mLastUpdateTime + "\n" +
                "Latitude: " + lat + "\n" +
                "Longitude: " + lng + "\n" +
                "Accuracy: " + mCurrentLocation.getAccuracy() + "\n" +
                "Provider: " + mCurrentLocation.getProvider());


        /*Draw Circle ---------------------------------------------------------------------
        if(mapCircle!=null){
            mapCircle.remove();
        }

        mapCircle = GMap.addCircle(new CircleOptions()
                .center(new LatLng(lat, lng))
                .radius(400)
                .strokeWidth(1)
                .strokeColor(0xFF00FF00)
                .fillColor(0x3300FF00));

       /Move Camera ---------------------------------------------------------------------*/
        moveCamera(lat, lng);

        selectMarker();
    }


    protected void stopLocationUpdates() {

        //TODO: check if Google API CLient is loaded

        if(mGoogleApiClient.isConnected()){
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
            Log.d(TAG, "Location update stopped .......................");
        }
    }

    /* só assinatura */
    public void searchMode(){
    }

    /*
    ####################### GUILHERME BORGES BASTOS #########################
    ##   CALCULA A MELHOR ROTA ENTRE O USUÄRIO E A FILIAL NO MAPA     #######
    #########################################################################
    */
    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;

        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while( ( line = br.readLine()) != null){
                sb.append(line);

            }
            data = sb.toString();
            br.close();


        }catch(Exception e){
            Log.d("Exc. while down url", e.toString());

        }finally{
            iStream.close();
            urlConnection.disconnect();

        }
        return data;

    }

    /*public void setfiliais(List<Branche> mfiliais) {
        this.mfiliais = mfiliais;
    }*/

    // Fetches data from url passed
    public class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    public Polyline polylineFinal;

    /** A class to parse the Google Places in JSON format */
    public class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> > {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }


        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            if(polylineFinal != null){
                polylineFinal.remove();
            }

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.BLUE);
            }

            // Drawing polyline in the Google Map for the i-th route
            polylineFinal = GMap.addPolyline(lineOptions);

            //da Zoom a onde a pessoa está
            //findHer(friendPoint);

        }
    }
}

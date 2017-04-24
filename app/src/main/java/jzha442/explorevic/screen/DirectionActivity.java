package jzha442.explorevic.screen;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.Language;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.model.Step;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.database.FavouritePlaceService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.Place;

import static android.R.attr.fragment;

public class DirectionActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    private boolean isPlace;
    private FavouritePlace fp;
    private Place place;
    private LatLng currentLocation;

    private String title = "";
    private double latitude;
    private double longitude;

    TextView dis;
    TextView dur;
    AVLoadingIndicatorView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction);
        dis = (TextView) findViewById(R.id.text_distance);
        dur = (TextView) findViewById(R.id.text_duration);
        loading = (AVLoadingIndicatorView) findViewById(R.id.loading_direction);

        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        //新页面接收数据
        Bundle bundle = this.getIntent().getExtras();
        //接收name值
        isPlace = bundle.getBoolean("isPlace");
        if(isPlace) {
            int pID = bundle.getInt("pID");
            PlaceService ps = new PlaceService(this);
            place = ps.getPlaceById(pID);
        }else{
            int fID = bundle.getInt("fID");
            FavouritePlaceService fps = new FavouritePlaceService(this);
            fp = fps.getPlaceById(fID);
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //set dis and dur as non-visible
        isLoading(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setVisible(false);
        MenuItem item2 = menu.findItem(R.id.action_add_favourite);
        item2.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == android.R.id.home){
            Log.i("Direction ---home", "click back button.");
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        if(isPlace){
            title = place.getName();
            latitude = place.getLatitude();
            longitude = place.getLongitude();
        }else{
            title = fp.getName();
            latitude = fp.getLatitude();
            longitude = fp.getLongitude();
        }

        LatLng dest=new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(dest).title(title));
        //updateMap(dest);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(dest, 13));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    99);

            //Toast.makeText(getApplicationContext(), "Sorry, you haven't allowed the app to get your location!",Toast.LENGTH_SHORT).show();
            //onBackPressed();
            //return;
        }

        mMap.setMyLocationEnabled(true);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                // TODO Auto-generated method stub
                //mMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                //updateMap(location);
                if(currentLocation==null) {
                    currentLocation = new LatLng(arg0.getLatitude(), arg0.getLongitude());
                    setRoute(currentLocation);
                }
            }
        });

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if(mMap.getMyLocation()!=null) {
                    LatLng location = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                    updateMap(location);
                    if(currentLocation == null) {
                        currentLocation = location;
                        setRoute(currentLocation);
                    }
                }
                return false;
            }
        });


    }

    private void updateMap(LatLng ll) {
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 13));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(ll));
    }

    private void setRoute(LatLng origin){
        // Add a marker in Sydney and move the camera
        //LatLng origin = new LatLng(-37.42, 147.444538);
        LatLng dest=new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(origin).title("Your current location"));

        updateMap(origin);

        GoogleDirection.withServerKey(getResources().getString(R.string.google_maps_key))
                .from(origin)
                .to(dest)
                .transportMode(TransportMode.DRIVING)
                .language(Language.ENGLISH)
                .avoid(AvoidType.FERRIES)
                .avoid(AvoidType.HIGHWAYS)
                .execute(new DirectionCallback() {
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        if(direction.isOK()) {
                            // Do something

                            Route route = direction.getRouteList().get(0);
                            Leg leg = route.getLegList().get(0);

                            //get the total distance and duration of the route
                            Info distanceInfo = leg.getDistance();
                            Info durationInfo = leg.getDuration();
                            String distance = distanceInfo.getText();
                            String duration = durationInfo.getText();
                            Log.i("distance",distance);
                            Log.i("duration",duration);

                            //draw route line

                            ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                            PolylineOptions polylineOptions = DirectionConverter.createPolyline(DirectionActivity.this, directionPositionList, 5, Color.RED);
                            mMap.addPolyline(polylineOptions);

                            /*
                            List<Step> stepList = leg.getStepList();
                            ArrayList<PolylineOptions> polylineOptionList = DirectionConverter.createTransitPolyline(DirectionActivity.this, stepList, 5, Color.RED, 3, Color.BLUE);
                            for (PolylineOptions polylineOption : polylineOptionList) {
                                mMap.addPolyline(polylineOption);
                            }
                            */
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.7681994, -122.444538)));

                            setDisAndDur(distance,duration);
                            isLoading(false);

                        } else {
                            // Do something
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                        // Do something
                    }
                });
    }

    public void isLoading(boolean isLoading){
        if(isLoading) {
            dis.setVisibility(View.GONE);
            dur.setVisibility(View.GONE);
            loading.setVisibility(View.VISIBLE);
        }else{
            dis.setVisibility(View.VISIBLE);
            dur.setVisibility(View.VISIBLE);
            loading.setVisibility(View.GONE);
        }
    }

    public void setDisAndDur(String distance, String duration){
        dis.setText("Distance: "+distance);
        dur.setText("Duration: "+duration);
    }
}

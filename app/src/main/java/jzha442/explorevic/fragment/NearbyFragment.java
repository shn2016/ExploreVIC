package jzha442.explorevic.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.database.ActivityPlaceService;
import jzha442.explorevic.model.NearbyPlace;
import jzha442.explorevic.screen.CustomFavouriteActivity;
import jzha442.explorevic.screen.DirectionActivity;
import jzha442.explorevic.utility.Utils;
import jzha442.explorevic.webservice.NearbyActivityService;
import jzha442.explorevic.webservice.NearbyPlaceService;
import jzha442.explorevic.webservice.ParseLocation;


/**
 * Created by kaigao on 17/4/13.
 */

public class NearbyFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, LocationListener, GoogleApiClient.OnConnectionFailedListener {

    FloatingActionButton main_fab;
    FloatingActionButton toilet_fab;
    FloatingActionButton parking_fab;
    FloatingActionButton activity_fab;

    LinearLayout ballooningLayout;
    LinearLayout campingLayout;
    LinearLayout canoeingLayout;
    LinearLayout mountain_bikingLayout;
    LinearLayout sailingLayout;
    LinearLayout skydivingLayout;
    LinearLayout snorkellingLayout;
    LinearLayout surfingLayout;
    LinearLayout swimmingLayout;

    FloatingActionButton ballooning;
    FloatingActionButton camping;
    FloatingActionButton canoeing;
    FloatingActionButton mountain_biking;
    FloatingActionButton sailing;
    FloatingActionButton skydiving;
    FloatingActionButton snorkelling;
    FloatingActionButton surfing;
    FloatingActionButton swimming;

    FloatingActionButton route_fab;
    Button address_button;
    PlacesAutocompleteTextView edittext;

    AutoCompleteTextView autocompleteView;

    String destination_address;
    LatLng destination;
    private GoogleMap mMap;
    double latitude = -37.8771;
    double longitude = 145.044;
    CardView loading;

    Route route;
    Leg leg;
    ArrayList<LatLng> pointList;
    Location myLocation;

    // GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private static final int PERMISSION_ACCESS_COARSE_LOCATION = 0;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nearby_map, null, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //asdqw = (ImageButton) v.findViewById(R.id.location_mark);
        main_fab = (FloatingActionButton) v.findViewById(R.id.main_fab);
        toilet_fab = (FloatingActionButton) v.findViewById(R.id.toilet_fab);
        parking_fab = (FloatingActionButton) v.findViewById(R.id.parking_fab);
        activity_fab = (FloatingActionButton) v.findViewById(R.id.activity_fab);

        ballooningLayout = (LinearLayout) v.findViewById(R.id.ballooningLayout);
        campingLayout = (LinearLayout) v.findViewById(R.id.campingLayout);
        canoeingLayout = (LinearLayout) v.findViewById(R.id.canoeingLayout);
        mountain_bikingLayout = (LinearLayout) v.findViewById(R.id.mountain_bikingLayout);
        sailingLayout = (LinearLayout) v.findViewById(R.id.sailingLayout);
        skydivingLayout = (LinearLayout) v.findViewById(R.id.skydivingLayout);
        snorkellingLayout = (LinearLayout) v.findViewById(R.id.snorkellingLayout);
        surfingLayout = (LinearLayout) v.findViewById(R.id.surfingLayout);
        swimmingLayout = (LinearLayout) v.findViewById(R.id.swimmingLayout);

        ballooning = (FloatingActionButton) v.findViewById(R.id.ballooning);
        camping = (FloatingActionButton) v.findViewById(R.id.camping);
        canoeing = (FloatingActionButton) v.findViewById(R.id.canoeing);
        mountain_biking = (FloatingActionButton) v.findViewById(R.id.mountain_biking);
        sailing = (FloatingActionButton) v.findViewById(R.id.sailing);
        skydiving = (FloatingActionButton) v.findViewById(R.id.skydiving);
        snorkelling = (FloatingActionButton) v.findViewById(R.id.snorkelling);
        surfing = (FloatingActionButton) v.findViewById(R.id.surfing);
        swimming = (FloatingActionButton) v.findViewById(R.id.swimming);

        route_fab = (FloatingActionButton) v.findViewById(R.id.route_fab);
        edittext = (PlacesAutocompleteTextView) v.findViewById(R.id.edittext);
        address_button = (Button) v.findViewById(R.id.address_button);

        destination = new LatLng(0.0, 0.0);
        pointList = new ArrayList<LatLng>();
        loading = (CardView) v.findViewById(R.id.loading_direction);
        isLoading(false);
        //autocompleteView = (AutoCompleteTextView) v.findViewById(R.id.autocomplete);
        //autocompleteView.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), mGoogleApiClient, null));
        return v;

    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    99);

            //Toast.makeText(getActivity(), "Sorry, you haven't allowed the app to get your location!", Toast.LENGTH_SHORT).show();
            //return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);

        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if(mMap.getMyLocation()!=null) {
                    LatLng location = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
                }
                return false;
            }
        });

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude,longitude)));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

/*
        autocompleteView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get data associated with the specified position
                // in the list (AdapterView)
                AutocompletePrediction description = (AutocompletePrediction) parent.getItemAtPosition(position);
                String des = description.getDescription();
                Toast.makeText(getActivity(), des, Toast.LENGTH_SHORT).show();
            }
        });*/


        main_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (toilet_fab.getVisibility() == View.VISIBLE &&
                        parking_fab.getVisibility() == View.VISIBLE &&
                        activity_fab.getVisibility() == View.VISIBLE) {
                    activity_fab.setVisibility(View.GONE);
                    parking_fab.setVisibility(View.GONE);
                    toilet_fab.setVisibility(View.GONE);
                }
                else {
                    activity_fab.setVisibility(View.VISIBLE);
                    parking_fab.setVisibility(View.VISIBLE);
                    toilet_fab.setVisibility(View.VISIBLE);
                }
                if(ballooningLayout.getVisibility() == View.VISIBLE &&
                 campingLayout.getVisibility() == View.VISIBLE &&
                 canoeingLayout.getVisibility() == View.VISIBLE &&
                 mountain_bikingLayout.getVisibility() == View.VISIBLE &&
                 sailingLayout.getVisibility() == View.VISIBLE &&
                 skydivingLayout.getVisibility() == View.VISIBLE &&
                 snorkellingLayout.getVisibility() == View.VISIBLE &&
                 surfingLayout.getVisibility() == View.VISIBLE &&
                 swimmingLayout.getVisibility() == View.VISIBLE){
                    ballooningLayout.setVisibility(View.GONE);
                    campingLayout.setVisibility(View.GONE);
                    canoeingLayout.setVisibility(View.GONE);
                    mountain_bikingLayout.setVisibility(View.GONE);
                    sailingLayout.setVisibility(View.GONE);
                    skydivingLayout.setVisibility(View.GONE);
                    snorkellingLayout.setVisibility(View.GONE);
                    surfingLayout.setVisibility(View.GONE);
                    swimmingLayout.setVisibility(View.GONE);
                }

            }
        });
// dodododo
        route_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edittext.getVisibility() == View.VISIBLE &&
                        address_button.getVisibility() == View.VISIBLE){
                    edittext.setVisibility(View.GONE);
                    address_button.setVisibility(View.GONE);
                }
                else {
                    edittext.setVisibility(View.VISIBLE);
                    address_button.setVisibility(View.VISIBLE);

                }
            }
        });
        activity_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ballooningLayout.getVisibility() == View.VISIBLE &&
                        campingLayout.getVisibility() == View.VISIBLE &&
                        canoeingLayout.getVisibility() == View.VISIBLE &&
                        mountain_bikingLayout.getVisibility() == View.VISIBLE &&
                        sailingLayout.getVisibility() == View.VISIBLE &&
                        skydivingLayout.getVisibility() == View.VISIBLE &&
                        snorkellingLayout.getVisibility() == View.VISIBLE &&
                        surfingLayout.getVisibility() == View.VISIBLE &&
                        swimmingLayout.getVisibility() == View.VISIBLE){
                    ballooningLayout.setVisibility(View.GONE);
                    campingLayout.setVisibility(View.GONE);
                    canoeingLayout.setVisibility(View.GONE);
                    mountain_bikingLayout.setVisibility(View.GONE);
                    sailingLayout.setVisibility(View.GONE);
                    skydivingLayout.setVisibility(View.GONE);
                    snorkellingLayout.setVisibility(View.GONE);
                    surfingLayout.setVisibility(View.GONE);
                    swimmingLayout.setVisibility(View.GONE);
                }
                else {
                    ballooningLayout.setVisibility(View.VISIBLE);
                    campingLayout.setVisibility(View.VISIBLE);
                    canoeingLayout.setVisibility(View.VISIBLE);
                    mountain_bikingLayout.setVisibility(View.VISIBLE);
                    sailingLayout.setVisibility(View.VISIBLE);
                    skydivingLayout.setVisibility(View.VISIBLE);
                    snorkellingLayout.setVisibility(View.VISIBLE);
                    surfingLayout.setVisibility(View.VISIBLE);
                    swimmingLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        parking_fab.setOnClickListener(new View.OnClickListener() {
            String keyword = "type=parking";
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                mMap.clear();
                NearbyPlaceService.placeIdTask asyncTask = new NearbyPlaceService.placeIdTask(
                        new NearbyPlaceService.AsyncResponse() {
                            public void processFinish(List<NearbyPlace> np) {
                                ShowNearbyPlaces(np);
                            }
                        });
                asyncTask.execute(latitude + "", longitude + "", keyword + "");
                Log.d("onClick", keyword);
                Log.i("String", latitude + " " + longitude);
            }
        });

        setOnItemActionListener(ballooning,2);
        setOnItemActionListener(skydiving,1);
        setOnItemActionListener(camping,3);
        setOnItemActionListener(mountain_biking,4);
        setOnItemActionListener(surfing,5);
        setOnItemActionListener(swimming,6);
        setOnItemActionListener(snorkelling,7);
        setOnItemActionListener(sailing,8);
        setOnItemActionListener(canoeing,9);

        address_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                destination_address = edittext.getText().toString();
                mMap.clear();
                isLoading(true);
                if(destination_address.length() != 0) {
                    ParseLocation.placeIdTask asyncTask = new ParseLocation.placeIdTask(
                            new ParseLocation.AsyncResponse() {
                                public void processFinish(LatLng np) {
                                    destination  = np;
                                    Log.i("zhege LatLon", destination+"");
                                    getRoute(destination);
                                }
                            });
                    asyncTask.execute(destination_address);
                    Log.i("LatLon", destination+"");
                }
                         }
        });


        toilet_fab.setOnClickListener(new View.OnClickListener() {
            String keyword = "type=toilet";
            @Override
            public void onClick(View v) {
                Log.d("onClick", "Button is Clicked");
                mMap.clear();
                NearbyPlaceService.placeIdTask asyncTask = new NearbyPlaceService.placeIdTask(
                        new NearbyPlaceService.AsyncResponse() {
                            public void processFinish(List<NearbyPlace> np) {
                                ShowNearbyPlaces(np);
                            }
                        });
                asyncTask.execute(latitude + "", longitude + "", keyword + "");
                Log.d("onClick", keyword);
                Log.i("String", latitude + " " + longitude);
            }
        });

    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this.getActivity());
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(this.getActivity(), result,
                        0).show();
            }
            return false;
        }
        return true;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.nearby_map;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);

    }

    @Override
    protected void initData() {




    }


    /*protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this.getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }*/


    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.i("ldddd", latitude+"  "+ longitude);
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        mCurrLocationMarker = mMap.addMarker(markerOptions);

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        Log.d("onLocationChanged", String.format("latitude:%.3f longitude:%.3f",latitude,longitude));

        //stop location updates
       /* if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (com.google.android.gms.location.LocationListener) this);
            Log.d("onLocationChanged", "Removing Location Updates");
        }
        Log.d("onLocationChanged", "Exit"); */

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public void setOnItemActionListener(FloatingActionButton fab, final int activity_id){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoading(true);
                NearbyActivityService n = new NearbyActivityService(mActivity);
                final List<HashMap<String, String>> ll = new ArrayList<HashMap<String, String>>();
                if (pointList.size() != 0) {
                    NearbyActivityService.placeIdTask asyncTask = new NearbyActivityService.placeIdTask(
                            new NearbyActivityService.AsyncResponse() {
                                public void processFinish(List<HashMap<String, String>> np) {
                                    List<HashMap<String, String>> l = np;
                                    for (int i = 0; i < l.size(); i++) {
                                        HashMap<String, String> a = l.get(i);
                                        if (activity_id == Integer.parseInt(a.get("activity_id"))) {
                                            ll.add(a);
                                        }
                                    }
                                    ShowNearbyActivities(ll);
                                }
                            });
                    asyncTask.execute(latitude + "", longitude + "");

                } else {
                    for (int i = 0; i < pointList.size(); i++) {
                        LatLng point_latlng = pointList.get(i);
                        Double lat = point_latlng.latitude;
                        Double lon = point_latlng.longitude;
                        NearbyActivityService.placeIdTask gasyncTask = new NearbyActivityService.placeIdTask(new NearbyActivityService.AsyncResponse() {
                            public void processFinish(List<HashMap<String, String>> np) {
                                List<HashMap<String, String>> l = np;
                                for (int i = 0; i < l.size(); i++) {
                                    HashMap<String, String> a = l.get(i);
                                    if (activity_id == Integer.parseInt(a.get("activity_id"))) {
                                        ll.add(a);
                                    }
                                }
                                ShowNearbyActivities(ll);
                            }
                        });
                        gasyncTask.execute(latitude + "", longitude + "");
                    }

                }
            }
    });
    }



            public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

            public boolean checkLocationPermission() {
                if (ContextCompat.checkSelfPermission(this.getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Asking user if explanation is needed
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this.getActivity(),
                            Manifest.permission.ACCESS_FINE_LOCATION)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                        //Prompt the user once explanation has been shown
                        ActivityCompat.requestPermissions(this.getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    } else {
                        // No explanation needed, we can request the permission.
                        ActivityCompat.requestPermissions(this.getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                MY_PERMISSIONS_REQUEST_LOCATION);
                    }
                    return false;
                } else {
                    return true;
                }
            }

            private void ShowNearbyPlaces(List<NearbyPlace> nearbyPlacesList) {
                Log.d("onPostExecute", nearbyPlacesList.size() + "");
                if (nearbyPlacesList.size() != 0) {
                    for (int i = 0; i < nearbyPlacesList.size(); i++) {
                        Log.d("onPostExecute", "Entered into showing locations");
                        MarkerOptions markerOptions = new MarkerOptions();
                        NearbyPlace googlePlace = nearbyPlacesList.get(i);
                        double lat = Double.parseDouble(googlePlace.getLatitude());
                        double lng = Double.parseDouble(googlePlace.getLongitude());
                        String placeName = googlePlace.getPlaceName();
                        String vicinity = googlePlace.getVicinity();
                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        markerOptions.title(placeName + " : " + vicinity);
                        mMap.addMarker(markerOptions);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        //move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                    }
                } else {
                    Toast.makeText(getActivity(), "Cannot find anything.", Toast.LENGTH_SHORT).show();
                }
            }


            private void ShowNearbyActivities(List<HashMap<String, String>> NearbyActivities) {
                Log.d("onPostExecute", NearbyActivities.size() + "");
                if (NearbyActivities.size() != 0) {
                    for (int i = 0; i < NearbyActivities.size(); i++) {
                        Log.d("onPostExecute", "Entered into showing locations");
                        MarkerOptions markerOptions = new MarkerOptions();
                        HashMap<String, String> h = NearbyActivities.get(i);
                        String name = h.get("name");
                        String address = h.get("vicinity");
                        double lat = Double.parseDouble(h.get("Latitude"));
                        double lng = Double.parseDouble(h.get("longitude"));
                        LatLng latLng = new LatLng(lat, lng);
                        markerOptions.position(latLng);
                        markerOptions.title(name + " : " + address);
                        mMap.addMarker(markerOptions);
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                        //move map camera
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                    }
                } else {
                    isLoading(false);
                    Toast.makeText(getActivity(), "There is no activity nearby.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onConnected(@Nullable Bundle bundle) {

                if (ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_ACCESS_COARSE_LOCATION);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkLocationPermission();
                }

                //Check if Google Play Services Available or not
                if (!CheckGooglePlayServices()) {
                    Log.d("onCreate", "Finishing test case since Google Play Services are not available");
                    //finish();
                } else {
                    Log.d("onCreate", "Google Play Services available.");
                }
            }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
            public void onActivityCreated(Bundle savedInstanceState) {
                super.onActivityCreated(savedInstanceState);
                FrameLayout view = (FrameLayout) getActivity().findViewById(R.id.nearyby_fragment);
                setupUI(view);
                setHasOptionsMenu(true);
            }


            public void setupUI(View view) {

                // Set up touch listener for non-text box views to hide keyboard.
                if (!(view instanceof EditText)) {
                    view.setOnTouchListener(new View.OnTouchListener() {
                        public boolean onTouch(View v, MotionEvent event) {
                            Utils.hideSoftKeyboard(getActivity());
                            return false;
                        }
                    });
                }

                //If a layout container, iterate over children and seed recursion.
                if (view instanceof ViewGroup) {
                    for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                        View innerView = ((ViewGroup) view).getChildAt(i);
                        setupUI(innerView);
                    }
                }
            }

            public void getRoute(LatLng dest) {

                String serverKey = getResources().getString(R.string.google_maps_key);
                LatLng origin = new LatLng(mMap.getMyLocation().getLatitude(), mMap.getMyLocation().getLongitude());
                GoogleDirection.withServerKey(serverKey)
                        .from(origin)
                        .to(dest)
                        .execute(new DirectionCallback() {
                            @Override
                            public void onDirectionSuccess(Direction direction, String rawBody) {
                                String status = direction.getStatus();
                                if (status.equals(RequestResult.OK)) {
                                    route = direction.getRouteList().get(0);
                                    leg = route.getLegList().get(0);
                                    pointList = leg.getSectionPoint();

                                    ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                    PolylineOptions polylineOptions = DirectionConverter.createPolyline(mActivity, directionPositionList, 5, Color.RED);
                                    mMap.addPolyline(polylineOptions);
                                    isLoading(false);
                                    // Do something
                                } else if (status.equals(RequestResult.NOT_FOUND)) {
                                    // Do something
                                    Toast.makeText(mActivity, "Cannot find location.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onDirectionFailure(Throwable t) {
                                // Do something here
                                Toast.makeText(getActivity(), "Something Wrong.", Toast.LENGTH_SHORT).show();
                            }
                        });

            }


            public void isLoading(boolean isLoading) {
                if (isLoading) {
                    loading.setVisibility(View.VISIBLE);
                } else {
                    loading.setVisibility(View.GONE);
                }
            }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
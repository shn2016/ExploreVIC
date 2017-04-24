package jzha442.explorevic.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.adapter.FacilityAdapter;
import jzha442.explorevic.database.ActivityPlaceService;
import jzha442.explorevic.database.FavouritePlaceService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.database.PlaceWeatherService;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.model.Weather;
import jzha442.explorevic.screen.DirectionActivity;
import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.Utils;

public class FavouriteDetailFragment extends Fragment {

    //elements in screen
    @Bind(R.id.image_detail)
    ImageView pix;
    @Bind(R.id.txt_title_detail)
    TextView title;
    @Bind(R.id.txt_address_detail)
    TextView address;

    @Bind(R.id.text_open_time)
    TextView open_time;

    @Bind(R.id.text_facility)
    TextView facility;

    @Bind(R.id.text_desc)
    TextView desc;

    @Bind(R.id.web)
    FloatingActionButton web;

    @Bind(R.id.recycler_activity_icon)
    RecyclerView activity_view;

    @Bind(R.id.recycler_facility_icon)
    RecyclerView facility_view;

    @Bind(R.id.current_weather_card)
    FrameLayout current_weather_card;

    @Bind(R.id.plus_icon)
    MaterialIconView plus_icon;

    @Bind(R.id.minus_icon)
    MaterialIconView minus_icon;

    @Bind(R.id.image_favourite)
    ImageView favourite;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int id;//can be
    private boolean isCustom;
    private int pID;
    private boolean isPlace = true;

    //private OnFragmentInteractionListener mListener;

    protected Activity mActivity;

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        /*
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
        this.mActivity = context;
    }

    private void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    public FavouriteDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @param isCustom Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    public static FavouriteDetailFragment newInstance(int id, boolean isCustom) {
        FavouriteDetailFragment fragment = new FavouriteDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, id);
        args.putBoolean(ARG_PARAM2, isCustom);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_PARAM1);
            isCustom = getArguments().getBoolean(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(mActivity)
                .inflate(R.layout.fragment_detail, container, false);
        initView(view, savedInstanceState);
        return view;
        //return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    private void initData(){

        if(isCustom){
            FavouritePlaceService fps = new FavouritePlaceService(mActivity);
            FavouritePlace fp = fps.getPlaceById(id);
            if (BitmapCache.getInstance().getBitmapByPath("custom" + fp.getID()) == null) {
                Bitmap bmap;
                if (fp.getPix().getHeight() > fp.getPix().getWidth()) {
                    bmap = Utils.zoomBitmap(fp.getPix(), 300, 400);
                } else {
                    bmap = Utils.zoomBitmap(fp.getPix(), 400, 300);
                }
                BitmapCache.getInstance().addBitmapToCache("custom" + fp.getID(), bmap);
            }
            pix.setImageBitmap(BitmapCache.getInstance().getBitmapByPath("custom" + fp.getID()));
            title.setText(fp.getName());
            address.setText(fp.getAddress());

            //set activity icons
            activity_view.setVisibility(View.GONE);
            ArrayList<String> availableActies = Utils.getSingleElement(fp.getActivity());

            if(availableActies.size()>0){
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
                activity_view.setLayoutManager(layoutManager);
                activity_view.setVisibility(View.VISIBLE);
                FacilityAdapter mAdapter = new FacilityAdapter(mActivity, availableActies);
                activity_view.setAdapter(mAdapter);
            }

            //hide facility icons by default
            facility.setVisibility(View.GONE);
            facility_view.setVisibility(View.GONE);
            ArrayList<String> availableFacies = Utils.getSingleElement(fp.getFacility());

            if (availableFacies.size()>0) {
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
                facility_view.setLayoutManager(layoutManager);
                facility_view.setVisibility(View.VISIBLE);
                FacilityAdapter mAdapter = new FacilityAdapter(mActivity, availableFacies);
                facility_view.setAdapter(mAdapter);
            }

            //set description
            desc.setText(fp.getDesc());
            web.setVisibility(View.GONE);
            favourite.setVisibility(View.GONE);

            //set weather
            PlaceWeatherService pws = new PlaceWeatherService(false);
            List<Weather> weathers = pws.getWeatherList(fp.getID());
            if (weathers == null) {
                current_weather_card.setVisibility(View.GONE);
                hideWeatherIcon();
            } else {
                if (weathers.size() > 0) {
                    current_weather_card.setVisibility(View.VISIBLE);
                    if(fp.getLatitude()!=0 && fp.getLongitude()!=0){
                        isPlace = false;
                    }
                    WeatherFragment wf = WeatherFragment.newInstance(fp.getID(), true, isPlace);

                    getChildFragmentManager().beginTransaction()//.addToBackStack(null)
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .replace(R.id.current_weather_card, wf).commit();
                }
            }

        }else {
            //load data from Activity_Places
            ActivityPlaceService aps = new ActivityPlaceService(mActivity);
            //if it's not a customized activity place
            //existing activity place from application
            //id is activity_place id
            final ActivityPlace ap = aps.getActivityPlaceById(id);
            pID = ap.getPlace_id();
            //getActivity().getActionBar().setTitle("");

            if (BitmapCache.getInstance().getBitmapByPath("ap" + ap.getID()) == null) {
                Bitmap bmap;
                if (ap.getPix().getHeight() > ap.getPix().getWidth()) {
                    bmap = Utils.zoomBitmap(ap.getPix(), 300, 400);
                } else {
                    bmap = Utils.zoomBitmap(ap.getPix(), 400, 300);
                }
                BitmapCache.getInstance().addBitmapToCache("ap" + ap.getID(), bmap);
            }

            pix.setImageBitmap(BitmapCache.getInstance().getBitmapByPath("ap" + ap.getID()));
            title.setText(ap.getName());

            //load data from Place
            PlaceService ps = new PlaceService(mActivity);
            final Place p = ps.getPlaceById(pID);
            address.setText(p.getAddress());
            if (p.getOpeningTime() != null) {
                open_time.setVisibility(View.VISIBLE);
                open_time.setText(p.getOpeningTime());
            }

            //set activity icons
            activity_view.setVisibility(View.GONE);
            ArrayList<String> availableActies = aps.getAvailableActivityList(ap.getPlace_id());

            if(availableActies.size()>0){
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
                activity_view.setLayoutManager(layoutManager);
                activity_view.setVisibility(View.VISIBLE);
                FacilityAdapter mAdapter = new FacilityAdapter(mActivity, availableActies);
                activity_view.setAdapter(mAdapter);
            }

            //hide facility icons by default
            facility.setVisibility(View.GONE);
            facility_view.setVisibility(View.GONE);

            if (p.getFacility() != null && !p.getFacility().isEmpty()) {
                ArrayList<String> faci = p.getSingleFacility();
                if (faci.size() > 0) {
                    facility.setVisibility(View.GONE);
                    facility.setText(p.getFacility());

                    LinearLayoutManager layoutManager
                            = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
                    facility_view.setLayoutManager(layoutManager);
                    facility_view.setVisibility(View.VISIBLE);
                    FacilityAdapter mAdapter = new FacilityAdapter(mActivity, faci);
                    facility_view.setAdapter(mAdapter);
                }
            }

            //set description
            desc.setText(p.getDesc());
            //web button click listener
            if (p.getWebsite() != null && !p.getWebsite().trim().isEmpty()) {
                final String webUrl = p.getWebsite();
                web.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                        String finaUrl = "";
                        if (!webUrl.startsWith("http")) {
                            finaUrl = "http://" + webUrl;
                        } else {
                            finaUrl = webUrl;
                        }
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finaUrl));
                        startActivity(browserIntent);
                    }
                });
            } else {
                web.setVisibility(View.GONE);
            }

            PlaceWeatherService pws = new PlaceWeatherService(true);
            List<Weather> weathers = pws.getWeatherList(p.getID());
            if (weathers == null) {
                current_weather_card.setVisibility(View.GONE);
                hideWeatherIcon();
            } else {
                if (weathers.size() > 0) {
                    current_weather_card.setVisibility(View.VISIBLE);
                    WeatherFragment wf = WeatherFragment.newInstance(p.getID(),true, isPlace);

                    getChildFragmentManager().beginTransaction()//.addToBackStack(null)
                            .setCustomAnimations(android.R.anim.fade_in,
                                    android.R.anim.fade_out)
                            .replace(R.id.current_weather_card, wf).commit();
                }
            }

            final FavouritePlaceService fps = new FavouritePlaceService(mActivity);
            if (fps.checkPlaceById(ap.getID())) {
                favourite.setImageResource(R.drawable.favourite);
            } else {
                favourite.setImageResource(R.drawable.favourite_outline);
            }
            //favourite icon - click to add this activity_place to the favourite list
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fps.checkPlaceById(ap.getID())) {
                        fps.deletePlaceById(ap.getID());
                        Utils.showMessage(mActivity, "Removed this place from favourites.");
                        favourite.setImageResource(R.drawable.favourite_outline);
                    } else {
                        FavouritePlace fp = new FavouritePlace(ap.getID());
                        fp.setName(ap.getName());
                        fps.insert(fp);
                        Utils.showMessage(mActivity, "Added this place as favourite!");
                        favourite.setImageResource(R.drawable.favourite);
                    }
                }
            });
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                Log.i("Detail Fragment", "click back button.");
                getFragmentManager().popBackStack();
                break;
            case R.id.action_direction:
                Intent intent =new Intent(getActivity(),DirectionActivity.class);
                //用Bundle携带数据
                Bundle bundle=new Bundle();
                if(pID>0) {
                    bundle.putBoolean("isPlace", true);
                    bundle.putInt("pID", pID);
                }else{
                    bundle.putBoolean("isPlace", false);
                    bundle.putInt("fID", id);
                }
                intent.putExtras(bundle);

                startActivity(intent);
                break;
        }
        return true;
    }

    public void switchIcon(boolean isPlus){
        if(!isPlus){
            plus_icon.setVisibility(View.VISIBLE);
            minus_icon.setVisibility(View.GONE);
        }else{
            plus_icon.setVisibility(View.GONE);
            minus_icon.setVisibility(View.VISIBLE);
        }
    }

    public void hideWeatherIcon(){
        plus_icon.setVisibility(View.GONE);
        minus_icon.setVisibility(View.GONE);
    }
}

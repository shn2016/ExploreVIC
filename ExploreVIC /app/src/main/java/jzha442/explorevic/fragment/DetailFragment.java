package jzha442.explorevic.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.adapter.FacilityAdapter;
import jzha442.explorevic.adapter.SearchConditionAdapter;
import jzha442.explorevic.database.ActivityPlaceService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.database.PlaceWeatherService;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.model.PlaceWeather;
import jzha442.explorevic.model.Weather;
import jzha442.explorevic.screen.DirectionActivity;
import jzha442.explorevic.screen.MainActivity;
import jzha442.explorevic.screen.PlaceActivity;
import jzha442.explorevic.utility.Utils;

import static android.R.attr.filter;
import static jzha442.explorevic.R.drawable.wind;
import static jzha442.explorevic.R.id.fab;

public class DetailFragment extends Fragment {

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

    @Bind(R.id.recycler_facility_icon)
    RecyclerView facility_view;

    @Bind(R.id.current_weather_card)
    FrameLayout current_weather_card;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int aID;
    private int pID;

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

    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param aID Parameter 1.
     * @param pID Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    public static DetailFragment newInstance(int aID, int pID) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, aID);
        args.putInt(ARG_PARAM2, pID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            aID = getArguments().getInt(ARG_PARAM1);
            pID = getArguments().getInt(ARG_PARAM2);
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
        //load data from Activity_Places
        ActivityPlaceService aps = new ActivityPlaceService(mActivity);
        ActivityPlace ap = aps.getActivityPlaceById(aID, pID);

        //getActivity().getActionBar().setTitle("");

        Bitmap b = ap.getPix();
        Bitmap bmap = null;
        if(b.getHeight() > b.getWidth()){
            bmap = Utils.zoomBitmap(ap.getPix(), 400,600);
        }else{
            bmap = Utils.zoomBitmap(ap.getPix(), 600,400);
        }

        pix.setImageBitmap(bmap);
        title.setText(ap.getName());

        //load data from Place
        PlaceService ps = new PlaceService(mActivity);
        Place p = ps.getPlaceById(pID);
        address.setText(p.getAddress());
        if (p.getOpeningTime()!=null){
            open_time.setVisibility(View.VISIBLE);
            open_time.setText(p.getOpeningTime());
        }
        if(p.getFacility()!=null && !p.getFacility().isEmpty()) {
            ArrayList<String> faci = p.getSingleFacility();
            if (faci.size()>0) {
                facility.setVisibility(View.GONE);
                facility.setText(p.getFacility());

                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
                facility_view.setLayoutManager(layoutManager);
                facility_view.setVisibility(View.VISIBLE);
                FacilityAdapter mAdapter = new FacilityAdapter(mActivity, faci);
                facility_view.setAdapter(mAdapter);
            }
        }else{
            facility.setVisibility(View.GONE);
            facility_view.setVisibility(View.GONE);
        }
        desc.setText(p.getDesc());
        //web button click listener
        if(p.getWebsite()!=null && !p.getWebsite().trim().isEmpty()){
            final String webUrl = p.getWebsite();
            web.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    String finaUrl = "";
                    if(!webUrl.startsWith("http")){
                        finaUrl = "http://"+webUrl;
                    }else{
                        finaUrl = webUrl;
                    }
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finaUrl));
                    startActivity(browserIntent);
                }
            });
        }else {
            web.setVisibility(View.GONE);
        }

        PlaceWeatherService pws = new PlaceWeatherService();
        List<Weather> weathers = pws.getWeatherList(p.getID());
        if(weathers!=null && weathers.size()>0){
            current_weather_card.setVisibility(View.VISIBLE);
            Weather currentWeather = weathers.get(0);
            WeatherFragment wf = WeatherFragment.newInstance(p.getID(),false);

            getChildFragmentManager().beginTransaction()//.addToBackStack(null)
                    .setCustomAnimations(android.R.anim.fade_in,
                            android.R.anim.fade_out)
                    .replace(R.id.current_weather_card, wf).commit();
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
                bundle.putInt("pID", pID);
                intent.putExtras(bundle);

                startActivity(intent);
                break;
        }
        return true;
    }
}

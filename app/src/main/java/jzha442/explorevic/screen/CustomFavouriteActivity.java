package jzha442.explorevic.screen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import net.steamcrafted.materialiconlib.MaterialDrawableBuilder;
import net.steamcrafted.materialiconlib.MaterialIconView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.Bind;
import jzha442.explorevic.R;
import jzha442.explorevic.adapter.PlacesAutocompleteAdapter;
import jzha442.explorevic.adapter.SelectAdapter;
import jzha442.explorevic.database.ActivityService;
import jzha442.explorevic.database.FavouritePlaceService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.fragment.ActivityFragment;
import jzha442.explorevic.fragment.SelectFragment;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.PhotoHelper;
import jzha442.explorevic.utility.TakePhotoActivity;
//import com.jph.takephoto.app.TakePhotoActivity;
import jzha442.explorevic.utility.Utils;

public class CustomFavouriteActivity extends TakePhotoActivity {

    Handler handler = new Handler();

    private FavouritePlace fp;
    private boolean isAdd;
    private PhotoHelper photoHelper;

    private ScrollView scroll;
    private ImageView image;
    private EditText name;
    private PlacesAutocompleteTextView address;
    private MaterialSpinner region;
    private EditText description;
    private LinearLayout actiButton;
    private MaterialIconView actiIcon;
    private FrameLayout actiFragment;
    private LinearLayout faciButton;
    private MaterialIconView faciIcon;
    private FrameLayout faciFragment;

    private ArrayList<String> actiChoice = new ArrayList<String>();
    private ArrayList<String> faciChoice = new ArrayList<String>();

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView= LayoutInflater.from(this).inflate(R.layout.activity_custom_favourite,null);
        setContentView(contentView);
        //setContentView(R.layout.activity_custom_favourite);
        photoHelper=PhotoHelper.of(contentView);

        //receive data from previous activity
        Bundle bundle = this.getIntent().getExtras();
        isAdd = bundle.getBoolean("isAdd");


        ActionBar ab = getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        if(isAdd) {
            ab.setTitle("Add Custom Favourite");
        }else{
            ab.setTitle("Edit Custom Favourite");
            int fID = bundle.getInt("fID");
            FavouritePlaceService fps = new FavouritePlaceService(this);
            fp = fps.getPlaceById(fID);
        }

        //CustomHelper
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        //set layout element
        scroll = (ScrollView) findViewById(R.id.custom_scroll);
        setupUI(scroll);
        image = (ImageView) findViewById(R.id.new_image);
        name = (EditText) findViewById(R.id.custom_name);
        description = (EditText) findViewById(R.id.custom_desc);

        address = (PlacesAutocompleteTextView) findViewById(R.id.places_autocomplete);
        address.setOnPlaceSelectedListener(
                new OnPlaceSelectedListener() {
                    @Override
                    public void onPlaceSelected(final Place place) {
                        // do something awesome with the selected place
                        Log.i("place", place.place_id);
                        address.setTag(place.place_id);
                        address.getDetailsFor(place, new DetailsCallback() {
                            @Override
                            public void onSuccess(PlaceDetails placeDetails) {
                                latitude = placeDetails.geometry.location.lat;
                                longitude = placeDetails.geometry.location.lng;
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });
                    }
                }
        );

        //address.setAdapter(new PlacesAutocompleteAdapter(this, address.getApi(), address.getResultType(), address.getHistoryManager()));

        region = (MaterialSpinner) findViewById(R.id.spinner_custom_region);
        PlaceService ps = new PlaceService(this);
        ArrayList<String> items =  ps.getRegionList();
        region.setItems(items);
        //region.setBackgroundColor(Color.TRANSPARENT);
        region.setArrowColor(Color.parseColor("#ff33b5e5"));
        region.setDropdownMaxHeight(600);
        region.setBackgroundColor(Color.parseColor("#edf9ff"));

        actiButton = (LinearLayout) findViewById(R.id.activity_layout);
        actiIcon = (MaterialIconView) findViewById(R.id.plus_activity_icon);
        actiFragment = (FrameLayout) findViewById(R.id.activity_fragment);
        actiFragment.setVisibility(View.GONE);
        faciButton = (LinearLayout) findViewById(R.id.facility_layout);
        faciIcon = (MaterialIconView) findViewById(R.id.plus_facility_icon);
        faciFragment = (FrameLayout) findViewById(R.id.facility_fragment);
        faciFragment.setVisibility(View.GONE);

        setFragement();

        //CardView takeButton;
        CardView pickButton = (CardView) findViewById(R.id.btnPickBySelect);

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoHelper.onClick(v,getTakePhoto());
            }
        });

        CardView takeButton = (CardView) findViewById(R.id.btnPickByTake);

        takeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoHelper.onClick(v,getTakePhoto());
            }
        });

        actiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(actiFragment.getVisibility() == View.VISIBLE) {
                    actiFragment.startAnimation(fragmentAnimation(false));
                    actiFragment.setVisibility(View.GONE);
                    //actiFragment.setVisibility(View.GONE);
                    actiIcon.setIcon(MaterialDrawableBuilder.IconValue.PLUS_BOX);
                }else{
                    actiFragment.startAnimation(fragmentAnimation(true));
                    actiFragment.setVisibility(View.VISIBLE);
                    //actiFragment.setVisibility(View.VISIBLE);
                    actiIcon.setIcon(MaterialDrawableBuilder.IconValue.MINUS_BOX);
                    /*handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.smoothScrollBy(0, actiFragment.getHeight());
                            //scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });*/
                }
            }
        });

        faciButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(faciFragment.getVisibility() == View.VISIBLE) {
                    faciFragment.startAnimation(fragmentAnimation(false));
                    faciFragment.setVisibility(View.GONE);
                    faciIcon.setIcon(MaterialDrawableBuilder.IconValue.PLUS_BOX);
                }else{
                    faciFragment.startAnimation(fragmentAnimation(true));
                    faciFragment.setVisibility(View.VISIBLE);
                    faciIcon.setIcon(MaterialDrawableBuilder.IconValue.MINUS_BOX);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.smoothScrollBy(0, faciFragment.getHeight());
                            //scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
            }
        });

        //if editing, set default value
        if(!isAdd){
            if (BitmapCache.getInstance().getBitmapByPath("custom" + fp.getID()) == null) {
                Bitmap bmap;
                if (fp.getPix().getHeight() > fp.getPix().getWidth()) {
                    bmap = Utils.zoomBitmap(fp.getPix(), 300, 400);
                } else {
                    bmap = Utils.zoomBitmap(fp.getPix(), 400, 300);
                }
                BitmapCache.getInstance().addBitmapToCache("custom" + fp.getID(), bmap);
            }
            //image.setImageBitmap(BitmapCache.getInstance().getBitmapByPath("custom" + fp.getID()));
            Glide.with(this).load(Utils.Bitmap2Bytes(BitmapCache.getInstance().getBitmapByPath("custom" + fp.getID()))).centerCrop().into(image);

            name.setText(fp.getName());
            address.setText(fp.getAddress());
            description.setText(fp.getDesc());
            region.setSelectedIndex(items.indexOf(fp.getRegion()));
            String activity = fp.getActivity();
            String facility = fp.getFacility();
            if(activity!=null && !activity.trim().isEmpty()){
                actiChoice = Utils.getSingleElement(activity);
            }
            if(facility!=null && !facility.trim().isEmpty()){
                faciChoice = Utils.getSingleElement(facility);
            }
        }
    }

    public AnimationSet fragmentAnimation(boolean isShow){
        AnimationSet animationSet = new AnimationSet(true);//共用动画补间
        animationSet.setDuration(300);
        if(isShow) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
            alphaAnimation.setDuration(300);
            animationSet.addAnimation(alphaAnimation);

            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -0.5f, Animation.RELATIVE_TO_SELF, 0.0f);
            translateAnimation.setDuration(300);
            animationSet.addAnimation(translateAnimation);
        }else{
            AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
            alphaAnimation.setDuration(300);
            animationSet.addAnimation(alphaAnimation);

            TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                    -0.5f);
            translateAnimation.setDuration(300);
            animationSet.addAnimation(translateAnimation);
        }
        return animationSet;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.manage_custom, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        } else if (id == R.id.action_save){
            //save custom favourite
            save();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImage());
    }

    private void showImg(TImage images) {
        //Intent intent=new Intent(this,ResultActivity.class);
        //intent.putExtra("images",images);
        //startActivity(intent);
        Glide.with(this).load(new File(images.getCompressPath())).centerCrop().into(image);
    }

    private void setFragement(){
        //set activity fragment
        ActivityService as = new ActivityService(this);
        ArrayList<String> acties = as.getActivityNameList();

        String activity = "";
        if(fp!=null && fp.getActivity()!=null && !fp.getActivity().trim().isEmpty()){
            activity = fp.getActivity();
        }
        SelectFragment actiSF = SelectFragment.newInstance(acties, true, activity);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up)
                .replace(R.id.activity_fragment, actiSF, "facility").commit();

        //set facility fragment
        PlaceService ps = new PlaceService(this);
        ArrayList<String> facies = ps.getFacilityList();

        String facility = "";
        if(fp!=null && fp.getFacility()!=null && !fp.getFacility().trim().isEmpty()){
            facility = fp.getFacility();
        }
        SelectFragment faciSF = SelectFragment.newInstance(facies, false, facility);

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_down, R.anim.slide_out_up)
                .replace(R.id.facility_fragment, faciSF, "facility").commit();
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    Utils.hideSoftKeyboard(CustomFavouriteActivity.this);
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

    public void setIconChoice(ArrayList<String> choice, boolean isActivity){
        if(isActivity) {
            actiChoice = choice;
        }else {
            faciChoice = choice;
        }
    }

    public void save(){
        if(image.getDrawable() == null){
            Utils.showMessage(this, "Please take a photo first!");
            return;
        }
        Bitmap bitmap = ((GlideBitmapDrawable) image.getDrawable()).getBitmap();
        if(bitmap==null){
            Utils.showMessage(this, "Please take a photo first!");
            return;
        }
        String name = this.name.getText().toString().trim();

        if(name.isEmpty()){
            Utils.showMessage(this, "Please enter the place name first!");
            return;
        }

        String r = region.getItems().get(region.getSelectedIndex()).toString();

        if(r == "Region"){
            Utils.showMessage(this, "Please select a region of the place first!");
            return;
        }

        String desc = description.getText().toString().trim();
        String add = address.getText().toString().trim();
        String activity = Utils.Array2String(actiChoice);
        String facility = Utils.Array2String(faciChoice);

        if(isAdd){
            FavouritePlace fp;
            if(latitude!=0 && longitude!=0) {
                fp = new FavouritePlace(name, desc, r, add, latitude, longitude, activity, facility, bitmap);
            }else{
                fp = new FavouritePlace(name, desc, r, add, activity, facility, bitmap);
            }
            FavouritePlaceService fps = new FavouritePlaceService(this);
            fps.insert(fp);
            Utils.showMessage(this, "Save a new custom place successfully!");
            onBackPressed();
        }else{
            fp.setName(name);
            fp.setDesc(desc);
            fp.setRegion(r);
            fp.setAddress(add);
            if(latitude!=0 && longitude!=0) {
                fp.setLatitude(latitude);
                fp.setLongitude(longitude);
            }
            fp.setActivity(activity);
            fp.setFacility(facility);
            fp.setPix(bitmap);
            FavouritePlaceService fps = new FavouritePlaceService(this);
            fps.updateFavourite(fp);
            Utils.showMessage(this, "Save the custom place successfully!");
            onBackPressed();
        }
    }
}

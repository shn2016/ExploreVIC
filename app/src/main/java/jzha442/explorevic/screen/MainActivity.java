package jzha442.explorevic.screen;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

import jzha442.explorevic.R;
import jzha442.explorevic.adapter.PlaceAdapter;
import jzha442.explorevic.adapter.SearchAdapter;
import jzha442.explorevic.adapter.SearchConditionAdapter;
import jzha442.explorevic.database.ActivityPlaceService;
import jzha442.explorevic.database.ActivityService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.database.SearchRecordService;
import jzha442.explorevic.fragment.ActivityFragment;
import jzha442.explorevic.fragment.DetailFragment;
import jzha442.explorevic.fragment.FavouriteFragment;
import jzha442.explorevic.fragment.NearbyFragment;
import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.SearchRecord;
import jzha442.explorevic.utility.Timeconversion;
import jzha442.explorevic.utility.Utils;

import static android.R.attr.country;
import static android.R.attr.filter;
import static android.R.id.toggle;
import static jzha442.explorevic.R.id.action_add_favourite;
import static jzha442.explorevic.R.id.imageView;
import static jzha442.explorevic.utility.Timeconversion.unix2time;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ArrayList<String> mRecords;
    private NavigationView navigationView;
    private boolean isSearch = false;
    private RecyclerView searchConditionView;
    private Filter searchFilter;

    public static int CHANGE_CUSTOM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        navigationView.getMenu().performIdentifierAction(R.id.nav_activity, 0);

        searchConditionView = (RecyclerView) findViewById(R.id.recycler_search_condition);
        searchConditionView.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(isSearch){
                clearSearch();
            }else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);

        Fragment activityFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("activity");
        if (activityFragment != null && activityFragment.isVisible()) {
            item.setVisible(true);
        } else {
            item.setVisible(false);
        }

        MenuItem itemAdd = menu.findItem(action_add_favourite);

        Fragment favouriteFragment = (Fragment) getSupportFragmentManager().findFragmentByTag("favourite");
        if (favouriteFragment != null && favouriteFragment.isVisible()) {
            itemAdd.setVisible(true);
        } else {
            itemAdd.setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            //if click search button in the activity screen (home page)
            loadToolBarSearch();
        } else if (id == R.id.home){
            Log.i("Main ---home", "click back button.");
            getFragmentManager().popBackStack();
        } else if (id == R.id.action_add_favourite){
            addCustomFavourite();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        if (id == R.id.nav_activity) {

            //check if drawer layout is open
            if(drawer.isDrawerOpen(GravityCompat.START)) {
                //drawer is open
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.container, new ActivityFragment(),"activity").commit();
                    }
                }, 300);
            }else{
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.container, new ActivityFragment(),"activity").commit();
            }


        } else if (id == R.id.nav_favourite){
            if(drawer.isDrawerOpen(GravityCompat.START)) {
                //drawer is open
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .replace(R.id.container, new FavouriteFragment(),"favourite").commit();
                    }
                }, 300);
            }
            /*else{
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.container, new ActivityFragment()).commit();
            }*/
        } else if (id == R.id.nav_nearby){
            //Handle the camera action
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .replace(R.id.container, new NearbyFragment(),"nearby").commit();
                }
            }, 300);

        }

        /*
        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        */

        drawer.closeDrawer(GravityCompat.START);

        // Handle the camera action
        if(isSearch) {
            clearFilter();
        }
        return true;
    }

    //show search view
    public void loadToolBarSearch() {
        final SearchRecordService srs = new SearchRecordService(MainActivity.this);
        //get latest search history
        final ArrayList<String> recordStored = srs.getLatestSearchList();

        //get search view in layout
        View view = MainActivity.this.getLayoutInflater().inflate(R.layout.search_view, null);
        //get search layout
        LinearLayout parentToolbarSearch = (LinearLayout) view.findViewById(R.id.parent_toolbar_search);
        //back button image in search bar
        ImageView imgToolBack = (ImageView) view.findViewById(R.id.img_tool_back);
        //edit text in search bar
        final EditText edtToolSearch = (EditText) view.findViewById(R.id.edt_tool_search);
        //mic button in search bar
        //ImageView imgToolMic = (ImageView) view.findViewById(R.id.img_tool_mic);
        //search button in search bar
        final ImageView imgToolSearch = (ImageView) view.findViewById(R.id.img_tool_search);
        //clear button in search bar
        final ImageView imgToolClear = (ImageView) view.findViewById(R.id.img_tool_clear);
        //similar list for search records
        final ListView listSearch = (ListView) view.findViewById(R.id.list_search);
        //hint for empty result
        final TextView txtEmpty = (TextView) view.findViewById(R.id.txt_empty);
        //clear history button
        final Button clearButton = (Button) view.findViewById(R.id.button_clear_history);

        //checkboxes in search view
        final CheckBox air = (CheckBox) view.findViewById(R.id.checkBox_Air);
        final CheckBox land = (CheckBox) view.findViewById(R.id.checkBox_Land);
        final CheckBox sea = (CheckBox) view.findViewById(R.id.checkBox_Sea);

        //Spinner in search view
        final Spinner sp = (Spinner) view.findViewById(R.id.spinner_region);

        PlaceService ps = new PlaceService(this);
        ArrayList<String> items =  ps.getRegionList();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);

        //set list view height
        Utils.setListViewHeightBasedOnChildren(listSearch);

        edtToolSearch.setHint("Search activity...");

        //search dialog
        final Dialog toolbarSearchDialog = new Dialog(MainActivity.this, R.style.MaterialSearch);
        toolbarSearchDialog.setContentView(view);
        toolbarSearchDialog.setCancelable(false);
        toolbarSearchDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        toolbarSearchDialog.getWindow().setGravity(Gravity.BOTTOM);
        toolbarSearchDialog.show();

        toolbarSearchDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        //change status bar color
        Window window = toolbarSearchDialog.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        //check if recordStored is empty for history
        if(recordStored!=null && recordStored.size()>0){
            clearButton.setVisibility(View.VISIBLE);
        }else{
            clearButton.setVisibility(View.GONE);
        }
        final SearchAdapter searchAdapter = new SearchAdapter(MainActivity.this, recordStored, false);

        listSearch.setVisibility(View.VISIBLE);
        listSearch.setAdapter(searchAdapter);

        listSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String word = String.valueOf(adapterView.getItemAtPosition(position));
                //add search word into database for search history
                //SharedPreference.addList(MainActivity.this, Utils.PREFS_NAME, Utils.KEY_COUNTRIES, country);
                edtToolSearch.setText(word);
                listSearch.setVisibility(View.GONE);
                clearButton.setVisibility(View.GONE);

            }
        });
        edtToolSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //actions before user enter text in search bar

                //get all search records
                mRecords = srs.getSearchList();
                listSearch.setVisibility(View.VISIBLE);
                searchAdapter.updateList(mRecords, true);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ArrayList<String> filterList = new ArrayList<String>();
                boolean isNodata = false;
                if (s.length() > 0) {
                    imgToolClear.setVisibility(View.VISIBLE);
                    for (int i = 0; i < mRecords.size(); i++) {

                        if (mRecords.get(i).toLowerCase().startsWith(s.toString().trim().toLowerCase())) {

                            filterList.add(mRecords.get(i));

                            listSearch.setVisibility(View.VISIBLE);
                            searchAdapter.updateList(filterList, true);
                            isNodata = true;
                        }
                    }
                    if (!isNodata) {
                        listSearch.setVisibility(View.GONE);
                        clearButton.setVisibility(View.GONE);
                        //txtEmpty.setVisibility(View.VISIBLE);
                        //txtEmpty.setText("No data found");
                    }
                } else {
                    imgToolClear.setVisibility(View.GONE);
                    listSearch.setVisibility(View.GONE);
                    clearButton.setVisibility(View.GONE);
                    txtEmpty.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toolbarSearchDialog.dismiss();
            }
        });

        //click search button
        imgToolSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String word = edtToolSearch.getText().toString().trim();
                String region = sp.getSelectedItem().toString().trim();

                //if nothing to search, dismiss dialog directly
                if(edtToolSearch.getText().toString().trim().equals("") &&
                        air.isChecked() && land.isChecked() && sea.isChecked()
                        && region.equalsIgnoreCase("Region")){
                    Toast.makeText(getApplicationContext(), "Sorry, you haven't enter any search criteria!",
                    Toast.LENGTH_SHORT).show();
                    toolbarSearchDialog.dismiss();
                    return;
                }

                //insert keyword to search table
                if (word!=null && !word.isEmpty()) {
                    srs.insert(word);
                }
                toolbarSearchDialog.dismiss();

                //call search function with filter
                Filter filter = new Filter();
                filter.setWord(word);
                filter.setHasAir(air.isChecked());
                filter.setHasLand(land.isChecked());
                filter.setHasSea(sea.isChecked());

                if (!region.equalsIgnoreCase("Region")) {
                    filter.setRegion(region);
                }
                searchFilter = filter;
                search();

            }
        });

        imgToolClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rest search bar
                edtToolSearch.setText("");
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                srs.deleteData();
                listSearch.setVisibility(View.GONE);
                clearButton.setVisibility(View.GONE);
            }
        });

        toolbarSearchDialog.setOnKeyListener(new Dialog.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                //return when key down to avoid twice click
                if (event.getAction()!=KeyEvent.ACTION_DOWN)
                    return true;

                //run when key up
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    toolbarSearchDialog.dismiss();
                }else if (keyCode == KeyEvent.KEYCODE_DEL){
                    String word = edtToolSearch.getText().toString();
                    if (word != null & !word.isEmpty()){
                        word = word.substring(0,word.length()-1);
                        edtToolSearch.setText(word);
                        edtToolSearch.setSelection(word.length());
                    }
                }else if(keyCode == KeyEvent.KEYCODE_ENTER){
                    imgToolSearch.performClick();

                }
                return true;
            }
        });
    }

    //search function
    public void search(){
        ActivityFragment af = new ActivityFragment();
        af.setFilter(searchFilter);
        getSupportFragmentManager().beginTransaction()//.addToBackStack(null)
                .setCustomAnimations(R.anim.slide_in_up,
                        R.anim.slide_out_up)
                .replace(R.id.container, af).commit();

        isSearch=true;
        //set search condition view
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        searchConditionView.setLayoutManager(layoutManager);
        searchConditionView.setVisibility(View.VISIBLE);
        final SearchConditionAdapter mAdapter = new SearchConditionAdapter(this, searchFilter);
        searchConditionView.setAdapter(mAdapter);
    }

    public void updateSearchCondition(int filterID){
        SearchConditionAdapter sca = (SearchConditionAdapter) searchConditionView.getAdapter();
        searchFilter.deleteCondition(filterID);
        sca.changeFilter(searchFilter);
        sca.notifyDataSetChanged();
        search();
    }

    public void clearSearch(){
        navigationView.getMenu().performIdentifierAction(R.id.nav_activity, 0);
        clearFilter();
    }

    public void clearFilter(){
        isSearch=false;
        searchConditionView.setVisibility(View.GONE);
        searchFilter = null;
    }

    public void addCustomFavourite(){
        Intent intent =new Intent(MainActivity.this, CustomFavouriteActivity.class);

        //用Bundle携带数据
        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putBoolean("isAdd", true);
        intent.putExtras(bundle);

        startActivityForResult(intent, CHANGE_CUSTOM);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}

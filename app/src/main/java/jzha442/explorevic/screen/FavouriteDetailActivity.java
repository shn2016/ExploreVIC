package jzha442.explorevic.screen;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import jzha442.explorevic.R;
import jzha442.explorevic.database.FavouritePlaceService;
import jzha442.explorevic.fragment.DetailFragment;
import jzha442.explorevic.fragment.FavouriteDetailFragment;
import jzha442.explorevic.fragment.PlaceFragment;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.model.Filter;

public class FavouriteDetailActivity extends AppCompatActivity {

    private boolean isCustom;
    private FavouritePlace fp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);
        //getSupportActionBar().hide();//Ocultar ActivityBar anterior
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab =  getSupportActionBar();
        ab.setHomeButtonEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        Fragment fragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.container);

        if (fragment == null) {

            //新页面接收数据
            Bundle bundle = this.getIntent().getExtras();
            //接收name值
            int id = bundle.getInt("fID");

            FavouritePlaceService fps = new FavouritePlaceService(this);
            fp = fps.getPlaceById(id);

            ab.setTitle(fp.getName());

            if(fp.getAp_id()<=0) {
                isCustom = true;
                FavouriteDetailFragment fdf = FavouriteDetailFragment.newInstance(id, true);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fdf, "custom").commit();
            }else{
                isCustom = false;
                FavouriteDetailFragment fdf = FavouriteDetailFragment.newInstance(fp.getAp_id(), false);
                getSupportFragmentManager().beginTransaction().replace(R.id.container, fdf, "ap").commit();
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_has_map, menu);

        MenuItem item = menu.findItem(R.id.action_direction);

        if (!isCustom) {
            item.setVisible(true);
        } else {
            if(fp.getLongitude() == 0 && fp.getLatitude() == 0) {
                item.setVisible(false);
            }
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == android.R.id.home){
            Log.i("Main ---home", "click back button.");
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


}

package jzha442.explorevic.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.adapter.PlaceAdapter;
import jzha442.explorevic.adapter.WeatherAdapter;
import jzha442.explorevic.database.FavouritePlaceService;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.Weather;
import jzha442.explorevic.screen.MainActivity;

/**
 * Created by Jiao on 02/04/2017.
 */

public class WeatherFragment extends BaseFragment{
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private WeatherAdapter mAdapter;
    private int placeID;
    private boolean isFavourite;
    private boolean isPlace;//not customised favourite

    public static WeatherFragment newInstance(int pId, boolean isFavourite, boolean isPlace) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt("param1", pId);
        args.putBoolean("param2", isFavourite);
        args.putBoolean("param3", isPlace);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get argument - aID
        if (getArguments() != null) {
            placeID = getArguments().getInt("param1");
            isFavourite = getArguments().getBoolean("param2");
            isPlace = getArguments().getBoolean("param3");
        }

        //setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.recycler;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void initData() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        //mAdapter = new WaterFallAdapter(mActivity);
        mAdapter = new WeatherAdapter(mActivity, placeID, isPlace);
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.getList().addAll(list);
        //mAdapter.getRandomHeight(list);
        //mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemActionListener(new WeatherAdapter.OnItemActionListener() {
            private final String TAG = MainActivity.class.getSimpleName();

            @Override
            public void onItemClickListener(View view, int position) {
                Log.i(TAG, "onItemClickListener: " + position);
                mAdapter.notifyDataSetChanged();
                if(isFavourite){
                    FavouriteDetailFragment df = (FavouriteDetailFragment) getParentFragment();
                    df.switchIcon(mAdapter.isCurrent());
                }else {
                    DetailFragment df = (DetailFragment) getParentFragment();
                    df.switchIcon(mAdapter.isCurrent());
                }

            }

            @Override
            public boolean onItemLongClickListener(View view, int position) {
                Log.i(TAG, "onItemLongClickListener: " + position);
                return true;
            }
        });
    }
}

package jzha442.explorevic.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.adapter.ActivityAdapter;
import jzha442.explorevic.adapter.PlaceAdapter;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.screen.MainActivity;
import jzha442.explorevic.screen.PlaceActivity;

import static android.R.attr.action;
import static android.R.attr.id;
import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;

/**
 * Created by Jiao on 23/03/2017.
 */

public class PlaceFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private PlaceAdapter mAdapter;
    private int aID;
    private Filter filter;

    public static PlaceFragment newInstance(int aId, Filter filter) {
        PlaceFragment fragment = new PlaceFragment();
        Bundle args = new Bundle();
        args.putInt("param", aId);
        args.putSerializable("filter", filter);
        Log.i("place fragment", "parameter: "+aId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get argument - aID
        if (getArguments() != null) {
            aID = getArguments().getInt("param");
            filter = (Filter) getArguments().getSerializable("filter");
        }

        setHasOptionsMenu(true);
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
        Log.i("place fragment", "init data parameter: "+aID);

        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            mRecyclerView.setLayoutManager(new
                    StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
            Log.i("info", "landscape"); // 横屏
        }  else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            mRecyclerView.setLayoutManager(new
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            Log.i("info", "portrait"); // 竖屏
        }

        //mAdapter = new WaterFallAdapter(mActivity);
        mAdapter = new PlaceAdapter(mActivity, aID, filter);
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.getList().addAll(list);
        //mAdapter.getRandomHeight(list);
        //mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemActionListener(new PlaceAdapter.OnItemActionListener() {
            private final String TAG = MainActivity.class.getSimpleName();

            @Override
            public void onItemClickListener(View view, int position) {
                Log.i(TAG, "onItemClickListener: " + position);
                DetailFragment df = DetailFragment.newInstance(mAdapter.getActivityID(), mAdapter.getPlaceID(position));

                getFragmentManager().beginTransaction().addToBackStack(null)
                        .setCustomAnimations(R.anim.slide_in_up,
                                R.anim.slide_out_up, android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.container,df,"detail").commit();
            }

            @Override
            public boolean onItemLongClickListener(View view, int position) {
                Log.i(TAG, "onItemLongClickListener: "+position);
                return true;
            }
        });
    }

}

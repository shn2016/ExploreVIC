package jzha442.explorevic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.adapter.ActivityAdapter;
import jzha442.explorevic.adapter.SelectAdapter;
import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.screen.CustomFavouriteActivity;
import jzha442.explorevic.screen.MainActivity;
import jzha442.explorevic.screen.PlaceActivity;
import jzha442.explorevic.utility.Utils;

/**
 * Created by Jiao on 23/03/2017.
 */

public class SelectFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private SelectAdapter mAdapter;
    private ArrayList<String> list;
    private boolean isActivity;

    private String choice;

    public static SelectFragment newInstance(ArrayList<String> array, boolean isActivity, String choice) {
        SelectFragment fragment = new SelectFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("list", array);
        args.putBoolean("isActivity", isActivity);
        args.putString("choice", choice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            list = getArguments().getStringArrayList("list");
            isActivity = getArguments().getBoolean("isActivity");
            choice = getArguments().getString("choice");
        }
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

        /*
        if(this.getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
        {
            mRecyclerView.setLayoutManager(new
                    StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL));
            Log.i("info", "landscape"); // 横屏
        }  else if(this.getResources().getConfiguration().orientation==Configuration.ORIENTATION_PORTRAIT)
        {
            mRecyclerView.setLayoutManager(new
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            Log.i("info", "portrait"); // 竖屏
        }
        */
        GridLayoutManager layout = new GridLayoutManager(getActivity(), 5);

        mRecyclerView.setLayoutManager(layout);
        Log.i("info", "portrait"); // 竖屏

        //set Activity Adapter
        if(choice!=null && !choice.trim().isEmpty()){
            ArrayList<String> selection = Utils.getSingleElement(choice);
            mAdapter = new SelectAdapter(mActivity, list, selection);
        }else {
            mAdapter = new SelectAdapter(mActivity, list);
        }
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.getList().addAll(list);
        //mAdapter.getRandomHeight(list);
        //mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemActionListener(new SelectAdapter.OnItemActionListener() {
            private final String TAG = MainActivity.class.getSimpleName();

            @Override
            public void onItemClickListener(View view, int position) {
                Log.i(TAG, "onItemClickListener: " + position);
                ArrayList<String> array = mAdapter.getChoice();
                CustomFavouriteActivity cfa = (CustomFavouriteActivity) getActivity();
                cfa.setIconChoice(array, isActivity);
                view.getId();
                //click

            }

            @Override
            public boolean onItemLongClickListener(View view, int position) {
                Log.i(TAG, "onItemLongClickListener: "+position);
                return true;
            }
        });
    }

}

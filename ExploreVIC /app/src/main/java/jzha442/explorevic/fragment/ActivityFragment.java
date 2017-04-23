package jzha442.explorevic.fragment;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.adapter.ActivityAdapter;
import jzha442.explorevic.fragment.BaseFragment;
import jzha442.explorevic.R;
import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.screen.MainActivity;
import jzha442.explorevic.screen.PlaceActivity;

import static android.R.id.list;

/**
 * Created by Jiao on 23/03/2017.
 */

public class ActivityFragment extends BaseFragment {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ActivityAdapter mAdapter;
    private Filter filter;

    public void setFilter(Filter filter){
        this.filter = filter;
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

        //set Activity Adapter
        mAdapter = new ActivityAdapter(mActivity, filter);
        mRecyclerView.setAdapter(mAdapter);

        //mAdapter.getList().addAll(list);
        //mAdapter.getRandomHeight(list);
        //mAdapter.notifyDataSetChanged();

        mAdapter.setOnItemActionListener(new ActivityAdapter.OnItemActionListener() {
            private final String TAG = MainActivity.class.getSimpleName();

            @Override
            public void onItemClickListener(View view, int position) {
                Log.i(TAG, "onItemClickListener: " + position);
                Activity a = mAdapter.getActivity(position);

                Intent intent =new Intent(getActivity(),PlaceActivity.class);

                //用Bundle携带数据
                Bundle bundle=new Bundle();
                //传递name参数为tinyphp
                bundle.putInt("aID", a.getID());
                bundle.putString("title", a.getName());
                bundle.putSerializable("filter", filter);
                intent.putExtras(bundle);

                startActivity(intent);

            }

            @Override
            public boolean onItemLongClickListener(View view, int position) {
                Log.i(TAG, "onItemLongClickListener: "+position);
                return true;
            }
        });
    }

}

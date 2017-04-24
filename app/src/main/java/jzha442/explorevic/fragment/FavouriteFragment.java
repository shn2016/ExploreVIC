package jzha442.explorevic.fragment;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.superdialog.SuperDialog;
import com.mylhyl.superdialog.res.values.ColorRes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.halfbit.pinnedsection.PinnedSectionListView;
import jzha442.explorevic.R;
import jzha442.explorevic.adapter.ActivityAdapter;
import jzha442.explorevic.adapter.FavouriteAdapter;
import jzha442.explorevic.database.FavouritePlaceService;
import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.FavouriteItem;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.screen.CustomFavouriteActivity;
import jzha442.explorevic.screen.FavouriteDetailActivity;
import jzha442.explorevic.screen.MainActivity;
import jzha442.explorevic.screen.PlaceActivity;
import jzha442.explorevic.utility.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Jiao on 23/03/2017.
 */

public class FavouriteFragment extends BaseFragment {//implements View.OnClickListener

    private boolean isFastScroll = true;
    private boolean isShadowVisible = true;

    private FavouriteAdapter fa;

    @Bind(android.R.id.list)
    PinnedSectionListView list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_favourite;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void initData() {
        int[] colors = {0, 0xFFFFFFFF, 0}; // red for the example
        //new ColorDrawable(Color.TRANSPARENT)
        list.setDivider(new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors));
        list.setDividerHeight(10);
        list.setFastScrollEnabled(isFastScroll);
        //list.setFastScrollStyle();
        if (isFastScroll) {
            list.setFastScrollAlwaysVisible(true);
            fa = new FavouriteAdapter(mActivity, R.layout.favourite_text, android.R.id.text1);
            list.setAdapter(fa);
        } else {
            //setListAdapter(new SimpleAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1));
        }
        ((PinnedSectionListView)list).setShadowVisible(isShadowVisible);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FavouriteItem item = (FavouriteItem) list.getAdapter().getItem(position);
                if (item != null) {
                    if(item.getFavouriteID()>0){
                        //FavouritePlaceService fps = new FavouritePlaceService(getContext());
                        Intent intent =new Intent(getActivity(),FavouriteDetailActivity.class);

                        //用Bundle携带数据
                        Bundle bundle=new Bundle();
                        //传递name参数为tinyphp
                        bundle.putInt("fID", item.getFavouriteID());
                        intent.putExtras(bundle);

                        getActivity().startActivityForResult(intent, MainActivity.CHANGE_CUSTOM);
                    }
                } else {
                    Toast.makeText(getContext(), "Item " + position, Toast.LENGTH_SHORT).show();
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FavouriteItem item = (FavouriteItem) list.getAdapter().getItem(position);
                if (item != null) {
                    if(item.getFavouriteID()>0){
                        ArrayList<String> operation = new ArrayList<String>();
                        final FavouritePlaceService fps = new FavouritePlaceService(mActivity);
                        final FavouritePlace fp = fps.getPlaceById(item.getFavouriteID());
                        final String[] choice = {"Edit", "Delete", "Removed from favourites"};
                        if(fp.getAp_id()<=0){
                            operation.add(choice[0]);
                            operation.add(choice[1]);
                        } else {
                            operation.add(choice[2]);
                        }

                        final List<String> strings = operation;
                        new SuperDialog.Builder(getActivity())
                                .setAlpha(0.9f)
                                .setGravity(Gravity.CENTER)
                                .setTitle(item.text).setMessage(fp.getAddress())
                                .setCanceledOnTouchOutside(true)
                                .setItems(strings, ColorRes.negativeButton, 45, new SuperDialog.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(int position) {
                                        switch (Arrays.asList(choice).indexOf(strings.get(position))){
                                            case 0:
                                                Intent intent =new Intent(getActivity(), CustomFavouriteActivity.class);

                                                //用Bundle携带数据
                                                Bundle bundle=new Bundle();
                                                //传递name参数为tinyphp
                                                bundle.putBoolean("isAdd", false);
                                                bundle.putInt("fID", fp.getID());
                                                intent.putExtras(bundle);

                                                getActivity().startActivityForResult(intent, MainActivity.CHANGE_CUSTOM);
                                                Utils.showMessage(mActivity, "Edit the place successfully!");
                                                break;
                                            case 1:
                                            case 2:
                                                fps.deleteFavourtiePlaceById(fp.getID());
                                                fa.generateDataset(true);
                                                Utils.showMessage(mActivity, "Remove the place successfully!");
                                                break;
                                        }
                                        //Toast.makeText(mActivity, strings.get(position), Toast.LENGTH_LONG).show();
                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .build();

                        }
                }
                return true;
            }
        });
    }

    /*
    @Override
    public void onClick(View v) {
        Toast.makeText(this, "Item: " + v.getTag() , Toast.LENGTH_SHORT).show();
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MainActivity.CHANGE_CUSTOM) {
            fa.generateDataset(true);
        }
    }

}

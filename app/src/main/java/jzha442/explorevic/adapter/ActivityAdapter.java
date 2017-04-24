package jzha442.explorevic.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.database.ActivityService;
import jzha442.explorevic.database.DBHelper;
import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.Picture;
import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.Utils;

import static android.R.attr.width;
import static java.lang.System.load;

/**
 * Created by Administrator on 2016/5/31 0031.
 *
 *
 */
public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private Context mContext;
    //activity list
    private List<Activity> mList = new ArrayList<>();
    private List<Integer> mHeights;
    private OnItemActionListener mOnItemActionListener;
    private Filter filter;

    public ActivityAdapter(Context context, Filter filter){
        this.mContext = context;
        if (filter!=null) {
            this.filter = filter;
        }
        initData();
    }

    private void initData(){
        ActivityService as = new ActivityService(this.mContext);
        if(filter==null) {
            mList = as.getActivityList();
        }else{
            //filter list
            mList = as.searchActivity(filter);
        }

        if(mList.size() == 0){
            Toast.makeText(mContext, "Sorry, no result found!",
                    Toast.LENGTH_SHORT).show();
        }
        getRandomHeight(mList);
    }

    public void getRandomHeight(List<Activity> mList){
        mHeights = new ArrayList<>();
        for(int i=0; i < mList.size();i++){
            //随机的获取一个范围为400-600直接的高度
            mHeights.add((int)(400+ Math.random()*200));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = mHeights.get(position);
        holder.itemView.setLayoutParams(layoutParams);

        Activity a = mList.get(position);

        //show picture from database
        holder.title.setText(a.getName());
        //set pix showing for activity
        //holder.mImageView.setImageResource(R.drawable.cohete_flat);

        if(BitmapCache.getInstance().getBitmapByPath("a"+a.getID())==null) {
            Bitmap bmap = Utils.zoomBitmap(a.getPix(), 300, 300);
            BitmapCache.getInstance().addBitmapToCache("a" + a.getID(), bmap);
        }
        //Bitmap bmap = Utils.fitBitmap(a.getPix(), 100);
        holder.mImageView.getHeight();
        holder.mImageView.setImageBitmap(BitmapCache.getInstance().getBitmapByPath("a"+a.getID()));

        //Glide.with(mContext).load("http://104.154.41.56/a/a1002.jpg").into(holder.mImageView);

        //part of item click function to get position
        if (mOnItemActionListener == null)
            throw new NullPointerException("onItemClickListener refer to a null position");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据
                mOnItemActionListener.onItemClickListener(v,holder.getPosition());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return mOnItemActionListener.onItemLongClickListener(v,holder.getPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //click and long click action

    public interface OnItemActionListener{
        void onItemClickListener(View view,int position);
        boolean onItemLongClickListener(View view,int position);
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener){
        this.mOnItemActionListener = onItemActionListener;
    }

    public Activity getActivity(int position){
        return mList.get(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.txt_title)
        TextView title;
        @Bind(R.id.imageView)
        ImageView mImageView;

        public ViewHolder(View view){
            //需要设置super
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<Activity> getList() {
        return mList;
    }
}

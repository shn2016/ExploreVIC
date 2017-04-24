package jzha442.explorevic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.littlechoc.cornerlabel.CornerLabel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.database.ActivityService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.Utils;

/**
 * Created by Administrator on 2016/5/31 0031.
 *
 *
 */
public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private Context mContext;
    //activity list
    private List<Place> mList = new ArrayList<>();
    private List<Integer> mHeights;
    private OnItemActionListener mOnItemActionListener;
    private int aID;
    private Filter filter;

    public PlaceAdapter(Context context, int aID, Filter filter){
        this.mContext = context;
        this.aID = aID;
        this.filter = filter;
        initData();
    }

    private void initData(){
        PlaceService ps = new PlaceService(this.mContext);
        if(filter == null) {
            mList = ps.getPlaceListByActivity(aID);
        }else{
            mList = ps.searchPlace(aID,filter);
        }
        getRandomHeight(mList);
    }

    public void getRandomHeight(List<Place> mList){
        mHeights = new ArrayList<>();
        for(int i=0; i < mList.size();i++){
            //随机的获取一个范围为800-1000直接的高度
            mHeights.add((int)(800+ Math.random()*200));
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

        Place a = mList.get(position);

        //show picture from database
        holder.title.setText(a.getName());
        if(a.getRegion()!=null && !a.getRegion().isEmpty()) {
            holder.region.setVisibility(View.VISIBLE);
            holder.region.setText(a.getRegion());
        }
        //set pix showing for activity
        //holder.mImageView.setImageResource(R.drawable.cohete_flat);

        if(BitmapCache.getInstance().getBitmapByPath("p"+a.getID())==null) {
            Bitmap bmap = Utils.zoomBitmap(a.getPix(), 300, 300);
            BitmapCache.getInstance().addBitmapToCache("p" + a.getID(), bmap);
        }

        //Bitmap bmap = Utils.fitBitmap(a.getPix(), 100);
        holder.mImageView.getHeight();
        holder.mImageView.setImageBitmap(BitmapCache.getInstance().getBitmapByPath("p"+a.getID()));

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
        void onItemClickListener(View view, int position);
        boolean onItemLongClickListener(View view, int position);
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener){
        this.mOnItemActionListener = onItemActionListener;
    }

    public int getActivityID(){
        return aID;
    }

    public int getPlaceID(int position){
        return mList.get(position).getID();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.txt_title)
        TextView title;
        @Bind(R.id.imageView)
        ImageView mImageView;
        @Bind(R.id.label_region)
        TextView region;

        public ViewHolder(View view){
            //需要设置super
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<Place> getList() {
        return mList;
    }
}

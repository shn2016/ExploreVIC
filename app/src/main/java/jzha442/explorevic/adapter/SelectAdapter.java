package jzha442.explorevic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.Utils;

/**
 * Created by Administrator on 2016/5/31 0031.
 *
 *
 */
public class SelectAdapter extends RecyclerView.Adapter<SelectAdapter.ViewHolder> {

    private Context mContext;
    private OnItemActionListener mOnItemActionListener;
    private List<String> mList = new ArrayList<String>();
    private ArrayList<String> choice = new ArrayList<String>();

    public SelectAdapter(Context context, ArrayList<String> list){
        mList = list;
        this.mContext = context;
    }

    public SelectAdapter(Context context, ArrayList<String> list, ArrayList<String> selection){
        mList = list;
        choice = selection;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.facility_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String f = mList.get(position);

        holder.card.setRadius(20);
        holder.card.setTag(0);
        holder.card.setCardBackgroundColor(Color.parseColor("#7dffffff"));
        if(choice.size()>0){
            if(choice.contains(f)){
                holder.card.setTag(1);
                holder.card.setCardBackgroundColor(Color.parseColor("#C877CFF2"));
            }
        }

        holder.label.setText(f);

        //part of item click function to get position
        int iconID = Utils.getImageResourceId(mContext, Utils.getFaciIconName(f));

        if(iconID == 0) {
            iconID = android.R.drawable.btn_star_big_on;
        }

        if(BitmapCache.getInstance().getBitmapByPath("icon"+Utils.getFaciIconName(f))==null) {
            Bitmap bitmap = BitmapFactory. decodeResource (mContext.getResources(), iconID);
            Bitmap bmap = Utils.zoomBitmap(bitmap, 100, 100);
            BitmapCache.getInstance().addBitmapToCache("icon"+Utils.getFaciIconName(f), bmap);
        }
        holder.image.setImageBitmap(BitmapCache.getInstance().getBitmapByPath("icon"+Utils.getFaciIconName(f)));
        holder.image.setTag(f);

        if (mOnItemActionListener == null)
            throw new NullPointerException("onItemClickListener refer to a null position");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据
                mOnItemActionListener.onItemClickListener(v,holder.getPosition());

                int tag = (int) holder.card.getTag();
                String name = (String) holder.image.getTag();
                if(tag == 0){
                    holder.card.setTag(1);
                    holder.card.setCardBackgroundColor(Color.parseColor("#C877CFF2"));
                    choice.add(name);
                }else {
                    holder.card.setTag(0);
                    holder.card.setCardBackgroundColor(Color.parseColor("#7dffffff"));
                    if(choice.contains(name)){
                        choice.remove(name);
                    }
                }
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

    //click action

    public interface OnItemActionListener{
        void onItemClickListener(View view, int position);
        boolean onItemLongClickListener(View view,int position);
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener){
        this.mOnItemActionListener = onItemActionListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.text_facility_label)
        TextView label;
        @Bind(R.id.image_facility)
        ImageView image;
        @Bind(R.id.icon_card)
        CardView card;

        public ViewHolder(View view){
            //需要设置super
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<String> getList() {
        return mList;
    }

    public ArrayList<String> getChoice(){
        return choice;
    }
}

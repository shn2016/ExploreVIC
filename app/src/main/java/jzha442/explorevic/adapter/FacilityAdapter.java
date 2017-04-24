package jzha442.explorevic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.SearchCondition;
import jzha442.explorevic.screen.MainActivity;
import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.Utils;

/**
 * Created by Administrator on 2016/5/31 0031.
 *
 *
 */
public class FacilityAdapter extends RecyclerView.Adapter<FacilityAdapter.ViewHolder> {

    private Context mContext;
    private List<String> mList = new ArrayList<String>();

    public FacilityAdapter(Context context, ArrayList<String> array){
        mList = array;
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

        //show picture from database
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
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    //click action

    /*
    public interface OnItemActionListener{
        void onItemClickListener(View view, int position);
        boolean onItemLongClickListener(View view,int position);
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener){
        this.mOnItemActionListener = onItemActionListener;
    }
    */

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.text_facility_label)
        TextView label;
        @Bind(R.id.image_facility)
        ImageView image;

        public ViewHolder(View view){
            //需要设置super
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<String> getList() {
        return mList;
    }
}

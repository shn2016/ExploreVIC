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

import com.littlechoc.cornerlabel.CornerLabel;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jzha442.explorevic.R;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.database.PlaceWeatherService;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.Place;
import jzha442.explorevic.model.Weather;
import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.Utils;

import static android.R.attr.filter;
import static android.R.attr.icon;

/**
 * Created by Administrator on 2016/5/31 0031.
 *
 *
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {

    private Context mContext;
    private int pID;
    private boolean isPlace;
    private boolean isCurrent;
    private List<Weather> mList = new ArrayList<>();
    private OnItemActionListener mOnItemActionListener;

    public WeatherAdapter(Context context, int pID, boolean isPlace){
        this.mContext = context;
        this.pID = pID;
        this.isPlace = isPlace;
        initData();
    }

    public void initData(){
        PlaceWeatherService pws = new PlaceWeatherService(isPlace);
        //mList = pws.getWeatherList(pID);
        mList = pws.getWeatherList(pID).subList(0, 1);
        isCurrent=true;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.weather_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Weather w = mList.get(position);

        holder.temp.setText(w.getTemp());
        holder.temp.setX(20);
        //holder.temp.setPadding(10,0,0,0);
        holder.wind.setText(w.getWindSpeed());
        if(isCurrent) {
            holder.label.setVisibility(View.GONE);
        }else{
            holder.label.setLabel(w.getDate());
            holder.label.setVisibility(View.VISIBLE);
        }
        int iconID = Utils.getImageResourceId(mContext,w.getIcon());
        if(iconID==0){
            iconID = R.drawable.clear_day;//default icon
        }
        if(BitmapCache.getInstance().getBitmapByPath("weather"+w.getIcon())==null) {

            Bitmap bitmap = BitmapFactory. decodeResource (mContext.getResources(), iconID);
            BitmapCache.getInstance().addBitmapToCache("weather"+w.getIcon(), bitmap);
        }
        holder.weatherIcon.setImageBitmap(BitmapCache.getInstance().getBitmapByPath("weather"+w.getIcon()));

        //part of item click function to get position
        if (mOnItemActionListener == null)
            throw new NullPointerException("onItemClickListener refer to a null position");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据
                mOnItemActionListener.onItemClickListener(v,holder.getPosition());
                PlaceWeatherService pws = new PlaceWeatherService(isPlace);
                if (getItemCount() != 1) {
                    mList = pws.getWeatherList(pID).subList(0, 1);
                    isCurrent=true;
                } else {
                    mList = pws.getWeatherList(pID);
                    isCurrent=false;
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

    //click and long click action

    public interface OnItemActionListener{
        void onItemClickListener(View view, int position);
        boolean onItemLongClickListener(View view, int position);
    }

    public void setOnItemActionListener(OnItemActionListener onItemActionListener){
        this.mOnItemActionListener = onItemActionListener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.txt_wind)
        TextView wind;

        @Bind(R.id.image_weather)
        ImageView weatherIcon;

        @Bind(R.id.txt_temperature)
        TextView temp;

        @Bind(R.id.label_weekday)
        CornerLabel label;

        public ViewHolder(View view){
            //需要设置super
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<Weather> getList() {
        return mList;
    }

    public boolean isCurrent(){
        return isCurrent;
    }
}

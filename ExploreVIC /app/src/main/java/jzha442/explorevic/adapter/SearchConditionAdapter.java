package jzha442.explorevic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
import jzha442.explorevic.database.ActivityService;
import jzha442.explorevic.model.Activity;
import jzha442.explorevic.model.Filter;
import jzha442.explorevic.model.SearchCondition;
import jzha442.explorevic.screen.MainActivity;
import jzha442.explorevic.utility.Utils;

/**
 * Created by Administrator on 2016/5/31 0031.
 *
 *
 */
public class SearchConditionAdapter extends RecyclerView.Adapter<SearchConditionAdapter.ViewHolder> {

    private Context mContext;
    private Filter filter;
    private List<SearchCondition> mList = new ArrayList<SearchCondition>();

    public SearchConditionAdapter(Context context, Filter filter){
        this.mContext = context;
        if (filter!=null) {
            initData(filter);
        }
    }

    public void initData(Filter filter){
        this.filter = filter;
        mList=filter.getSearchCondition();
    }

    public void changeFilter(Filter filter){
        if(filter.isFilterClean()){
            ((MainActivity) mContext).clearSearch();
        }else {
            initData(filter);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.search_condition_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        SearchCondition sc = mList.get(position);

        //show picture from database
        holder.value.setText(sc.getValue());

        //part of item click function to get position
        holder.close_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注意这里必须使用viewHolder.getPosition()而不能用i，因为为了保证动画，没有使用NotifyDatasetChanged更新位置数据

                //mOnItemActionListener.onItemClickListener(v,holder.getPosition());
                ((MainActivity) mContext).updateSearchCondition(getFilterID(holder.getPosition()));
            }
        });
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



    public int getFilterID(int position){
        return mList.get(position).getFilterID();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.txt_search_condition)
        TextView value;
        @Bind(R.id.search_close_icon)
        ImageView close_icon;

        public ViewHolder(View view){
            //需要设置super
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public List<SearchCondition> getList() {
        return mList;
    }
}

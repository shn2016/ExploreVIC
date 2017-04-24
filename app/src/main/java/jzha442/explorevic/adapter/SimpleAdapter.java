package jzha442.explorevic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import de.halfbit.pinnedsection.PinnedSectionListView;
import jzha442.explorevic.R;
import jzha442.explorevic.database.ActivityPlaceService;
import jzha442.explorevic.database.FavouritePlaceService;
import jzha442.explorevic.database.PlaceService;
import jzha442.explorevic.model.ActivityPlace;
import jzha442.explorevic.model.FavouriteItem;
import jzha442.explorevic.model.FavouritePlace;
import jzha442.explorevic.utility.BitmapCache;
import jzha442.explorevic.utility.Utils;

/**
 * Created by Jiao on 14/04/2017.
 */

public class SimpleAdapter extends ArrayAdapter<FavouriteItem> implements PinnedSectionListView.PinnedSectionListAdapter {
    private static final int[] COLORS = new int[] {
            R.color.sky_blue_light
            };
            /*
            R.color.green_light, R.color.orange_light,
            R.color.red_light, R.color.sky_blue_light,
            R.color.purple_light, R.color.yellow_light
            */
    private Context context;

    public SimpleAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
        this.context = context;
        generateDataset(false);
    }

    public void generateDataset(boolean clear) {

        if (clear) clear();

        FavouritePlaceService fps = new FavouritePlaceService(context);
        ArrayList<String> regions = fps.getRegionList();
        final int sectionsNumber = regions.size();
        if(sectionsNumber==0){
            Utils.showMessage(context,"Your favourite list is empty currently!");
        }
        prepareSections(sectionsNumber);

        //set section title
        int sectionPosition = 0, listPosition = 0;
        for (int i=0; i<sectionsNumber; i++) {
            FavouriteItem section = new FavouriteItem(FavouriteItem.SECTION, regions.get(i));
            section.sectionPosition = sectionPosition;
            section.listPosition = listPosition++;
            onSectionAdded(section, sectionPosition);
            add(section);

            //set section content
            ArrayList<FavouritePlace> fpList = fps.getFavouritePlacesByRegion(regions.get(i));
            ArrayList<FavouritePlace> customFPList = fps.getCustomFavouriteByRegion(regions.get(i));
            fpList.addAll(customFPList);
            final int itemsNumber = fpList.size();
            for (int j=0;j<itemsNumber;j++) {
                FavouritePlace fp = fpList.get(j);
                FavouriteItem item = new FavouriteItem(FavouriteItem.ITEM, fp.getName(), fp.getID());
                item.sectionPosition = sectionPosition;
                item.listPosition = listPosition++;
                add(item);
            }

            sectionPosition++;
        }
    }

    protected void prepareSections(int sectionsNumber) { }
    protected void onSectionAdded(FavouriteItem section, int sectionPosition) { }

    //section title
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);

        FavouriteItem item = getItem(position);
        view.setTag("" + item.getFavouriteID());

        FavouritePlaceService fps = new FavouritePlaceService(context);
        FavouritePlace fp = fps.getPlaceById(item.getFavouriteID());

        if (item.type == FavouriteItem.SECTION) {
            view.setTextColor(Color.DKGRAY);
            //view.setOnClickListener(PinnedSectionListActivity.this);
            view.setBackgroundColor(parent.getResources().getColor(COLORS[item.sectionPosition % COLORS.length]));

        }else {
            //set content background
            if(fp.getAp_id()>0) {
                ActivityPlaceService aps = new ActivityPlaceService(context);
                ActivityPlace ap = aps.getActivityPlaceById(fp.getAp_id());
                if (BitmapCache.getInstance().getBitmapByPath("ap" + ap.getID()) == null) {
                    Bitmap bmap;
                    if (ap.getPix().getHeight() > ap.getPix().getWidth()) {
                        bmap = Utils.zoomBitmap(ap.getPix(), 300, 400);
                    } else {
                        bmap = Utils.zoomBitmap(ap.getPix(), 400, 300);
                    }
                    BitmapCache.getInstance().addBitmapToCache("ap" + ap.getID(), bmap);
                }
                Drawable d = new BitmapDrawable(context.getResources(), BitmapCache.getInstance().getBitmapByPath("ap" + ap.getID()));
                view.setBackground(d);
            }else{
                if (BitmapCache.getInstance().getBitmapByPath("custom" + fp.getID()) == null) {
                    Bitmap bmap;
                    if (fp.getPix().getHeight() > fp.getPix().getWidth()) {
                        bmap = Utils.zoomBitmap(fp.getPix(), 300, 400);
                    } else {
                        bmap = Utils.zoomBitmap(fp.getPix(), 400, 300);
                    }
                    BitmapCache.getInstance().addBitmapToCache("custom" + fp.getID(), bmap);
                }
                Drawable d = new BitmapDrawable(context.getResources(), BitmapCache.getInstance().getBitmapByPath("custom" + fp.getID()));
                view.setBackground(d);
            }
            view.setHeight(300);
            view.setGravity(Gravity.BOTTOM);
            view.setTextColor(Color.WHITE);
        }
        return view;
    }

    @Override public int getViewTypeCount() {
        return 2;
    }

    @Override public int getItemViewType(int position) {
        return getItem(position).type;
    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return viewType == FavouriteItem.SECTION;
    }
}

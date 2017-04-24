package jzha442.explorevic.adapter;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.util.Locale;

import de.halfbit.pinnedsection.PinnedSectionListView;
import jzha442.explorevic.R;
import jzha442.explorevic.model.FavouriteItem;

/**
 * Created by Jiao on 14/04/2017.
 */

public class FavouriteAdapter extends SimpleAdapter implements SectionIndexer {

    private FavouriteItem[] sections;

    public FavouriteAdapter(Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    @Override protected void prepareSections(int sectionsNumber) {
        sections = new FavouriteItem[sectionsNumber];
    }

    @Override protected void onSectionAdded(FavouriteItem section, int sectionPosition) {
        sections[sectionPosition] = section;
    }

    @Override public FavouriteItem[] getSections() {
        return sections;
    }

    @Override public int getPositionForSection(int section) {
        if (section >= sections.length) {
            section = sections.length - 1;
        }
        return sections[section].listPosition;
    }

    @Override public int getSectionForPosition(int position) {
        if (position >= getCount()) {
            position = getCount() - 1;
        }
        return getItem(position).sectionPosition;
    }

}

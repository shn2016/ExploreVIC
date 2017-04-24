package jzha442.explorevic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seatgeek.placesautocomplete.PlacesApi;
import com.seatgeek.placesautocomplete.adapter.AbstractPlacesAutocompleteAdapter;
import com.seatgeek.placesautocomplete.history.AutocompleteHistoryManager;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.Place;

import jzha442.explorevic.R;

/**
 * Created by Jiao on 22/04/2017.
 */

public class PlacesAutocompleteAdapter extends AbstractPlacesAutocompleteAdapter {

    public PlacesAutocompleteAdapter(final Context context, final PlacesApi api, final AutocompleteResultType resultType, final AutocompleteHistoryManager history) {
        super(context, api, resultType, history);
    }

    @Override
    protected View newView(final ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.places_autocomplete_item, parent, false);
    }

    @Override
    protected void bindView(final View view, final Place item) {
        ((TextView) view).setText(item.description);
    }
}

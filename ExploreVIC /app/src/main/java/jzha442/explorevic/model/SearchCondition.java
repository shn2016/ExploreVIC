package jzha442.explorevic.model;

import static android.R.attr.id;

/**
 * Created by Jiao on 01/04/2017.
 */

public class SearchCondition {

    private int filterID;
    private String value;

    public SearchCondition() {
    }

    public SearchCondition(int filterID, String value) {
        this.filterID = filterID;
        this.value = value;
    }

    public int getFilterID() {
        return filterID;
    }

    public void setFilterID(int filterID) {
        this.filterID = filterID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}

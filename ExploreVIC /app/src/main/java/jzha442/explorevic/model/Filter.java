package jzha442.explorevic.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

/**
 * Created by Jiao on 31/03/2017.
 */

public class Filter implements Serializable {
    private String[] typeFilters= {"Air", "Land", "Sea"};
    private String word;
    private static final int wordID = 0;
    private boolean hasAir;
    private static final int airID = 1;
    private boolean hasLand;
    private static final int landID = 2;
    private boolean hasSea;
    private static final int seaID = 3;
    private String region;
    private static final int regionID = 4;
    private String weather;
    private String distance;

    public Filter() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word.trim();
    }

    public boolean isHasAir() {
        return hasAir;
    }

    public void setHasAir(boolean hasAir) {
        this.hasAir = hasAir;
    }

    public boolean isHasLand() {
        return hasLand;
    }

    public void setHasLand(boolean hasLand) {
        this.hasLand = hasLand;
    }

    public boolean isHasSea() {
        return hasSea;
    }

    public void setHasSea(boolean hasSea) {
        this.hasSea = hasSea;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region.trim();
    }

    //change filter when click search condition
    public void deleteCondition(int filterID){
        switch (filterID){
            case wordID:
                word="";
                break;
            case airID:
                hasAir = false;
                break;
            case landID:
                hasLand = false;
                break;
            case seaID:
                hasSea = false;
                break;
            case regionID:
                region="";
                break;
        }
    }

    //check if search conditions has been cleared
    public boolean isFilterClean(){
        if((word == null || word.isEmpty())
                && !isHasAir()
                && !isHasLand()
                && !isHasSea()
                && (region==null || region.isEmpty())){
            return true;
        }
        return false;
    }

    //get search conditions list
    public List<SearchCondition> getSearchCondition(){
        ArrayList<SearchCondition> sc = new ArrayList<SearchCondition>();
        if(word != null && !word.isEmpty()){
            sc.add(new SearchCondition(wordID, word));
        }
        if(hasAir){
            sc.add(new SearchCondition(airID,typeFilters[0]));
        }
        if(hasLand){
            sc.add(new SearchCondition(landID,typeFilters[1]));
        }
        if(hasSea){
            sc.add(new SearchCondition(seaID,typeFilters[2]));
        }
        if(region != null && !region.isEmpty()){
            sc.add(new SearchCondition(regionID, region));
        }
        return sc;
    }

}

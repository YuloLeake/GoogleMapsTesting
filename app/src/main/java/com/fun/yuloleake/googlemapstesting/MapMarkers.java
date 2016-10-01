package com.fun.yuloleake.googlemapstesting;

import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;

/**
 * Created by Yulo Leake on 9/21/2016.
 *
 * Wrapper class to contain map Markers in some sort of list or dictionary
 */
public class MapMarkers {

    private HashMap<Marker, MarkerRecord> markers;

    public MapMarkers(){
        markers = new HashMap<>();
    }

    public void add(MarkerRecord record){
        markers.put(record.getMarker(), record);
    }

    public void remove(MarkerRecord record){
        markers.remove(record.getMarker());
    }

    public MarkerRecord getRecordWithMarker(Marker marker){
        return markers.get(marker);
    }

    public int size(){
        return markers.size();
    }

}

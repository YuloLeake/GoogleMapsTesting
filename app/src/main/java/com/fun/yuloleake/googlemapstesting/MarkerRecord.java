package com.fun.yuloleake.googlemapstesting;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by Yulo Leake on 9/21/2016.
 *
 * Record class to contain info on a map Marker
 */
public class MarkerRecord{

    private Marker marker;
    private Circle circle;
    private String name;

    public MarkerRecord(Marker marker, Circle circle, String name){
        this.marker = marker;
        this.circle = circle;
        this.name = name;
    }

    public Marker getMarker(){
        return marker;
    }

    public Circle getCircle(){
        return circle;
    }

    public String getName(){
        return name;
    }

}

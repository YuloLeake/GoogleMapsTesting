package com.fun.yuloleake.googlemapstesting;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.BottomSheetBehavior;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private MapMarkers markers;

    private BottomSheetBehavior mBottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        this.markers = new MapMarkers();

        // Bottom sheet initialization
        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set origin to Seattle
        LatLng seattle = new LatLng(47.6, -122.3);
        centerCameraAndZoom(seattle, 10.0f);


        // Add marker where user taps
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(final LatLng latLng) {
                buildAddMarkerDialog(latLng).show();
            }
        });
    }

    private AlertDialog.Builder buildAddMarkerDialog(final LatLng latLng){
        // Inflate and get GUI element of the dialog
        View v = getLayoutInflater().inflate(R.layout.dialog_add_marker, null);
        final EditText markerName = (EditText)v.findViewById(R.id.marker_name);
        final EditText markerRadius = (EditText)v.findViewById(R.id.marker_radius);

        // Build AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder( MapsActivity.this );
        builder.setView(v)
                .setTitle("Add a new Marker on the map")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = markerName.getText().toString();
                        int radius = Integer.parseInt(markerRadius.getText().toString());
                        addMarker(mMap, latLng, name, radius);
                    }
                })
                .setNegativeButton("Cancel", null);

        return builder;
    }


    private void addMarker(GoogleMap map, LatLng point, String name, int mRadius){
        int radiusColor = getRandomColor();
        BitmapDescriptor markerColor = getHueGivenColor(radiusColor);

        Circle circle = addCircleToMap(map, point, mRadius, radiusColor);
        Marker marker = addMarkerToMap(map, point, name, markerColor);

        MarkerRecord record = new MarkerRecord(marker, circle, name);
        markers.add(record);

        updateBottomSheet(record);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                updateBottomSheet(markers.getRecordWithMarker(marker));

                return false;
            }
        });

    }

    private int getRandomColor(){
        int color = 0;
        int alphaValue = 128; // 255/2

        int num = (int)(Math.random() * 5);
        switch (num){
            case 0:
                color = Color.argb(alphaValue, 255, 0, 0);  // Red
                break;
            case 1:
                color = Color.argb(alphaValue, 255, 0, 255);    // Magenta
                break;
            case 2:
                color = Color.argb(alphaValue, 0, 0, 255);  // Blue
                break;
            case 3:
                color = Color.argb(alphaValue, 255, 255, 0);    // Yellow
                break;
            case 4:
                color = Color.argb(alphaValue, 0, 255, 0); // Green
                break;
        }

        return color;
    }

    private BitmapDescriptor getHueGivenColor(int color){
        BitmapDescriptor hue = null;
        switch (color){
            case Color.RED:
                hue = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
                break;
            case Color.MAGENTA:
                hue = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
                break;
            case Color.BLUE:
                hue = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
                break;
            case Color.YELLOW:
                hue = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);
                break;
            case Color.GREEN:
                hue = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
                break;
        }

        return hue;
    }

    private Circle addCircleToMap(GoogleMap map, LatLng point, int mRadius, int color){
        return map.addCircle(new CircleOptions()
                .center(point)
                .radius(mRadius)
                .strokeColor(color)
                .fillColor(color));
    }

    private Marker addMarkerToMap(GoogleMap map, LatLng point, String name, BitmapDescriptor color){
        return map.addMarker(new MarkerOptions()
                .position(point)
                .icon(color)
                .title(name));
    }

    private void removeMarkerOnMap(MarkerRecord record){
        record.getCircle().remove();
        record.getMarker().remove();
        markers.remove(record);
    }

    private void hideBottomSheet(){
        mBottomSheetBehavior.setPeekHeight(0);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private void updateBottomSheet(final MarkerRecord record){
        mBottomSheetBehavior.setPeekHeight(150);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        updateBottomSheetTextViews(record);
        updateBottomSheetButtons(record);
    }
    private void updateBottomSheetTextViews(final MarkerRecord record){
        TextView textViewName = (TextView)findViewById(R.id.bs_marker_name);
        TextView textViewLat = (TextView)findViewById(R.id.bs_marker_lat);
        TextView textViewLong = (TextView)findViewById(R.id.bs_marker_long);
        TextView textViewRadius = (TextView)findViewById(R.id.bs_marker_radius);

        textViewName.setText(record.getName());
        LatLng latLng = record.getMarker().getPosition();
        String lat = Math.abs(latLng.latitude) + " ";
        if(latLng.latitude >= 0){
            lat += "N";
        }else{
            lat += "S";
        }
        textViewLat.setText(lat);

        String lng = Math.abs(latLng.longitude) + " ";
        if(latLng.longitude >= 0){
            lng += "E";
        }else{
            lng += "W";
        }
        textViewLong.setText(lng);

        textViewRadius.setText((int)record.getCircle().getRadius() + " meters");
    }
    private void updateBottomSheetButtons(final MarkerRecord record){
        Button btnRemove = (Button)findViewById(R.id.bs_btn_remove);
        Button btnNotify = (Button)findViewById(R.id.bs_btn_notify);

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeMarkerOnMap(record);
                hideBottomSheet();
            }
        });

        btnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createPushNotification(record);
            }
        });
    }

    private void createPushNotification(MarkerRecord record){
        AlarmReceiver ar = new AlarmReceiver();

        ar.setAlarm(this, record);

        System.out.println("Notification sent");
    }

    public void centerCamera(LatLng latLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }

    public void centerCameraAndZoom(LatLng latLng, float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
}

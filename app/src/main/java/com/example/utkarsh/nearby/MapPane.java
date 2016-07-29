package com.example.utkarsh.nearby;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

/**
 * Created by utkarsh on 13/7/16.
 */
public class MapPane extends Activity implements OnMapReadyCallback {
    double x = 28.56269;
    double y = 77.244646;
    private List<Place> list;
    private MapFragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_activity);

         mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        LatLng s = new LatLng(x, y);

        map.setMyLocationEnabled(true);
        if (map != null) {


            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                @Override
                public void onMyLocationChange(Location arg0) {
                    // TODO Auto-generated method stub

                    map.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                 //   map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(arg0.getLatitude(), arg0.getLongitude()), 17));
                    CameraPosition myPosition = new CameraPosition.Builder()
                            .target(new LatLng(arg0.getLatitude(), arg0.getLongitude())).zoom(17).bearing(90).tilt(30).build();
                    map.animateCamera(
                            CameraUpdateFactory.newCameraPosition(myPosition));
                }
            });

        }
        list = MyActivity.placelist;
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {


                map.addMarker(new MarkerOptions()
                        .title(list.get(i).getname())
                        .snippet("FAmous MOMO")
                        .position(list.get(i).getlatlag()));
            }
        }
    }
}
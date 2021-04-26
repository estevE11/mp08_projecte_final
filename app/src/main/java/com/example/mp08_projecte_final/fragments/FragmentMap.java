package com.example.mp08_projecte_final.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.mp08_projecte_final.R;
import com.example.mp08_projecte_final.db.DBDatasource;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class FragmentMap extends Fragment {

    private GoogleMap map;

    private DBDatasource db;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.frag_map, container, false);

        this.db = new DBDatasource(getContext());

        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        smf.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                load();
            }
        });
        
        return view;
    }

    public void load() {
        Cursor machines = this.db.getMachines();
        machines.moveToFirst();


        LatLngBounds.Builder bld = new LatLngBounds.Builder();

        for(int i = 0; i < machines.getCount(); i++) {

            String address = machines.getString(machines.getColumnIndex("address"));
            String city = machines.getString(machines.getColumnIndex("city"));
            LatLng pos = this.getLocationFromAddress(address + ", " + city);

            String serial = machines.getString(machines.getColumnIndex("serial_number"));

            int id_type = machines.getInt(machines.getColumnIndex("id_type"));
            Cursor type = this.db.getType(id_type);
            type.moveToFirst();
            int color = type.getInt(type.getColumnIndex("color"));


            this.map.addMarker(new MarkerOptions().position(pos).title(serial).icon(this.getMarkerIcon(color)));
            bld.include(pos);

            machines.moveToNext();
        }
        LatLngBounds bounds = bld.build();
        this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 250));
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this.getContext());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return p1;
    }

    private BitmapDescriptor getMarkerIcon(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
}

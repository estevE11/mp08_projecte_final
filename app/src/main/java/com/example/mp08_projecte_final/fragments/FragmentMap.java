package com.example.mp08_projecte_final.fragments;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mp08_projecte_final.R;
import com.example.mp08_projecte_final.db.DBDatasource;
import com.example.mp08_projecte_final.weather.WeatherData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class FragmentMap extends Fragment {

    private GoogleMap map;

    private DBDatasource db;

    private TextView txt_temp, txt_loc;
    private ImageView img_weather;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = (ViewGroup) inflater.inflate(R.layout.frag_map, container, false);
        this.db = new DBDatasource(getContext());

        this.txt_temp = (TextView)view.findViewById(R.id.txt_temp);
        this.txt_loc = (TextView)view.findViewById(R.id.txt_loc);
        this.img_weather = (ImageView)view.findViewById(R.id.img_weather);

        Bundle bundle = this.getArguments();

        SupportMapFragment smf = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        smf.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                loader(bundle);
                map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        String location = (String)(marker.getTag());
                        loadWeather(location);
                        return false;
                    }
                });
            }
        });

        return view;
    }

    public void loader(Bundle bundle) {
        boolean zone = bundle.getBoolean("zone");
        boolean machine = bundle.getBoolean("machine");
        Log.d("asdf", zone + ", " + machine);
        if(zone) {
            int id = bundle.getInt("zone_id");
            loadZone(id);
        } else if(machine) {
            int id = bundle.getInt("machine_id");
            loadFocus(id);
        } else {
            loadFocus(null);
        }
    }

    public void loadFocus(Integer id) {
        Cursor machines = this.db.getMachines();
        this.load(id, machines);
    }

    public void loadZone(int zone_id) {
        Cursor machines = this.db.getMachinesByZone(zone_id);
        this.load(null, machines);
    }

    public void load(Integer id, Cursor machines) {
        boolean focus = id != null;
        int focus_id = 0;
        if(focus) focus_id = id;

        Log.d("asdf", "id: " + focus_id);
        machines.moveToFirst();

        LatLngBounds.Builder bld = new LatLngBounds.Builder();

        for(int i = 0; i < machines.getCount(); i++) {
            int current_id = machines.getInt(machines.getColumnIndex("_id"));

            String address = machines.getString(machines.getColumnIndex("address"));
            String city = machines.getString(machines.getColumnIndex("city"));
            LatLng pos = this.getLocationFromAddress(address + ", " + city);

            String serial = machines.getString(machines.getColumnIndex("serial_number"));
            String name = machines.getString(machines.getColumnIndex("name"));

            int id_type = machines.getInt(machines.getColumnIndex("id_type"));
            Cursor type = this.db.getType(id_type);
            type.moveToFirst();
            int color = type.getInt(type.getColumnIndex("color"));

            Marker current_marker = this.map.addMarker(new MarkerOptions().position(pos).title(name + ", nº" + serial).icon(this.getMarkerIcon(color)));
            current_marker.setTag(city);

            if(!focus) {
                bld.include(pos);
            } else {
                if(focus_id == current_id) {
                    bld.include(pos);
                }
            }

            machines.moveToNext();
        }
        LatLngBounds bounds = bld.build();
        this.map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, focus ? 500 : 250));
    }

    public void loadWeather(String location) {
        WeatherData.fromLocation(location, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode != 200) return;
                JSONObject data = null;
                String str = new String(responseBody);

                try {
                    data = new JSONObject(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    fillWeatherData(new WeatherData(data));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    public void fillWeatherData(WeatherData weather_data) {
        String loc = weather_data.getLocation_name();
        String temp = String.valueOf((int)weather_data.getTemp());
        this.txt_temp.setText(temp + "ºC");
        this.txt_loc.setText(loc);
        Picasso.get().load(weather_data.getIconUrl()).error(R.drawable.ic_baseline_error_outline_24).into(this.img_weather);
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

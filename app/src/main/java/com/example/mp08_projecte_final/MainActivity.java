package com.example.mp08_projecte_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mp08_projecte_final.db.DBDatasource;
import com.example.mp08_projecte_final.fragments.FragmentMachineTypes;
import com.example.mp08_projecte_final.fragments.FragmentMachines;
import com.example.mp08_projecte_final.fragments.FragmentMap;
import com.example.mp08_projecte_final.fragments.FragmentZones;
import com.example.mp08_projecte_final.fragments.FragmentsSlideAdapter;
import com.example.mp08_projecte_final.managers.MachineManager;
import com.example.mp08_projecte_final.managers.TypeManager;
import com.example.mp08_projecte_final.managers.ZoneManager;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBDatasource db;

    private int defaultTab = 0;

    private TabLayout tabLayout;
    private ViewPager2 pager;
    private FragmentsSlideAdapter pagerAdapter;

    LinkedList<Fragment> fragments;
    FragmentMachines frag_machines;
    FragmentMachineTypes frag_types;
    FragmentZones frag_zones;
    FragmentMap frag_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = new DBDatasource(this);

        Bundle b = this.getIntent().getExtras();

        this.fragments = new LinkedList<Fragment>();

        this.frag_machines = new FragmentMachines();
        this.frag_types = new FragmentMachineTypes();
        this.frag_zones = new FragmentZones();
        this.frag_map = new FragmentMap();

        fragments.add(this.frag_machines);
        fragments.add(this.frag_types);
        fragments.add(this.frag_zones);
        fragments.add(this.frag_map);

        Bundle bundle = new Bundle();
        bundle.putBoolean("machine", false);
        bundle.putBoolean("zone", false);

        this.frag_map.setArguments(bundle);

        this.tabLayout = findViewById(R.id.tabLayout);
        this.pager = findViewById(R.id.pager);

        this.pagerAdapter = new FragmentsSlideAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        this.pager.setAdapter(this.pagerAdapter);

        try {
            this.defaultTab = b.getInt("tab");
            this.pager.setCurrentItem(this.defaultTab);
        } catch (Exception e) {

        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
                Log.d("asdf", "Selected: " + tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        this.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                tabLayout.setScrollPosition(position, positionOffset, false);
                //tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    public void openMapByZone(int zone_id) {
        this.refreshFragmentMap();
        Bundle bundle = new Bundle();
        bundle.putInt("zone_id", zone_id);
        bundle.putBoolean("machine", false);
        bundle.putBoolean("zone", true);
        this.frag_map.setArguments(bundle);
        this.pager.setCurrentItem(3);
    }

    public void openMapWithFocus(int machine_id) {
        this.refreshFragmentMap();
        Bundle bundle = new Bundle();
        bundle.putInt("machine_id", machine_id);
        bundle.putBoolean("zone", false);
        bundle.putBoolean("machine", true);
        this.frag_map.setArguments(bundle);
        this.pager.setCurrentItem(3);
    }

    private void refreshFragmentMap() {
        this.frag_map = new FragmentMap();
        Bundle bundle = new Bundle();
        bundle.putBoolean("zone", false);
        bundle.putBoolean("machine", false);
        this.frag_map.setArguments(bundle);
        this.fragments.set(3, this.frag_map);

        this.pagerAdapter = new FragmentsSlideAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        this.pager.setAdapter(this.pagerAdapter);
    }

    private void openCreateMachineActivity() {
        Intent intent = new Intent(getApplicationContext(), MachineManager.class);
        Bundle b = new Bundle();

        b.putBoolean("editMode", false);

        intent.putExtras(b);
        startActivity(intent);
    }
    private void openCreateZoneActivity() {
        Intent intent = new Intent(getApplicationContext(), ZoneManager.class);
        Bundle b = new Bundle();

        b.putBoolean("editMode", false);

        intent.putExtras(b);
        startActivity(intent);
    }
    private void openCreateTypeActivity() {
        Intent intent = new Intent(getApplicationContext(), TypeManager.class);
        Bundle b = new Bundle();

        b.putBoolean("editMode", false);

        intent.putExtras(b);

        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btn_add_machine:
                this.openCreateMachineActivity();
                return false;
            case R.id.btn_add_zones:
                this.openCreateZoneActivity();
                return false;
            case R.id.btn_add_type:
                this.openCreateTypeActivity();
                return false;
        }
        return super.onOptionsItemSelected(item);
    }
}
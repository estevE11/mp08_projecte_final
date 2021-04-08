package com.example.mp08_projecte_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.Menu;

import com.example.mp08_projecte_final.db.DBDatasource;
import com.example.mp08_projecte_final.fragments.FragmentMachineTypes;
import com.example.mp08_projecte_final.fragments.FragmentMachines;
import com.example.mp08_projecte_final.fragments.FragmentMap;
import com.example.mp08_projecte_final.fragments.FragmentZones;
import com.example.mp08_projecte_final.fragments.FragmentsSlideAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DBDatasource db;

    private TabLayout tabLayout;
    private ViewPager2 pager;
    private FragmentsSlideAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.db = new DBDatasource(this);

        LinkedList<Fragment> fragments = new LinkedList<Fragment>();
        fragments.add(new FragmentMachines());
        fragments.add(new FragmentMachineTypes());
        fragments.add(new FragmentZones());
        fragments.add(new FragmentMap());

        this.tabLayout = findViewById(R.id.tabLayout);
        this.pager = findViewById(R.id.pager);

        this.pagerAdapter = new FragmentsSlideAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        this.pager.setAdapter(this.pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
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
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }}
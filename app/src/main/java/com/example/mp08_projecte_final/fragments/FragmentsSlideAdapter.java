package com.example.mp08_projecte_final.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.LinkedList;

public class FragmentsSlideAdapter extends FragmentStateAdapter {
    private LinkedList<Fragment> fragments = new LinkedList<Fragment>();

    public FragmentsSlideAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, LinkedList<Fragment> fragments) {
        super(fragmentManager, lifecycle);
        this.fragments = fragments;
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return this.fragments.size();
    }
}

// FragmentsSlideAdapter
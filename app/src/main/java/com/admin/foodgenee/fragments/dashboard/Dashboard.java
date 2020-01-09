package com.admin.foodgenee.fragments.dashboard;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.admin.foodgenee.R;
import com.admin.foodgenee.fragments.dashboard.tabui.acceptedorders.AcceptedOrders;
import com.admin.foodgenee.fragments.dashboard.tabui.neworders.NewOrders;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    private FragmentActivity myContext;

    private TabStateAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tabs);
        adapter = new TabStateAdapter(getFragmentManager());
        adapter.addFragment(new NewOrders(), "New Orders");
        adapter.addFragment(new AcceptedOrders(), "Accepted Orders");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;


    }



}

package com.matthieu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentStacker extends Fragment {
    private static final String TAG = "FragmentStacker";
    Fragment top, bottom;

    public FragmentStacker() {
        Log.w(TAG, "Called empty constructor"); // should be initializing top and bottom from savedInstance
    }

    public FragmentStacker(Fragment f1, Fragment f2) {
        top = f1;
        bottom = f2;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true); // works with or without this... keep it out if not needed
        if (savedInstanceState != null) {
            top = getFragmentManager().getFragment(savedInstanceState, "top");
            bottom = getFragmentManager().getFragment(savedInstanceState, "bottom");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View res = inflater.inflate(R.layout.fragment_stack, container, false);
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment top_saved = fm.findFragmentByTag("t");
        Fragment bottom_saved = fm.findFragmentByTag("b");

        if (top_saved == null) {
            ft.add(R.id.top_fragment, top, "t");
            ft.add(R.id.bottom_fragment, bottom, "b");
            ft.commit();
        } else {
            ft.attach(top_saved);
            ft.attach(bottom_saved);
        }

        return res;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            getFragmentManager().putFragment(outState, "top", top);
        } catch (IllegalStateException e) {}
        try {
            getFragmentManager().putFragment(outState, "bottom", bottom);
        } catch (IllegalStateException e) {}
    }


}
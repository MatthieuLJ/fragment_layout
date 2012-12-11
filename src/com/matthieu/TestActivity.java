package com.matthieu;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class TestActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */

    private Fragment fragments[] = new TestFragment[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        setContentView(R.layout.main_large);

        if (savedInstanceState != null) {
            fragments[0] = getSupportFragmentManager().getFragment(savedInstanceState, "fragment0");
            fragments[1] = getSupportFragmentManager().getFragment(savedInstanceState, "fragment1");
            fragments[2] = getSupportFragmentManager().getFragment(savedInstanceState, "fragment2");

        } else {
            for (int i=0; i<3; i++)
                fragments[i] = new TestFragment(i);
        }

        ViewPager mPager = (ViewPager)findViewById(R.id.main_viewpager);
        if (mPager != null) {
            TestFragmentAdapter mAdapter = new TestFragmentAdapter(getSupportFragmentManager(), 3);
            mPager.setAdapter(mAdapter);
        }
        ViewPager mLargePager = (ViewPager)findViewById(R.id.main_viewpager_large);
        if (mLargePager != null) {
            TestFragmentAdapterLarge mAdapter = new TestFragmentAdapterLarge(this);
            mLargePager.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        try {
            getSupportFragmentManager().putFragment(outState, "fragment0", fragments[0]);
        } catch (IllegalStateException e) {}
        if (fragments[1] != null) {
            try {
                getSupportFragmentManager().putFragment(outState, "fragment1", fragments[1]);
            } catch (IllegalStateException e) {}
        }
        if (fragments[2] != null) {
            try {
                getSupportFragmentManager().putFragment(outState, "fragment2", fragments[2]);
            } catch (IllegalStateException e) {}
        }

    }

    class TestFragmentAdapter extends FragmentPagerAdapter {

        private int mCount;

        public TestFragmentAdapter(FragmentManager fm, int num) {
            super(fm);
            mCount = num;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return mCount;
        }
    }

    class TestFragmentAdapterLarge extends PagerAdapter {
        private FragmentActivity mContext;

        public TestFragmentAdapterLarge(FragmentActivity context) {
            mContext = context;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            FragmentManager fm = mContext.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (position == 0) {
                ft.remove((Fragment) container.findViewById(R.id.top_fragment).getTag(R.id.fragment_tag));
                ft.remove((Fragment) container.findViewById(R.id.bottom_fragment).getTag(R.id.fragment_tag));
            } else  { // (position == 1)
                ft.remove((Fragment) container.getTag(R.id.fragment_tag));
            }
            ft.commit();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View res;
            LayoutInflater inflater = mContext.getLayoutInflater();
            FragmentManager fm = mContext.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            if (position == 0) {
                res = inflater.inflate(R.layout.fragment_stack, container, false);
                Fragment top = fragments[0];
                ft.add(R.id.top_fragment, top);
                res.findViewById(R.id.top_fragment).setTag(R.id.fragment_tag, top);

                Fragment bottom = fragments[1];
                ft.add(R.id.bottom_fragment, bottom);
                res.findViewById(R.id.bottom_fragment).setTag(R.id.fragment_tag, bottom);

            } else  { // (position == 1)
                res = new FrameLayout(mContext);
                res.setId(R.id.second_page);
                Fragment second_page = fragments[2];
                ft.add(R.id.second_page, second_page);
                res.setTag(R.id.fragment_tag, second_page);
            }

            ft.commit();
            container.addView(res);
            return res;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view==o;
        }
    }

}

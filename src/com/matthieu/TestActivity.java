package com.matthieu;

import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TestActivity extends FragmentActivity {
    /**
     * Called when the activity is first created.
     */

    private Fragment fragments[] = new TestFragment[3];
    private Fragment stacker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main);
        setContentView(R.layout.main_large);

        ViewPager mPager = (ViewPager)findViewById(R.id.main_viewpager);
        ViewPager mLargePager = (ViewPager)findViewById(R.id.main_viewpager_large);

        if (savedInstanceState != null) {
            if (mPager != null) {
                fragments[0] = getSupportFragmentManager().getFragment(savedInstanceState, "fragment0");
                fragments[1] = getSupportFragmentManager().getFragment(savedInstanceState, "fragment1");
            }
            if (mLargePager != null) {
                stacker = getSupportFragmentManager().getFragment(savedInstanceState, "stacker");
            }
            fragments[2] = getSupportFragmentManager().getFragment(savedInstanceState, "fragment2");
        } else {
            for (int i=0; i<3; i++)
                fragments[i] = new TestFragment(i);
        }

        if (mPager != null) {
            TestFragmentAdapter mAdapter = new TestFragmentAdapter(getSupportFragmentManager(), 3);
            mPager.setAdapter(mAdapter);
        } else if (mLargePager != null) {
            TestFragmentAdapterLarge mAdapter = new TestFragmentAdapterLarge(this);
            mLargePager.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ViewPager mPager = (ViewPager)findViewById(R.id.main_viewpager);
        ViewPager mLargePager = (ViewPager)findViewById(R.id.main_viewpager_large);

        if (mPager != null) {
            if (fragments[0] != null) {
                try {
                    getSupportFragmentManager().putFragment(outState, "fragment0", fragments[0]);
                } catch (IllegalStateException e) {}
            }
            if (fragments[1] != null) {
                try {
                    getSupportFragmentManager().putFragment(outState, "fragment1", fragments[1]);
                } catch (IllegalStateException e) {}
            }
        }
        if (mLargePager != null) {
            if (stacker != null) {
                try {
                    getSupportFragmentManager().putFragment(outState, "stacker", stacker);
                } catch (IllegalStateException e) {}
            }
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
                ft.remove(stacker);
            } else  { // (position == 1)
                ft.remove(fragments[2]);
            }
            ft.commit();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment res;
            FragmentManager fm = mContext.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            if (position == 0) {
                if (stacker == null) {
                    stacker = new FragmentStacker(fragments[0], fragments[1]);
                    ft.add(container.getId(), stacker);
                }
                else {
                    ft.attach(stacker);
                }
                res = stacker;
            } else  { // (position == 1)
                Fragment second_page = fragments[2];
                if (fm.findFragmentByTag("2") == null)
                    ft.add(container.getId(), second_page, "2");
                else
                    ft.attach(second_page);
                res = second_page;
            }

            ft.commit();
            return res;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return ((Fragment)o).getView()==view;
        }
    }
}

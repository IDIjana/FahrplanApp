package ch.hsr.se.mas.fahrplanapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ViewPager viewPager = (ViewPager) findViewById(R.id.about_viewpager);
        viewPager.setAdapter(new AboutFragmentPagerAdapter(getSupportFragmentManager(),
                AboutActivity.this));

        // don't recreate the fragments when changing tabs
        viewPager.setOffscreenPageLimit(2);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.about_sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    protected class AboutFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context context;

        protected AboutFragmentPagerAdapter(FragmentManager fm, Context context) {
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 1) {
                return new AboutDevelopersFragment();
            }
            return new AboutApplicationFragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 1) {
                return getString(R.string.tab_about_developers);
            }
            return getString(R.string.tab_about_application);
        }
    }
}

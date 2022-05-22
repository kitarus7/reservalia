package com.kitarsoft.reservalia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity{

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initElements();

        Intent i = getIntent();
    }

    private void initElements() {
        tabLayout = findViewById(R.id.tabLayout_main);
        viewPager = findViewById(R.id.viewPager2_main);

        //  Menú
        tabLayout.addTab(tabLayout.newTab().setText("Reservar"), 0);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_book);
        tabLayout.addTab(tabLayout.newTab().setText("Favoritos"), 1);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_favorite);
        tabLayout.addTab(tabLayout.newTab().setText("Perfil"), 2);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_profile);

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(MainActivity.this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position==0) {
                    tab.setText("Reservar");
                    tab.setIcon(R.drawable.ic_book);
                }
                if (position==1) {
                    tab.setText("Favoritos");
                    tab.setIcon(R.drawable.ic_favorite);
                }
                if (position==2) {
                    tab.setText("Perfil");
                    tab.setIcon(R.drawable.ic_profile);
                }
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        viewPager.setCurrentItem(0);
        viewPager.setUserInputEnabled(false);
    }

    class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(AppCompatActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment frag_new = null;
            Bundle params = new Bundle();
            params.putString("param1", "result1");
            params.putString("param2", "result2");

            if(position==0)frag_new= new MapsFragment();
            if(position==1)frag_new= new Favoritos();
            if(position==2)frag_new= new Perfil();

            frag_new.setArguments(params);

            return frag_new;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

}
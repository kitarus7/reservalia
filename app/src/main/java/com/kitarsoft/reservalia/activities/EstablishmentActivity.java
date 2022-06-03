package com.kitarsoft.reservalia.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.fragments.BookFragment;
import com.kitarsoft.reservalia.fragments.FavoritesFragment;
import com.kitarsoft.reservalia.fragments.MenuFragment;
import com.kitarsoft.reservalia.fragments.ProfileFragment;

import java.util.Arrays;

public class EstablishmentActivity extends AppCompatActivity {

    //  Menú config (Se debe seguir el orden del menú)
    private final static int menuSize = 2;
    private final String[] menuOptions = {
            //"Carta", "Recomendación", "Reservar", "Valoraciones"
            "Carta", "Reservar"
    };
    private final int[] menuIcons = {
            //R.drawable.ic_menu, R.drawable.ic_chef, R.drawable.ic_dishes, R.drawable.ic_favorite
            R.drawable.ic_menu, R.drawable.ic_dishes
    };

    private final static Fragment[] menuFragments = {
            //new MenuFragment(), new FavoritesFragment(), new ProfileFragment(), new BookFragment()
            new MenuFragment(), new BookFragment()
    };

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment);

        Intent i = getIntent();

        initElements();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for(int i=0;i<menuFragments.length;i++){
            menuFragments[i] = null;
        }
    }

    private void initElements() {
        tabLayout = findViewById(R.id.tabLayout_establishment);
        viewPager = findViewById(R.id.viewPager2_establishment);

        //  Menú
        for(int i = 0; i < menuSize;i++){
            tabLayout.addTab(tabLayout.newTab(), i);
        }

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(EstablishmentActivity.this);
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(menuOptions[position]);
                tab.setIcon(menuIcons[position]);
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

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.setCurrentItem(0);
        viewPager.setUserInputEnabled(true);
    }

    public static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(AppCompatActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment frag_new = null;
            frag_new = menuFragments[position];
            return frag_new;
        }

        @Override
        public int getItemCount() {
            return menuSize;
        }
    }
}
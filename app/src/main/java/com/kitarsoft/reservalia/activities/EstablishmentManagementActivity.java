package com.kitarsoft.reservalia.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.fragments.BookFragment;
import com.kitarsoft.reservalia.fragments.BookingsFragment;
import com.kitarsoft.reservalia.fragments.EstablishmentManagementFragment;
import com.kitarsoft.reservalia.fragments.MenuFragment;
import com.kitarsoft.reservalia.fragments.MenuManagementFragment;

public class EstablishmentManagementActivity extends AppCompatActivity {

    //  Menú config (Se debe seguir el orden del menú)
    private final static int menuSize = 3;
    private final String[] menuOptions = {
            "Local", "Carta", "Reservas"
    };
    private final int[] menuIcons = {
            R.drawable.ic_menu, R.drawable.ic_dishes, R.drawable.ic_book
    };

    private final static Fragment[] menuFragments = {
            new EstablishmentManagementFragment(), new MenuManagementFragment(), new BookingsFragment()
    };

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    private static String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establishment_management);
        Intent i = getIntent();
        userId = i.getExtras().get("userId").toString();
        initElements();
    }



    private void initElements() {
        tabLayout = findViewById(R.id.tabLayout_establishmentManagement);
        viewPager = findViewById(R.id.viewPager2_establishmentManagement);

        //  Menú
        for(int i = 0; i < menuSize;i++){
            tabLayout.addTab(tabLayout.newTab(), i);
        }

        FragmentStateAdapter pagerAdapter = new EstablishmentManagementActivity.ScreenSlidePagerAdapter(EstablishmentManagementActivity.this);
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

            Bundle args = new Bundle();
            args.putString("userId", userId);
            frag_new.setArguments(args);

            return frag_new;
        }

        @Override
        public int getItemCount() {
            return menuSize;
        }
    }


}
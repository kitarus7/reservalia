package com.kitarsoft.reservalia.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.kitarsoft.reservalia.R;
import com.kitarsoft.reservalia.models.Menu;
import com.kitarsoft.reservalia.models.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {

    List<MenuItem> dummyMenuItems = new ArrayList<MenuItem>();

    //  Menú config (Se debe seguir el orden del menú)
    List<Fragment> menuFragments = new ArrayList<Fragment>();

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initElements(view);
    }

    private void initElements(View view) {
        dummyMenuData();
        for (MenuItem menuItem:dummyMenuItems) {
            menuFragments.add(new MenuItemFragment());
        }
        tabLayout = (TabLayout)view.findViewById(R.id.tabLayout_menu);
        viewPager = (ViewPager2)view.findViewById(R.id.viewPager2_menu);

        //  Menú
        for(int i = 0; i < dummyMenuItems.size();i++){
            tabLayout.addTab(tabLayout.newTab(), i);
        }

        FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter((AppCompatActivity) getActivity());
        viewPager.setAdapter(pagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(dummyMenuItems.get(position).getName());
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
        viewPager.setUserInputEnabled(false);
    }

    class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(AppCompatActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment frag_new = null;
            frag_new = menuFragments.get(position);
            return frag_new;
        }

        @Override
        public int getItemCount() {
            return dummyMenuItems.size();
        }
    }

     private void dummyMenuData(){

        /*dummyMenu.setId(0);
        dummyMenu.setName("Menú de prueba");
        dummyMenu.setMenuDays("Lunes - Viernes");
        dummyMenu.setAnotations("Viernes noche no hay menú");
        dummyMenu.setMenuPrice(16.5f);*/

//        List<MenuItem> dummyMenuItems = new ArrayList<MenuItem>();

        for(int i = 0; i < 5; i++){
            dummyMenuItems.add(new MenuItem(i, false, "Categorie "+i, "Item "+i, "Dummy item", "It's only a dummy menú item", 8.75f));
        }
        //dummyMenu.setMenuItems(dummyMenuItems);
     }
}
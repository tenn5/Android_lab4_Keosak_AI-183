package com.example.lab4_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import fragments.SkyrimFragment;
import fragments.WarframeFragment;
import fragments.DishonoredFragment;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        viewPager = findViewById(R.id.container);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position){
                switch (position) {
                    case 0:
                        bottomNavigationView.setSelectedItemId(R.id.dishonored);
                        break;
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.warframe);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.skyrim);
                        break;
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.dishonored:
                    viewPager.setCurrentItem(0, true);
                    break;
                case R.id.warframe:
                    viewPager.setCurrentItem(1, true);
                    break;
                case R.id.skyrim:
                    viewPager.setCurrentItem(2, true);
                    break;
            }
            return true;
        });

        Adapter adapter = new Adapter(this);
        adapter.addFragment(new DishonoredFragment());
        adapter.addFragment(new WarframeFragment());
        adapter.addFragment(new SkyrimFragment());
        viewPager.setAdapter(adapter);
    }

    private class Adapter extends FragmentStateAdapter {

        private List<Fragment> list = new ArrayList<>();

        public Adapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public void addFragment(Fragment fragment) {
            list.add(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }
}
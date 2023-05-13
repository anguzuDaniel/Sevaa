package com.danotech.sevaa.UI.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.danotech.sevaa.UI.fragments.CreditCardFragment;
import com.danotech.sevaa.UI.fragments.HomeFragment;
import com.danotech.sevaa.UI.fragments.SubscriptionsFragment;
import com.danotech.sevaa.UI.fragments.SettingsFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new CreditCardFragment();
            case 2:
                return new SubscriptionsFragment();
            case 3:
                return new SettingsFragment();
            case 0:
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

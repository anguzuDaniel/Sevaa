package com.danotech.sevaa.UI.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.danotech.sevaa.UI.fragments.FirstIntroPageFragment;
import com.danotech.sevaa.UI.fragments.SecondIntroPageFragment;
import com.danotech.sevaa.UI.fragments.ThirdIntroPageFragment;

public class IntroPageAdapter extends ViewPagerAdapter {
    public IntroPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 1:
                return new SecondIntroPageFragment();
            case 2:
                return new ThirdIntroPageFragment();
            case 0:
            default:
                return new FirstIntroPageFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

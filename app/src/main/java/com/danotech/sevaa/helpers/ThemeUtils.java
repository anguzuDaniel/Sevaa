package com.danotech.sevaa.helpers;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;

import com.danotech.sevaa.R;

import java.util.ArrayList;
import java.util.List;

public class ThemeUtils {

    public static List<View> findAllViewsById(View view, int targetId) {
        List<View> result = new ArrayList<>();

        if (view.getId() == targetId) {
            result.add(view);
        }

        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                List<View> childViews = findAllViewsById(child, targetId);
                result.addAll(childViews);
            }
        }

        return result;
    }

    public static void updateBackgroundColor(View view, boolean isDarkModeEnabled) {
        int backgroundColorResId = isDarkModeEnabled ? R.color.color_background_dark : R.color.color_background_light;
        int backgroundColor = ContextCompat.getColor(view.getContext(), backgroundColorResId);
        view.setBackgroundColor(backgroundColor);
    }

    // Rest of your ThemeUtils code
    // ...
}

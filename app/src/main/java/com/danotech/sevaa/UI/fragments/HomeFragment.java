package com.danotech.sevaa.UI.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.danotech.sevaa.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // using the gradients to change the background color of cards
        GradientDrawable expenseGradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#FFCE54"), Color.parseColor("#F68842")});

        GradientDrawable savingsGradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TL_BR,
                new int[]{Color.parseColor("#A0D468"), Color.parseColor("#8CC152")});

        // setting the backgrounds
        CardView expenseCard = view.findViewById(R.id.expense_card);
        expenseCard.setBackground(expenseGradientDrawable);
        expenseCard.setRadius(20);

        CardView savingsCard = view.findViewById(R.id.savings_card);
        savingsCard.setBackground(savingsGradientDrawable);
        savingsCard.setRadius(20);

        LinearLayout dashboard = view.findViewById(R.id.dashboard);
        dashboard.bringToFront();

        Bundle bundle = new Bundle();
        bundle.putString("FirstName", "Daniel");
        bundle.putString("SurName", "Anguzu");
        bundle.putInt("Age", 10);
        bundle.putString("Gender", "Male");


        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.setArguments(bundle);
        setArguments(bundle);



        // Inflate the layout for this fragment
        return view;
    }
}
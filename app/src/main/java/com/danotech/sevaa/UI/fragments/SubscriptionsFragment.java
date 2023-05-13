package com.danotech.sevaa.UI.fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.danotech.sevaa.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class SubscriptionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subcriptions, container, false);

//        // using the gradients to change the background color of cards
//        GradientDrawable expenseGradientDrawable = new GradientDrawable(
//                GradientDrawable.Orientation.TL_BR,
//                new int[]{Color.parseColor("#FFCE54"), Color.parseColor("#F68842")});
//
//        // setting the backgrounds
//        CardView expenseCard = view.findViewById(R.id.balance);
//        expenseCard.setBackground(expenseGradientDrawable);

        PieChart chart = view.findViewById(R.id.chart);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(15f, "Rent"));
        entries.add(new PieEntry(5f, "shopping"));
        entries.add(new PieEntry(20f, "Food"));
        entries.add(new PieEntry(3f, "Water"));
        entries.add(new PieEntry(7f, "Entertainment"));
        entries.add(new PieEntry(25f, "Emergencies"));
        entries.add(new PieEntry(25f, "Medical"));

        PieDataSet dataSet = new PieDataSet(entries, "Expense Categories");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//        int[] colors = {Color.parseColor("#8BC34A"), Color.parseColor("#689F38"), Color.parseColor("#558B2F"), Color.parseColor("#33691E")};
//        dataSet.setColors(colors);

//        int[] colors = {Color.parseColor("#63B457"), Color.parseColor("#D45B5B"), Color.parseColor("#F9A74B"), Color.parseColor("#9B59B6"), Color.parseColor("#3498DB"), Color.parseColor("#1ABC9C"), Color.parseColor("#E67E22"), Color.parseColor("#E74C3C"), Color.parseColor("#95A5A6")};
//        dataSet.setColors(colors);

        dataSet.setDrawValues(true); // Enable value display
        dataSet.setDrawIcons(true); // Disable icons
        dataSet.setSliceSpace(2f); // Set space between slices
        dataSet.setValueTextColor(Color.TRANSPARENT); // Set label color
        dataSet.setValueTextSize(18f); // Set label text size

        PieData data = new PieData(dataSet);
        chart.setData(data);
        chart.invalidate();
        chart.setUsePercentValues(true); // Show percentages instead of values
        chart.getDescription().setEnabled(true); // enable chart description
        chart.getLegend().setEnabled(true); // Disable legend
        chart.setDrawEntryLabels(true); // Disable drawing labels inside slices
//        chart.setExtraOffsets(10f, 10f, 30f, 10f); // Add padding around the chart
        chart.setCenterTextTypeface(Typeface.DEFAULT_BOLD);
        chart.setHoleRadius(60f);
        chart.setTransparentCircleRadius(65f);
        chart.setEntryLabelColor(R.color.color_transaction_figures);
        chart.setEntryLabelTextSize(12f);
        chart.isUsePercentValuesEnabled();

        // Inflate the layout for this fragment
        return view;
    }
}
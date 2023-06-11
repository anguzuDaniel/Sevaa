package com.danotech.sevaa.helpers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import java.io.IOException;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

public class CurrencyUtils {

    public static Currency getCurrencyFromLocation(Context context, Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Currency currency = null;

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                String countryCode = address.getCountryCode();
                currency = Currency.getInstance(new Locale("", countryCode));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return currency;
    }
}
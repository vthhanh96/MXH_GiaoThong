package com.khoaluan.mxhgiaothong.utils;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.khoaluan.mxhgiaothong.Application;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Hong Hanh on 4/11/2018.
 */

public class GeoHelper {

    @SuppressLint("NewApi")
    public static String getAddress(Location location) {
        Geocoder geocoder = new Geocoder(Application.getContext(), Locale.forLanguageTag("vi_VN"));
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (addresses.size() < 1) return "Chưa xác định";
            Log.v("GetAddress", "Address: " + addresses.get(0).getAddressLine(0));
            return addresses.get(0).getAddressLine(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return "Chưa xác định";
    }
}

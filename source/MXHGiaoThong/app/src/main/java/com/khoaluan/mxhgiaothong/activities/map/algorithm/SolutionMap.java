package com.khoaluan.mxhgiaothong.activities.map.algorithm;

import android.graphics.Color;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.map.model.PlaceResult;
import com.khoaluan.mxhgiaothong.activities.map.view.MapActivity;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.khoaluan.mxhgiaothong.activities.map.view.MapActivity.myProgress;


public class SolutionMap implements DirectionFinderListener {
    private GoogleMap mMap;
    private ArrayList<String> listPlaceSearch = new ArrayList<>();
    public static Route[][] matrixRoutes;
    private static List<String> listPlaces = new ArrayList<String>();
    private List<Route> listRouteToMatrix = new ArrayList<Route>();
    public static int[] bestPath = new int[11];
    private static List<Marker> originMarkers = new ArrayList<>();
    private static List<Marker> destinationMarkers = new ArrayList<>();
    public static List<Polyline> polylinePaths = new ArrayList<>();
    private int[] images = new int[11];
    public static boolean isFail = false;
    private String TAG = "test";
    public SolutionMap(List<String> listPlaces, GoogleMap mMap) {
        matrixRoutes = new Route[100][100];
        this.listPlaces = listPlaces;
        this.mMap = mMap;
        isFail = false;
    }

    public ArrayList<String> getListPlaceSearch() {
        return listPlaceSearch;
    }

    public void setListPlaceSearch(ArrayList<String> listPlaceSearch) {
        this.listPlaceSearch = listPlaceSearch;
    }

    public static int getSizeListPlaces() {
        return listPlaces.size();
    }

    public void createListRoute() throws UnsupportedEncodingException {
        for (int i = 0; i < listPlaces.size(); i++) {
            Log.d("Test list", listPlaces.get(i));
            for (int j = 0; j < listPlaces.size(); j++)
                new CustomDirectionFinder(this, listPlaces.get(i), listPlaces.get(j)).execute();
        }
    }

    @Override
    public void onDirectionFinderStart() {

    }

    @Override
    public void onDirectionFinderSuccess(List<Route> listRouteToAddMatrix) {
        if (isFail) {
            myProgress.dismiss();
            MapActivity.builderFail.show();
            return;
        }
        Log.d(TAG, "onDirectionFinderSuccess: "+ listRouteToAddMatrix.size());
        Log.d(TAG, "onDirectionFinderSuccess: "+ listPlaces.size());
        if (listRouteToAddMatrix.size() == (listPlaces.size() * listPlaces.size())) {

            int k = 0;

            for (int i = 1; i <= listPlaces.size(); i++)
                for (int j = 1; j <= listPlaces.size(); j++) {
                    matrixRoutes[i][j] = listRouteToAddMatrix.get(k);

                    if (i == j) {
                        matrixRoutes[i][j].distance.value = 0;
                        matrixRoutes[i][j].distance.text = "0";
                    } else if (i > j) {
                        matrixRoutes[i][j] = matrixRoutes[j][i];
                    }

                    Log.d("Test Matrix", matrixRoutes[i][j].startAddress.toString() + matrixRoutes[i][j].endAddress.toString() + matrixRoutes[i][j].distance.text);
                    k++;
                }

            CustomDirectionFinder.listRouteToAddMatrix.clear();
            // tifm dduong di ngan nhat
            FindBestPath.matrixRoute = this.matrixRoutes;
            bestPath = FindBestPath.getBestPath(listPlaces.size());
            // ve duong di ngan nhat
            drawBestPath();

            //--------------------------------------------------------------------- Hien thi Result
            MapActivity.listPlaceResult.clear();
            bestPath.toString();

            for (Integer i = 1; i <= listPlaces.size(); i++) {
                PlaceResult placeResult = new PlaceResult(i.toString(), getListPlaceSearch().get(bestPath[i] - 1));
                MapActivity.listPlaceResult.add(placeResult);
            }
            MapActivity.listPlaceResult.size();
            MapActivity.adapterResult.notifyDataSetChanged();
            MapActivity.listViewResult.setAdapter(MapActivity.adapterResult);
            MapActivity.tvBestDistance.setText(((Double) (FindBestPath.best / 1000)).toString() + " km");
            myProgress.dismiss();

            MapActivity.fabAdd.setVisibility(View.GONE);
            MapActivity.bottom_sheet.setVisibility(View.VISIBLE);
        }
    }

    public void drawBestPath() {
        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline : polylinePaths) {
                polyline.remove();
            }
        }

        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        images[1] = R.drawable.img1;
        images[2] = R.drawable.img2;
        images[3] = R.drawable.img3;
        images[4] = R.drawable.img4;
        images[5] = R.drawable.img5;
        images[6] = R.drawable.img6;
        images[7] = R.drawable.img7;
        images[8] = R.drawable.img8;
        images[9] = R.drawable.img9;
        images[10] = R.drawable.img10;

        for (int i = 1; i < listPlaces.size(); i++) {
            if (i == 1) {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(matrixRoutes[bestPath[i]][bestPath[i + 1]].startLocation, 10));

                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.img0))
                        .title(matrixRoutes[bestPath[i]][bestPath[i + 1]].startAddress)
                        .position(matrixRoutes[bestPath[i]][bestPath[i + 1]].startLocation)));
            } else {
                originMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(images[i - 1]))
                        .title(matrixRoutes[bestPath[i]][bestPath[i]].startAddress)
                        .position(matrixRoutes[bestPath[i]][bestPath[i]].startLocation)));
            }

            if (i == listPlaces.size() - 1)
                destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(images[i]))
                        .title(matrixRoutes[bestPath[i + 1]][bestPath[i + 1]].startAddress)
                        .position(matrixRoutes[bestPath[i + 1]][bestPath[i + 1]].startLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(7);

            for (int j = 0; j < matrixRoutes[bestPath[i]][bestPath[i + 1]].points.size(); j++)
                polylineOptions.add(matrixRoutes[bestPath[i]][bestPath[i + 1]].points.get(j));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }
}

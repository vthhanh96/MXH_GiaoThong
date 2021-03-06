package com.khoaluan.mxhgiaothong.activities.map.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.map.adapter.ListPlaceResultAdapter;
import com.khoaluan.mxhgiaothong.activities.map.algorithm.SolutionMap;
import com.khoaluan.mxhgiaothong.activities.map.fragment.MapFragment;
import com.khoaluan.mxhgiaothong.activities.map.model.PlaceResult;
import com.khoaluan.mxhgiaothong.customView.TopBarView;
import com.khoaluan.mxhgiaothong.activities.map.fragment.SearchMapDialogFragment;
import com.khoaluan.mxhgiaothong.customView.dialog.listener.CustomDialogActionListener;
import com.khoaluan.mxhgiaothong.drawer.DrawerActivity;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.TiltEffect;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.khoaluan.mxhgiaothong.AppConstants.LEFT_MENU;
import static com.khoaluan.mxhgiaothong.activities.map.algorithm.SolutionMap.bestPath;
import static com.khoaluan.mxhgiaothong.activities.map.algorithm.SolutionMap.matrixRoutes;
import static com.khoaluan.mxhgiaothong.activities.map.fragment.MapFragment.myMap;

public class MapActivity extends DrawerActivity {

    public static int index = -1;
    public static TextView tvChiTietCachDi;
    private LinearLayout updownBottomSheet;
    public static LinearLayout bottom_sheet;
    public static ImageButton btnUpDown;
    public static FloatingActionsMenu fabAdd;

    private FloatingActionButton fabSearchPlace;
    private boolean kt_Bottom = true;

    public static JazzyListView listViewResult;
    public static ArrayList<PlaceResult> listPlaceResult = new ArrayList<PlaceResult>();
    public static ListPlaceResultAdapter adapterResult;
    public static TextView tvBestDistance;
    public static FragmentManager fragmentManager;
    public static boolean isLoadHistory = false;
    private FloatingActionButton fabLookup;
    private boolean pressLookup;
    public static ProgressDialog myProgress;
    public static AlertDialog.Builder builderFail;

    @BindView(R.id.topBar)
    TopBarView topBar;
    private SearchMapDialogFragment searchMapDialogFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_map;
    }

    @Override
    protected int getNavId() {
        return AppConstants.NAV_DRAWER_MAP;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MapFragment map_fragment = new MapFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_main, map_fragment, map_fragment.getTag()).commit();
        initTopbar();
        addControls();
        addEvents();
    }

    private void initTopbar() {
        topBar.setImageViewLeft(LEFT_MENU);
        topBar.setOnClickListener(new TopBarView.OnItemClickListener() {
            @Override
            public void onImvLeftClicked() {
                openDrawer();
            }

            @Override
            public void onImvRightClicked() {
            }

            @Override
            public void onTvLeftClicked() {
            }

            @Override
            public void onTvRightClicked() {

            }
        });
    }

    private void addEvents() {
        fabSearchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchMapDialogFragment = new SearchMapDialogFragment();
                searchMapDialogFragment.show(getSupportFragmentManager(),"");
                searchMapDialogFragment.setDialogActionListener(new CustomDialogActionListener() {
                    @Override
                    public void dialogCancel() {
                        searchMapDialogFragment.clickThoat();
                    }

                    @Override
                    public void dialogPerformAction() {
                        searchMapDialogFragment.clickTimDuong();
                    }
                });
                fabAdd.collapse();
            }
        });

        tvChiTietCachDi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kt_Bottom)
                {
                    listViewResult.setVisibility(View.GONE);
                    kt_Bottom = false;
                }
                else
                {
                    listViewResult.setVisibility(View.VISIBLE);
                    kt_Bottom = true;
                }
            }
        });

        updownBottomSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(kt_Bottom)
                {
                    listViewResult.setVisibility(View.GONE);
                    kt_Bottom = false;
                }
                else
                {
                    listViewResult.setVisibility(View.VISIBLE);
                    kt_Bottom = true;
                }
            }
        });

        //----------------------------- click result item

        listViewResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView tvResultDes = (TextView) view.findViewById(R.id.txtResultDestination);
                tvResultDes.setTextColor(Color.RED);

                CameraUpdate moveCamera = CameraUpdateFactory.newLatLngZoom(
                        matrixRoutes[bestPath[i+1]][bestPath[i+1]].startLocation, 15);
                myMap.animateCamera(moveCamera);

//                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(matrixRoutes[bestPath[i]][bestPath[i+1]].startLocation, 16));

                for (int j = 0; j < listViewResult.getChildCount(); j++) {
                    if (j != i) {
                        TextView tv = (TextView) listViewResult.getChildAt(j).findViewById(R.id.txtResultDestination);
                        tv.setTextColor(Color.rgb(0, 153, 255));
                    }
                }

                if (i > 0) {
                    for (int j = 1; j < listViewResult.getChildCount(); j++) {
                        if (j != i) {
                            SolutionMap.polylinePaths.get(j-1).setColor(Color.BLUE);
                        }
                    }

                    SolutionMap.polylinePaths.get(i-1).setColor(Color.RED);
                }
                else {
                    for (int j = 1; j < listViewResult.getChildCount(); j++) {
                        SolutionMap.polylinePaths.get(j-1).setColor(Color.BLUE);
                    }
                }
            }
        });

        fabLookup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pressLookup = true;
                int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setBoundsBias(new LatLngBounds(
                                            new LatLng(8.407168163601074, 104.1448974609375),
                                            new LatLng(10.7723923007117563, 106.6981029510498)))
                                    .build(MapActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
                fabAdd.collapse();
            }
        });
    }

    private void addControls() {
        fabSearchPlace = (FloatingActionButton) findViewById(R.id.fabSearchPlace);
        tvChiTietCachDi = (TextView) findViewById(R.id.tvChiTietCachDi);
        fabAdd = (FloatingActionsMenu)findViewById(R.id.fabAdd);
        bottom_sheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        updownBottomSheet = (LinearLayout) findViewById(R.id.updownBottomSheet);

        listViewResult = (JazzyListView) findViewById(R.id.listViewResult);
        listViewResult.setTransitionEffect(new TiltEffect());
        adapterResult = new ListPlaceResultAdapter(this, R.layout.place_result_item, listPlaceResult);
        listViewResult.setAdapter(adapterResult);

        tvBestDistance = (TextView) findViewById(R.id.tvBestDistance);

        fabLookup = (FloatingActionButton) findViewById(R.id.fabLookup);

        builderFail = new AlertDialog.Builder(this);
        builderFail.setMessage("Không thể tìm được đường đi")
                .setTitle("  Thông báo  ")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        bottom_sheet.setVisibility(View.GONE);
                        fabAdd.setVisibility(View.VISIBLE);
                        searchMapDialogFragment.getListPlaceSearch().clear();
                        searchMapDialogFragment.getListLagLngSearch().clear();
                        index = -1;
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builderFail.create();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (tvChiTietCachDi.getVisibility() == View.VISIBLE) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Bạn có muốn về màn hình chính để tìm kiếm kết quả khác không ?")
                        .setTitle("  Thông báo  ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                bottom_sheet.setVisibility(View.GONE);
                                fabAdd.setVisibility(View.VISIBLE);
                                searchMapDialogFragment.getListPlaceSearch().clear();
                                searchMapDialogFragment.getListLagLngSearch().clear();
                                index = -1;
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Bạn có thực sự muốn thoát ?")
                        .setTitle("  Thoát chương trình ")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MapActivity.this.finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        }
    }

    //region Search DialogFragment
    public void clickChangeSearchItem(View view) {
        int index = searchMapDialogFragment.getListViewSearch().getPositionForView(view);

        if (index > 0)
        {
            searchMapDialogFragment.getListPlaceSearch().add(index - 1, searchMapDialogFragment.getListPlaceSearch().get(index));
            searchMapDialogFragment.getListPlaceSearch().remove(index + 1);
            searchMapDialogFragment.getListLagLngSearch().add(index - 1, searchMapDialogFragment.getListPlaceSearch().get(index));
            searchMapDialogFragment.getListLagLngSearch().remove(index + 1);
            searchMapDialogFragment.getAdapter().notifyDataSetChanged();
            searchMapDialogFragment.getListViewSearch().setAdapter(searchMapDialogFragment.getAdapter());
        }
    }

    public void clickSubSearchItem(View view) {
        if (searchMapDialogFragment.getListViewSearch().getChildCount() > 1) {
            int index = searchMapDialogFragment.getListViewSearch().getPositionForView(view);
            searchMapDialogFragment.getListPlaceSearch().remove(index);
            searchMapDialogFragment.getListLagLngSearch().remove(index);
            searchMapDialogFragment.getAdapter().notifyDataSetChanged();
            searchMapDialogFragment.getListViewSearch().setAdapter(searchMapDialogFragment.getAdapter());
        }
        else {
            Toast.makeText(getApplicationContext(), "Không thể xóa", Toast.LENGTH_LONG).show();
        }
    }

    public void clickDestination(View view) {
        index = searchMapDialogFragment.getListViewSearch().getPositionForView(view);

        // gọi cái  Place autoComplete lên
        int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .setBoundsBias(new LatLngBounds(
                                    new LatLng(8.407168163601074, 104.1448974609375),
                                    new LatLng(10.7723923007117563, 106.6981029510498)))
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);


        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }
    }
    //endregion

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1
                && resultCode == Activity.RESULT_OK && !pressLookup) {
            final Place place = PlacePicker.getPlace(data, this);

            if (place != null) {
                CharSequence placeNameSearch = place.getName();
                String[] parts = place.getLatLng().toString().split("\\(");
                String[] parts1 = parts[1].split("\\)");
                String latLngSearch = parts1[0];

                searchMapDialogFragment.getListPlaceSearch().set(MapActivity.index, placeNameSearch.toString());
                searchMapDialogFragment.getListLagLngSearch().set(MapActivity.index, latLngSearch);
                searchMapDialogFragment.getAdapter().notifyDataSetChanged();
                searchMapDialogFragment.getListViewSearch().setAdapter(searchMapDialogFragment.getAdapter());
            }
        }

        if(requestCode == 1
                && resultCode == Activity.RESULT_OK && pressLookup)
        {
            final Place place = PlacePicker.getPlace(data, this);
            if(place != null)
            {
                LatLng latSearch = place.getLatLng();
                myMap.addMarker(new MarkerOptions().position(latSearch).title(place.getAddress().toString()));
                myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                CameraUpdate dichuyen = CameraUpdateFactory.newLatLngZoom(latSearch, 18);
                myMap.animateCamera(dichuyen);
            }
            pressLookup = false;
        }

        if (resultCode == RESULT_OK && null != data && searchMapDialogFragment.isVoice()) {
            searchMapDialogFragment.setVoice(false);
            ArrayList<String> result = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            try {
                String[] temp1 = result.get(0).split("tới ");
                ArrayList<String> arrPlaceFromVoice = new ArrayList<>();
                for (int i = 0; i < temp1.length; i++) {
                    arrPlaceFromVoice.add(i, temp1[i]);
                }
                arrPlaceFromVoice.set(0, (temp1[0].split("đi từ "))[1]);

                for (int i = 0; i < arrPlaceFromVoice.size(); i++) {
                    System.out.println("\nđịa điểm thứ " + i + " là : " + arrPlaceFromVoice.get(i));
                }
                if (arrPlaceFromVoice.size() < 2) {
                    arrPlaceFromVoice.clear();
                    String[] temp2 = result.get(0).split("đến ");
                    for (int i = 0; i < temp2.length; i++) {
                        arrPlaceFromVoice.add(i, temp2[i]);
                    }
                    arrPlaceFromVoice.set(0, (temp2[0].split("đi từ "))[1]);
                }

                if (arrPlaceFromVoice.size() >= 2) {
                    searchMapDialogFragment.getListLagLngSearch().clear();

                    searchMapDialogFragment.getTxtStartPlace().setText(arrPlaceFromVoice.get(0));
                    searchMapDialogFragment.setLatLngStartPlace(arrPlaceFromVoice.get(0));

                    searchMapDialogFragment.getListPlaceSearch().clear();
                    for (int i = 1; i < arrPlaceFromVoice.size(); i++) {
                        searchMapDialogFragment.getListPlaceSearch().add(arrPlaceFromVoice.get(i));
                        searchMapDialogFragment.getListLagLngSearch().add(arrPlaceFromVoice.get(i));
                    }
                    searchMapDialogFragment.getAdapter().notifyDataSetChanged();
                    searchMapDialogFragment.getListViewSearch().setAdapter(searchMapDialogFragment.getAdapter());
                }


            }
            catch (Exception e) {
                Toast.makeText(MapActivity.this, "Không thể nhận dạng câu lệnh. Cú pháp: Đi từ ... đến ...", Toast.LENGTH_LONG).show();
            }
        }
    }
}
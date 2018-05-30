package com.khoaluan.mxhgiaothong.customView.dialog.map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.map.adapter.ListPlaceAdapter;
import com.khoaluan.mxhgiaothong.activities.map.algorithm.SolutionMap;
import com.khoaluan.mxhgiaothong.activities.map.fragment.MapFragment;
import com.khoaluan.mxhgiaothong.activities.map.view.MapActivity;
import com.khoaluan.mxhgiaothong.customView.dialog.BaseCustomDialogFragment;
import com.khoaluan.mxhgiaothong.utils.FontHelper;
import com.like.OnLikeListener;
import com.twotoasters.jazzylistview.JazzyListView;
import com.twotoasters.jazzylistview.effects.GrowEffect;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by hieu-nm on 2018/05/30.
 */

@SuppressLint("ValidFragment")
public class SearchMapDialogFragment extends BaseCustomDialogFragment {

    private JazzyListView listViewSearch;
    private ArrayList<String> listPlaceSearch = new ArrayList<>();
    private ArrayList<String> listLagLngSearch = new ArrayList<>();
    private ImageButton btnAddSearchItem;
    private ListPlaceAdapter adapter;
    private boolean voice;
    private EditText txtStartPlace;
    private String latLngStartPlace;

    private SolutionMap solutionMap;

    private ImageButton btnSpeak;

    private ImageButton btnMyLocation;
    private final int REQ_CODE_SPEECH_INPUT = 100;
    private LinearLayout ln_dialog;
    private com.like.LikeButton btnSave;
    private boolean isSave = false;

    public JazzyListView getListViewSearch() {
        return listViewSearch;
    }

    public ArrayList<String> getListPlaceSearch() {
        return listPlaceSearch;
    }

    public ArrayList<String> getListLagLngSearch() {
        return listLagLngSearch;
    }

    public ImageButton getBtnAddSearchItem() {
        return btnAddSearchItem;
    }

    public ListPlaceAdapter getAdapter() {
        return adapter;
    }

    public boolean isVoice() {
        return voice;
    }

    public EditText getTxtStartPlace() {
        return txtStartPlace;
    }

    public String getLatLngStartPlace() {
        return latLngStartPlace;
    }

    public void setLatLngStartPlace(String latLngStartPlace) {
        this.latLngStartPlace = latLngStartPlace;
    }

    public void setVoice(boolean voice) {
        this.voice = voice;
    }

    @SuppressLint("ValidFragment")
    public SearchMapDialogFragment() {
        super();
        setView(R.layout.search_map_dialog);
        setHasAction(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        setActionName("Tìm Đường");

        listViewSearch = (JazzyListView) view.findViewById(R.id.listViewSearch);

        // click add search item
        btnAddSearchItem = (ImageButton) view.findViewById(R.id.btnAddSearchItem);
        txtStartPlace = (EditText)view.findViewById(R.id.txtStartPlace);
        btnMyLocation = (ImageButton) view.findViewById(R.id.btnMyLocation);
        btnSpeak = (ImageButton) view.findViewById(R.id.btnSpeak);
        btnSave = (com.like.LikeButton) view.findViewById(R.id.btnSave);
        btnMyLocation = (ImageButton) view.findViewById(R.id.btnMyLocation);
        ln_dialog = view.findViewById(R.id.ln_dialog);

        if (MapActivity.isLoadHistory) {
            try {
                StringBuilder data = new StringBuilder();
                listPlaceSearch.clear();
                listLagLngSearch.clear();
                String[] listData = null;
                File pathCacheDir = getActivity().getCacheDir();
                String strCacheFileName = "myCacheFile.cache";
                File newCacheFile = new File(pathCacheDir, strCacheFileName);
                Scanner sc = new Scanner(newCacheFile);
                while (sc.hasNext())
                    data.append(sc.nextLine()).append("\n");
                if (!data.toString().equals("") && data.toString().contains("-")) {
                    listData = data.toString().split("-");
                    txtStartPlace.setText(listData[0]);
                    latLngStartPlace = listData[1];
                    for (int i = 2; i < listData.length; i += 2) {
                        listPlaceSearch.add(listData[i]);
                        listLagLngSearch.add(listData[i + 1]);
                    }
                }
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (listPlaceSearch.size() == 0) {
            listPlaceSearch.add(null);
            listPlaceSearch.add(null);

            listLagLngSearch.add(null);
            listLagLngSearch.add(null);
        }

        adapter = new ListPlaceAdapter(
                getActivity(), R.layout.place_search_item, listPlaceSearch);

        listViewSearch.setAdapter(adapter);
        listViewSearch.setTransitionEffect(new GrowEffect());

        addSearchItem();
        clickThoat();
        clickStartPlace();
        clickMyLocation();
        clickTimDuong();
        clickSpeak();
        clickSave();


        return view;
    }

    private void clickSave() {
        btnSave.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(com.like.LikeButton likeButton) {
                isSave = true;
            }

            @Override
            public void unLiked(com.like.LikeButton likeButton) {
                isSave = false;
            }
        });
    }

    public void clickTimDuong() {
        if (isSave) {
            try {
                File pathCacheDir = getActivity().getCacheDir();
                String strCacheFileName = "myCacheFile.cache";
                StringBuilder strFileContents = new StringBuilder(txtStartPlace.getText().toString() + "-" + latLngStartPlace);
                for (int i = 0; i < listPlaceSearch.size(); i++)
                    strFileContents.append("-").append(listPlaceSearch.get(i)).append("-").append(listLagLngSearch.get(i));
                File newCacheFile = new File(pathCacheDir, strCacheFileName);
                newCacheFile.createNewFile();
                FileOutputStream foCache = new FileOutputStream(newCacheFile.getAbsolutePath());
                foCache.write(strFileContents.toString().getBytes());
                foCache.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (MapActivity.isLoadHistory)
            MapActivity.isLoadHistory = false;


        if (checkTimDuong()) {
            dismiss();

            listPlaceSearch.add(0, txtStartPlace.getText().toString());
            listLagLngSearch.add(0, latLngStartPlace);

            try {
                solutionMap = new SolutionMap(listLagLngSearch, MapFragment.myMap);
                solutionMap.createListRoute();
                // Tạo Progress Bar
                MapActivity.myProgress = new ProgressDialog(getActivity());
                MapActivity.myProgress.setTitle("Map Loading ...");
                MapActivity.myProgress.setMessage("Please wait...");
                MapActivity.myProgress.setCancelable(true);

                // Hiển thị Progress Bar
                MapActivity.myProgress.show();

            } catch (UnsupportedEncodingException e) {
                Toast.makeText(getActivity(), "Không thể tìm được đường đi", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkTimDuong() {
        if (txtStartPlace.getText().length() <= 0) {
            txtStartPlace.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate));
            return false;
        }

        for (int i = 0; i < listPlaceSearch.size(); i++) {
            if (listPlaceSearch.get(i) == null || listPlaceSearch.get(i).equals("") ||
                    listLagLngSearch.get(i).equals("") || listLagLngSearch.get(i) == null) {
                Toast.makeText(getActivity(), "Bạn nhập thiếu địa điểm ! Vui lòng thử lại", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        return true;
    }

    private void clickMyLocation() {
        btnMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                String provider = locationManager.getBestProvider(new Criteria(), true);
                if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                Location locations = locationManager.getLastKnownLocation(provider);
                List<String> providerList = locationManager.getAllProviders();
                if (null != locations && null != providerList && providerList.size() > 0) {
                    double longitude = locations.getLongitude();
                    double latitude = locations.getLatitude();
                    latLngStartPlace = latitude + "," + longitude;
                    Geocoder geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
                    try {
                        List<Address> listAddresses = geocoder.getFromLocation(latitude, longitude, 1);
                        if (null != listAddresses && listAddresses.size() > 0) {
                            String location = listAddresses.get(0).getAddressLine(0);
                            txtStartPlace.setText(location);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void clickStartPlace() {
        txtStartPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
                try {
                    Intent intent =
                            new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                                    .setBoundsBias(new LatLngBounds(
                                            new LatLng(8.407168163601074, 104.1448974609375),
                                            new LatLng(10.7723923007117563, 106.6981029510498)))
                                    .build(getActivity());
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1
                && resultCode == Activity.RESULT_OK) {
            final Place place = PlacePicker.getPlace(data, getContext());

            if (place != null) {
                txtStartPlace.setText(place.getName().toString());
                String[] parts = place.getLatLng().toString().split("\\(");
                String[] parts1 = parts[1].split("\\)");
                latLngStartPlace = parts1[0];
            }
        }
    }

    public void addSearchItem() {
        btnAddSearchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listPlaceSearch.size() < 10) {
                    listPlaceSearch.add("");
                    listLagLngSearch.add("");
                    adapter.notifyDataSetChanged();
                    listViewSearch.setAdapter(adapter);
                } else {
                    Toast.makeText(getActivity(), "Chỉ được nhập tối đa 10 điểm đến", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    public void clickThoat() {
        if (MapActivity.isLoadHistory) {
            listPlaceSearch.clear();
            listLagLngSearch.clear();
            MapActivity.isLoadHistory = false;
        }
        dismissDialog();
    }

    private void clickSpeak() {
        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                voice = true;
                promptSpeechInput();
            }
        });
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.label_Speak));
        try {
            int REQ_CODE_SPEECH_INPUT = 100;
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (MapActivity.isLoadHistory) {
            listPlaceSearch.clear();
            listLagLngSearch.clear();
            MapActivity.isLoadHistory = false;
        }
    }


}
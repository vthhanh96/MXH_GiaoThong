package com.khoaluan.mxhgiaothong.activities.map.algorithm;

import java.util.List;

/**
 * Created by DELL on 12/8/2016.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> listRouteToAddMatrix);
}
package com.khoaluan.mxhgiaothong.restful;

import com.khoaluan.mxhgiaothong.restful.service.PostService;

public class ApiManager {
    private static ApiManager instance = null;

    private static PostService mPostService;

    private ApiManager() {}

    public static synchronized ApiManager getInstance() {
        if(instance == null) {
            instance = new ApiManager();
        }

        return instance;
    }

    public PostService getPostService() {
        ApiGenerator.changeBaseUrl("http://10.0.3.2:3000/api/");
        if(mPostService == null) {
            mPostService = ApiGenerator.createService(PostService.class);
        }
        return mPostService;
    }
}

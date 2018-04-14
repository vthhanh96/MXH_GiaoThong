package com.khoaluan.mxhgiaothong.restful;

import com.khoaluan.mxhgiaothong.restful.service.PostService;
import com.khoaluan.mxhgiaothong.restful.service.UserService;

public class ApiManager {
    private static ApiManager instance = null;

    private static PostService mPostService;
    private static UserService mUserService;

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

    public UserService getUserService(){
        ApiGenerator.changeBaseUrl("http://10.0.2.2:3000/api/user/");
        if(mUserService == null) {
            mUserService = ApiGenerator.createService(UserService.class);
        }
        return mUserService;
    }

}

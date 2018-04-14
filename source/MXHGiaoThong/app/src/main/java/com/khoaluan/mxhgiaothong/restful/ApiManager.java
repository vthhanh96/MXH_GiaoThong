package com.khoaluan.mxhgiaothong.restful;

import com.khoaluan.mxhgiaothong.AppConstants;
import com.khoaluan.mxhgiaothong.restful.service.CategoryService;
import com.khoaluan.mxhgiaothong.restful.service.PostService;
import com.khoaluan.mxhgiaothong.restful.service.UserService;

public class ApiManager {
    private static ApiManager instance = null;

    private static PostService mPostService;
    private static UserService mUserService;
    private static CategoryService mCategoryService;

    private ApiManager() {}

    public static synchronized ApiManager getInstance() {
        if(instance == null) {
            instance = new ApiManager();
        }

        return instance;
    }

    public PostService getPostService() {
        ApiGenerator.changeBaseUrl(AppConstants.BASE_SERVER_URL);
        if(mPostService == null) {
            mPostService = ApiGenerator.createService(PostService.class);
        }
        return mPostService;
    }

    public UserService getUserService(){
        ApiGenerator.changeBaseUrl(AppConstants.BASE_USER_URL);
        if(mUserService == null) {
            mUserService = ApiGenerator.createService(UserService.class);
        }
        return mUserService;
    }

    public CategoryService getCategoryService() {
        ApiGenerator.changeBaseUrl(AppConstants.BASE_SERVER_URL);
        if(mCategoryService == null) {
            mCategoryService = ApiGenerator.createService(CategoryService.class);
        }

        return mCategoryService;
    }
}

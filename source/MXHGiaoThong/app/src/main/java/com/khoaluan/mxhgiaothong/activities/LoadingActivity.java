package com.khoaluan.mxhgiaothong.activities;


import com.daimajia.androidanimations.library.Techniques;
import com.khoaluan.mxhgiaothong.R;
import com.khoaluan.mxhgiaothong.activities.post.ListPostActivity;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

public class LoadingActivity extends AwesomeSplash {


    @Override
    public void initSplash(ConfigSplash configSplash) {
        configSplash.setBackgroundColor(R.color.colorPrimary);
        configSplash.setAnimCircularRevealDuration(2000);
        configSplash.setRevealFlagX(Flags.REVEAL_LEFT);
        configSplash.setRevealFlagY(Flags.REVEAL_TOP);

        configSplash.setLogoSplash(R.drawable.ic_netfic_small);
        configSplash.setAnimLogoSplashDuration(2000);
        configSplash.setAnimLogoSplashTechnique(Techniques.FadeIn); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        configSplash.setTitleSplash("");
    }

    @Override
    public void animationsFinished() {
        ListPostActivity.start(LoadingActivity.this);
    }
}

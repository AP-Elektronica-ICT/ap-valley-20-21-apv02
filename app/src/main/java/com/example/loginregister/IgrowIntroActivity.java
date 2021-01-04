package com.example.loginregister;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;

import com.example.loginregister.ui.dashboard.DashboardFragment;
import com.example.loginregister.ui.dashboard.QrScanner;
import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.FragmentSlide;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;
import com.heinrichreimersoftware.materialintro.slide.Slide;

public class IgrowIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(true);
        setButtonNextVisible(true);
        setButtonCtaVisible(true);
        setButtonCtaTintMode(BUTTON_CTA_TINT_MODE_BACKGROUND);
        TypefaceSpan labelSpan = new TypefaceSpan(
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? "sans-serif-medium" : "sans serif");
        SpannableString label = SpannableString
                .valueOf("Get started");
        label.setSpan(labelSpan, 0, label.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        setButtonCtaLabel(label);

        setPageScrollDuration(500);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setPageScrollInterpolator(android.R.interpolator.fast_out_slow_in);
        }

        addSlide(new SimpleSlide.Builder()
                .background(R.color.white)
                .backgroundDark(R.color.white)
                .layout(R.layout.slide_igrowintro)
                .build());

        addSlide(new SimpleSlide.Builder()
                .background(R.color.white)
                .backgroundDark(R.color.white)
                .layout(R.layout.slide_igrowintro2)
                .build());




        final Slide permissionsSlide;
        permissionsSlide = new SimpleSlide.Builder()
                            .title("Give permisions")
                            .description("this app needs camera, storage and location permission")
                            .background(R.color.white)
                            .scrollable(true)
                            .permissions(new String[]{Manifest.permission.CAMERA,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.RECEIVE_BOOT_COMPLETED})
                            .build();
        addSlide(permissionsSlide);



        final Slide scanSlide;
        scanSlide = new FragmentSlide.Builder()
                    .background(R.color.white)
                    .fragment(DashboardFragment.newInstance())
                    .build();

        addSlide(scanSlide);
        //autoplay(2500, INFINITE);
    }

}
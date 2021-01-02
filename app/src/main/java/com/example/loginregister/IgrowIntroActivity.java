package com.example.loginregister;

import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TypefaceSpan;

import com.heinrichreimersoftware.materialintro.app.IntroActivity;
import com.heinrichreimersoftware.materialintro.slide.SimpleSlide;

public class IgrowIntroActivity extends IntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setButtonBackVisible(false);
        setButtonNextVisible(false);
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
                .title("titel1")
                .description("beschrijving1")
                .image(R.drawable.achtergrondoptie)
                .background(R.color.white)
                .backgroundDark(R.color.white)
                .layout(R.layout.slide_igrowintro)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("titel2")
                .description("beschrijving2")
                .image(R.drawable.achtergrondoptie)
                .background(R.color.white)
                .backgroundDark(R.color.white)
                .layout(R.layout.slide_igrowintro)
                .build());

        addSlide(new SimpleSlide.Builder()
                .title("titel2")
                .description("beschrijving2")
                .image(R.drawable.achtergrondoptie)
                .background(R.color.white)
                .backgroundDark(R.color.white)
                .layout(R.layout.slide_igrowintro)
                .build());

        autoplay(2500, INFINITE);
    }

}
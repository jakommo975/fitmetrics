package com.example.bmi_adam;

import android.view.View;

public class HomePageItem {

    String text;
    boolean withButton;

    View.OnClickListener onClickListener;

    public HomePageItem(String text, boolean withButton, View.OnClickListener onClickListener) {
        this.text = text;
        this.withButton = withButton;
        this.onClickListener = onClickListener;
    }
}
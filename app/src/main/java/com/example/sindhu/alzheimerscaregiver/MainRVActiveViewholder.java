package com.example.sindhu.alzheimerscaregiver;

import android.os.CountDownTimer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainRVActiveViewholder extends RecyclerView.ViewHolder {
    TextView title, time_remaining;
    ImageView imageView;
    CardView finishTaskButton;
    LinearLayout linearLayout, bottomBar;
    CountDownTimer countDownTimer;

    public MainRVActiveViewholder(View itemView) {
        super(itemView);
        title = (TextView) itemView.findViewById(R.id.item_title);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.timeBackground);
        time_remaining = (TextView) itemView.findViewById(R.id.time_remaining);
        finishTaskButton = (CardView) itemView.findViewById(R.id.finishButton);
        bottomBar = (LinearLayout) itemView.findViewById(R.id.bottomBar);
    }
}

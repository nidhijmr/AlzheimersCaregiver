package com.example.sindhu.alzheimerscaregiver;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by neel on 05/11/16 at 1:22 PM.
 */

public class NewTaskViewholder extends RecyclerView.ViewHolder {
    public TextView textView;

    public NewTaskViewholder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.textView);
    }
}

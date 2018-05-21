package com.example.sindhu.alzheimerscaregiver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sindhu on 4/28/18.
 */

public class ImageGridViewAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<ImageDetails> imageList;
    private int imageWidth;
    private int layout;

    public ImageGridViewAdapter(Context c, int layout, ArrayList<ImageDetails> imgList) {
        this.mContext = c;
        this.layout = layout;
        this.imageList = imgList;
    }

    public int getCount() {
        return imageList.size();
    }

    public Object getItem(int position) {
        return imageList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtname, txtrelation;

    }

    public View getView(int position, View convertView, ViewGroup parent) {
        //View row;
        View row = convertView;
        ViewHolder holder = new ViewHolder();

        if (convertView == null) {
            row = new View(mContext);
        } else {
            row = convertView;
        }

        //if (row == null) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(layout, null);

        holder.txtname = (TextView) row.findViewById(R.id.txtName);
        holder.txtrelation = (TextView) row.findViewById(R.id.txtRelation);
        holder.imageView = (ImageView) row.findViewById(R.id.imageView);
        row.setTag(holder);

        ImageDetails imageDetails = imageList.get(position);
        System.out.println("######## --- " + imageList.get(position));
        System.out.println("######## --- " + imageDetails.getName());

        holder.txtname.setText(imageDetails.getName());
        holder.txtrelation.setText(imageDetails.getRelationship());
        byte[] image = imageDetails.getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        holder.imageView.setImageBitmap(bitmap);

        return row;
    }

}

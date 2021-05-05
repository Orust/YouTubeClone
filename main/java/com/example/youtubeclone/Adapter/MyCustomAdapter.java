package com.example.youtubeclone.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youtubeclone.Classifier;
import com.example.youtubeclone.Model.VideoDetails;
import com.example.youtubeclone.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MyCustomAdapter extends BaseAdapter {

    Activity activity;
    ArrayList<VideoDetails> videoDetailsArrayList;
    LayoutInflater inflater;

    public MyCustomAdapter(Activity activity, ArrayList<VideoDetails> videoDetailsArrayList) {
        this.activity = activity;
        this.videoDetailsArrayList = videoDetailsArrayList;
    }

    @Override
    public Object getItem(int position) {
        return this.videoDetailsArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater == null) {
            inflater = this.activity.getLayoutInflater();
        }

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.custom_item,null);
        }

        ImageView imageView =(ImageView)convertView.findViewById(R.id.imageView);
        TextView textView = (TextView)convertView.findViewById(R.id.mytitle);
        LinearLayout linearLayout = (LinearLayout)convertView.findViewById(R.id.root);
        final VideoDetails videoDetails = (VideoDetails)this.videoDetailsArrayList.get(position);


        //videoPlayActivityの呼び出し

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Classifier classifier = (Classifier) activity.getApplication();
                //classifier.classify();
                Log.d("after click video list", classifier.getTask_type());

                Intent i = new Intent(activity, com.example.youtubeclone.VideoPlayActivity.class);
                i.putExtra("videoId",videoDetails.getVideoId());
                i.putExtra("title", videoDetails.getTitle());
                i.putExtra("categoryId", videoDetails.getCategoryId());
                activity.startActivity(i);
            }
        });



        Picasso.get().load(videoDetails.getUrl()).into(imageView);

        textView.setText(videoDetails.getTitle());


        return convertView;
    }

    @Override
    public int getCount() {
        return this.videoDetailsArrayList.size();
    }
}

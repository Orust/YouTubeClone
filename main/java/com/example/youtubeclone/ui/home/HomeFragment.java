package com.example.youtubeclone.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.youtubeclone.Adapter.MyCustomAdapter;
import com.example.youtubeclone.Classifier;
import com.example.youtubeclone.Common;
import com.example.youtubeclone.Model.VideoDetails;
import com.example.youtubeclone.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    ListView listView;
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        listView = (ListView)root.findViewById(R.id.listView);
        videoDetailsArrayList = new ArrayList<>();

        final Classifier classifier = (Classifier) getActivity().getApplication();


        //ここでViewModelからvideoDetailsArrayListもらう

        homeViewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<ArrayList<VideoDetails>>() {

            @Override
            public void onChanged(@Nullable ArrayList<VideoDetails> v) {
                videoDetailsArrayList = v;
                Log.d("VDA in fragment", Integer.toString(videoDetailsArrayList.size()));

                myCustomAdapter = new MyCustomAdapter(getActivity(), videoDetailsArrayList);

                listView.setAdapter(myCustomAdapter);
                myCustomAdapter.notifyDataSetChanged();


                if (classifier.getCount() == 0) {
                    classifier.setIndex(0);
                }

                listView.setOnScrollListener(new ListView.OnScrollListener() {

                    @Override
                    public void onScrollStateChanged(AbsListView absListView, int i) {

                    }

                    @Override
                    public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                        if (classifier.getCount() < 2) {
                            Log.d("first item index", Integer.toString(firstVisibleItem));

                            if (classifier.getIndex() != firstVisibleItem) {
                                classifier.setIndex(firstVisibleItem);
                                classifier.setScroll_depth(classifier.getScroll_depth() + 1);
                                Log.d("scroll depth", Integer.toString(classifier.getScroll_depth()));
                            }
                            //Log.d("scroll depth", Integer.toString(classifier.getScroll_depth() + firstVisibleItem));
                            //classifier.setScroll_depth(firstVisibleItem);
                        }
                    }
                });
            }

        });

        return root;
    }



}
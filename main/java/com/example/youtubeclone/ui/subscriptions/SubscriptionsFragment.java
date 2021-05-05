package com.example.youtubeclone.ui.subscriptions;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

//import com.example.youtubeclone.Common;
import com.example.youtubeclone.Adapter.MyCustomAdapter;
import com.example.youtubeclone.Classifier;
import com.example.youtubeclone.MainActivity;
import com.example.youtubeclone.Model.VideoDetails;
import com.example.youtubeclone.R;
import com.example.youtubeclone.SearchResultsActivity;

import java.util.ArrayList;

public class SubscriptionsFragment extends Fragment {

    private SubscriptionsViewModel subscriptionsViewModel;
    MainActivity ma = (MainActivity)getActivity();
    //String at = ma.common.getAccessToken();

    ListView listView;
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        subscriptionsViewModel =
                ViewModelProviders.of(this).get(SubscriptionsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_subscriptions, container, false);
        /*
        final TextView textView = root.findViewById(R.id.text_subscriptions);
        subscriptionsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s + common.getAccessToken());
                //textView.setText(s + at);
                textView.setText(s);
            }
        });

         */

        listView = (ListView)root.findViewById(R.id.listView);
        videoDetailsArrayList = new ArrayList<>();

        final Classifier classifier = (Classifier) getActivity().getApplication();

        //ここでViewModelからvideoDetailsArrayListもらう

        subscriptionsViewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<ArrayList<VideoDetails>>() {

            @Override
            public void onChanged(@Nullable ArrayList<VideoDetails> v) {
                videoDetailsArrayList = v;
                Log.d("VDA in fragment", Integer.toString(videoDetailsArrayList.size()));

                myCustomAdapter = new MyCustomAdapter(getActivity(), videoDetailsArrayList);

                listView.setAdapter(myCustomAdapter);
                myCustomAdapter.notifyDataSetChanged();


                //ここからスクロール深度チェック
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
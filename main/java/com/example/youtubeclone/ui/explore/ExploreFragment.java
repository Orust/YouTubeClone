package com.example.youtubeclone.ui.explore;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.example.youtubeclone.Adapter.MyCustomAdapter;
import com.example.youtubeclone.Classifier;
import com.example.youtubeclone.MainActivity;
import com.example.youtubeclone.Model.VideoDetails;
import com.example.youtubeclone.R;
import com.example.youtubeclone.ui.home.HomeViewModel;

import java.util.ArrayList;

public class ExploreFragment extends Fragment {

    private ExploreViewModel exploreViewModel;


    ListView listView;
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        exploreViewModel =
                ViewModelProviders.of(this).get(ExploreViewModel.class);
        View root = inflater.inflate(R.layout.fragment_explore, container, false);

        listView = (ListView)root.findViewById(R.id.listView);
        videoDetailsArrayList = new ArrayList<>();

        final Classifier classifier = (Classifier) getActivity().getApplication();

        //ここでViewModelからvideoDetailsArrayListもらう

        exploreViewModel.getPlaylists().observe(getViewLifecycleOwner(), new Observer<ArrayList<VideoDetails>>() {

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

                //↓たぶん許されない
                /*
                MainActivity ma = (MainActivity)getActivity();
                ma.common.setScroll_depth(y);

                 */
            }

        });

        return root;
    }


    /*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

     */
}
package com.example.youtubeclone.ui.explore;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtubeclone.Classifier;
import com.example.youtubeclone.Model.VideoDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ExploreViewModel extends AndroidViewModel {

    private ArrayList<String> homeChannelsIdList;
    private ArrayList<VideoDetails> videoDetailsArrayList;
    private MutableLiveData<ArrayList<VideoDetails>> mVideoDetailsArrayList;


    //to get subscribed channels Id
    private String url;

    public ExploreViewModel(Application application) {
        super(application);

        final Classifier common = (Classifier) getApplication();

            url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&maxResults=16&chart=mostPopular&regionCode=JP&key=" +
                    common.getAPI_KEY();

            mVideoDetailsArrayList = new MutableLiveData<>();
            homeChannelsIdList = new ArrayList<>();
            videoDetailsArrayList = new ArrayList<>();

            mVideoDetailsArrayList.setValue(videoDetailsArrayList);

            RequestQueue requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");

                        for(int i = 0; i<jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            //JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                            JSONObject jsonObjectSnippet = jsonObject1.getJSONObject("snippet");
                            //JSONObject jsonTitle = jsonObjectSnippet.getJSONObject("title");

                            JSONObject jsonObjectDefault = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");
                            //JSONObject jsonObjectTitle = jsonObjectSnippet.getJSONObject("title");

                            String id = jsonObject1.getString("id");
                            String title = jsonObjectSnippet.getString("title");

                            VideoDetails vd = new VideoDetails();

                            vd.setVideoId(id);
                            vd.setTitle(title);
                            vd.setDescription("description");
                            vd.setUrl(jsonObjectDefault.getString("url"));

                            String categoryId = jsonObjectSnippet.getString("categoryId");
                            vd.setCategoryId(categoryId);

                            videoDetailsArrayList.add(vd);

                        }

                        mVideoDetailsArrayList.setValue(videoDetailsArrayList);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    common.changeAPI_KEY();
                    Toast.makeText(getApplication().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            );

            requestQueue.add(stringRequest);


    }


    public LiveData<ArrayList<VideoDetails>> getPlaylists() { return mVideoDetailsArrayList; }
}
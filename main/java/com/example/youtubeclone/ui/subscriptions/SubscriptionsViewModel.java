package com.example.youtubeclone.ui.subscriptions;

import android.app.Application;
import android.util.Log;
import android.widget.ListView;
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
import com.example.youtubeclone.Adapter.MyCustomAdapter;
import com.example.youtubeclone.Classifier;
import com.example.youtubeclone.Common;
import com.example.youtubeclone.MainActivity;
import com.example.youtubeclone.Model.VideoDetails;
import com.example.youtubeclone.R;
import com.example.youtubeclone.SearchResultsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class SubscriptionsViewModel extends AndroidViewModel {

    //private MutableLiveData<String> mText;
    //Common common = (Common)getApplication();
    //ListView listView;
    private ArrayList<String> subscribedChannelsIdList;
    private ArrayList<VideoDetails> videoDetailsArrayList;
    private MutableLiveData<ArrayList<VideoDetails>> mVideoDetailsArrayList;
    //MyCustomAdapter myCustomAdapter;

    //to get subscribed channels Id
    private String url1;
    //to get detailed video list
    private String url2;


    public SubscriptionsViewModel(Application application) {
        super(application);
        Classifier common = (Classifier) getApplication();

        if (!(common.getAuthorized())) {
            int max_results = 6;
            int channelId_size = common.getName().get(common.getParticipant()).getChannel_ids().size();
            if (channelId_size == 1) {
                max_results = 20;
            } else if (channelId_size == 2) {
                max_results = 10;
            }
            url1 = "https://www.googleapis.com/youtube/v3/search?part=snippet&order=date&maxResults=" +
                    max_results +"&key=" +
                    common.getAPI_KEY() + "&channelId=";
            url2 = "";

            mVideoDetailsArrayList = new MutableLiveData<>();
            subscribedChannelsIdList = new ArrayList<>();
            videoDetailsArrayList = new ArrayList<>();

            mVideoDetailsArrayList.setValue(videoDetailsArrayList);

            for (int i = 0; i < common.getName().get(common.getParticipant()).getChannel_ids().size(); i++) {
                subscribedChannelsIdList.add(common.getName().get(common.getParticipant()).getChannel_ids().get(i));
            }

            RequestQueue requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());


            for (int i = 0; i < subscribedChannelsIdList.size(); i++) {
                String url = url1 + subscribedChannelsIdList.get(i);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("to get new videos:",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");


                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                                JSONObject jsonObjectSnippet = jsonObject1.getJSONObject("snippet");

                                String publishedAt = jsonObjectSnippet.getString("publishedAt");

                                JSONObject jsonObjectDefault = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                                String video_id = jsonVideoId.getString("videoId");
                                String title = jsonObjectSnippet.getString("title");

                                VideoDetails vd = new VideoDetails();

                                vd.setVideoId(video_id);
                                vd.setTitle(title);
                                vd.setDescription("description");
                                vd.setUrl(jsonObjectDefault.getString("url"));

                                videoDetailsArrayList.add(vd);
                            }

                            //mVideoDetailsArrayList = new MutableLiveData<>();
                            Log.d("final vDAL", Integer.toString(videoDetailsArrayList.size()));


                            mVideoDetailsArrayList.setValue(videoDetailsArrayList);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplication().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                );

                if (i == subscribedChannelsIdList.size()) {
                    Log.d("finalI", Integer.toString(i));

                    mVideoDetailsArrayList.setValue(videoDetailsArrayList);
                }

                requestQueue.add(stringRequest);
                Log.d("check vDAL", Integer.toString(videoDetailsArrayList.size()));


            }

        } else {
            url1 = "https://www.googleapis.com/youtube/v3/subscriptions?part=snippet&maxResults=20&mine=true&access_token=";
            url2 = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&key=AIzaSyB4GVmXhvgtj6YAoYBXwFhSu6LLXKKNbJc&channelId=";
            //url2 = "https://www.googleapis.com/youtube/v3/playlists?part=snippet&key=AIzaSyB4GVmXhvgtj6YAoYBXwFhSu6LLXKKNbJc&channelId=";


            /*
            mText = new MutableLiveData<>();
            Classifier common = (Classifier) getApplication();
            mText.setValue("This is subscriptions fragment" + common.getAccessToken());

             */

            //listView = (ListView)view.findViewById(R.id.listView);

            mVideoDetailsArrayList = new MutableLiveData<>();
            subscribedChannelsIdList = new ArrayList<>();
            videoDetailsArrayList = new ArrayList<>();

            mVideoDetailsArrayList.setValue(videoDetailsArrayList);

            final RequestQueue requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());
            url1 += common.getAccessToken();

            StringRequest stringRequest1 = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("to get ChannelIds:", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");

                        Log.d("JsonArrayList", Integer.toString(jsonArray.length()));
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            //JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                            JSONObject jsonObjectSnippet = jsonObject1.getJSONObject("snippet");

                            String channelId = jsonObjectSnippet.getJSONObject("resourceId").getString("channelId");

                            subscribedChannelsIdList.add(channelId);

                        }
                        Log.d("ChannelIdsList.length:", Integer.toString(subscribedChannelsIdList.size()));

                        for (int i = 0; i < subscribedChannelsIdList.size(); i++) {
                            String url = url2 + subscribedChannelsIdList.get(i);

                            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //Log.d("to get new videos:",response);

                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        JSONArray jsonArray = jsonObject.getJSONArray("items");

                                        JSONObject jsonObject0 = jsonArray.getJSONObject(0);
                                        JSONObject jsonVideoId = jsonObject0.getJSONObject("id");
                                        JSONObject jsonObjectSnippet = jsonObject0.getJSONObject("snippet");

                                        String latestPublishAt = jsonObjectSnippet.getString("publishedAt");

                                        JSONObject jsonObjectDefault = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                                        String video_id = jsonVideoId.getString("videoId");
                                        String title = jsonObjectSnippet.getString("title");

                                        VideoDetails vd = new VideoDetails();

                                        vd.setVideoId(video_id);
                                        vd.setTitle(title);
                                        vd.setDescription("description");
                                        vd.setUrl(jsonObjectDefault.getString("url"));

                                        videoDetailsArrayList.add(vd);
                                        Log.d("videoDetailsArrayList1", Integer.toString(videoDetailsArrayList.size()));

                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                            jsonVideoId = jsonObject1.getJSONObject("id");
                                            jsonObjectSnippet = jsonObject1.getJSONObject("snippet");

                                            String publishedAt = jsonObjectSnippet.getString("publishedAt");

                                            if (publishedAt.compareTo(latestPublishAt) > 0) {
                                                videoDetailsArrayList.remove(videoDetailsArrayList.size() - 1);
                                                Log.d("removeVDArrayList", Integer.toString(videoDetailsArrayList.size()));

                                                jsonObjectDefault = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");

                                                video_id = jsonVideoId.getString("videoId");
                                                title = jsonObjectSnippet.getString("title");

                                                vd = new VideoDetails();

                                                vd.setVideoId(video_id);
                                                vd.setTitle(title);
                                                vd.setDescription("description");
                                                vd.setUrl(jsonObjectDefault.getString("url"));

                                                videoDetailsArrayList.add(vd);
                                                Log.d("addVDArrayList", Integer.toString(videoDetailsArrayList.size()));
                                            }

                                        }

                                        //mVideoDetailsArrayList = new MutableLiveData<>();
                                        Log.d("final vDAL", Integer.toString(videoDetailsArrayList.size()));


                                        mVideoDetailsArrayList.setValue(videoDetailsArrayList);


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(getApplication().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                            );

                            if (i == subscribedChannelsIdList.size()) {
                                Log.d("finalI", Integer.toString(i));

                                mVideoDetailsArrayList.setValue(videoDetailsArrayList);
                            }

                            requestQueue.add(stringRequest2);
                            Log.d("check vDAL", Integer.toString(videoDetailsArrayList.size()));


                        }


                        Log.d("#videoDetailsArrayList", Integer.toString(videoDetailsArrayList.size()));
                        //mVideoDetailsArrayList.setValue(videoDetailsArrayList);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplication().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            );

            requestQueue.add(stringRequest1);

            //Log.d("ChannelIdsList.length:", Integer.toString(subscribedChannelsIdList.size()));

            //たぶんこれ全く回ってない，LIｓｔSize０
            //これを上のふぉｒ分の名kに入れ個すると多分うまくいく

            //Log.d("ChannelIdsList.length2", Integer.toString(subscribedChannelsIdList.size()));

            //videoDetailArrayListがこの時点で空


            //Log.d("first vDAL", Integer.toString(videoDetailsArrayList.size()));
            //mVideoDetailsArrayList.setValue(videoDetailsArrayList);


            //requestQueue.add(stringRequest);
            //myCustomAdapter = new MyCustomAdapter(SearchResultsActivity.this, videoDetailsArrayList);
        }

    }

    /*
    public LiveData<String> getText() {
        return mText;
    }

     */

    public LiveData<ArrayList<VideoDetails>> getPlaylists() { return mVideoDetailsArrayList; }
}
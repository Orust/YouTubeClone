package com.example.youtubeclone.ui.home;

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
import com.example.youtubeclone.Common;
import com.example.youtubeclone.Model.VideoDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeViewModel extends AndroidViewModel {

    private ArrayList<VideoDetails> videoDetailsArrayList;
    private MutableLiveData<ArrayList<VideoDetails>> mVideoDetailsArrayList;


    //to get
    private String url;

    public HomeViewModel(Application application) {
        super(application);

        Classifier common = (Classifier) getApplication();

        if (common.getAuthorized()) {
            url = "https://www.googleapis.com/youtube/v3/activities?part=snippet&mine=true&access_token=";
            //url2 = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&key=AIzaSyB4GVmXhvgtj6YAoYBXwFhSu6LLXKKNbJc&channelId=";

            mVideoDetailsArrayList = new MutableLiveData<>();
            videoDetailsArrayList = new ArrayList<>();

            common.init();

            /*

            url1 = "https://www.googleapis.com/youtube/v3/activities?part=snippet&maxResults=3&mine=true&access_token=";
            url2 = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=3&key=AIzaSyB4GVmXhvgtj6YAoYBXwFhSu6LLXKKNbJc&channelId=";


            mVideoDetailsArrayList = new MutableLiveData<>();
            homeChannelsIdList = new ArrayList<>();
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

                            String channelId = jsonObjectSnippet.getString("channelId");

                            homeChannelsIdList.add(channelId);

                        }
                        Log.d("ChannelIdsList.length:", Integer.toString(homeChannelsIdList.size()));

                        for (int i = 0; i < homeChannelsIdList.size(); i++) {
                            String url = url2 + homeChannelsIdList.get(i);

                            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Log.d("to get new videos:", response);

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

                            if (i == homeChannelsIdList.size()) {
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

             */
        } else if (common.getParticipant() == "none") {
            url = "https://www.googleapis.com/youtube/v3/activities?part=snippet&mine=true&access_token=";

            mVideoDetailsArrayList = new MutableLiveData<>();
            videoDetailsArrayList = new ArrayList<>();

            common.init();

        } else {
            url = "https://www.googleapis.com/youtube/v3/videos?part=snippet&maxResults=5&chart=mostPopular&regionCode=JP&key=" +
                    common.getAPI_KEY() + "&videoCategoryId=";


            mVideoDetailsArrayList = new MutableLiveData<>();
            videoDetailsArrayList = new ArrayList<>();

            mVideoDetailsArrayList.setValue(videoDetailsArrayList);

            //Log.d("else part", "true");
            //Log.d("get category[0]", Integer.toString(common.getName().get(common.getParticipant()).getCategories().get(0)));
            //Log.d("get category size", Integer.toString(common.getName().get(common.getParticipant()).getCategories().size()));
            //参加者情報は正しく取得できている
            if (common.getCount() == 0) {
                common.setMillis(System.currentTimeMillis());
                Log.d("first time", String.valueOf(System.currentTimeMillis()));
            }

            //この後一週目の動画リスト4つは取得できたっぽい，二週目以降に問題．Exploreと違うのもここだけ（loopｇふぁあること）
            RequestQueue requestQueue = Volley.newRequestQueue(getApplication().getApplicationContext());

            for (int i = 0; i < common.getName().get(common.getParticipant()).getCategories().size(); i++){
                String url1 = url + common.getName().get(common.getParticipant()).getCategories().get(i);


                StringRequest stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("to get videos for home:",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");

                            for (int i = 0; i < jsonArray.length(); i++) {
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
                        Toast.makeText(getApplication().getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                );

                requestQueue.add(stringRequest);
            }


        }
    }


    public LiveData<ArrayList<VideoDetails>> getPlaylists() { return mVideoDetailsArrayList; }
}
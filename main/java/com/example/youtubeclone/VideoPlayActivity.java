package com.example.youtubeclone;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtubeclone.Adapter.MyCustomAdapter;
import com.example.youtubeclone.Model.VideoDetails;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.client.json.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class VideoPlayActivity extends YouTubeBaseActivity {

    private Context context;
    private File file;
    private String filename = "data1.txt";

    OutputStream out;

    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;

    Classifier classifier;
    String url;
    String rurl;

    ListView listView;
    public YouTubePlayer youTubePlayer;

    YouTubePlayerView mYouTubePlayerView;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;
    YouTubePlayer.PlaybackEventListener mPlaybackEventListener;

    long millis;
    Boolean quitThread = false;
    int pre_index = 0;

    public void setYouTubePlayer(YouTubePlayer youTubePlayer) {
        this.youTubePlayer = youTubePlayer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        classifier = (Classifier) this.getApplication();

        classifier.setCumulative_clicks(classifier.getCumulative_clicks() + 1);

        final Intent intent = getIntent();

        mYouTubePlayerView = findViewById(R.id.YouTube_view);


        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                //youTubePlayer.loadVideo("BtwBxBDE39s");
                setYouTubePlayer(youTubePlayer);

                /*
                if (classifier.getCount() == 0) {
                    millis = System.currentTimeMillis();
                    classifier.setMillis(millis);
                    Log.d("first time", String.valueOf(millis));
                }

                 */

                //ココ注意，追加するものがNullだと強制終了しちゃう
                //classifier.add_history(intent.getStringExtra("title"));
                youTubePlayer.loadVideo(intent.getStringExtra("videoId"));

                classifier.setCount(classifier.getCount() + 1);
                Log.d("Count in vpa", Integer.toString(classifier.getCount()));
                if (classifier.getCount() == 2) {
                    //long reading_second = TimeUnit.MILLISECONDS.toSeconds(millis - youTubePlayer.getCurrentTimeMillis());
                    //Log.d("second time", String.valueOf(System.currentTimeMillis()));
                    //Log.d("millis at second time", String.valueOf(millis));
                    long reading_second = TimeUnit.MILLISECONDS.toSeconds((long) System.currentTimeMillis() - classifier.getMillis());
                    Log.d("duration", String.valueOf(System.currentTimeMillis() - classifier.getMillis()));
                    classifier.setReading_time((int) reading_second);


                    file = new File(getApplicationContext().getFilesDir(), filename);

                    classifier.classify();

                    Log.d("in video playing", "try to write\n\n\n");

                    try {
                        out = openFileOutput(filename,MODE_PRIVATE|MODE_APPEND);
                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(out,"UTF-8"));

                        //追記する
                        writer.append(classifier.getParticipant() + "\n");
                        writer.append(classifier.getQuery_length() + "," +
                                classifier.getReading_time() + "," +
                                classifier.getScroll_depth() + "," +
                                classifier.getTask_type() + "\n");
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    /*
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(classifier.getParticipant() + "\n");
                        writer.write(classifier.getQuery_length() + "," +
                                classifier.getReading_time() + "," +
                                classifier.getScroll_depth() + "," +
                                classifier.getTask_type() + "\n");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                     */


                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                //getErrorDialog(this, int)
            }
        };

        mYouTubePlayerView.initialize(classifier.getAPI_KEY(), mOnInitializedListener);


        mPlaybackEventListener = new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {


            }

            @Override
            public void onPaused() {
                //youTubePlayer.getCurrentTimeMillis();
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onStopped() {
                Log.d("in VPA", "when stopped\n\n\n");
                /*
                long reading_second = TimeUnit.MILLISECONDS.toSeconds(millis - youTubePlayer.getCurrentTimeMillis());
                classifier.setReading_time((int) reading_second);
                classifier.setCount(classifier.getCount() + 1);
                if (classifier.getCount() == 1) {
                    file = new File(context.getFilesDir(), filename);

                    //classifier.classify();

                    Log.d("in video playing", "try to write\n\n\n");
                    try (FileWriter writer = new FileWriter(file)) {
                        writer.write(classifier.getParticipant() + "\n");
                        writer.write(classifier.getQuery_length() + "," +
                                classifier.getReading_time() + "," +
                                classifier.getScroll_depth() + "," +
                                "lookup" + "\n");
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }


                    FileOutputStream output = null;
                    try {
                        output = openFileOutput(filename, Context.MODE_WORLD_READABLE);

                        output.write(classifier.getParticipant().getBytes());
                        output.write("¥r¥n".getBytes());
                        output.write(classifier.getQuery_length());
                        output.write(",".getBytes());
                        output.write(classifier.getReading_time());
                        output.write(",".getBytes());
                        output.write(classifier.getCumulative_clicks());
                        output.write(",".getBytes());
                        output.write("lookup".getBytes());
                        output.write("¥r¥n".getBytes());

                        output.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
                quitThread = true;

                 */
            }

            @Override
            public void onBuffering(boolean b) {

            }

            @Override
            public void onSeekTo(int i) {

            }
        };



        //readingtimeの記録

        /*
        boolean first = true;
        int currentVideoTime = youTubePlayer.getCurrentTimeMillis();
        long startTime = currentVideoTime;
        long updateTime;
        while (!quitThread) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            if (youTubePlayer.isPlaying()) {
                if (currentVideoTime == youTubePlayer.getCurrentTimeMillis()) {
                    if (first) {
                        startTime = SystemClock.uptimeMillis();
                        first = false;
                    }
                    long timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
                    updateTime = currentVideoTime + timeInMilliseconds;
                    classifier.setReading_time((int) updateTime);
                } else {
                    updateTime = currentVideoTime = youTubePlayer.getCurrentTimeMillis();
                    first = true;
                    classifier.setReading_time((int) updateTime);
                }
            }
        }

         */


        //ここから関連動画表示

        listView = (ListView)findViewById(R.id.listView);
        videoDetailsArrayList = new ArrayList<>();

        myCustomAdapter = new MyCustomAdapter(VideoPlayActivity.this, videoDetailsArrayList);

        final RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        url = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=20&key=" +
                classifier.getAPI_KEY() + "&type=video&relatedToVideoId=";
        String url1 =  url + intent.getStringExtra("videoId");
        Log.d("related by id", intent.getStringExtra("videoId"));

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    //Log.d("JSONObject", response);

                    for(int i = 0; i<jsonArray.length(); i++) {
                        //各タスクタイプに応じた推薦
                        if (i==2 && classifier.getSystem()) {
                            Participant p = classifier.getName().get(classifier.getParticipant());

                            if (classifier.getTask_type() == "exploration") {
                                int index = new Random().nextInt(p.getCategories().size());
                                Log.d("task_type", classifier.getTask_type() +
                                        ", category=" + (intent.getStringExtra("categoryId")=="" ?
                                        String.valueOf(p.getCategories().get(index)) : intent.getStringExtra("categoryId")));
                                Log.d("by user", String.valueOf(p.getCategories().get(index)));
                                Log.d("by pre video", intent.getStringExtra("categoryId"));

                                rurl = "https://www.googleapis.com/youtube/v3/videos?part=snippet&maxResults=2&chart=mostPopular&regionCode=JP&key=" +
                                        classifier.getAPI_KEY() + "&videoCategoryId=" +
                                        (intent.getStringExtra("categoryId")=="" ?
                                                p.getCategories().get(index) : intent.getStringExtra("categoryId"));

                                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, rurl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        //Log.d("to get new videos:",response);
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

                                                videoDetailsArrayList.add(2, vd);

                                            }
                                            listView.setAdapter(myCustomAdapter);
                                            myCustomAdapter.notifyDataSetChanged();


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

                                requestQueue.add(stringRequest2);
                            } else if (classifier.getTask_type() == "repeat") {
                                Log.d("task_type", classifier.getTask_type());
                                int index = new Random().nextInt(p.getChannel_ids().size());
                                rurl = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=2&key=" +
                                        classifier.getAPI_KEY() +
                                        "&channelId=" +
                                        p.getChannel_ids().get(index);
                                StringRequest stringRequest2 = new StringRequest(Request.Method.GET, rurl, new Response.Listener<String>() {
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


                                                videoDetailsArrayList.add(2, vd);
                                            }

                                            //mVideoDetailsArrayList = new MutableLiveData<>();
                                            //Log.d("final vDAL", Integer.toString(videoDetailsArrayList.size()));

                                            listView.setAdapter(myCustomAdapter);
                                            myCustomAdapter.notifyDataSetChanged();

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

                                requestQueue.add(stringRequest2);

                            }
                        }

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
                        if (jsonObject1.has("snippet")) {
                            JSONObject jsonObjectSnippet = jsonObject1.getJSONObject("snippet");


                            //JSONObject jsonTitle = jsonObjectSnippet.getJSONObject("title");

                            JSONObject jsonObjectDefault = jsonObjectSnippet.getJSONObject("thumbnails").getJSONObject("medium");
                            //JSONObject jsonObjectTitle = jsonObjectSnippet.getJSONObject("title");

                            String video_id = jsonVideoId.getString("videoId");
                            String title = jsonObjectSnippet.getString("title");

                            VideoDetails vd = new VideoDetails();

                            vd.setVideoId(video_id);
                            vd.setTitle(title);
                            vd.setDescription("description");
                            vd.setUrl(jsonObjectDefault.getString("url"));


                            videoDetailsArrayList.add(vd);
                        }

                    }



                    listView.setAdapter(myCustomAdapter);
                    myCustomAdapter.notifyDataSetChanged();



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        );

        requestQueue.add(stringRequest);


        //ここからスクロールリスナー
        listView.setOnScrollListener(new ListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (classifier.getCount() < 2) {
                    Log.d("first item index", Integer.toString(firstVisibleItem));

                    if (pre_index != firstVisibleItem) {
                        pre_index = firstVisibleItem;
                        classifier.setScroll_depth(classifier.getScroll_depth() + 1);
                        Log.d("scroll depth", Integer.toString(classifier.getScroll_depth()));
                    }
                    //Log.d("scroll depth", Integer.toString(classifier.getScroll_depth() + firstVisibleItem));
                    //classifier.setScroll_depth(firstVisibleItem);
                }
            }
        });

    }
}

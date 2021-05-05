package com.example.youtubeclone;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtubeclone.Adapter.MyCustomAdapter;
import com.example.youtubeclone.Model.VideoDetails;
import com.example.youtubeclone.ui.search.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    //private final String API_KEY = "AIzaSyB4GVmXhvgtj6YAoYBXwFhSu6LLXKKNbJc";
    ArrayList<VideoDetails> videoDetailsArrayList;
    MyCustomAdapter myCustomAdapter;

    Classifier classifier;
    //Classifier classifier = (Classifier) this.getApplication();

    String url;

    ListView listView;


    int pre_index = 0;


    /** Global instance of the HTTP transport. */
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /** Global instance of the max number of videos we want returned (50 = upper limit per page). */
    private static final long NUMBER_OF_VIDEOS_RETURNED = 25;

    /** Global instance of Youtube object to make all API requests. */
    private static YouTube youtube;
    //private SearchFragment searchfragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);


        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_subscriptions,
                R.id.navigation_notifications, R.id.navigation_library)
                .build();
        //NavController navController = getNavController();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        //NavigationUI.setupWithNavController(navView, navController);

        //Navigation.findNavController().navigate(R.id.navigation_search);

        listView = (ListView)findViewById(R.id.listView);
        videoDetailsArrayList = new ArrayList<>();

        myCustomAdapter = new MyCustomAdapter(SearchResultsActivity.this, videoDetailsArrayList);

        //Classifier

        classifier = (Classifier)  this.getApplication();

        //displayVideos();

        //setContentView(R.layout.activity_main);
        //recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        //videoDetailsArrayList = new ArrayList<>();




        handleIntent(getIntent());
    }

    private void displayVideos(String query) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        url = "https://www.googleapis.com/youtube/v3/search?part=snippet&type=video&maxResults=20&key=" +
                classifier.getAPI_KEY() + "&q=";
        String url1 = url + query;
        classifier.setQuery_length(query);
        //classifier.classify();

        //Log.d("exploration", "b=" + classifier.getTask_type());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("items");

                    for(int i = 0; i<jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        JSONObject jsonVideoId = jsonObject1.getJSONObject("id");
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

    @Override
    protected void onNewIntent(Intent intent) {
        //...
        super.onNewIntent(intent);
        handleIntent(intent);
    }



    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            displayVideos(query);

            try {
                /*
                 * The YouTube object is used to make all API requests. The last argument is required, but
                 * because we don't need anything initialized when the HttpRequest is initialized, we override
                 * the interface and provide a no-op function.
                 */
                youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                    public void initialize(HttpRequest request) throws IOException {}
                }).setApplicationName("youtube-cmdline-search-sample").build();

                // Get query term from user.
                //String queryTerm = getInputQuery();
                //String queryTerm = "abc";

                YouTube.Search.List search = youtube.search().list("id,snippet");
                /*
                 * It is important to set your API key from the Google Developer Console for
                 * non-authenticated requests (found under the Credentials tab at this link:
                 * console.developers.google.com/). This is good practice and increased your quota.
                 */
                //String apiKey = properties.getProperty("youtube.apikey");
                //String apiKey = "AIzaSyDLewcKRA1tOZ0C6Is5mM02kaWSyKW3t0s";
                //"youtube.apikey" is replaced by API_KEY
                //search.setKey(apiKey);
                //search.setKey(API_KEY);
                search.setQ(query);
                /*
                 * We are only searching for videos (not playlists or channels). If we were searching for
                 * more, we would add them as a string like this: "video,playlist,channel".
                 */
                search.setType("video");
                /*
                 * This method reduces the info returned to only the fields we need and makes calls more
                 * efficient.
                 */
                search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
                search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
                //SearchListResponse searchResponse = search.execute();

                //List<SearchResult> searchResultList = searchResponse.getItems();
                //searchResultList = (ArrayList<SearchResult>) searchResultList;

                //↑をフラグメントに渡してビューを定義したい
                //もしくはクエリだけフラグメントにわたしてフラグメントで検索させる

                /*
                FragmentManager fm = getFragmentManager();
                FragmentTransaction t = fm.beginTransaction();

                SearchFragment fragment = new SearchFragment();
                Bundle bundle = new Bundle();

                 */

                //bundle.putIntegerArrayList("sr", searchResultList);

                // フラグメントに渡す値をセット

                /*
                fragment.setArguments(bundle);
                //t.add(R.id.search_fragment, fragment, "sample_fragment");
                t.commit();
                */

                /*
                SearchFragment newFragment = new SearchFragment();
                Bundle args = new Bundle();
                args.putInt(SearchFragment.ARG_POSITION, position);
                newFragment.setArguments(args);

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack so the user can navigate back
                transaction.replace(R.id.fragment_container, newFragment);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();

                 */


                /*
                if (searchResultList != null) {
                    searchResult(searchResultList.iterator(), query);

                }

                 */
            } catch (GoogleJsonResponseException e) {
                System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                        + e.getDetails().getMessage());
            } catch (IOException e) {
                System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    private static void searchResult(Iterator<SearchResult> iteratorSearchResults, String query) {
        if (!iteratorSearchResults.hasNext()) {
            //System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Double checks the kind is video.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");

                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView


        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));


        /*
        MenuItem menuItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) menuItem.getActionView();
        mSearchView.setOnQueryTextListener(this.onQueryTextListener);

         */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        } else if (id == R.id.action_view_account) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private NavController getNavController() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (!(fragment instanceof NavHostFragment)) {
            throw new IllegalStateException("Activity " + this
                    + " does not have a NavHostFragment");
        }
        return ((NavHostFragment) fragment).getNavController();
    }


}


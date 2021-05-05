package com.example.youtubeclone;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.common.net.UrlEscapers;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import static com.example.youtubeclone.R.id.nav_host_fragment;
import com.example.youtubeclone.Common;

public class MainActivity extends AppCompatActivity {
    //implements SearchFragment.SearchFragmentListener


    //private final String API_KEY = "AIzaSyB4GVmXhvgtj6YAoYBXwFhSu6LLXKKNbJc";

    SearchView mSearchView;
    public Classifier common;


    @NonNull
    private NavController getNavController() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (!(fragment instanceof NavHostFragment)) {
            throw new IllegalStateException("Activity " + this
                    + " does not have a NavHostFragment");
        }
        return ((NavHostFragment) fragment).getNavController();
    }

    private SearchView.OnQueryTextListener onQueryTextListener  = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String searchWord) {
            // ★ 検索ボタンでここが呼ばれる
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            return false;
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_explore, R.id.navigation_subscriptions,
                R.id.navigation_notifications, R.id.navigation_library)
                .build();
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavController navController = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).;
        NavController navController = getNavController();
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        common = (Classifier) this.getApplication();
        //common.init();


        if (common ==null) {
            Intent i = new Intent(MainActivity.this, com.example.youtubeclone.AccountActivity.class);
            MainActivity.this.startActivity(i);
        }



        //handleIntent(getIntent());

        /**
         * Prints a list of videos based on a search term.
         *
         */

        /*
        Properties properties = new Properties();
        try {
            try (InputStream in = YouTube.Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME)) {
                properties.load(in);
            }

        } catch (IOException e) {
            System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
                    + " : " + e.getMessage());
            System.exit(1);
        }
        */

        /*
        try {
            /*
             * The YouTube object is used to make all API requests. The last argument is required, but
             * because we don't need anything initialized when the HttpRequest is initialized, we override
             * the interface and provide a no-op function.

            youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {}
            }).setApplicationName("youtube-cmdline-search-sample").build();

            // Get query term from user.
            String queryTerm = getInputQuery();
            //String queryTerm = "abc";

            YouTube.Search.List search = youtube.search().list("id,snippet");
            /*
             * It is important to set your API key from the Google Developer Console for
             * non-authenticated requests (found under the Credentials tab at this link:
             * console.developers.google.com/). This is good practice and increased your quota.

            //String apiKey = properties.getProperty("youtube.apikey");
            //String apiKey = "AIzaSyDLewcKRA1tOZ0C6Is5mM02kaWSyKW3t0s";
            //"youtube.apikey" is replaced by API_KEY
            //search.setKey(apiKey);
            search.setKey(API_KEY);
            search.setQ(queryTerm);
            /*
             * We are only searching for videos (not playlists or channels). If we were searching for
             * more, we would add them as a string like this: "video,playlist,channel".

            search.setType("video");
            /*
             * This method reduces the info returned to only the fields we need and makes calls more
             * efficient.

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);
            SearchListResponse searchResponse = search.execute();

            List<SearchResult> searchResultList = searchResponse.getItems();

            if (searchResultList != null) {
                prettyPrint(searchResultList.iterator(), queryTerm);
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
        */
    }

    /*
     * Returns a query term (String) from user via the terminal.
     */

    /*
    private static String getInputQuery() throws IOException {

        String inputQuery = "";

        System.out.print("Please enter a search term: ");
        BufferedReader bReader = new BufferedReader(new InputStreamReader(System.in));
        inputQuery = bReader.readLine();

        if (inputQuery.length() < 1) {
            // If nothing is entered, defaults to "YouTube Developers Live."
            inputQuery = "YouTube Developers Live";
        }
        return inputQuery;
    }

     */

    /*
     * Prints out all SearchResults in the Iterator. Each printed line includes title, id, and
     * thumbnail.
     *
     * @param iteratorSearchResults Iterator of SearchResults to print
     *
     * @param query Search query (String)
     */

    /*
    private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        System.out.println("\n=============================================================");
        //System.out.println(
          //      "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
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

     */


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

    /*
    @Override
    protected void onNewIntent(Intent intent) {
            //...
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow
        }
    }

     */

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



            Intent i = new Intent(MainActivity.this, com.example.youtubeclone.AccountActivity.class);
            MainActivity.this.startActivity(i);


            //RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());


            /*
            StringRequest stringRequest = new StringRequest(Request.Method.POST, endpoint, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {



                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            );

            requestQueue.add(stringRequest);

             */

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
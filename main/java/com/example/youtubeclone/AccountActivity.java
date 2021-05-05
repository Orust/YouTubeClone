package com.example.youtubeclone;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.youtubeclone.Model.VideoDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


//public class AccountActivity extends AppCompatActivity implements View.OnClickListener{
public class AccountActivity extends AppCompatActivity{

    private final String clientId = "101466230350-tkhroar87369giah0gv49k4qhcic4fsq.apps.googleusercontent.com";

    public String uri = "https://accounts.google.com/o/oauth2/v2/auth?"
            + "client_id=" + clientId + "&"
            //+ "client_id=" + "101466230350-viqt7h0ahunf1jbdfg4e2bheseafenl3.apps.googleusercontent.com" + "&"
            + "redirect_uri="
            //+ UrlEscapers.urlFormParameterEscaper().escape( "http://localhost:8080/auth" )
            //+ "https://localhost"
            + "urn:ietf:wg:oauth:2.0:oob"
            //+ "oauthviewer://callback"
            + "&"
            + "access_type=" + "offline" + "&"
            + "state=" + "test_state" + "&"
            + "response_type=" + "code" + "&"
            + "scope="
            //+ UrlEscapers.urlFormParameterEscaper().escape( "email profile" );
            + "https://www.googleapis.com/auth/youtube%20"
            + "https://www.googleapis.com/auth/youtube.force-ssl%20"
            + "https://www.googleapis.com/auth/youtube.readonly%20"
            + "https://www.googleapis.com/auth/youtubepartner%20";

    private String[] key_list = {
            "AIzaSyBSuEsG_TQQLr3x-8FFOqYYaFCL9cfHDgQ",
            "AIzaSyBJzBeThXgz_cExqCmeYstTA76_uw8RuAQ",
            "AIzaSyB4GVmXhvgtj6YAoYBXwFhSu6LLXKKNbJc"
    };
    private int index = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        //final Common common = (Common)getApplication();
        final Classifier common = (Classifier)getApplication();

        final Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        final Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText editText = findViewById(R.id.edit_text);
                String input_code = editText.getText().toString();
                common.setCode(input_code);

                //Log.d("Code","code = " + common.getCode());

                /*
                String url = "https://accounts.google.com/o/oauth2/token?"
                        + "code=" + common.getCode() + "&"
                        //+ "code=" + input_code + "&"
                        + "client_id="
                        + "101466230350-viqt7h0ahunf1jbdfg4e2bheseafenl3.apps.googleusercontent.com" + "&"
                        + "client_secret="
                        + "7eJm3gRuo-pEEf-e_qqOmKBa" + "&"
                        + "redirect_uri=" + "http://localhost/oauth2callback" + "&"
                        + "grant_type=authorization_code";

                 */

                String url = "https://accounts.google.com/o/oauth2/token";


                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("accessToken:",response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String access_token = jsonObject.getString("access_token");
                            common.setAccessToken(access_token);
                            common.setAuthorized(true);

                            Log.d("access_token", "ac = " + common.getAccessToken());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params=new HashMap<String,String>();
                        params.put("grant_type","authorization_code");
                        params.put("code",common.getCode());
                        params.put("client_id", "101466230350-tkhroar87369giah0gv49k4qhcic4fsq.apps.googleusercontent.com");
                        //params.put("client_secret","");
                        //params.put("scope","scope_test");
                        //params.put("state","state_test");
                        params.put("redirect_uri", "urn:ietf:wg:oauth:2.0:oob");
                        return params;

                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String,String> headers=new HashMap<String,String>();
                        headers.put("Accept","application/json");
                        headers.put("Content-Type","application/x-www-form-urlencoded");
                        return headers;
                    }

                };
                //ApplicationController.getInstance().addToRequestQueue(stringRequest);

                requestQueue.add(stringRequest);

                Intent new_intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(new_intent);
            }
        });



        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        for (int i = 0; i < common.getNames().length; i++) {
            adapter.add(common.getNames()[i]);
        }
        /*
        adapter.add("参加者１");
        adapter.add("参加者２");
        adapter.add("参加者３");
        adapter.add("参加者４");

         */
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);


        final Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String selected = (String) spinner.getSelectedItem();
                common.init();
                common.setParticipant(selected);

                Log.d("selected spinner", selected);

                Intent new_intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(new_intent);
            }
        });

        final Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                index++;
                common.setAPI_KEY(key_list[index % 3]);
                TextView textView = findViewById(R.id.text_view);
                textView.setText(common.getAPI_KEY());
            }
        });

        final Button button5 = findViewById(R.id.button5);
        button5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                common.changeSystem();
                TextView textView = findViewById(R.id.text_view2);
                textView.setText(Boolean.toString(common.getSystem()));
            }
        });


        //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        //startActivity(intent);

    }


    /*
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button1:

                String uri = "https://accounts.google.com/o/oauth2/v2/auth?"
                        + "client_id=" + clientId + "&"
                        + "redirect_uri="
                        //+ UrlEscapers.urlFormParameterEscaper().escape( "http://localhost:8080/auth" )
                        //+ "https://localhost"
                        + "urn:ietf:wg:oauth:2.0:oob"
                        //+ "oauthviewer://callback"
                        + "&"
                        + "access_type=" + "offline" + "&"
                        + "state=" + "test_state" + "&"
                        + "response_type=" + "code" + "&"
                        + "scope="
                        //+ UrlEscapers.urlFormParameterEscaper().escape( "email profile" );
                        + "https://www.googleapis.com/auth/youtube";

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            case R.id.button2:
                EditText editText = findViewById(R.id.edit_text);
                String input_code = editText.getText().toString();
                common.setCode(input_code);

                String url = "https://accounts.google.com/o/oauth2/token?"
                        + "code=" + common.getCode() + "&"
                        + "client_id="
                        + "101466230350-viqt7h0ahunf1jbdfg4e2bheseafenl3.apps.googleusercontent.com" + "&"
                        + "client_secret="
                        + "i8VVBUOmJ9d6qLpxekQpCANH" + "&"
                        + "redirect_uri=" + "http://localhost/oauth2callback" + "&"
                        + "grant_type=authorization_code";


                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String access_token = jsonObject.getString("access_token");
                            common.setAccessToken(access_token);
                            common.setAuthorized(true);

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

                Intent new_intent = new Intent(AccountActivity.this, MainActivity.class);
                startActivity(new_intent);
        }
    }

     */
}

package com.example.youtubeclone;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Classifier extends Application {
    private String API_KEY = "AIzaSyBSuEsG_TQQLr3x-8FFOqYYaFCL9cfHDgQ";
    private String API_KEY2 = "AIzaSyBJzBeThXgz_cExqCmeYstTA76_uw8RuAQ";
    //private String API_KEY3 = "AIzaSyB4GVmXhvgtj6YAoYBXwFhSu6LLXKKNbJc";

    private int query_length;
    private int reading_time;
    private int cumulative_clicks;
    private int scroll_depth;
    private String task_type = "lookup";
    private Boolean system = false;

    private int count;
    private long millis;
    private int index;
    private String participant = "none";
    private ArrayList<String> history;

    private String access_token = "aaa";
    private String code;
    private Boolean authorized = false;

    private String[] names = {"小西", "須貝", "中村", "池田", "遠藤", "笹森", "照井", "原田", "深山", "松尾", "森田", "井出"};
    private Map<String, Participant> name;


    public void init() {
        query_length = 0;
        reading_time = 0;
        cumulative_clicks = 0;
        scroll_depth = 0;
        count = 0;
        index = 0;
        history = new ArrayList<>();

        ArrayList<Participant> participants = new ArrayList<>();
        Participant p1 = new Participant(Arrays.asList(20,24), Arrays.asList("UC38hCsF3st1pBJBw8JnAu2g", "UCjtEttSHpJacfd7a6fc4VXQ", "UCFFVoLXDz1vlCQd4cEt69jg", "UCaRHSON-7fRrcxzffHFefrQ", "UCSKMdEMw4UkUrlcQFep705g"));
        participants.add(p1);
        Participant p2 = new Participant(Arrays.asList(20,24), Arrays.asList("UCGev0wAWHH0Iro34i4y7biQ", "UCuxXGm_2oXyInlsoMnFODqw", "UClzY_2nmb6Lgq5ScgdTR_jQ", "UCuQ5bzjS-9qV4eBbGJ19qmw"));
        participants.add(p2);
        Participant p3 = new Participant(Arrays.asList(10,17,20,28), Arrays.asList("UCxuipVSw8ajLZPgSyKmw6Ag", "UCWc-XpFHPK1SwGcvpFPZ8NA"));
        participants.add(p3);
        Participant p4 = new Participant(Arrays.asList(1,2), Arrays.asList("a", "b"));
        participants.add(p4);
        Participant p5 = new Participant(Arrays.asList(1,2), Arrays.asList("a", "b"));
        participants.add(p5);
        Participant p6 = new Participant(Arrays.asList(1,10,15,17,19,20,22,23,24), Arrays.asList("UC9PDC4xeAqgoq1mEoS53Pig", "UC2j4lymo8Ce_1RXMeo4afcQ", "UCPyNsNSTUtywkekbDdCA_8Q"));
        participants.add(p6);
        Participant p7 = new Participant(Arrays.asList(26,28), Arrays.asList("UCFBjsYvwX7kWUjQoW7GcJ5A", "UCvzrbCJbp7jw2EdPPblJy8w","UCTONPvJ-etKr_2uq5k-RVoQ"));
        participants.add(p7);
        Participant p8 = new Participant(Arrays.asList(1,10,17,20), Arrays.asList("UCpGpA7mSYmNJjLiJxKso5QA"));
        participants.add(p8);
        Participant p9 = new Participant(Arrays.asList(10,20,23,24), Arrays.asList("UCutJqz56653xV2wwSvut_hQ", "UC3swo6utVXOSYwxazwoKWBQ", "UCFDWRdFhAFyFOlmWMWYy2qQ", "UCx1nAvtVDIsaGmCMSe8ofsQ"));
        participants.add(p9);
        Participant p10 = new Participant(Arrays.asList(10,17,23), Arrays.asList("UCpGpA7mSYmNJjLiJxKso5QA", "UChwgNUWPM-ksOP3BbfQHS5Q", "UCOfESRUR5duQ2hMnTQ4oqhA", "UClIA2_cErgsKq7pfG2jaX2g"));
        participants.add(p10);
        Participant p11 = new Participant(Arrays.asList(10,20,24), Arrays.asList("UCXRlIK3Cw_TJIQC5kSJJQMg", "UCD-miitqNY3nyukJ4Fnf4_A", "UCP-qK04cla6Qj_WToco8vPg"));
        participants.add(p11);
        Participant p12 = new Participant(Arrays.asList(1,10,20,24,28), Arrays.asList("UCx1nAvtVDIsaGmCMSe8ofsQ", "UC2j4lymo8Ce_1RXMeo4afcQ", "UCkiCNZ6c5Th3NpL2U8IhUWw", "UCmvmF7CnMl6lLiRPUbKzAMg"));
        participants.add(p12);

        name = new HashMap<>();
        for (int i = 0; i < names.length; i++) {
            name.put(names[i], participants.get(i));
        }
        /*
        name.put("小西", p1);
        name.put("須貝", p2);

         */

        /*
        participants = new HashMap<>();

        participants.put("小西", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("須貝", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("中村", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("池田", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("井出", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("遠藤", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("笹森", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("照井", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("原田", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("深山", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("松尾", new ArrayList<Integer>(Arrays.asList(1, 2)));
        participants.put("森田", new ArrayList<Integer>(Arrays.asList(1, 2)));

         */

    }

    public void classify() {
        Log.d("parameters", Integer.toString(query_length) + "," +
                Integer.toString(scroll_depth) + "," +
                Integer.toString(reading_time));
        if (query_length <= 0) {
            if (scroll_depth <= 13) {
                task_type = "repeat";
            } else {
                task_type = "exploration";
            }
        } else {
            if (scroll_depth <= 16) {
                task_type = "lookup";
            } else {
                task_type = "exploration";
            }
        }
    }

    public void start_session() {
        init();
    }


    public String getAPI_KEY() {
        return API_KEY;
    }

    public void setAPI_KEY(String API_KEY) {
        this.API_KEY = API_KEY;
    }

    public String[] getNames() {
        return names;
    }

    public Map<String, Participant> getName() {
        return name;
    }

    public void add_history(String title) {
        history.add(title);
    }

    public int getQuery_length() {
        return query_length;
    }

    public int getReading_time() {
        return reading_time;
    }

    public int getCumulative_clicks() {
        return cumulative_clicks;
    }

    public Integer getScroll_depth() {
        return scroll_depth;
    }

    public int getCount() { return count; }

    public String getTask_type() {
        return task_type;
    }

    public String getParticipant() { return participant; }

    public void setQuery_length(String query) {

        String[] q = query.replaceAll("　", " ").split(" ");

        this.query_length = q.length;
    }

    public void setReading_time(int reading_time) {
        this.reading_time = reading_time;
    }

    public void setCumulative_clicks(int cumulative_clicks) {
        this.cumulative_clicks = cumulative_clicks;
    }

    public void setScroll_depth(int scroll_depth) {
        this.scroll_depth = scroll_depth;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public void setParticipant(String participant) { this.participant = participant; }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

    public String getAccessToken() {
        return this.access_token;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setAuthorized(Boolean authorized) {
        this.authorized = authorized;
    }

    public Boolean getAuthorized() {
        return authorized;
    }


    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void changeAPI_KEY() {
        //this.API_KEY = API_KEY3;
    }

    public Boolean getSystem() {
        return system;
    }

    public void changeSystem() {
        this.system = !(system);
    }
}

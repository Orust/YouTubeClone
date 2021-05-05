package com.example.youtubeclone;

import java.util.List;

public class Participant {
    private List<Integer> categories;
    private List<String> channel_ids;

    public Participant(List<Integer> categories, List<String> channel_ids) {
        this.categories = categories;
        this.channel_ids = channel_ids;
    }

    public List<Integer> getCategories() {
        return this.categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<String> getChannel_ids() {
        return this.channel_ids;
    }

    public void setChannel_ids(List<String> channel_ids) {
        this.channel_ids = channel_ids;
    }
}

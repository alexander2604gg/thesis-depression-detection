package com.alexandersaul.Alexander.dto;

import lombok.Data;
import java.util.List;

@Data
public class RedditResponseDto {
    private RedditData data;

    @Data
    public static class RedditData {
        private List<RedditChild> children;
    }

    @Data
    public static class RedditChild {
        private RedditPost data;
    }

    @Data
    public static class RedditPost {
        private String id;
        private String author;
        private String title;
        private String selftext;
        private long created_utc;
    }
}
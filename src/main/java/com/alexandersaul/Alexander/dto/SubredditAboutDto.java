package com.alexandersaul.Alexander.dto;

import lombok.Data;

@Data
public class SubredditAboutDto {
    private String kind;
    private DataNode data;

    @Data
    public static class DataNode {
        private String id;
        private String display_name;
        private boolean over18;
    }
}
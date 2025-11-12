package com.alexandersaul.Alexander.service;

public interface RedditMessageService {
    boolean sendPrivateMessage(String to, String subject, String text);
}
package com.alexandersaul.Alexander.client;

import com.alexandersaul.Alexander.config.RedditFeignConfig;
import com.alexandersaul.Alexander.dto.RedditResponseDto;
import com.alexandersaul.Alexander.dto.SubredditAboutDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "redditClient",
        url = "https://oauth.reddit.com",
        configuration = RedditFeignConfig.class
)
public interface RedditClient {

    @GetMapping("/r/{subreddit}/new.json")
    RedditResponseDto getNewPosts(
            @PathVariable("subreddit") String subreddit,
            @RequestParam(value = "limit", required = false, defaultValue = "6") int limit
    );

    @GetMapping("/r/{subreddit}/about")
    SubredditAboutDto getSubredditAbout(@PathVariable("subreddit") String subreddit);
}

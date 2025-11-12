package com.alexandersaul.Alexander.service;

import com.alexandersaul.Alexander.dto.PostResponseDto;
import com.alexandersaul.Alexander.entity.Post;

import java.util.List;

public interface PostService {

    List<Post> getAllPost ();
    List<PostResponseDto> findPostByIdLot(Long idLot);

}

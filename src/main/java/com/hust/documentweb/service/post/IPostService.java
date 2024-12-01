package com.hust.documentweb.service.post;

import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;

import java.util.List;

public interface IPostService {
    List<PostResDTO> findAll();

    PostResDTO findById(Long id);

    PostResDTO create(PostReqDTO dto);

    PostResDTO update(Long id, PostUpdateDTO dto);

    void deleteAllById(List<Long> ids);
}

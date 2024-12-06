package com.hust.documentweb.service.post;

import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IPostService {
    ResponsePageDTO<List<PostResDTO>> findAll(Pageable pageable, String advancedSearch);

    PostResDTO findById(Long id);

    PostResDTO create(PostReqDTO dto);

    PostResDTO update(Long id, PostUpdateDTO dto);

    void deleteAllById(List<Long> ids);
}

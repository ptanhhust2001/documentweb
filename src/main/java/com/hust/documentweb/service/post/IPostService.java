package com.hust.documentweb.service.post;

import java.util.List;

import com.hust.documentweb.constant.enums.EPostType;
import org.springframework.data.domain.Pageable;

import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;

public interface IPostService {
    ResponsePageDTO<List<PostResDTO>> findAll(Pageable pageable, String advancedSearch);

    PostResDTO findById(Long id);

    PostResDTO create(PostReqDTO dto, EPostType postType);

    PostResDTO update(Long id, PostUpdateDTO dto);

    void deleteAllById(List<Long> ids);
}

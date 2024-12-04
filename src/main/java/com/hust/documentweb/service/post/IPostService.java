package com.hust.documentweb.service.post;

import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IPostService {
    List<PostResDTO> findAll();

    PostResDTO findById(Long id);

    PostResDTO create(PostReqDTO dto, MultipartFile file);

    PostResDTO update(Long id, PostUpdateDTO dto, MultipartFile file);

    void deleteAllById(List<Long> ids);
}

package com.hust.documentweb.service.comment;

import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.comment.CommentReqDTO;
import com.hust.documentweb.dto.comment.CommentResDTO;
import com.hust.documentweb.dto.comment.CommentUpdateDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICommentService {
    ResponsePageDTO<List<CommentResDTO>> findAll(String advanceSearch, Pageable pageable);

    CommentResDTO findById(Long id);

    CommentResDTO create(CommentReqDTO dto);

    CommentResDTO update(Long id, CommentUpdateDTO dto);

    void deleteAllById(List<Long> id);
}

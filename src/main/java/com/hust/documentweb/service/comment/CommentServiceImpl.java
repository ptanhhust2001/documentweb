package com.hust.documentweb.service.comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hust.documentweb.constant.ErrorCommon;
import com.hust.documentweb.constant.FunctionError;
import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.comment.CommentReqDTO;
import com.hust.documentweb.dto.comment.CommentResDTO;
import com.hust.documentweb.dto.comment.CommentUpdateDTO;
import com.hust.documentweb.entity.Comment;
import com.hust.documentweb.entity.Post;
import com.hust.documentweb.exception.BookException;
import com.hust.documentweb.repository.CommentRepository;
import com.hust.documentweb.repository.PostRepository;
import com.hust.documentweb.utils.spec.BaseSpecs;
import com.hust.documentweb.utils.spec.Utils;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommentServiceImpl implements ICommentService {
    CommentRepository repository;
    PostRepository postRepository;
    ModelMapper commentMapper;

    @Override
    public ResponsePageDTO<List<CommentResDTO>> findAll(String advanceSearch, Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("updateAt").descending());
        Page<Comment> page = repository.findAll(BaseSpecs.searchQuery(advanceSearch), pageable);
        List<CommentResDTO> dtoList = Utils.mapList(commentMapper, page.getContent(), CommentResDTO.class);
        return ResponsePageDTO.success(dtoList, page.getTotalElements());
    }

    @Override
    public CommentResDTO findById(Long id) {
        Comment data = repository
                .findById(id)
                .orElseThrow(() ->
                        new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.COMMENT_NOT_FOUND, List.of(id))));
        return commentMapper.map(data, CommentResDTO.class);
    }

    @Override
    public CommentResDTO create(CommentReqDTO dto) {
        Comment data = commentMapper.map(dto, Comment.class);
        // thieu
        Optional<Post> postOpt = postRepository.findAllById(List.of(dto.getPostId())).stream().findFirst();
        if (postOpt.isEmpty())
            throw new BookException(
                    FunctionError.CREATE_FAILED, Map.of(ErrorCommon.POST_DOES_NOT_EXIST, dto.getPostId()));
        data.setPost(postOpt.get());
        save(data, true);
        return commentMapper.map(repository.save(data), CommentResDTO.class);
    }

    @Override
    public CommentResDTO update(Long id, CommentUpdateDTO dto) {
        Comment data = repository
                .findById(id)
                .orElseThrow(() ->
                        new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.COMMENT_NOT_FOUND, List.of(id))));
        commentMapper.map(dto, data);
        save(data, false);
        return commentMapper.map(data, CommentResDTO.class);
    }

    @Override
    public void deleteAllById(List<Long> id) {
        repository.deleteAllById(id);
    }

    private void save(Comment data, boolean isCreate) {
        if (isCreate) {
            data.setCreateAt(LocalDateTime.now());
            data.setCreateBy(Utils.getCurrentUser());
        }
        data.setUpdateAt(LocalDateTime.now());
        data.setUpdateBy(Utils.getCurrentUser());
        repository.save(data);
    }
}

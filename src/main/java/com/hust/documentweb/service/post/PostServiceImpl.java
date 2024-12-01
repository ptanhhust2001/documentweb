package com.hust.documentweb.service.post;

import com.hust.documentweb.constant.ErrorCommon;
import com.hust.documentweb.constant.FunctionError;
import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;
import com.hust.documentweb.entity.ClassEntity;
import com.hust.documentweb.entity.Post;
import com.hust.documentweb.entity.Subject;
import com.hust.documentweb.entity.User;
import com.hust.documentweb.exception.BookException;
import com.hust.documentweb.repository.ClassEntityRepository;
import com.hust.documentweb.repository.PostRepository;
import com.hust.documentweb.repository.SubjectRepository;
import com.hust.documentweb.repository.UserRepository;
import com.hust.documentweb.utils.spec.BaseSpecs;
import com.hust.documentweb.utils.spec.Utils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PostServiceImpl implements IPostService{
    PostRepository repository;
    SubjectRepository subjectRepository;
    ClassEntityRepository classRepository;
    UserRepository userRepository;
    ModelMapper postMapper;


    @Override
    public List<PostResDTO> findAll() {
        return repository.findAll().stream().map(post -> postMapper.map(post, PostResDTO.class)).toList();
    }

    public ResponsePageDTO<List<PostResDTO>> findAllByPage(String advanceSearch, Pageable pageable) {
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("updateAt").descending());
        Page<Post> page = repository.findAll(BaseSpecs.searchQuery(advanceSearch), pageable);
        List<PostResDTO> dtos = Utils.mapList(postMapper, page.getContent(), PostResDTO.class);
        return ResponsePageDTO.success(dtos, page.getTotalElements());
    }

    @Override
    public PostResDTO findById(Long id) {
        Post data = repository.findById(id)
                .orElseThrow(()
                        -> new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, List.of(id))));
        return postMapper.map(data, PostResDTO.class);
    }

    @Override
    public PostResDTO create(PostReqDTO dto) {
        Post data = postMapper.map(dto, Post.class);
        Map<Object,Object> errorsMap = new HashMap<>();
        Optional<ClassEntity> classOpt = classRepository.findById(dto.getClassEntityId());
        Optional<Subject> subjectOpt = subjectRepository.findById(dto.getSubjectId());
        Optional<User> userOpt = userRepository.findByUsername(Utils.getCurrentUser());

        if (classOpt.isEmpty()) errorsMap.put(ErrorCommon.CLASS_DOES_NOT_EXIST, List.of(dto.getClassEntityId()));
        if (subjectOpt.isEmpty()) errorsMap.put(ErrorCommon.SUBJECT_DOES_NOT_EXIST, List.of(dto.getSubjectId()));
        if (userOpt.isEmpty()) errorsMap.put(ErrorCommon.USER_DOES_NOT_EXIST, List.of(Utils.getCurrentUser()));

        if (!errorsMap.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, errorsMap);

        data.setClassEntity(classOpt.get());
        data.setSubject(subjectOpt.get());
        data.setUser(userOpt.get());
        data.setAuthor(userOpt.get().getLastName());

        save(data, true);
        return postMapper.map(data, PostResDTO.class);
    }

    @Override
    public PostResDTO update(Long id, PostUpdateDTO dto) {
        Post data = repository.findById(id).orElseThrow(() ->new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, List.of(id))));
        postMapper.map(dto, data);

        save(data, false);
        return postMapper.map(data, PostResDTO.class);
    }

    public void deleteAllById(List<Long> ids) {
        List<Long> notFound = ids.stream().filter(id -> repository.findById(id).isEmpty()).toList();
        if (!notFound.isEmpty())
            throw new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, notFound));
        repository.deleteAllByIdInBatch(ids);
    }

    private void save(Post data,boolean isCreate) {
        if (isCreate) {
            data.setCreateAt(LocalDateTime.now());
            data.setCreateBy(Utils.getCurrentUser());
        }
        data.setUpdateAt(LocalDateTime.now());
        data.setUpdateBy(Utils.getCurrentUser());
        repository.save(data);
    }
}
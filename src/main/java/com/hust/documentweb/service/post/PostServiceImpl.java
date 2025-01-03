package com.hust.documentweb.service.post;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.hust.documentweb.constant.enums.EPostType;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.hust.documentweb.constant.ErrorCommon;
import com.hust.documentweb.constant.FunctionError;
import com.hust.documentweb.dto.ResponsePageDTO;
import com.hust.documentweb.dto.comment.CommentResDTO;
import com.hust.documentweb.dto.material.MaterialResDTO;
import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;
import com.hust.documentweb.entity.*;
import com.hust.documentweb.exception.BookException;
import com.hust.documentweb.repository.*;
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
public class PostServiceImpl implements IPostService {
    PostRepository repository;
    SubjectRepository subjectRepository;
    ClassEntityRepository classRepository;
    UserRepository userRepository;
    ModelMapper postMapper;
    ModelMapper commentMapper;
    CommentRepository commentRepository;

    @Override
    public ResponsePageDTO<List<PostResDTO>> findAll(Pageable pageable, String advancedSearch) {
        Pageable page = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("updateAt").descending());
        Page<Post> data = repository.findAll(BaseSpecs.searchQuery(advancedSearch), page);
        List<PostResDTO> results = data.getContent().stream()
                .map(post -> {
                    PostResDTO resDTO = postMapper.map(post, PostResDTO.class);
                    resDTO.setComments(post.getComments().stream()
                            .map(comment -> commentMapper.map(comment, CommentResDTO.class))
                            .toList());
                    return resDTO;
                })
                .toList();

        return ResponsePageDTO.success(results, data.getTotalElements());
    }

    public ResponsePageDTO<List<PostResDTO>> findAllByPage(String advanceSearch, Pageable pageable) {
        pageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by("updateAt").descending());
        Page<Post> page = repository.findAll(BaseSpecs.searchQuery(advanceSearch), pageable);
        List<PostResDTO> dtos = Utils.mapList(postMapper, page.getContent(), PostResDTO.class);
        return ResponsePageDTO.success(dtos, page.getTotalElements());
    }

    @Override
    public PostResDTO findById(Long id) {
        Post data = repository
                .findAllById(List.of(id)).stream().findFirst()
                .orElseThrow(() ->
                        new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, List.of(id))));
        PostResDTO result = postMapper.map(data, PostResDTO.class);
        data.setViews(data.getViews() + 1);
        repository.save(data);
        result.setComments(data.getComments().stream()
                .map(comment -> commentMapper.map(comment, CommentResDTO.class))
                .toList());

        result.setMaterials(data.getMaterials().stream()
                .map(material -> commentMapper.map(material, MaterialResDTO.class))
                .toList());
        return result;
    }

    @Override
    public PostResDTO create(PostReqDTO dto, EPostType postType) {
        Post data = postMapper.map(dto, Post.class);
        Map<Object, Object> errorsMap = new HashMap<>();


        Set<Material> materials = dto.getMaterials().stream()
                .map(materialReqDTO -> {
                    Material material = postMapper.map(materialReqDTO, Material.class);
                    material.setPost(data);
                    return material;
                })
                .collect(Collectors.toSet());
        data.setMaterials(materials);
        if (postType.equals(EPostType.INTERNAL)) {
            Optional<ClassEntity> classOpt = classRepository.findById(dto.getClassEntityId());
            Optional<Subject> subjectOpt = subjectRepository.findById(dto.getSubjectId());
            if (classOpt.isEmpty()) errorsMap.put(ErrorCommon.CLASS_DOES_NOT_EXIST, List.of(dto.getClassEntityId()));
            if (subjectOpt.isEmpty()) errorsMap.put(ErrorCommon.SUBJECT_DOES_NOT_EXIST, List.of(dto.getSubjectId()));
            data.setClassEntity(classOpt.get());
            data.setSubject(subjectOpt.get());
        }
        Optional<User> userOpt = userRepository.findByUsername(Utils.getCurrentUser());
        if (userOpt.isEmpty()) errorsMap.put(ErrorCommon.USER_DOES_NOT_EXIST, List.of(Utils.getCurrentUser()));
        if (!errorsMap.isEmpty()) throw new BookException(FunctionError.CREATE_FAILED, errorsMap);
        data.setUser(userOpt.get());
        data.setAuthor(userOpt.get().getLastName());

        data.setPostType(postType);
        save(data, true);
        return postMapper.map(data, PostResDTO.class);
    }



    @Override
    public PostResDTO update(Long id, PostUpdateDTO dto) {
        Post data = repository
                .findAllById(List.of(id)).stream().findFirst()
                .orElseThrow(() ->
                        new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, List.of(id))));
        postMapper.map(dto, data);

        save(data, false);
        return postMapper.map(data, PostResDTO.class);
    }

    public void deleteAllById(List<Long> ids) {
        List<Post> posts = repository.findAllById(ids);

        if (posts.size() != ids.size())
            throw new BookException(FunctionError.NOT_FOUND, Map.of(ErrorCommon.POST_NOT_FOUND, ids));
        for (Post post : posts) {
            commentRepository.deleteAll(post.getComments());
        }
        repository.deleteAllByIdInBatch(ids);
    }

    private void save(Post data, boolean isCreate) {
        if (isCreate) {
            data.setCreateAt(LocalDateTime.now());
            data.setCreateBy(Utils.getCurrentUser());
        }
        data.setUpdateAt(LocalDateTime.now());
        data.setUpdateBy(Utils.getCurrentUser());
        repository.save(data);
    }
}

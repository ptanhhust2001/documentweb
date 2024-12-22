package com.hust.documentweb.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hust.documentweb.dto.comment.CommentReqDTO;
import com.hust.documentweb.dto.comment.CommentResDTO;
import com.hust.documentweb.dto.comment.CommentUpdateDTO;
import com.hust.documentweb.entity.Comment;

@Configuration
public class CommentConfigMapper {
    @Bean
    public ModelMapper commentMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?, ?> map1 = new PropertyMap<Comment, CommentResDTO>() {
            protected void configure() {}
        };
        PropertyMap<?, ?> map2 = new PropertyMap<CommentReqDTO, Comment>() {
            protected void configure() {
                skip(destination.getId());
            }
        };
        PropertyMap<?, ?> map3 = new PropertyMap<CommentUpdateDTO, Comment>() {
            @Override
            protected void configure() {
                skip(destination.getId());
            }
        };
        mapper.addMappings(map1);
        mapper.addMappings(map2);
        mapper.addMappings(map3);
        return mapper;
    }
}

package com.hust.documentweb.mapper;

import com.hust.documentweb.dto.post.PostReqDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.dto.post.PostUpdateDTO;
import com.hust.documentweb.entity.Post;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PostMapperConfig {
    @Bean
    public ModelMapper postMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?,?> map1 = new PropertyMap<Post, PostResDTO>(){
            protected void configure() {
            }
        };
        PropertyMap<?,?> map2 = new PropertyMap<PostReqDTO, Post>(){
            protected void configure() {
                skip(destination.getId());
            }
        };
        PropertyMap<?,?> map3 = new PropertyMap<PostUpdateDTO, Post>(){
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
package com.hust.documentweb.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hust.documentweb.dto.User.UserReqDTO;
import com.hust.documentweb.dto.User.UserResDTO;
import com.hust.documentweb.dto.User.UserUpdateDTO;
import com.hust.documentweb.entity.User;

@Configuration
public class UserConfigMapper {
    @Bean
    public ModelMapper userMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?, ?> map1 = new PropertyMap<User, UserResDTO>() {
            protected void configure() {}
        };
        PropertyMap<?, ?> map2 = new PropertyMap<UserReqDTO, User>() {
            protected void configure() {
                skip(destination.getId());
            }
        };
        PropertyMap<?, ?> map3 = new PropertyMap<UserUpdateDTO, User>() {
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

package com.hust.documentweb.mapper;

import com.hust.documentweb.dto.classenity.ClassReqDTO;
import com.hust.documentweb.dto.classenity.ClassResDTO;
import com.hust.documentweb.dto.post.PostResDTO;
import com.hust.documentweb.entity.ClassEntity;
import com.hust.documentweb.entity.Post;
import com.hust.documentweb.entity.Subject;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ClassConfigMapper {

    @Bean
    public ModelMapper classMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STANDARD);
        PropertyMap<?,?> map1 = getPropertyMap();
        PropertyMap<?,?> map2 = new PropertyMap<ClassReqDTO, ClassEntity>(){
            @Override
            protected void configure() {
                skip(destination.getId());
                skip(destination.getSubjects());
            }
        };
        mapper.addMappings(map1);
        mapper.addMappings(map2);
        return mapper;
    }

    private static PropertyMap<?, ?> getPropertyMap() {
        Converter<List<Subject>, List<Long>> toList = context -> {
            return context.getSource().stream().map(Subject::getId).toList();
        };


        return new PropertyMap<ClassEntity, ClassResDTO>() {
            protected void configure() {
                using(toList).map(source.getSubjects(), destination.getSubjectIds());
            }
        };
    }
}

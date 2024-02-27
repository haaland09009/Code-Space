package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.TopicEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TopicDTO {
    private Long id;
    private String name;

    public static TopicDTO toTopicDTO(TopicEntity topicEntity) {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setId(topicEntity.getId());
        topicDTO.setName(topicEntity.getName());
        return topicDTO;
    }
}

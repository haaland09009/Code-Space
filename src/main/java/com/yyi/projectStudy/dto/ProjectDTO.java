package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.ProjectEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProjectDTO {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private int readCount;
    private int headCount;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private LocalDateTime regDate;

    private String writer;
    private int fileAttached;
    private String storedFileName;
    private Long commentCount;
    private List<String> techList = new ArrayList<>();
//    private String techList;
    private String projectStudy;
    private int clipCount;

    private Long remainingDays;

    public static ProjectDTO toProjectDTO(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectEntity.getId());
        projectDTO.setTitle(projectEntity.getTitle());
        projectDTO.setContent(projectEntity.getContent());
        projectDTO.setReadCount(projectEntity.getReadCount());
        projectDTO.setHeadCount(projectEntity.getHeadCount()); //
        projectDTO.setRegDate(projectEntity.getRegDate());
        projectDTO.setEndDate(projectEntity.getEndDate()); //

        projectDTO.setUserId(projectEntity.getUserEntity().getId());
        projectDTO.setWriter(projectEntity.getUserEntity().getNickname());
        projectDTO.setFileAttached(projectEntity.getUserEntity().getFileAttached());
        if (projectEntity.getUserEntity().getFileAttached() == 1) {
            projectDTO.setStoredFileName(projectEntity.getUserEntity().getUserImageFileEntityList().get(0).getStoredFileName());
        }
        return projectDTO;
    }
}

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
    private Date startDate;
    private LocalDateTime regDate;
    private String status;
    private LocalDateTime updDate;

    private String writer;
    private int fileAttached;
    private String storedFileName;
    private Long commentCount;
    private List<String> positionList = new ArrayList<>();
    private List<String> techList = new ArrayList<>();
    private String projectStudy;
    private int clipCount;
    private String formattedDate;

    private Long remainingDays;


    public ProjectDTO(ProjectEntity projectEntity) {
        this.id = projectEntity.getId();
        this.title = projectEntity.getTitle();
        this.content = projectEntity.getContent();
        this.readCount = projectEntity.getReadCount();
        this.headCount = projectEntity.getHeadCount();
        this.regDate = projectEntity.getRegDate();
        this.startDate = projectEntity.getStartDate();
        this.status = projectEntity.getStatus();

        this.writer = projectEntity.getUserEntity().getNickname();
        this.fileAttached = projectEntity.getUserEntity().getFileAttached();
        if (projectEntity.getUserEntity().getFileAttached() == 1) {
            this.storedFileName = projectEntity.getUserEntity().getUserImageFileEntityList().get(0).getStoredFileName();
        }
    }

    public static ProjectDTO toProjectDTO(ProjectEntity projectEntity) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(projectEntity.getId());
        projectDTO.setTitle(projectEntity.getTitle());
        projectDTO.setContent(projectEntity.getContent());
        projectDTO.setReadCount(projectEntity.getReadCount());
        projectDTO.setHeadCount(projectEntity.getHeadCount());
        projectDTO.setRegDate(projectEntity.getRegDate());
        projectDTO.setStartDate(projectEntity.getStartDate());
        projectDTO.setStatus(projectEntity.getStatus());
        projectDTO.setUpdDate(projectEntity.getUpdDate());

        projectDTO.setUserId(projectEntity.getUserEntity().getId());
        projectDTO.setWriter(projectEntity.getUserEntity().getNickname());
        projectDTO.setFileAttached(projectEntity.getUserEntity().getFileAttached());
        if (projectEntity.getUserEntity().getFileAttached() == 1) {
            projectDTO.setStoredFileName(projectEntity.getUserEntity().getUserImageFileEntityList().get(0).getStoredFileName());
        }
        return projectDTO;
    }
}

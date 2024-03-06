package com.yyi.projectStudy.dto;

import com.yyi.projectStudy.entity.UserEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private LocalDateTime regDate;

    private MultipartFile profileImageFile;
    private String originalFileName; // 원본 파일 이름
    private String storedFileName; // 서버 저장용 파일 이름
    private int fileAttached; // 파일 첨부 여부 (첨부 : 1, 미첨부 : 0)
    private String jobName;

    // Entity -> DTO
    public static UserDTO toUserDTO(UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(userEntity.getId());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setNickname(userEntity.getNickname());
        userDTO.setRegDate(userEntity.getRegDate());
        if (userEntity.getFileAttached() == 0) {
            userDTO.setFileAttached(userEntity.getFileAttached());
        } else {
            userDTO.setFileAttached(userEntity.getFileAttached());
            userDTO.setOriginalFileName(userEntity.getUserImageFileEntityList().get(0).getOriginalFileName());
            userDTO.setStoredFileName(userEntity.getUserImageFileEntityList().get(0).getStoredFileName());
        }
        return userDTO;
    }

}

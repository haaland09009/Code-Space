package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.entity.UserImageFileEntity;
import com.yyi.projectStudy.repository.UserImageFileRepository;
import com.yyi.projectStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserImageFileRepository userImageFileRepository;

    // 회원가입 프로세스
    public void save(UserDTO userDTO) throws IOException {
        if (userDTO.getProfileImageFile().isEmpty()) {
            userRepository.save(UserEntity.toUserEntity(userDTO));
        } else {
            // 이미지 파일이 존재할 경우
            // 1. DTO에 담긴 이미지 파일 꺼내기
            MultipartFile profileImageFile = userDTO.getProfileImageFile();

            // 2. 파일의 이름 가져옴 (실제 사용자가 올린 파일 이름)
            String originalFilename = profileImageFile.getOriginalFilename();

            // 3. 서버 저장용 이름으로 수정 : 내 사진.jpg --> 8423424252525_내사진.jpg (currentTimeMills() -> 이거 대신 UUID도 사용가능)
            String storedFileName = System.currentTimeMillis() + "_" + originalFilename;

            // 4. 저장 경로 설정 (해당 폴더는 미리 만들어진 상태여야 한다.)
            String savePath = "C:/toyProject_img/" + storedFileName;

            // 5. 해당 경로에 파일 저장
            profileImageFile.transferTo(new File(savePath));

            // 6. user_table에 해당 데이터 save 처리
            UserEntity userEntity = UserEntity.toSaveFileEntity(userDTO);
            // id 값을 얻어오는 이유: 자식 테이블 입장에서 부모가 어떤 id(pk)인지 필요해서
            Long savedUserId = userRepository.save(userEntity).getId();
            UserEntity user = userRepository.findById(savedUserId).get();

            UserImageFileEntity userImageFileEntity
                    = UserImageFileEntity.toUserImageFileEntity(user, originalFilename, storedFileName);

            userImageFileRepository.save(userImageFileEntity);
        }
    }

    // 로그인 프로세스
    @Transactional
    public UserDTO login(UserDTO userDTO) {
        Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userDTO.getEmail());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            if (userEntity.getPassword().equals(userDTO.getPassword())) {
                return UserDTO.toUserDTO(userEntity);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    // 마이페이지 - 회원정보 조회
    @Transactional
    public UserDTO findById(Long id) {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (optionalUserEntity.isPresent()) {
            return UserDTO.toUserDTO(optionalUserEntity.get());
        } else {
            return null;
        }
    }

    // 회원탈퇴
//    public void deleteById(Long id) {
//        userRepository.deleteById(id);
//    }
}

package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.LoungeDTO;
import com.yyi.projectStudy.entity.LoungeEntity;
import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.entity.UserJobEntity;
import com.yyi.projectStudy.repository.LoungeRepository;
import com.yyi.projectStudy.repository.UserJobRepository;
import com.yyi.projectStudy.repository.UserRepository;
import com.yyi.projectStudy.time.StringToDate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoungeService {
    private final LoungeRepository loungeRepository;
    private final UserRepository userRepository;
    private final UserJobRepository userJobRepository;


    /* 게시글 작성 */
    public void save(LoungeDTO loungeDTO) {
        UserEntity userEntity = userRepository.findById(loungeDTO.getUserId()).get();
        LoungeEntity loungeEntity =  LoungeEntity.toLoungeEntity(loungeDTO, userEntity);
        loungeRepository.save(loungeEntity);
    }


    /* 게시글 목록 조회 */
    @Transactional
    public List<LoungeDTO> findAll() {
        List<LoungeEntity> loungeEntityList =
                loungeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<LoungeDTO> loungeDTOList = new ArrayList<>();
        for (LoungeEntity loungeEntity : loungeEntityList) {
            LoungeDTO loungeDTO = new LoungeDTO();

            String content = loungeEntity.getContent();
            content = content.replaceAll("<br>", "\n");
            loungeDTO.setContent(content);

            loungeDTO.setWriter(loungeEntity.getUserEntity().getNickname());
            loungeDTO.setFileAttached(loungeEntity.getUserEntity().getFileAttached());
            if (loungeEntity.getUserEntity().getFileAttached() == 1) {
                loungeDTO.setStoredFileName(loungeEntity.getUserEntity().getUserImageFileEntityList().get(0).getStoredFileName());
            }
            // 사용자의 직업
            UserJobEntity userJobEntity =
                    userJobRepository.findByUserEntity(loungeEntity.getUserEntity()).get();
            loungeDTO.setJobName(userJobEntity.getJobEntity().getName());

            // 날짜 변환하기
            String formatDateTime = StringToDate.formatDateTime(String.valueOf(loungeEntity.getRegDate()));
            loungeDTO.setFormattedDate(formatDateTime);

            loungeDTOList.add(LoungeDTO.toLoungeDTO(loungeDTO, loungeEntity));
        }
        return loungeDTOList;
    }

    /* 게시글 삭제 */
    public void deleteById(Long id) {
        loungeRepository.deleteById(id);
    }
}

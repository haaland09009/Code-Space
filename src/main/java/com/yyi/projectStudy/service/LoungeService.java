package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.LoungeDTO;
import com.yyi.projectStudy.dto.LoungeLikeDTO;
import com.yyi.projectStudy.entity.*;
import com.yyi.projectStudy.repository.LoungeLikeRepository;
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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoungeService {
    private final LoungeRepository loungeRepository;
    private final UserRepository userRepository;
    private final UserJobRepository userJobRepository;
    private final LoungeLikeRepository loungeLikeRepository;


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

            // 좋아요 개수
            int likeCount = loungeLikeRepository.countByLoungeEntity(loungeEntity);
            loungeDTO.setLikeCount(likeCount);

            loungeDTOList.add(LoungeDTO.toLoungeDTO(loungeDTO, loungeEntity));
        }
        return loungeDTOList;
    }

    /* 게시글 삭제 */
    public void deleteById(Long id) {
        loungeRepository.deleteById(id);
    }

    /* 게시글 좋아요 */
    public boolean like(LoungeLikeDTO loungeLikeDTO) {
        Optional<LoungeEntity> optionalLoungeEntity
                = loungeRepository.findById(loungeLikeDTO.getLoungeId());
        if (optionalLoungeEntity.isPresent()) {
            LoungeEntity loungeEntity = optionalLoungeEntity.get();
            UserEntity userEntity = userRepository.findById(loungeLikeDTO.getUserId()).get();
            if (loungeLikeRepository.countByLoungeEntityAndUserEntity(loungeEntity, userEntity) > 0) {
                /* 게시글에 이미 좋아요를 누른 상태이면 취소 */
                Long id = loungeLikeRepository.findByLoungeEntityAndUserEntity(loungeEntity, userEntity).get().getId();
                loungeLikeRepository.deleteById(id);
                return false;
            } else {
                /* 좋아요를 누르지 않았다면 좋아요 처리 */
                LoungeLikeEntity loungeLikeEntity = LoungeLikeEntity.toLoungeLikeEntity(loungeEntity, userEntity);
                loungeLikeRepository.save(loungeLikeEntity);
                return true;
            }
        }
        return false;
    }

    /* 본인이 작성한 게시글인지 확인 */
    public boolean isYourBoard(LoungeDTO loungeDTO) {
        Long sessionId = loungeDTO.getUserId();
        LoungeEntity loungeEntity = loungeRepository.findById(loungeDTO.getId()).get();
        if (sessionId.equals(loungeEntity.getUserEntity().getId())) {
            return true;
        } else {
            return false;
        }
    }

    /* 게시글 좋아요 개수 */
    public int likeCount(Long id) {
        LoungeEntity loungeEntity = loungeRepository.findById(id).get();
        return loungeLikeRepository.countByLoungeEntity(loungeEntity);
    }

    /* 게시글 좋아요 여부 (아이콘) */
    public int checkLike(Long loungeId, Long userId) {
        LoungeEntity loungeEntity = loungeRepository.findById(loungeId).get();
        UserEntity userEntity = userRepository.findById(userId).get();
        return loungeLikeRepository.countByLoungeEntityAndUserEntity(loungeEntity, userEntity);
    }
}

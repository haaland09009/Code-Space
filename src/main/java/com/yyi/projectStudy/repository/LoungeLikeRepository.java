package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.LoungeEntity;
import com.yyi.projectStudy.entity.LoungeLikeEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoungeLikeRepository extends JpaRepository<LoungeLikeEntity, Long> {
    /* 게시글에 좋아요를 눌렸는지의 여부 */
    int countByLoungeEntityAndUserEntity(LoungeEntity loungeEntity, UserEntity userEntity);

    /* 좋아요 pk 찾기 */
    Optional<LoungeLikeEntity> findByLoungeEntityAndUserEntity(LoungeEntity loungeEntity, UserEntity userEntity);

    /* 게시글 좋아요 개수 */
    int countByLoungeEntity(LoungeEntity loungeEntity);
}

package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.NotificationEntity;
import com.yyi.projectStudy.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {

    /* 안 읽은 알림 수 조회 */
    @Query(value = "select count(*) from NotificationEntity where receiver = :userEntity and readDate is null")
    int notReadNoticeCount(UserEntity userEntity);


    /* 회원 당 알림 조회 */
    List<NotificationEntity> findByReceiverOrderByIdDesc(UserEntity userEntity);

    /* 회원 당 안 읽은 알림만 조회 */
    List<NotificationEntity> findByReceiverAndReadDateIsNullOrderByIdDesc(UserEntity userEntity);


    /* 알림 삭제 */
    @Modifying
    @Query(value = "delete from NotificationEntity n where n.notTypeEntity.id = :notId and n.entityId = :entityId")
    void deleteNotification(@Param("notId") Long notId, @Param("entityId") Long id);


    /* 알림 읽음 처리 */
    @Modifying
    @Query(value = "update NotificationEntity set readDate = sysdate where id = :id")
    void updateAsRead(Long id);

}

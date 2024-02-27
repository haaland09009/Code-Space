package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.entity.QnaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QnaRepository extends JpaRepository<QnaEntity, Long> {
    // 게시글 조회수 증가
    // update qna_table set read_count = read_count + 1 where id = ?
    @Modifying
    @Query(value = "update QnaEntity q set q.readCount = q.readCount + 1 where q.id = :id")
    void updateReadCount(@Param("id") Long id);

    // 게시글 수정
    // update qna_table set title = :title, content = :content where id = ?
    @Modifying
    @Query(value = "update QnaEntity q set q.title = :title, q.content = :content where q.id = :id")
    void updateQna(@Param("title") String title, @Param("content") String content, @Param("id") Long id);
}

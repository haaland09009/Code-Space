package com.yyi.projectStudy.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yyi.projectStudy.dto.ProjectDTO;
import com.yyi.projectStudy.dto.QnaDTO;
import com.yyi.projectStudy.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class QnaCustomImpl implements QnaCustom {
    private final JPAQueryFactory queryFactory;



    BooleanBuilder builder = new BooleanBuilder();
    @Override
    public List<Long> findByCondition(String category, String sortKey,
                                        String searchWord, String tagName) {

        QQnaEntity q = QQnaEntity.qnaEntity;
        QQnaTopicEntity qt = QQnaTopicEntity.qnaTopicEntity;
        QQnaReplyEntity qr = QQnaReplyEntity.qnaReplyEntity;
        QQnaTagsEntity qte = QQnaTagsEntity.qnaTagsEntity;
        QTopicEntity t = QTopicEntity.topicEntity;

        JPAQuery<Long> query =
                queryFactory.select(q.id)
                        .from(q);

      /*  if (StringUtils.hasText(category)) {
            Long topicId;
            if (category.equals("tech")) {
                topicId = 1L;
            } else {
                topicId = 2L;
            }
            System.out.println("카테고리에 도달했나요 ? ");
            System.out.println("topicId는 " + topicId);
            query = query.join(q.qnaTopicEntityList, qt);
            builder.and(qt.topicEntity.id.eq(topicId));
        }*/

        if (StringUtils.hasText(sortKey)) {
            query = query.leftJoin(q.qnaReplyEntityList, qr);
            query.groupBy(q.id)
                    .orderBy(qr.qnaEntity.id.count().asc(), q.id.desc());
        }

        if (StringUtils.hasText(searchWord)) {
            String word = searchWord.toLowerCase();
            BooleanExpression titleContains = q.title.lower().like("%" + word + "%");
            BooleanExpression contentContains = q.content.lower().like("%" + word + "%");
            builder.and(titleContains.or(contentContains));
        }

        if (StringUtils.hasText(tagName)) {
            String word = tagName.toLowerCase();
            query = query.leftJoin(q.qnaTagsEntityList, qte);
            BooleanExpression titleContains = q.title.lower().like("%" + word + "%");
            BooleanExpression tagContains = qte.tag.lower().like("%" + word + "%");
            builder.and(tagContains.or(titleContains));
        }
        query = query.where(builder).orderBy(q.id.desc());
        return query.fetch();
    }







}

package com.yyi.projectStudy.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yyi.projectStudy.dto.ProjectDTO;
import com.yyi.projectStudy.entity.ProjectEntity;
import com.yyi.projectStudy.entity.QProjectEntity;
import com.yyi.projectStudy.entity.QProjectPositionCategoryLinkEntity;
import com.yyi.projectStudy.entity.QProjectTechCategoryLinkEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class ProjectCustomImpl implements ProjectCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<ProjectDTO> findByCondition(List<Long> techIdList, Long positionId,
                                            String status) {
        QProjectEntity p = QProjectEntity.projectEntity;
        QProjectTechCategoryLinkEntity pt = QProjectTechCategoryLinkEntity.projectTechCategoryLinkEntity;
        QProjectPositionCategoryLinkEntity pp = QProjectPositionCategoryLinkEntity.projectPositionCategoryLinkEntity;

        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<ProjectEntity> query = queryFactory.selectDistinct(p).from(p);

        if (techIdList != null) {
            query = query.join(p.projectTechCategoryLinkEntityList, pt);
            builder.and(pt.techCategoryEntity.id.in(techIdList));
        }

        if (positionId != null) {
            query = query.join(p.projectPositionCategoryLinkEntityList, pp);
            builder.and(pp.positionCategoryEntity.id.eq(positionId));
        }

        if (StringUtils.hasText(status)) {
            builder.and(p.status.eq(status));
        }

        query = query.where(builder).orderBy(p.id.desc());

        List<ProjectEntity> projectEntityList = query.fetch();

        return projectEntityList.stream()
                .map(ProjectDTO::toProjectDTO)
                .toList();
    }

}

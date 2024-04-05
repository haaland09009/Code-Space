package com.yyi.projectStudy.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yyi.projectStudy.dto.ProjectDTO;
import com.yyi.projectStudy.dto.ProjectSearchDTO;
import com.yyi.projectStudy.dto.UserDTO;
import com.yyi.projectStudy.entity.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;

@RequiredArgsConstructor
public class ProjectCustomImpl implements ProjectCustom {
    private final JPAQueryFactory queryFactory;

    QProjectEntity p = QProjectEntity.projectEntity;
    QProjectTechCategoryLinkEntity pt = QProjectTechCategoryLinkEntity.projectTechCategoryLinkEntity;
    QProjectPositionCategoryLinkEntity pp = QProjectPositionCategoryLinkEntity.projectPositionCategoryLinkEntity;
    QProjectStudyCategoryLinkEntity psc = QProjectStudyCategoryLinkEntity.projectStudyCategoryLinkEntity;
    QProjectClipEntity pc = QProjectClipEntity.projectClipEntity;

    @Override
    public List<ProjectDTO> findByCondition(ProjectSearchDTO projectSearchDTO) {

        List<Long> techIdList = projectSearchDTO.getTechIdList();
        Long positionId = projectSearchDTO.getPositionId();
        String status = projectSearchDTO.getStatus();
        Long categoryId = projectSearchDTO.getCategoryId();
        String clipYn = projectSearchDTO.getClipYn();
        Long userId = projectSearchDTO.getUserId();


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

        if (categoryId != null) {
            query = query.join(p.projectStudyCategoryLinkEntityList, psc);
            builder.and(psc.projectStudyCategoryEntity.id.eq(categoryId));
        }

        if (StringUtils.hasText(clipYn)) {
            query = query.join(p.projectClipEntityList, pc);
            builder.and(pc.userEntity.id.eq(userId));
        }

        query = query.where(builder).orderBy(p.id.desc());

        List<ProjectEntity> projectEntityList = query.fetch();

        return projectEntityList.stream()
                .map(ProjectDTO::toProjectDTO)
                .toList();
    }

}

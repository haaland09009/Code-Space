package com.yyi.projectStudy.repository;

import com.yyi.projectStudy.dto.QnaDTO;

import java.util.List;

public interface QnaCustom {
    List<Long> findByCondition(String category, String sortKey,
                                 String searchWord, String tagName);
}

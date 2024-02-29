package com.yyi.projectStudy.service;

import com.yyi.projectStudy.dto.QnaReplyDTO;
import com.yyi.projectStudy.entity.ProjectEntity;
import com.yyi.projectStudy.entity.QnaEntity;
import com.yyi.projectStudy.entity.QnaReplyEntity;
import com.yyi.projectStudy.entity.UserEntity;
import com.yyi.projectStudy.repository.QnaReplyRepository;
import com.yyi.projectStudy.repository.QnaRepository;
import com.yyi.projectStudy.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QnaReplyService {
    private final QnaReplyRepository qnaReplyRepository;
    private final QnaRepository qnaRepository;
    private final UserRepository userRepository;

    // 답변 작성
    public Long save(QnaReplyDTO qnaReplyDTO) {
        Optional<QnaEntity> optionalQnaEntity = qnaRepository.findById(qnaReplyDTO.getQnaId());
        if (optionalQnaEntity.isPresent()) {
            QnaEntity qnaEntity = optionalQnaEntity.get();
            UserEntity userEntity = userRepository.findById(qnaReplyDTO.getUserId()).get();
            QnaReplyEntity qnaReplyEntity = QnaReplyEntity.toQnaReplyEntity(qnaReplyDTO, qnaEntity, userEntity);
            return qnaReplyRepository.save(qnaReplyEntity).getId();
        } else {
            // 나중에 게시글이 존재하지 않습니다. 모달로 띄워야함.
            return null;
        }
    }


    // 게시글 당 답변 조회
    @Transactional
    public List<QnaReplyDTO> findAll(Long qnaId) {
        QnaEntity qnaEntity = qnaRepository.findById(qnaId).get();
        List<QnaReplyEntity> qnaReplyEntityList = qnaReplyRepository.findAllByQnaEntityOrderByIdDesc(qnaEntity);
        List<QnaReplyDTO> qnaReplyDTOList = new ArrayList<>();
        for (QnaReplyEntity qnaReplyEntity : qnaReplyEntityList) {
            qnaReplyDTOList.add(QnaReplyDTO.toQnaReplyDTO(qnaReplyEntity));
        }
        return qnaReplyDTOList;
    }

    // 답변 삭제
    public void deleteById(Long id) {
        qnaReplyRepository.deleteById(id);
    }


    // 답글 수 조회
    @Transactional
    public int count(Long id) {
        QnaEntity qnaEntity = qnaRepository.findById(id).get();
        return qnaReplyRepository.countByQnaEntity(qnaEntity);
    }
}

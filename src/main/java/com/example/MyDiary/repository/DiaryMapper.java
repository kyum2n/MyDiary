package com.example.mydiary.repository;

import com.example.mydiary.entity.Diary;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface DiaryMapper {
    Diary findById(Long id);             // ✅ 이미 있는 것으로 가정
    List<Diary> findAll();               // ✅ XML에 있던 메서드 추가
    List<Diary> findByUid(String uId);   // ✅ XML과 매칭 위해 추가
    void insertDiary(Diary diary);
    void updateDiary(Diary diary);
    void deleteDiary(Long id);
}
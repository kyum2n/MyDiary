package com.example.mydiary.repository;

import java.sql.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import com.example.mydiary.entity.Diary;

@Mapper
public interface DiaryMapper {

    // 모든 일기 조회
    List<Diary> selectAllDiaries(@Param("uId") String uId);

    // 특정 일기 조회
    Diary selectDiaryById(@Param("id") int id);

    // 특정 날짜에 작성된 일기 조회
    Diary findByDateAndUId(@Param("date") Date date, @Param("uId") String uId);

    // 일기 등록
    void insertDiary(Diary diary);

    // 일기 수정
    void updateDiary(Diary diary);

    // 일기 삭제
    void deleteDiary(@Param("id") int id);

}

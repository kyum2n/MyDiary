package com.example.mydiary.service;

import java.util.List;

import com.example.mydiary.entity.Diary;

public interface DiaryService {
    // 모든 일기 조회
    List<Diary> getAllDiaries(String uId);

    // 특정 일기 조회
    Diary getDiaryById(int id);

    // 일기 등록
    void addDiary(Diary diary);

    // 일기 수정
    void editDiary(Diary diary);

    // 일기 삭제
    void deleteDiary(int id);

}

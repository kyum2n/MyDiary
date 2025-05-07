package com.example.mydiary.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.mydiary.entity.Diary;
import com.example.mydiary.service.DiaryService;
import com.example.mydiary.repository.DiaryMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DiaryServiceImpl implements DiaryService {

    private final DiaryMapper diaryMapper;

    // 모든 일기 조회
    @Override
    public List<Diary> getAllDiaries(String uId) {
        return diaryMapper.selectAllDiaries(uId);
    }

    // 특정 일기 조회
    @Override
    public Diary getDiaryById(int id) {
        return diaryMapper.selectDiaryById(id);
    }

    // 일기 등록
    @Override
    public void addDiary(Diary diary) {
        diaryMapper.insertDiary(diary);
    }

    // 일기 수정
    @Override
    public void editDiary(Diary diary) {
        diaryMapper.updateDiary(diary);
    }

    // 일기 삭제
    @Override
    public void deleteDiary(int id) {
        diaryMapper.deleteDiary(id);
    }

}
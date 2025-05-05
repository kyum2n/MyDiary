package com.example.mydiary.repository;

import com.example.mydiary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
}
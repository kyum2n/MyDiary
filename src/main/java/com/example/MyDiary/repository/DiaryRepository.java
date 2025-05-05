package com.example.mydiary.repository;

import com.example.mydiary.entity.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

// Diary 엔티티에 대한 DB 접근 인터페이스
public interface DiaryRepository extends JpaRepository<Diary, Long> {
    /*이미 Jpa에서 다 재공하고 있어서 여기에 내용을 넣을 핗욘 없음
     *  JPA의 자동 기능을 쓰기 위한 연결고리
    */
}
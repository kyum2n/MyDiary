package com.example.mydiary.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity // JPA가 관리하는 엔티티 클래스임을 명시
@Table(name = "diary") // 이 엔티티가 매핑될 테이블 이름을 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {

    @Id // 기본 키(PK) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB가 자동 생성 (AUTO_INCREMENT)
    private Long id;

    @ManyToOne // 다이어리 여러 개가 하나의 멤버에 속함 (N:1 관계)
    @JoinColumn(name = "uid", referencedColumnName = "uid", nullable = false) // 외래키 지정
    private Member member;

    private String title; // 일기 제목

    private LocalDate date; // 일기 날짜

    private String image; // 이미지 파일명 또는 경로

    private String location; // 위치 정보 (선택사항)

    @Column(columnDefinition = "TEXT") // 긴 텍스트 저장을 위해 TEXT 타입 사용
    private String content; // 일기 내용
}
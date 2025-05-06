package com.example.mydiary.entity;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Diary {
    private Long id;
    private String uId;     // 외래키 → Member 참조 대신 직접 uId로 받음
    private String title;
    private LocalDate date;
    private String image;
    private String location;
    private String content;
}
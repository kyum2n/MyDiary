package com.example.mydiary.entity;

import java.sql.Date;

import org.springframework.security.core.Transient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Transient
public class Diary {
    private int id;
    private String uId;
    private String title;
    private Date date;
    private String content;
    private String image;
    private String location;
    private String weather;
}

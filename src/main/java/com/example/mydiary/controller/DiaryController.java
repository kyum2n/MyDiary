package com.example.mydiary.controller;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Member;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.mydiary.entity.Diary;
import com.example.mydiary.service.DiaryService;
import com.example.mydiary.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;
    private final UserService userService;

    // 이미지 저장 경로 설정
    @Value("${upload.diary-dir}")
    private String diaryUploadDir;

    // 구글 맵 API 키 설정
    @Value("${google.map.api.key}")
    private String googleMapApiKey;

    // 마이 페이지로 이동
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        String uId = principal.getName();
        com.example.mydiary.entity.Member user = userService.findUserByUId(uId);
        List<Diary> diaries = diaryService.getAllDiaries(uId);
        model.addAttribute("diaries", diaries);
        model.addAttribute("user", user);
        return "myPage";
    }

    // 구글 API 키로 지도 표시
    @GetMapping("/map")
    public String showMap(Model model) {
        model.addAttribute("googleMapApiKey", googleMapApiKey);
        return "myPage";
    }

    // 날짜 선택 후 일기 조회 및 작성
    @GetMapping("/api/diary/{date}")
    public ResponseEntity<?> getDiaryByDate(@PathVariable("date") String date,
            Principal principal) {
        String uId = principal.getName();

        try {
            java.sql.Date sqlDate = java.sql.Date.valueOf(date); // "yyyy-MM-dd" 형식
            Diary diary = diaryService.getDiaryByDateAndUser(sqlDate, uId);
            Map<String, Object> result = new HashMap<>();
            result.put("date", sqlDate.toString());

            if (diary != null) {
                result.put("id", diary.getId());
                result.put("title", diary.getTitle());
                result.put("content", diary.getContent());
                result.put("imageUrl", diary.getImage() != null ? diary.getImage() : "/image/welcome.png");
            } else {
                result.put("id", null);
                result.put("title", null);
                result.put("content", null);
                result.put("imageUrl", "/image/welcome.png");
            }
            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format");
        }
    }

    // 새 일기 추가
    @GetMapping("/newDiary")
    public String newDiary(Model model, Principal principal) {
        model.addAttribute("diary", new Diary());
        return "newDiary";
    }

    // 새 일기 저장
    @PostMapping("/diary/save")
    public String addDiary(@ModelAttribute Diary diary,
            @RequestParam("image") MultipartFile image,
            Principal principal) {

        // 세션에서 로그인 정보 확인
        diary.setUId(principal.getName());

        // 이미지 처리
        if (!image.isEmpty()) {
            try {
                File uploadDir = new File(diaryUploadDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 파일 이름 지정
                String originalFilename = image.getOriginalFilename();
                String filename = System.currentTimeMillis() + "_" + originalFilename;
                String filePath = diaryUploadDir + filename;

                // 파일 저장
                File dest = new File(filePath);
                image.transferTo(dest);

                // DB에는 상대 경로로 저장
                diary.setImage(filePath);
            } catch (IOException e) {
                e.printStackTrace();
                return "error"; // 이미지 저장 실패 시
            }
        }

        // 일기 저장
        try {
            diaryService.addDiary(diary);
            return "redirect:/myPage";
        } catch (Exception e) {
            System.out.println("Redirecting to myPage...");
            return "redirect:/myPage";
        }
    }

    // 일기 수정
    @GetMapping("/editDiary")
    public String editDiary(@RequestParam("id") int id,
            Model model,
            Principal principal) {

        Diary diary = diaryService.getDiaryById(id);

        String uId = principal.getName();

        // 다른 사용자 접근 제한
        if (!diary.getUId().equals(uId)) {
            return "redirect:/myPage";
        }

        model.addAttribute("diary", diary);
        return "editDiary";
    }

    // 일기 수정 사항 저장
    @PostMapping("/diary/edit")
    public String editDiary(@ModelAttribute Diary diary,
            @RequestParam("image") MultipartFile image,
            Principal principal) {

        String uId = principal.getName();
        diary.setUId(uId);

        if (!image.isEmpty()) {
            try {
                File uploadDir = new File(diaryUploadDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // 파일 이름 지정
                String originalFilename = image.getOriginalFilename();
                String filename = System.currentTimeMillis() + "_" + originalFilename;
                String filePath = diaryUploadDir + filename;

                // 파일 저장
                File dest = new File(filePath);
                image.transferTo(dest);

                // DB에는 상대 경로로 저장
                diary.setImage(filePath);

            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        // 일기 수정 사항 저장
        try {
            diaryService.editDiary(diary);
            return "redirect:/myPage";
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Redirecting to myPage...");
            return "redirect:/myPage";
        }
    }

}

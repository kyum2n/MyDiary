package com.example.mydiary.controller;

import java.io.IOException;
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

@Controller
public class DiaryController {

    private final DiaryService diaryService;

    // 이미지 저장 경로 설정
    @Value("${upload.path}")
    private String uploadPath;

    // 구글 맵 API 키 설정
    @Value("${google.map.api.key}")
    private String googleMapApiKey;

    // 생성자 주입
    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    // 마이 페이지로 이동
    @GetMapping("/myPage")
    public String myPage(Model model, Principal principal) {
        String uId = principal.getName();
        List<Diary> diaries = diaryService.getAllDiaries(uId);
        model.addAttribute("diaries", diaries);
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

            if (diary != null) {
                Map<String, Object> result = new HashMap<>();
                result.put("id", diary.getId());
                result.put("title", diary.getTitle());
                result.put("content", diary.getContent());
                result.put("imageUrl", diary.getImage() != null ? diary.getImage() : "/image/default.jpg");
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No diary found");
            }
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
        String uId = principal.getName();

        // 이미지 처리
        if (!image.isEmpty()) {
            try {
                String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();
                Path imagePath = Path.of(uploadPath, imageName);

                // 이미지 저장 경로 설정
                Files.createDirectories(imagePath.getParent());
                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                // 이미지 경로를 Diary 객체에 설정
                diary.setImage("/uploads/" + imageName);

            } catch (IOException e) {
                e.printStackTrace();
                return "error"; // 이미지 저장 실패 시
            }
        }

        // 일기 저장
        try {
            diary.setUId(uId);
            System.out.println("Saving diary: " + diary);
            diaryService.addDiary(diary);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }

        System.out.println("Redirecting to myPage...");
        return "redirect:/myPage";
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
                String imageName = UUID.randomUUID() + "_" + image.getOriginalFilename();

                Path imagePath = Path.of(uploadPath, imageName);

                Files.copy(image.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);

                diary.setImage("/uploads/" + imageName);

            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        diaryService.editDiary(diary);

        return "redirect:/myPage";
    }

}

package com.example.mydiary.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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

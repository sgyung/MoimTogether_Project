package com.project.moimtogether.service.member;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ProfileImageService {
    private static final String PROFILE_IMAGE_PATH = "D:/moimProject/moimtogether/moimtogether/uploads/";

    public String uploadProfileImage(MultipartFile profileImage) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + profileImage.getOriginalFilename();
        File file = new File(PROFILE_IMAGE_PATH, fileName);
        profileImage.transferTo(file);
        return PROFILE_IMAGE_PATH + fileName;
    }
}

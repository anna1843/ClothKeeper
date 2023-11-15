package org.project.dev.member.service;

import org.project.dev.member.dto.ImageResponseDto;
import org.project.dev.member.dto.ImageUploadDto;

public interface ImageService {

    void upload(ImageUploadDto imageUploadDto, String memberEmail);
    void deleteImage(String memberEmail);
    ImageResponseDto findImage(String memberEmail);

}

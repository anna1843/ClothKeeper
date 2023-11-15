package org.project.dev.notice.service;

import lombok.RequiredArgsConstructor;
import org.project.dev.notice.repository.InquiryImgRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryImgService {

    private final InquiryImgRepository inquiryImgRepository;
}

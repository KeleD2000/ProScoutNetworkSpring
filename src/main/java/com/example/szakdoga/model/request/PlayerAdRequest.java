package com.example.szakdoga.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlayerAdRequest {
    private String content;
    private String photo_path;
    private MultipartFile file;

}

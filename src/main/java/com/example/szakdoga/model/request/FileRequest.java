package com.example.szakdoga.model.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileRequest {
    private String file_path;
    private String type;
    private String format;
    private String username;
    private MultipartFile file;
}

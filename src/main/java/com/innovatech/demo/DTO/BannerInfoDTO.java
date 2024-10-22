package com.innovatech.demo.DTO;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BannerInfoDTO {
    private Long id;
    private String title;
    private byte[] picture;
    private Long adminId;

    
}

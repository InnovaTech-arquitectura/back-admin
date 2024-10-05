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
public class BannerDTO {
    private Long id;
    private String title;
    private MultipartFile picture;
    private Long adminId;

    public BannerDTO(String title, MultipartFile picture, Long adminId) {
        this.title = title;
        this.picture = picture;
        this.adminId = adminId;
    }
}

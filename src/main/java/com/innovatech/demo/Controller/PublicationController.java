package com.innovatech.demo.Controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.innovatech.demo.DTO.BannerDTO;
import com.innovatech.demo.DTO.BannerInfoDTO;
import com.innovatech.demo.Entity.Banner;
import com.innovatech.demo.Security.JWTGenerator;
import com.innovatech.demo.Service.MinioService;
import com.innovatech.demo.Service.PublicationService;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@RestController
@RequestMapping("/banner")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;


    @Autowired
    private MinioService minioService;

    @Autowired
    private JWTGenerator jwtGenerator;


    @GetMapping("/all")
    public ResponseEntity<?> listBanners(@RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer limit) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {

            List<Banner> banners = publicationService.listBanners(page, limit);
            List<BannerInfoDTO> bannersDTO = new ArrayList<>();
            //obtener de cada imagen de la lista de banners
            for (Banner banner : banners) {
                //obtener la imagen de minio
                BannerInfoDTO bannerDTO = new BannerInfoDTO(banner.getId(), banner.getTitle(), IOUtils.toByteArray(minioService.getObject(banner.getMultimedia())),banner.getAdministrativeEmployee().getId());
                bannersDTO.add(bannerDTO);
            }

       return ResponseEntity.ok(bannersDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findBanner(@PathVariable Long id) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
        try {
            Banner foundBanner = publicationService.findBanner(id);

            //obtener la imagen de minio
            return ResponseEntity.ok(new BannerInfoDTO(foundBanner.getId(), foundBanner.getTitle(), IOUtils.toByteArray(minioService.getObject(foundBanner.getMultimedia())),foundBanner.getAdministrativeEmployee().getId()));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Banner not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editBanner(@PathVariable Long id, @ModelAttribute BannerDTO editedBannerDto,@RequestHeader("Authorization") String token) {
        try {

            token = token.substring(7);

            String email = jwtGenerator.getUserFromJwt(token);

            Banner editedBanner = publicationService.editBanner(id, editedBannerDto, email);

            //save image in minio
            if (editedBannerDto.getPicture() !=null){
                try {
                    minioService.uploadFile("p-"+editedBanner.getId().toString(),editedBannerDto.getPicture());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    return ResponseEntity.internalServerError().body("Error uploading photo");
                }
             } else{
                    System.out.println("No se ha cambiado la imagen");
             }

            return ResponseEntity.ok(editedBanner);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Banner not found");
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a banner with the same name");
        }
    }

    @PostMapping("/new")
    public ResponseEntity<?> createBanner(@ModelAttribute BannerDTO newBannerDto, @RequestHeader("Authorization") String token) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException {
        
        try {
            token = token.substring(7);
            //System.out.println("UNICORNIO "+jwtGenerator.getUserFromJwt(token));
            String email = jwtGenerator.getUserFromJwt(token);
            
            Banner newBanner = publicationService.createBanner(newBannerDto,email);

            //save image in minio
            try {
                minioService.uploadFile("p-"+newBanner.getId().toString(),newBannerDto.getPicture());
            } catch (IOException e) {
                publicationService.deleteBanner(newBanner.getId());
                throw new RuntimeException(e);
            } catch (Exception e) {
                publicationService.deleteBanner(newBanner.getId());
                return ResponseEntity.badRequest().body("Error uploading photo");
            }

            return ResponseEntity.ok(newBanner);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("There is already a banner with the same name");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBanner(@PathVariable Long id) {
        try {
             publicationService.deleteBanner(id);

            //eliminar la imagen de minio
            try {
                minioService.deleteFile("p-"+id.toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Error deleting photo");
            }

            return ResponseEntity.ok("Banner deleted");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Banner not found");
        }
    }

    
}

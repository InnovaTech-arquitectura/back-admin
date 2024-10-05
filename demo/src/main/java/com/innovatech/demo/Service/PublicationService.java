package com.innovatech.demo.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.checkerframework.checker.units.qual.s;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.innovatech.demo.DTO.BannerDTO;
import com.innovatech.demo.Entity.AdministrativeEmployee;
import com.innovatech.demo.Entity.Banner;
import com.innovatech.demo.Entity.Course;
import com.innovatech.demo.Repository.AdministrativeEmployeeRepository;
import com.innovatech.demo.Repository.BannerRepository;
import com.innovatech.demo.Repository.EventRepository;

import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;

@Service
public class PublicationService {

    @Autowired
    private BannerRepository bannerRepository;
    
    @Autowired
    private AdministrativeEmployeeRepository administrativeEmployeeRepository;

    public Banner findBanner(Long id) {
        return bannerRepository.findById(id).orElseThrow();
    }

    public List<Banner> listBanners(Integer page, Integer limit) {
        PageRequest pageable = PageRequest.of(page - 1, limit);
        return bannerRepository.findAll(pageable).getContent();
    }

    public Banner createBanner( BannerDTO newBannerDto) throws InvalidKeyException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidResponseException, XmlParserException, InternalException, IOException
    {
        Optional<AdministrativeEmployee> adminOpt = administrativeEmployeeRepository.findById(newBannerDto.getAdminId());
        System.out.println("AdministrativeEmployee found with id: "+adminOpt);
        
        if (adminOpt.isPresent()) {
            System.out.println("AdministrativeEmployee found with id: " + newBannerDto.getAdminId());
            Banner banner = new Banner(newBannerDto.getTitle(), newBannerDto.getTitle(), adminOpt.get());
            bannerRepository.save(banner);
            return banner;
        } else {
            // Handle the case where the admin is not found
            throw new RuntimeException("AdministrativeEmployee not found with id: " + newBannerDto.getAdminId());
        }
    }

     public Banner deleteBanner(Long id) {
        Banner deleteBanner = bannerRepository.findById(id).orElseThrow();
        bannerRepository.delete(deleteBanner);

        return deleteBanner;
    }


    public Banner editBanner(Long id,BannerDTO editedBannerDto) {
        // Obtener la fecha y hora actuales como Timestamp
        Optional<AdministrativeEmployee> adminOpt = administrativeEmployeeRepository.findById(editedBannerDto.getAdminId());
        if (adminOpt.isPresent()) {
            Banner editedBanner= new Banner(editedBannerDto.getTitle(),editedBannerDto.getTitle(), adminOpt.get());
            return new Banner();
        } else {
            // Handle the case where the admin is not found
            throw new RuntimeException("AdministrativeEmployee not found with id: " + editedBannerDto.getAdminId());
        }
    }
    
}

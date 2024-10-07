package com.innovatech.demo.Repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.innovatech.demo.Entity.Banner;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    
	
}

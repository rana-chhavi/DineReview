package com.chhavirana.restaurant.services.impl;

import com.chhavirana.restaurant.domain.entities.Photo;
import com.chhavirana.restaurant.services.PhotoService;
import com.chhavirana.restaurant.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {

    private final StorageService storageService;

    @Override
    public Photo uploadPhoto(MultipartFile file) {
        String photoId = UUID.randomUUID().toString();
        String url = storageService.store(file, photoId);

        return Photo.builder().url(url).uploadDate(LocalDateTime.now()).build();
    }

    @Override
    public Optional<Resource> getPhotoAsResource(String id) {
        return storageService.loadAsResource(id);
    }
}

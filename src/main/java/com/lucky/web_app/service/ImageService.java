package com.lucky.web_app.service;

import com.lucky.web_app.domain.Folder;
import com.lucky.web_app.domain.Image;
import com.lucky.web_app.domain.User;
import com.lucky.web_app.model.ImageDTO;
import com.lucky.web_app.repos.FolderRepository;
import com.lucky.web_app.repos.ImageRepository;
import com.lucky.web_app.repos.UserRepository;
import com.lucky.web_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public ImageService(final ImageRepository imageRepository, final UserRepository userRepository,
            final FolderRepository folderRepository) {
        this.imageRepository = imageRepository;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
    }

    public List<ImageDTO> findAll() {
        final List<Image> images = imageRepository.findAll(Sort.by("imageId"));
        return images.stream()
                .map(image -> mapToDTO(image, new ImageDTO()))
                .toList();
    }

    public ImageDTO get(final Integer imageId) {
        return imageRepository.findById(imageId)
                .map(image -> mapToDTO(image, new ImageDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final ImageDTO imageDTO) {
        final Image image = new Image();
        mapToEntity(imageDTO, image);
        return imageRepository.save(image).getImageId();
    }

    public void update(final Integer imageId, final ImageDTO imageDTO) {
        final Image image = imageRepository.findById(imageId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(imageDTO, image);
        imageRepository.save(image);
    }

    public void delete(final Integer imageId) {
        imageRepository.deleteById(imageId);
    }

    public ImageDTO mapToDTO(final Image image, final ImageDTO imageDTO) {
        imageDTO.setImageId(image.getImageId());
        imageDTO.setDescription(image.getDescription());
        imageDTO.setUploadDate(image.getUploadDate());
        imageDTO.setFarmer(image.getFarmer() == null ? null : image.getFarmer().getUserId());
        imageDTO.setFolder(image.getFolder() == null ? null : image.getFolder().getFolderId());
        imageDTO.setHasImage((image.getPostImage() != null) && (image.getPostImage().length > 0) ? true : false);
        return imageDTO;
    }

    private Image mapToEntity(final ImageDTO imageDTO, final Image image) {
        image.setDescription(imageDTO.getDescription());
        image.setUploadDate(imageDTO.getUploadDate());
        final User farmer = imageDTO.getFarmer() == null ? null
                : userRepository.findById(imageDTO.getFarmer())
                        .orElseThrow(() -> new NotFoundException("farmer not found"));
        image.setFarmer(farmer);
        final Folder folder = imageDTO.getFolder() == null ? null
                : folderRepository.findById(imageDTO.getFolder())
                        .orElseThrow(() -> new NotFoundException("folder not found"));
        image.setFolder(folder);
        return image;
    }

}

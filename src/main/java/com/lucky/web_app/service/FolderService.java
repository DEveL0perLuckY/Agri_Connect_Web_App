package com.lucky.web_app.service;

import com.lucky.web_app.domain.Folder;
import com.lucky.web_app.domain.Image;
import com.lucky.web_app.model.FolderDTO;
import com.lucky.web_app.repos.FolderRepository;
import com.lucky.web_app.repos.ImageRepository;
import com.lucky.web_app.repos.UserRepository;
import com.lucky.web_app.util.NotFoundException;
import com.lucky.web_app.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;

    public FolderService(final FolderRepository folderRepository,
            final UserRepository userRepository, final ImageRepository imageRepository) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
        this.imageRepository = imageRepository;
    }

    public List<FolderDTO> findAll() {
        final List<Folder> folders = folderRepository.findAll(Sort.by("folderId"));
        return folders.stream()
                .map(folder -> mapToDTO(folder, new FolderDTO()))
                .toList();
    }

    public FolderDTO get(final Integer folderId) {
        return folderRepository.findById(folderId)
                .map(folder -> mapToDTO(folder, new FolderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final FolderDTO folderDTO) {
        final Folder folder = new Folder();
        mapToEntity(folderDTO, folder);
        return folderRepository.save(folder).getFolderId();
    }

    public void update(final Integer folderId, final FolderDTO folderDTO) {
        final Folder folder = folderRepository.findById(folderId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(folderDTO, folder);
        folderRepository.save(folder);
    }

    public void delete(final Integer folderId) {
        final Folder folder = folderRepository.findById(folderId)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByFarmerFolderMappingFolders(folder)
                .forEach(user -> user.getFarmerFolderMappingFolders().remove(folder));
        folderRepository.delete(folder);
    }

    public FolderDTO mapToDTO(final Folder folder, final FolderDTO folderDTO) {
        folderDTO.setFolderId(folder.getFolderId());
        folderDTO.setFolderName(folder.getFolderName());
        folderDTO.setRegion(folder.getRegion());
        folderDTO.setCrop(folder.getCrop());
        return folderDTO;
    }

    private Folder mapToEntity(final FolderDTO folderDTO, final Folder folder) {
        folder.setFolderName(folderDTO.getFolderName());
        folder.setRegion(folderDTO.getRegion());
        folder.setCrop(folderDTO.getCrop());
        return folder;
    }

    public ReferencedWarning getReferencedWarning(final Integer folderId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Folder folder = folderRepository.findById(folderId)
                .orElseThrow(NotFoundException::new);
        final Image folderImage = imageRepository.findFirstByFolder(folder);
        if (folderImage != null) {
            referencedWarning.setKey("folder.image.folder.referenced");
            referencedWarning.addParam(folderImage.getImageId());
            return referencedWarning;
        }
        return null;
    }

}

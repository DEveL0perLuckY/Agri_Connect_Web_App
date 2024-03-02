package com.lucky.web_app.service;

import com.lucky.web_app.domain.Folder;
import com.lucky.web_app.domain.Image;
import com.lucky.web_app.domain.Roles;
import com.lucky.web_app.domain.User;
import com.lucky.web_app.model.UserDTO;
import com.lucky.web_app.repos.FolderRepository;
import com.lucky.web_app.repos.ImageRepository;
import com.lucky.web_app.repos.RolesRepository;
import com.lucky.web_app.repos.UserRepository;
import com.lucky.web_app.util.NotFoundException;
import com.lucky.web_app.util.ReferencedWarning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final FolderRepository folderRepository;
    private final RolesRepository rolesRepository;
    private final ImageRepository imageRepository;

    public UserService(final UserRepository userRepository, final FolderRepository folderRepository,
            final RolesRepository rolesRepository, final ImageRepository imageRepository) {
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
        this.rolesRepository = rolesRepository;
        this.imageRepository = imageRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserId();
    }

    public void update(final Integer userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer userId) {
        userRepository.deleteById(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setFarmerFolderMappingFolders(user.getFarmerFolderMappingFolders().stream()
                .map(folder -> folder.getFolderId())
                .toList());
        userDTO.setRoleId(user.getRoleId().stream()
                .map(roles -> roles.getId())
                .toList());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        final List<Folder> farmerFolderMappingFolders = iterableToList(folderRepository.findAllById(
                userDTO.getFarmerFolderMappingFolders() == null ? Collections.emptyList() : userDTO.getFarmerFolderMappingFolders()));
        if (farmerFolderMappingFolders.size() != (userDTO.getFarmerFolderMappingFolders() == null ? 0 : userDTO.getFarmerFolderMappingFolders().size())) {
            throw new NotFoundException("one of farmerFolderMappingFolders not found");
        }
        user.setFarmerFolderMappingFolders(new HashSet<>(farmerFolderMappingFolders));
        final List<Roles> roleId = iterableToList(rolesRepository.findAllById(
                userDTO.getRoleId() == null ? Collections.emptyList() : userDTO.getRoleId()));
        if (roleId.size() != (userDTO.getRoleId() == null ? 0 : userDTO.getRoleId().size())) {
            throw new NotFoundException("one of roleId not found");
        }
        user.setRoleId(new HashSet<>(roleId));
        return user;
    }

    private <T> List<T> iterableToList(final Iterable<T> iterable) {
        final List<T> list = new ArrayList<T>();
        iterable.forEach(item -> list.add(item));
        return list;
    }

    public ReferencedWarning getReferencedWarning(final Integer userId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        final Image farmerImage = imageRepository.findFirstByFarmer(user);
        if (farmerImage != null) {
            referencedWarning.setKey("user.image.farmer.referenced");
            referencedWarning.addParam(farmerImage.getImageId());
            return referencedWarning;
        }
        return null;
    }

}

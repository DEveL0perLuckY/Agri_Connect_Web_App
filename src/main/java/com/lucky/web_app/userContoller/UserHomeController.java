package com.lucky.web_app.userContoller;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.lucky.web_app.domain.Folder;
import com.lucky.web_app.domain.Image;
import com.lucky.web_app.domain.User;
import com.lucky.web_app.model.FolderDTO;
import com.lucky.web_app.model.ImageDTO;
import com.lucky.web_app.repos.FolderRepository;
import com.lucky.web_app.repos.ImageRepository;
import com.lucky.web_app.repos.UserRepository;
import com.lucky.web_app.service.FolderService;
import com.lucky.web_app.service.ImageService;
import org.springframework.http.MediaType;
import com.lucky.web_app.util.WebUtils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
@SuppressWarnings("null")
@RequestMapping("/user")
public class UserHomeController {

    @Autowired
    ImageService imageService;

    @Autowired
    FolderRepository folderRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    FolderService folderService;
    @Autowired
    ImageRepository imageRepository;

    @GetMapping
    public String getUser() {
        return "userHome/user";
    }

    @GetMapping("/profile")
    public String getUserprofile(Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).get();

        m.addAttribute("user", user);
        m.addAttribute("obj1", user.getFarmerFolderMappingFolders().size());
        m.addAttribute("obj2", user.getFarmerImages().size());
        return "userHome/userProfile";
    }

    @GetMapping("/folders")
    public String getUserFolders(Model m) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByEmail(username).get();
        List<FolderDTO> folders = user.getFarmerFolderMappingFolders().stream()
                .map(obj -> folderService.mapToDTO(obj, new FolderDTO())).collect(Collectors.toList());
        m.addAttribute("folders", folders);
        return "userHome/userFolder";
    }

    @GetMapping("/folders/add")
    public String getUserFoldersAdd(Model m) {
        m.addAttribute("obj", new FolderDTO());
        return "userHome/userFolderAdd";
    }

    @PostMapping("/folders/add")
    public String getUserFoldersAddPost(Model m, final RedirectAttributes redirectAttributes,
            @ModelAttribute("obj") FolderDTO folderDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username).orElseThrow();
        Folder folder = new Folder();
        folder.setFolderName(folderDTO.getFolderName());
        folder.setCrop(folderDTO.getCrop());
        folder.setRegion(folderDTO.getRegion());
        folder.setFarmerFolderMappingUsers(Set.of(user));
        Folder savedFolder = folderRepository.save(folder);
        user.getFarmerFolderMappingFolders().add(savedFolder);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("Folder Created successfully"));
        return "redirect:/user/folders";
    }

    @GetMapping("/folders/delete/{folderId}")
    public String deleteFolder(@PathVariable Integer folderId, final RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(username).orElseThrow();

        List<Image> imagesToDelete = user.getFarmerImages().stream()
                .filter(img -> img.getFolder().getFolderId().equals(folderId))
                .collect(Collectors.toList());
        imagesToDelete.forEach(imageRepository::delete);
        folderRepository.deleteById(folderId);

        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("Folder Delete successfully"));
        return "redirect:/user/folders";
    }

    @GetMapping("/folders/details/{folderId}")
    public String getFolderDetails(@PathVariable Integer folderId, Model m) {
        m.addAttribute("id", folderId);
        m.addAttribute("cropImageList", folderRepository.findById(folderId).get().getFolderImages().stream()
                .map((x) -> imageService.mapToDTO(x, new ImageDTO())).collect(Collectors.toList()));
        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setFolder(folderId);
        m.addAttribute("cropImage", imageDTO);

        return "userHome/userImage";
    }

    @PostMapping("/folderData/add")
    public String addFolderData(@ModelAttribute("cropImage") ImageDTO imageDTO,
            final RedirectAttributes redirectAttributes, @RequestParam("file") MultipartFile file) throws IOException {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            User user = userRepository.findByEmail(username).get();

            Image img = new Image();
            img.setFarmer(user);
            img.setUploadDate(LocalDateTime.now());
            img.setDescription(imageDTO.getDescription());
            img.setFolder(folderRepository.findById(imageDTO.getFolder()).get());
            if (!file.isEmpty()) {
                img.setPostImage(file.getBytes());
            }
            imageRepository.save(img);

        } catch (Exception e) {
        }
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("Data Added successfully"));
        System.out.println(imageDTO.getFolder());
        return "redirect:/user/folders/details/" + imageDTO.getFolder();
    }

    @GetMapping("/image/delete/{imageId}")
    public String deleteFolderImage(@PathVariable Integer imageId, final RedirectAttributes redirectAttributes) {
        imageRepository.deleteById(imageId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, WebUtils.getMessage("Delete data successfully"));
        return "redirect:/user/folders";
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getPost(@PathVariable("id") int id, Model model) {
        Image post = imageRepository.findById(id).get();
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(post.getPostImage());
    }
}

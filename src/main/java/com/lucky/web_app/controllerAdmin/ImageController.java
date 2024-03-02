package com.lucky.web_app.controllerAdmin;

import com.lucky.web_app.domain.Folder;
import com.lucky.web_app.domain.User;
import com.lucky.web_app.model.ImageDTO;
import com.lucky.web_app.repos.FolderRepository;
import com.lucky.web_app.repos.UserRepository;
import com.lucky.web_app.service.ImageService;
import com.lucky.web_app.util.CustomCollectors;
import com.lucky.web_app.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/images")
public class ImageController {

    private final ImageService imageService;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    public ImageController(final ImageService imageService, final UserRepository userRepository,
            final FolderRepository folderRepository) {
        this.imageService = imageService;
        this.userRepository = userRepository;
        this.folderRepository = folderRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("farmerValues", userRepository.findAll(Sort.by("userId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(User::getUserId, User::getUsername)));
        model.addAttribute("folderValues", folderRepository.findAll(Sort.by("folderId"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Folder::getFolderId, Folder::getFolderName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("images", imageService.findAll());
        return "image/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("image") final ImageDTO imageDTO) {
        return "image/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("image") @Valid final ImageDTO imageDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "image/add";
        }
        imageService.create(imageDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("image.create.success"));
        return "redirect:/admin/images";
    }

    @GetMapping("/edit/{imageId}")
    public String edit(@PathVariable(name = "imageId") final Integer imageId, final Model model) {
        model.addAttribute("image", imageService.get(imageId));
        return "image/edit";
    }

    @PostMapping("/edit/{imageId}")
    public String edit(@PathVariable(name = "imageId") final Integer imageId,
            @ModelAttribute("image") @Valid final ImageDTO imageDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "image/edit";
        }
        imageService.update(imageId, imageDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("image.update.success"));
        return "redirect:/admin/images";
    }

    @PostMapping("/delete/{imageId}")
    public String delete(@PathVariable(name = "imageId") final Integer imageId,
            final RedirectAttributes redirectAttributes) {
        imageService.delete(imageId);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("image.delete.success"));
        return "redirect:/admin/images";
    }

}

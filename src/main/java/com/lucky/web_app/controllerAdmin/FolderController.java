package com.lucky.web_app.controllerAdmin;

import com.lucky.web_app.model.FolderDTO;
import com.lucky.web_app.service.FolderService;
import com.lucky.web_app.util.ReferencedWarning;
import com.lucky.web_app.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/admin/folders")
public class FolderController {

    private final FolderService folderService;

    public FolderController(final FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("folders", folderService.findAll());
        return "folder/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("folder") final FolderDTO folderDTO) {
        return "folder/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("folder") @Valid final FolderDTO folderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "folder/add";
        }
        folderService.create(folderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("folder.create.success"));
        return "redirect:/admin/folders";
    }

    @GetMapping("/edit/{folderId}")
    public String edit(@PathVariable(name = "folderId") final Integer folderId, final Model model) {
        model.addAttribute("folder", folderService.get(folderId));
        return "folder/edit";
    }

    @PostMapping("/edit/{folderId}")
    public String edit(@PathVariable(name = "folderId") final Integer folderId,
            @ModelAttribute("folder") @Valid final FolderDTO folderDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "folder/edit";
        }
        folderService.update(folderId, folderDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("folder.update.success"));
        return "redirect:/admin/folders";
    }

    @PostMapping("/delete/{folderId}")
    public String delete(@PathVariable(name = "folderId") final Integer folderId,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = folderService.getReferencedWarning(folderId);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            folderService.delete(folderId);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("folder.delete.success"));
        }
        return "redirect:/admin/folders";
    }

}

package com.lucky.web_app.controllerAdmin;

import com.lucky.web_app.model.RolesDTO;
import com.lucky.web_app.service.RolesService;
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
@RequestMapping("/admin/roless")
public class RolesController {

    private final RolesService rolesService;

    public RolesController(final RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("roleses", rolesService.findAll());
        return "roles/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("roles") final RolesDTO rolesDTO) {
        return "roles/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("roles") @Valid final RolesDTO rolesDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "roles/add";
        }
        rolesService.create(rolesDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("roles.create.success"));
        return "redirect:/admin/roless";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("roles", rolesService.get(id));
        return "roles/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("roles") @Valid final RolesDTO rolesDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "roles/edit";
        }
        rolesService.update(id, rolesDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("roles.update.success"));
        return "redirect:/admin/roless";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        rolesService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("roles.delete.success"));
        return "redirect:/admin/roless";
    }

}

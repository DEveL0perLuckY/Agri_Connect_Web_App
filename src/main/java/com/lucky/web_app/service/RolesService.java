package com.lucky.web_app.service;

import com.lucky.web_app.domain.Roles;
import com.lucky.web_app.model.RolesDTO;
import com.lucky.web_app.repos.RolesRepository;
import com.lucky.web_app.repos.UserRepository;
import com.lucky.web_app.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class RolesService {

    private final RolesRepository rolesRepository;
    private final UserRepository userRepository;

    public RolesService(final RolesRepository rolesRepository,
            final UserRepository userRepository) {
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
    }

    public List<RolesDTO> findAll() {
        final List<Roles> roleses = rolesRepository.findAll(Sort.by("id"));
        return roleses.stream()
                .map(roles -> mapToDTO(roles, new RolesDTO()))
                .toList();
    }

    public RolesDTO get(final Long id) {
        return rolesRepository.findById(id)
                .map(roles -> mapToDTO(roles, new RolesDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final RolesDTO rolesDTO) {
        final Roles roles = new Roles();
        mapToEntity(rolesDTO, roles);
        return rolesRepository.save(roles).getId();
    }

    public void update(final Long id, final RolesDTO rolesDTO) {
        final Roles roles = rolesRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(rolesDTO, roles);
        rolesRepository.save(roles);
    }

    public void delete(final Long id) {
        final Roles roles = rolesRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        userRepository.findAllByRoleId(roles)
                .forEach(user -> user.getRoleId().remove(roles));
        rolesRepository.delete(roles);
    }

    private RolesDTO mapToDTO(final Roles roles, final RolesDTO rolesDTO) {
        rolesDTO.setId(roles.getId());
        rolesDTO.setName(roles.getName());
        return rolesDTO;
    }

    private Roles mapToEntity(final RolesDTO rolesDTO, final Roles roles) {
        roles.setName(rolesDTO.getName());
        return roles;
    }

    public boolean nameExists(final String name) {
        return rolesRepository.existsByNameIgnoreCase(name);
    }

}

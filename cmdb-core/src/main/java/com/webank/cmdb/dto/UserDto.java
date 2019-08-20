package com.webank.cmdb.dto;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.webank.cmdb.domain.AdmUser;
import com.webank.cmdb.util.CollectionUtils;
import com.webank.cmdb.util.DtoField;
import com.webank.cmdb.util.DtoId;

@JsonInclude(Include.NON_NULL)
public class UserDto extends BasicResourceDto<UserDto, AdmUser> {
    @DtoId
    @DtoField(domainField = "idAdmUser")
    private String userId;
    @NotEmpty
    @DtoField(domainField = "code")
    private String username;
    @DtoField(domainField = "name")
    private String fullName;
    private String description;
    @DtoField(domainField = "admRoleUsers", updatable = false)
    private List<RoleUserDto> roleUsers = new LinkedList<>();

    public UserDto() {
    }

    public UserDto(String userId, @NotEmpty String username, String fullName, String description) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.description = description;
    }

    @Override
    public UserDto fromDomain(AdmUser domainObj, List<String> refResource) {
        UserDto dto = from(domainObj, false);

        if (refResource != null && refResource.contains("roleusers")) {
            List<String> userReses = CollectionUtils.filterCurrentResourceLevel(refResource, "roleusers");
            domainObj.getAdmRoleUsers().forEach(x -> {
                dto.roleUsers.add(new RoleUserDto().fromDomain(x, userReses));
            });
        }
        return dto;
    }

    @Override
    public Class<AdmUser> domainClazz() {
        return AdmUser.class;
    }

    public static UserDto from(AdmUser domain, boolean withChild) {
        UserDto dto = new UserDto(domain.getIdAdmUser(), domain.getCode(), domain.getName(), domain.getDescription());
        if (withChild) {
            domain.getAdmRoleUsers().forEach(x -> {
                dto.roleUsers.add(RoleUserDto.from(x, true));
            });
        }
        return dto;
    }

    public AdmUser toDomain() {
        AdmUser domain = new AdmUser();
        domain.setCode(this.getUsername());
        domain.setName(this.getFullName());
        domain.setDescription(this.getDescription());
        return domain;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RoleUserDto> getRoleUsers() {
        return roleUsers;
    }

    public void setRoleUsers(List<RoleUserDto> roleUsers) {
        this.roleUsers = roleUsers;
    }
}
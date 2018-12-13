package com.Paul.web.app.security.methodSecurity;


import com.Paul.web.app.entity.Role;
import com.Paul.web.app.entity.User;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;



public class CustomMethodSecurityExpressionRoot
        extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    @Autowired
    UserService userService;

    private Object filterObject;
    private Object returnObject;


    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean isOrganisationOwner() {

        System.out.println(this.authentication.getName());
        System.out.println(this.getPrincipal().getClass());

//        MyUserPrincipal mupr = new MyUserPrincipal(new User());

//        User user = ((MyUserPrincipal)this.getPrincipal()).getUser();
        User user = userService.getUserFromSecurityContext();
        boolean hasRoleUser = false;
        for (Role role : user.getUserRoles()) {
            if (role.getName().equals("ORGANISATION_OWNER")) {
                hasRoleUser = true;
            }
        }
        return hasRoleUser;
    }


    /*public boolean isJustUser() {
        User user = userService.getCurrentUser();
        System.out.println(user.getRoles().contains("USER"));
        boolean hasRoleUser = false;
        for (Role role : user.getRoles()) {
            if (role.getRole().equals("USER")) {
                hasRoleUser = true;
            }
        }
        return (user.getRoles().size() == 1
                && hasRoleUser);
    }

    public boolean isGroupAdmin() {
        User user = userService.getCurrentUser();
        boolean hasRoleGroupAdmin = false;
        for (Role role : user.getRoles()) {
            if (role.getRole().equals("GROUP_ADMIN")) {
                hasRoleGroupAdmin = true;
            }
        }
        return hasRoleGroupAdmin;
    }

    public boolean isTestManager() {
        User user = userService.getCurrentUser();
        boolean hasRoleTestManager = false;
        for (Role role : user.getRoles()) {
            if (role.getRole().equals("TEST_MANAGER")) {
                hasRoleTestManager = true;
            }
        }
        return hasRoleTestManager;
    }

    public boolean isStudent() {
        User user = userService.getCurrentUser();
        boolean hasRoleStudent = false;
        for (Role role : user.getRoles()) {
            if (role.getRole().equals("STUDENT")) {
                hasRoleStudent = true;
            }
        }
        return hasRoleStudent;
    }*/

    @Override
    public void setFilterObject(Object o) {
        this.filterObject = o;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object o) {
        this.returnObject = o;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
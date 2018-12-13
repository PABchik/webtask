package com.Paul.web.app.security;

import com.Paul.web.app.entity.User;
import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

//    @PersistenceContext
//    private EntityManager em;

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<User> query = cb.createQuery(User.class);
//        Root<User> root = query.from(User.class);
//        query.where(cb.equal(cb.lower(root.get("email")), s.toLowerCase()));


        try {
//            return new com.Paul.web.app.security.UserDetails(em.createQuery(query).getSingleResult());
            return new com.Paul.web.app.security.UserDetails(userService.findUserByEmail(s));
        } catch (NoResultException e) {
            return null;
        }
    }

}
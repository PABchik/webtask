package com.Paul.web.app.security;

import com.Paul.web.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

//    @PersistenceContext
//    private EntityManager em;

    @Autowired
    UserService userService;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<User> query = cb.createQuery(User.class);
//        Root<User> root = query.from(User.class);
//        query.where(cb.equal(cb.lower(root.get("email")), s.toLowerCase()));


        try {
//            return new com.Paul.web.app.security.UserDetails(em.createQuery(query).getSingleResult());
            return new UserDetails(userService.findUserByEmail(s));
        } catch (NoResultException e) {
            return null;
        }
    }

}

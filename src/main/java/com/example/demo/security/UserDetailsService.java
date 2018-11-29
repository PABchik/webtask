package com.example.demo.security;

import com.example.demo.entity.User;
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

    @PersistenceContext
    private EntityManager em;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.where(cb.equal(cb.lower(root.get("email")), s.toLowerCase()));

        try {
            return new com.example.demo.security.UserDetails(em.createQuery(query).getSingleResult());
        } catch (NoResultException e) {
            return null;
        }
    }

}

package com.Paul.web.app.service;

import com.Paul.web.app.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class InitService {

    private final PlatformTransactionManager txManager;
    private final PasswordEncoder passwordEncoder;

//    @PersistenceContext
//    private EntityManager em;

    public InitService(PlatformTransactionManager txManager, PasswordEncoder passwordEncoder) {
        this.txManager = txManager;
        this.passwordEncoder = passwordEncoder;
    }

//    @PostConstruct
//    public void init() {
//        TransactionTemplate tmpl = new TransactionTemplate(txManager);
//        tmpl.execute(new TransactionCallbackWithoutResult() {
//            @Override
//            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
//                User user = new User();
//                user.setEmail("user@user");
//                user.setPassword(passwordEncoder.encode("user"));
//                em.persist(user);
//            }
//        });
//    }

}

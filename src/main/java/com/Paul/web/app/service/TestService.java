package com.Paul.web.app.service;


import com.Paul.web.app.entity.Group;
import com.Paul.web.app.entity.Test;

import java.util.Set;

public interface TestService {
    Test createTest(Test test);

    Test findById(int testId);

    void deleteTest(Test test);

    Set<Test> findAllOrgTests(String token);

    Test assignTest(Group group, Test test);

    Set<Test> findAllUserTests();
}

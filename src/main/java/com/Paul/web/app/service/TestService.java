package com.Paul.web.app.service;


import com.Paul.web.app.entity.Test;

import java.util.Set;

public interface TestService {
    Test createTest(Test test, String token);

    Test findById(int testId);

    void deleteTest(Test test, String token);

    Set<Test> findAllOrgTests(String token);
}

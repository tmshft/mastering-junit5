/*
 * (C) Copyright 2017 Boni Garcia (http://bonigarcia.github.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package io.github.bonigarcia;

import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

public class LoginService {

    static final Logger log = getLogger(lookup().lookupClass());

    private LoginRepository loginRepository = new LoginRepository();
    private LoginLockManager loginLockManager = new LoginLockManager();
    private List<String> usersLogged = new ArrayList<>();

    public boolean login(UserForm userForm) {
        log.debug("LoginService.login {}", userForm);

        // Preconditions
        checkForm(userForm);

        // Same user cannot be logged twice
        String username = userForm.getUsername();
        if (usersLogged.contains(username)) {
            throw new LoginException(username + " already logged");
        }

        // locked user cannnot be logged.
        if (loginLockManager.isLocked(username)) {
            throw new LoginException(username + " account locked");
        }

        // Call to repository to make logic
        boolean login = loginRepository.login(userForm);
        if (login) {
            usersLogged.add(username);
        } else {
            loginLockManager.failed(username);
        }
        return login;
    }

    public void logout(UserForm userForm) {
        log.debug("LoginService.logout {}", userForm);

        // Preconditions
        checkForm(userForm);

        // Same user cannot be logged twice
        String username = userForm.getUsername();
        if (!usersLogged.contains(username)) {
            throw new LoginException(username + " not logged");
        }

        usersLogged.remove(username);
    }

    private void checkForm(UserForm userForm) {
        assert userForm != null;
        assert userForm.getUsername() != null;
        assert userForm.getPassword() != null;
    }

}
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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceWithoutMock {
    private LoginService loginService;

    @BeforeEach
    void setup() {
        loginService = new LoginService();
    }

    private UserForm userOk = new UserForm("user1", "p1");
    private UserForm userPasswordFail = new UserForm("user1", "p1x");
    private UserForm userKo = new UserForm("foo", "bar");

    @Test
    void testLoginOk() {
        assertTrue(loginService.login(userOk));
    }

    @Test
    void testLoginKo() {
        assertFalse(loginService.login(userKo));
    }

    @Test
    void testLoginTwice() {
        loginService.login(userOk);
        assertThrows(LoginException.class, () -> loginService.login(userOk));
    }

    @Test
    void testLoginLocked() {
        assertFalse(loginService.login(userPasswordFail));
        assertFalse(loginService.login(userPasswordFail));
        assertFalse(loginService.login(userPasswordFail));
        assertThrows(LoginException.class, () -> loginService.login(userOk));
    }

    @Test
    void testLoginUnLocked() {
        assertFalse(loginService.login(userPasswordFail));
        assertFalse(loginService.login(userPasswordFail));
        assertTrue(loginService.login(userOk));
    }

}

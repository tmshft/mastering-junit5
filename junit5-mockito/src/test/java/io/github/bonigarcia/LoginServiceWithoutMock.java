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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceWithoutMock {
    private LoginService loginService;

    @BeforeEach
    void setup() {
        loginService = new LoginService();
    }

    private UserForm userOk = new UserForm("user1", "p1");
    private UserForm userKo = new UserForm("user1", "p1_x");

    @Test
    @DisplayName("アンロック／ユーザ情報が有効")
    void testAccountUnLockedValidAccount() {
        assertFalse(loginService.login(userKo));
        assertFalse(loginService.login(userKo));
        assertTrue(loginService.login(userOk));
    }

    @Test
    @DisplayName("アンロック／ユーザ情報が無効")
    void testAccountUnLockedInvalidAccount() {
        assertFalse(loginService.login(userKo));
        assertFalse(loginService.login(userKo));
        assertFalse(loginService.login(userKo));
    }

    @Test
    @DisplayName("ロック／ユーザ情報が有効")
    void testAccountLockedValidAccount() {
        assertFalse(loginService.login(userKo));
        assertFalse(loginService.login(userKo));
        assertFalse(loginService.login(userKo));
        assertThrows(LoginException.class, () -> loginService.login(userOk));
    }

    @Test
    @DisplayName("ロック／ユーザ情報が無効")
    void testAccountLockedInvalidAccount() {
        assertFalse(loginService.login(userKo));
        assertFalse(loginService.login(userKo));
        assertFalse(loginService.login(userKo));
        assertThrows(LoginException.class, () -> loginService.login(userOk));
    }

    @Test
    @DisplayName("2重ログイン")
    void testLoginTwice() {
        loginService.login(userOk);
        assertThrows(LoginException.class, () -> loginService.login(userOk));
    }
}

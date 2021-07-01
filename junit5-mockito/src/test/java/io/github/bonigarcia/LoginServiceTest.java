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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    LoginService loginService;

    @Mock
    LoginRepository loginRepository;

    @Mock
    LoginLockManager loginLockManager;

    private UserForm userForm = new UserForm("foo", "bar");

    @Test
    @DisplayName("アンロック／ユーザ情報が有効")
    void testAccountUnLockedValidAccount() {
        when(loginRepository.login(any(UserForm.class))).thenReturn(true);
        when(loginLockManager.isLocked(any(String.class))).thenReturn(false);
        assertTrue(loginService.login(userForm));
    }

    @Test
    @DisplayName("アンロック／ユーザ情報が無効")
    void testAccountUnLockedInvalidAccount() {
        when(loginRepository.login(any(UserForm.class))).thenReturn(false);
        when(loginLockManager.isLocked(any(String.class))).thenReturn(false);
        assertFalse(loginService.login(userForm));
    }

    @Test
    @DisplayName("ロック／ユーザ情報が有効")
    void testAccountLockedValidAccount() {
        when(loginLockManager.isLocked(any(String.class))).thenReturn(true);
        assertThrows(LoginException.class, () -> loginService.login(userForm));
        verify(loginLockManager).isLocked(userForm.username);
        verifyNoInteractions(loginRepository);
    }

    @Test
    @DisplayName("ロック／ユーザ情報が無効")
    void testAccountLockedInvalidAccount() {
        when(loginLockManager.isLocked(any(String.class))).thenReturn(true);
        assertThrows(LoginException.class, () -> loginService.login(userForm));
        verify(loginLockManager).isLocked(userForm.username);
        verifyNoInteractions(loginRepository);
    }

    @Test
    @DisplayName("2重ログイン")
    void testLoginTwice() {
        when(loginRepository.login(userForm)).thenReturn(true);
        when(loginLockManager.isLocked(any(String.class))).thenReturn(false);
        assertThrows(LoginException.class, () -> {
            loginService.login(userForm);
            loginService.login(userForm);
        });
        verify(loginRepository).login(userForm);
        verify(loginLockManager).isLocked(userForm.username);
        verify(loginLockManager,times(0)).failed(userForm.username);
    }
}
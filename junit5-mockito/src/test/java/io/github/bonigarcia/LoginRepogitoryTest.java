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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginRepogitoryTest {

    @InjectMocks
    LoginRepository loginRepository;

    @Spy
    LoginLockManager loginLockManager = new LoginLockManager(new HashMap<>());

    UserForm userForm = new UserForm("foo", "bar");

    @Test
    void testLoginUnLocked() {
        when(loginLockManager.isLocked(any(String.class))).thenReturn(false);
        assertDoesNotThrow(() ->{
            loginRepository.login(userForm);
        });
        verify(loginLockManager, atLeast(1)).isLocked(userForm.username);
    }

    @Test
    void testLoginLocked() {
        when(loginLockManager.isLocked(any(String.class))).thenReturn(true);
        assertThrows(LoginException.class, () -> {
            loginRepository.login(userForm);
        });
        verify(loginLockManager, atLeast(1)).isLocked(userForm.username);
    }

    @Test
    void testLoginFailedTwiceThenSucceed() {
        when(loginLockManager.matchAccount(any(String.class),any(String.class))).thenReturn(false);
        assertFalse(loginRepository.login(userForm));
        assertFalse(loginRepository.login(userForm));
        when(loginLockManager.matchAccount(any(String.class),any(String.class))).thenReturn(true);
        assertTrue(loginRepository.login(userForm));
        verify(loginLockManager,times(3)).isLocked(any(String.class));
    }

    @Test
    void testLoginFailed3TimesThenLocked() {
        when(loginLockManager.matchAccount(any(String.class),any(String.class))).thenReturn(false);
        for(int i=0;i < 3;i++) {
            assertFalse(loginRepository.login(userForm));
        }
        assertThrows(LoginException.class, () -> {
            loginRepository.login(userForm);
        });
        //verify(loginLockManager,times(1)).isLocked(any(String.class));
    }


    // Mockを使用しないテストケース
    // - アカウント情報が不完全
    // - アカウント情報が一致しない などなど
}
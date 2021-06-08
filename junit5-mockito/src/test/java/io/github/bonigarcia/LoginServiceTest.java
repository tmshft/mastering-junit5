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

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    LoginService loginService;

    @Mock
    LoginRepository loginRepository;

    private UserForm userForm = new UserForm("foo", "bar");

    @Test
    void testLoginOk() {
        when(loginRepository.login(any(UserForm.class))).thenReturn(true);
        assertTrue(loginService.login(userForm));
        verify(loginRepository).login(userForm);
    }

    @Test
    void testLoginKo() {
        when(loginRepository.login(any(UserForm.class))).thenReturn(false);
        assertFalse(loginService.login(userForm));
        verify(loginRepository).login(userForm);
    }

    @Test
    void testLoginTwice() {
        when(loginRepository.login(userForm)).thenReturn(true);
        assertThrows(LoginException.class, () -> {
            loginService.login(userForm);
            loginService.login(userForm);
        });
    }

//    @Test
//    void testLoginFailedTwiceAndSucceed() {
//        when(loginRepository.login(userForm)).thenReturn(false);
//        assertFalse(loginService.login(userForm));
//        assertFalse(loginService.login(userForm));
//
//        when(loginRepository.login(userForm)).thenReturn(true);
//        loginService.login(userForm);
//        verify(loginRepository,times(3)).login(userForm);
//    }

    @ExtendWith(MockitoExtension.class)
    @Nested
    @MockitoSettings(strictness = Strictness.LENIENT)
    class AccountLockTest {

        @Mock
        LoginLockManager loginLockManager;

//        @Mock
//        LoginRepository loginRepository;

        @Spy
        LoginRepository loginRepository;

        @Spy
        LoginService loginService;

        @Test
        void testAccountLocked() {
            UserForm userForm = new UserForm("user1", "bar");
            when(loginLockManager.isLocked(any(String.class))).thenReturn(true);
//            assertThrows(LoginException.class, () -> {
//                loginRepository.login(userForm);
//            });
            //when(loginRepository.matchAccount(userForm.username,userForm.password)).thenReturn(true);
            assertFalse(loginService.login(userForm));
        }

//        @Test
//        void testAccountUnLocked() {
//            UserForm userForm = new UserForm("user1", "bar");
//            when(loginLockManager.isLocked(userForm.username)).thenReturn(false);
//            when(loginRepository.matchAccount(userForm.username,userForm.password)).thenReturn(false);
//            assertFalse(loginService.login(userForm));
//            verify(loginLockManager,times(1)).failed(userForm.username);
//        }
    }
}
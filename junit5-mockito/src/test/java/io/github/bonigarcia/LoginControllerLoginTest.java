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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.exceptions.verification.NoInteractionsWanted;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class LoginControllerLoginTest {

    // Mocking objects
    @InjectMocks
    LoginController loginController;

    @Mock
    LoginService loginService;

    // Test data
    UserForm userForm = new UserForm("foo", "bar");

    @Test
    void testLoginOk() {
        // Setting expectations (stubbing methods)
        when(loginService.login(userForm)).thenReturn(true);

        // Exercise SUT
        String reseponseLogin = loginController.login(userForm);

        // Verification
        assertEquals("OK", reseponseLogin);
        //verify(loginService).login(userForm);
        verifyNoMoreInteractions(loginService);
    }

    @Test
    void testLoginKo() {
        // Setting expectations (stubbing methods)
        when(loginService.login(userForm)).thenReturn(false);

        // Exercise SUT
        String reseponseLogin = loginController.login(userForm);

        // Verification
        assertEquals("KO", reseponseLogin);
        verify(loginService).login(userForm);
    }

    @Test
    void exampleTest(){
        //mocking lists for the sake of the example (if you mock List in real you will burn in hell)
        List mock1 = mock(List.class), mock2 = mock(List.class);

        //stubbing mocks:
        when(mock1.get(0)).thenReturn(10);
        when(mock2.get(0)).thenReturn(20);

        //using mocks by calling stubbed get(0) methods:
        System.out.println(mock1.get(0)); //prints 10
        System.out.println(mock2.get(0)); //prints 20

        //using mocks by calling clear() methods:
        mock1.clear();
        mock2.clear();

        //verification:
        verify(mock1).clear();
        verify(mock2).clear();

        //verifyNoMoreInteractions() fails because get() methods were not accounted for.
        verifyNoMoreInteractions(mock1, mock2);
        try { verifyNoMoreInteractions(mock1, mock2); } catch (NoInteractionsWanted e){};

        //However, if we ignore stubbed methods then we can verifyNoMoreInteractions()
        verifyNoMoreInteractions(ignoreStubs(mock1, mock2));

        //Remember that ignoreStubs() *changes* the input mocks and returns them for convenience.
    }

}

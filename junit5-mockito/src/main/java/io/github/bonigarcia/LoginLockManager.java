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

import java.util.HashMap;
import java.util.Map;

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

@SuppressWarnings("FieldCanBeLocal")
public class LoginLockManager {
    private final Integer MAX_FAILED_TIME = 3;
    private static final Logger log = getLogger(lookup().lookupClass());

    private Map<String, Integer> usersLockedInfo;
    private Map<String, String> users;

    public LoginLockManager(Map<String, String> users) {
        this.users = new HashMap<>();
        this.usersLockedInfo = new HashMap<>();
    }

    public void failed(String username) {
        if (usersLockedInfo.containsKey(username)) {
            log.debug("LoginLockManager.login {}", username);
            usersLockedInfo.replace(username, usersLockedInfo.get(username) + 1);
        } else {
            usersLockedInfo.put(username, 1);
        }
    }

    public boolean isLocked(String username) {
        if (usersLockedInfo.containsKey(username)) {
            return usersLockedInfo.get(username) >= MAX_FAILED_TIME;
        } else {
            return false;
        }
    }

    boolean matchAccount(String username,String password) {
        return users.keySet().contains(username)
                && users.get(username).equals(password);
    }
}

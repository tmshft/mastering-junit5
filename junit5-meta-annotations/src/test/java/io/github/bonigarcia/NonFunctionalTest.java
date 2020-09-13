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

import static java.lang.invoke.MethodHandles.lookup;
import static org.slf4j.LoggerFactory.getLogger;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

class NonFunctionalTest {

    static final Logger log = getLogger(lookup().lookupClass());

    @Test
    @Load
    void testOne() {
        log.debug("Non-Functional Test 1 (Performance/Load)");
    }

    @Test
    @Stress
    void testTwo() {
        log.debug("Non-Functional Test 2 (Performance/Stress)");
    }

    @Test
    @Security
    void testThree() {
        log.debug("Non-Functional Test 2 (Security)");
    }

    @Test
    @Usability
    void testFour() {
        log.debug("Non-Functional Test 2 (Usability)");
    }

}

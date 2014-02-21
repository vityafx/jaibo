import Tests.ExtensionManagerTests;
import Tests.IrcChannelTests;
import Tests.IrcUserTests;

import junit.framework.TestResult;
import junit.framework.TestSuite;

/**
 * AIBO tests suite
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public final class AIBOTestSuite extends TestSuite {
    public AIBOTestSuite() {
        addTestSuite(IrcChannelTests.class);
        addTestSuite(IrcUserTests.class);
        addTestSuite(ExtensionManagerTests.class);
    }

    public static void main(String[] args) {
        AIBOTestSuite suite = new AIBOTestSuite();

        TestResult result = new TestResult();

        suite.run(result);

        String testResult;

        if (result.wasSuccessful()) {
            testResult = "passed";
        } else {
            testResult = "failed";
        }

        System.out.println(String.format("Tests %s!", testResult));

        System.out.println(String.format("Error count: %s", result.errorCount()));
        System.out.println(String.format("Failure count: %s", result.failureCount()));
    }
}

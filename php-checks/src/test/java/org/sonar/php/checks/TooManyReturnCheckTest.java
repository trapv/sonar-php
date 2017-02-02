/*
 * SonarQube PHP Plugin
 * Copyright (C) 2010-2017 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.php.checks;

import com.google.common.collect.ImmutableList;
import org.junit.Test;
import org.sonar.php.tree.visitors.LegacyIssue;
import org.sonar.plugins.php.TestUtils;
import org.sonar.plugins.php.api.tests.PHPCheckTest;

public class TooManyReturnCheckTest {

  private TooManyReturnCheck check = new TooManyReturnCheck();
  private String fileName = "TooManyReturnCheck.php";

  @Test
  public void defaultValue() throws Exception {
    String message = "Reduce the number of returns of this function 4, down to the maximum allowed 3.";
    PHPCheckTest.check(check, TestUtils.getCheckFile(fileName), ImmutableList.of(new LegacyIssue(check, message).line(3)));
  }

  @Test
  public void custom() throws Exception {
    check.max = 2;
    PHPCheckTest.check(check, TestUtils.getCheckFile(fileName));
  }
}

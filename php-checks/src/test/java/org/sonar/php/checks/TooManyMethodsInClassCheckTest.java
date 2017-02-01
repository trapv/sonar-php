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
import org.sonar.plugins.php.api.visitors.PhpIssue;

public class TooManyMethodsInClassCheckTest {

  private TooManyMethodsInClassCheck check = new TooManyMethodsInClassCheck();
  private static final String fileName= "TooManyMethodsInClassCheck.php";

  @Test
  public void defaultValue() throws Exception  {
    PHPCheckTest.check(check, TestUtils.getCheckFile(fileName), ImmutableList.<PhpIssue>of());
  }

  @Test
  public void custom_maximum_method_threshold() throws Exception {
    check.maximumMethodThreshold = 2;
    PHPCheckTest.check(check, TestUtils.getCheckFile(fileName));
  }

  @Test
  public void custom_count_non_public_method() throws Exception {
    check.maximumMethodThreshold = 2;
    check.countNonpublicMethods = false;

    ImmutableList<PhpIssue> issues = ImmutableList.<PhpIssue>of(
      new LegacyIssue(check, "Class \"I\" has 3 methods, which is greater than 2 authorized. Split it into smaller classes.").line(3),
      new LegacyIssue(check, "This anonymous class has 3 methods, which is greater than 2 authorized. Split it into smaller classes.").line(35)
    );
    PHPCheckTest.check(check, TestUtils.getCheckFile(fileName), issues);
  }

}

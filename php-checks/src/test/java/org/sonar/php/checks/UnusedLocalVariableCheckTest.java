/*
 * SonarQube PHP Plugin
 * Copyright (C) 2010 SonarSource and Akram Ben Aissi
 * dev@sonar.codehaus.org
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
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.php.checks;

import org.junit.Test;
import org.sonar.php.PHPAstScanner;
import org.sonar.plugins.php.CheckTest;
import org.sonar.plugins.php.TestUtils;
import org.sonar.squidbridge.api.SourceFile;

public class UnusedLocalVariableCheckTest extends CheckTest {

  @Test
  public void test() {
    SourceFile file = PHPAstScanner.scanSingleFile(TestUtils.getCheckFile("UnusedLocalVariableCheck.php"), new UnusedLocalVariableCheck());

    checkMessagesVerifier.verify(file.getCheckMessages())
      .next().atLine(8).withMessage("Remove this unused \"$b\" local variable.")
      .next().atLine(9).withMessage("Remove this unused \"$c\" local variable.")
      .next().atLine(19)
      .next().atLine(29)
      .next().atLine(38)
      .next().atLine(54)
      .next().atLine(65)
      .next().atLine(65)
      .next().atLine(124)
      .next().atLine(126).withMessage("Remove this unused \"$c\" local variable.")
      .noMore();
  }
}

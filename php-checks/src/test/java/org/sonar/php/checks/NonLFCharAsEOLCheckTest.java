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
import com.google.common.io.Files;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.sonar.php.tree.visitors.LegacyIssue;
import org.sonar.plugins.php.api.tests.PHPCheckTest;
import org.sonar.plugins.php.api.visitors.PhpIssue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class NonLFCharAsEOLCheckTest {

  @Rule
  public TemporaryFolder temporaryFolder = new TemporaryFolder();

  private NonLFCharAsEOLCheck check = new NonLFCharAsEOLCheck();
  private File ok_file;
  private File ko_file;

  @Before
  public void setUp() throws Exception {
    ok_file = temporaryFolder.newFile();
    Files.write("<?php $foo = 1; \n", ok_file, Charset.defaultCharset());

    ko_file = temporaryFolder.newFile();
    Files.write("<?php $foo = 1; \r\n", ko_file, Charset.defaultCharset());
  }

  @Test
  public void ok() throws IOException {
    PHPCheckTest.check(check, ok_file);
  }

  @Test
  public void ko() throws IOException {
    ImmutableList<PhpIssue> issues = ImmutableList.<PhpIssue>of(
      new LegacyIssue(check, "Replace all non line feed end of line characters in this file \"" + ko_file.getName() + "\" by LF."));
    PHPCheckTest.check(check, ko_file, issues);
  }

}

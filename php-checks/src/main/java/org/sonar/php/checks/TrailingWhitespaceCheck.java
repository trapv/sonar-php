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

import com.google.common.io.Files;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;
import org.sonar.check.Rule;
import org.sonar.php.api.CharsetAwareVisitor;
import org.sonar.php.parser.LexicalConstant;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.visitors.PHPVisitorCheck;

@Rule(key = TrailingWhitespaceCheck.KEY)
public class TrailingWhitespaceCheck extends PHPVisitorCheck implements CharsetAwareVisitor {

  public static final String KEY = "S1131";
  private static final String MESSAGE = "Remove the useless trailing whitespaces at the end of this line.";

  private Charset charset;
  private static final Pattern WHITESPACE_PATTERN = Pattern.compile("[" + LexicalConstant.WHITESPACE + "]");

  @Override
  public void setCharset(Charset charset) {
    this.charset = charset;
  }

  @Override
  public void visitCompilationUnit(CompilationUnitTree tree) {
    List<String> lines;
    try {
      lines = Files.readLines(context().file(), charset);
    } catch (IOException e) {
      throw new IllegalStateException("Check S1131: Can't read the file", e);
    }
    for (int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if (line.length() > 0 && WHITESPACE_PATTERN.matcher(line.subSequence(line.length() - 1, line.length())).matches()) {
        context().newLineIssue(this, i + 1, MESSAGE);
      }
    }

  }

}

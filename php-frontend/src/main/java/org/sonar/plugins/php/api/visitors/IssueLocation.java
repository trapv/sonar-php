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
package org.sonar.plugins.php.api.visitors;

import javax.annotation.Nullable;
import org.sonar.php.tree.impl.PHPTree;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;

public class IssueLocation {

  private final SyntaxToken firstToken;
  private final SyntaxToken lastToken;
  private final String message;

  public IssueLocation(Tree tree, @Nullable String message) {
    this.firstToken = ((PHPTree) tree).getFirstToken();
    this.lastToken = ((PHPTree) tree).getLastToken();
    this.message = message;
  }

  public IssueLocation(Tree startTree, Tree endTree, @Nullable String message) {
    this.firstToken = ((PHPTree) startTree).getFirstToken();
    this.lastToken = ((PHPTree) endTree).getLastToken();
    this.message = message;
  }

  public int startLine() {
    return firstToken.line();
  }

  public int startLineOffset() {
    return firstToken.column();
  }

  public int endLine() {
    return lastToken.endLine();
  }

  public int endLineOffset() {
    return lastToken.endColumn();
  }

  @Nullable
  public String message() {
    return message;
  }
}

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
package org.sonar.php.tree.impl.expression;

import org.junit.Test;
import org.sonar.php.PHPTreeModelTest;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.expression.LexicalVariablesTree;

import static org.assertj.core.api.Assertions.assertThat;

public class LexicalVariablesTreeTest extends PHPTreeModelTest {

  @Test
  public void single() throws Exception {
    LexicalVariablesTree tree = parse("use ($a)", Kind.LEXICAL_VARIABLES);

    assertThat(tree.is(Kind.LEXICAL_VARIABLES)).isTrue();
    assertThat(expressionToString(tree.useToken())).isEqualTo("use");
    assertThat(tree.openParenthesisToken().text()).isEqualTo("(");
    assertThat(tree.variables()).hasSize(1);
    assertThat(expressionToString(tree.variables().get(0))).isEqualTo("$a");
    assertThat(tree.closeParenthesisToken().text()).isEqualTo(")");
  }

  @Test
  public void multiple() throws Exception {
    LexicalVariablesTree tree = parse("use ($a, $b)", Kind.LEXICAL_VARIABLES);

    assertThat(tree.is(Kind.LEXICAL_VARIABLES)).isTrue();
    assertThat(expressionToString(tree.useToken())).isEqualTo("use");
    assertThat(tree.openParenthesisToken().text()).isEqualTo("(");
    assertThat(tree.variables()).hasSize(2);
    assertThat(expressionToString(tree.variables().get(0))).isEqualTo("$a");
    assertThat(expressionToString(tree.variables().get(1))).isEqualTo("$b");
    assertThat(tree.closeParenthesisToken().text()).isEqualTo(")");
  }

}

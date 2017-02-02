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
import org.sonar.plugins.php.api.tree.expression.NewExpressionTree;

import static org.assertj.core.api.Assertions.assertThat;

public class NewExpressionTreeTest extends PHPTreeModelTest {

  @Test
  public void test() throws Exception {
    NewExpressionTree tree = parse("new Foo", Kind.NEW_EXPRESSION);

    assertThat(tree.is(Kind.NEW_EXPRESSION)).isTrue();

    assertThat(tree.newToken().text()).isEqualTo("new");
    assertThat(expressionToString(tree.expression())).isEqualTo("Foo");
  }

}

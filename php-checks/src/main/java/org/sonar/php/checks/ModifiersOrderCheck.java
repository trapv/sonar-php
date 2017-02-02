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
import java.util.List;
import java.util.Locale;
import org.sonar.check.Rule;
import org.sonar.php.api.PHPKeyword;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.tree.declaration.ClassPropertyDeclarationTree;
import org.sonar.plugins.php.api.tree.declaration.MethodDeclarationTree;
import org.sonar.plugins.php.api.tree.lexical.SyntaxToken;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;

@Rule(key = ModifiersOrderCheck.KEY)
public class ModifiersOrderCheck extends PHPSubscriptionCheck {

  public static final String KEY = "S1124";

  private static final String MESSAGE = "Reorder the modifiers to comply with the PSR2 standard.";

  private static final String[] EXPECTED_ORDER = {
    PHPKeyword.FINAL.getValue(),
    PHPKeyword.ABSTRACT.getValue(),
    PHPKeyword.PUBLIC.getValue(),
    PHPKeyword.PROTECTED.getValue(),
    PHPKeyword.PRIVATE.getValue(),
    PHPKeyword.STATIC.getValue()};

  @Override
  public List<Kind> nodesToVisit() {
    return ImmutableList.of(Kind.METHOD_DECLARATION, Kind.CLASS_PROPERTY_DECLARATION);
  }

  @Override
  public void visitNode(Tree tree) {
    if (tree.is(Kind.METHOD_DECLARATION)) {
      checkModifiers(((MethodDeclarationTree) tree).modifiers());
    } else {
      checkModifiers(((ClassPropertyDeclarationTree) tree).modifierTokens());
    }
  }

  private void checkModifiers(List<SyntaxToken> modifiers) {
    if (modifiers.size() > 1) {
      int i = 0;
      for (SyntaxToken modifier : modifiers) {
        String normalizedModifier = modifier.text().toLowerCase(Locale.ENGLISH);
        while (i < EXPECTED_ORDER.length && !EXPECTED_ORDER[i].equals(normalizedModifier)) {
          i++;
        }
      }
      if (i == EXPECTED_ORDER.length) {
        context().newIssue(this, modifiers.get(0), modifiers.get(modifiers.size() - 1), MESSAGE);
      }
    }
  }

}

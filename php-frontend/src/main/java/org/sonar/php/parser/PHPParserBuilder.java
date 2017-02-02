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
package org.sonar.php.parser;

import java.nio.charset.Charset;

import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.sslr.grammar.GrammarRuleKey;

import com.sonar.sslr.api.typed.ActionParser;

public class PHPParserBuilder {

  private PHPParserBuilder(){
  }

  public static ActionParser<Tree> createParser(Charset charset) {
    return createParser(PHPLexicalGrammar.COMPILATION_UNIT, charset);
  }

  /**
   * This method should be used by tests only.
   * Provides ability to start parsing from some rule other than PHPLexicalGrammar.COMPILATION_UNIT.
   * @param rootRule rule from which parsing starts
   * @param charset
   */
  public static ActionParser<Tree> createParser(GrammarRuleKey rootRule, Charset charset) {
    return createParser(rootRule, charset, 0);
  }

  /**
   * This method should be used if required to shift line of tokens
   */
  public static ActionParser<Tree> createParser(GrammarRuleKey rootRule, Charset charset, int lineOffset) {
    return new ActionParser<>(
        charset,
        PHPLexicalGrammar.createGrammarBuilder(),
        PHPGrammar.class,
        new TreeFactory(charset),
        new PHPNodeBuilder(lineOffset),
        rootRule);
  }

}

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
package org.sonar.php;

import com.google.common.collect.ImmutableList;
import com.sonar.sslr.api.typed.ActionParser;
import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.highlighting.NewHighlighting;
import org.sonar.api.batch.sensor.symbol.NewSymbolTable;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.php.api.CharsetAwareVisitor;
import org.sonar.php.highlighter.SymbolHighlighter;
import org.sonar.php.highlighter.SyntaxHighlighterVisitor;
import org.sonar.php.metrics.FileMeasures;
import org.sonar.php.metrics.MetricsVisitor;
import org.sonar.php.parser.PHPParserBuilder;
import org.sonar.php.tree.symbols.SymbolTableImpl;
import org.sonar.plugins.php.api.symbols.SymbolTable;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.visitors.PHPCheck;
import org.sonar.plugins.php.api.visitors.PhpIssue;

public class PHPAnalyzer {

  private final ActionParser<Tree> parser;
  private final ImmutableList<PHPCheck> checks;

  private CompilationUnitTree currentFileTree;
  private File currentFile;
  private SymbolTable currentFileSymbolTable;

  public PHPAnalyzer(Charset charset, ImmutableList<PHPCheck> checks) {
    this.parser = PHPParserBuilder.createParser(charset);
    this.checks = checks;

    for (PHPCheck check : checks) {
      if (check instanceof CharsetAwareVisitor) {
        ((CharsetAwareVisitor) check).setCharset(charset);
      }
      check.init();
    }
  }

  public void nextFile(File file) {
    currentFile = file;
    currentFileTree = (CompilationUnitTree) parser.parse(file);
    currentFileSymbolTable = SymbolTableImpl.create(currentFileTree);
  }

  public List<PhpIssue> analyze() {
    ImmutableList.Builder<PhpIssue> issuesBuilder = ImmutableList.builder();
    for (PHPCheck check : checks) {
      issuesBuilder.addAll(check.analyze(currentFile, currentFileTree, currentFileSymbolTable));
    }

    return issuesBuilder.build();
  }

  public FileMeasures computeMeasures(FileLinesContext fileLinesContext, Map<File, Integer> numberOfLinesOfCode, boolean saveExecutableLines) {
    return new MetricsVisitor().getFileMeasures(currentFile, currentFileTree, fileLinesContext, numberOfLinesOfCode, saveExecutableLines);
  }

  public NewHighlighting getSyntaxHighlighting(SensorContext context, InputFile inputFile) {
    NewHighlighting highlighting = context.newHighlighting().onFile(inputFile);
    SyntaxHighlighterVisitor.highlight(currentFileTree, highlighting);
    return highlighting;
  }

  public NewSymbolTable getSymbolHighlighting(SensorContext context, InputFile inputFile) {
    NewSymbolTable symbolTable = context.newSymbolTable().onFile(inputFile);
    new SymbolHighlighter().highlight(currentFileSymbolTable, symbolTable);
    return symbolTable;
  }

}

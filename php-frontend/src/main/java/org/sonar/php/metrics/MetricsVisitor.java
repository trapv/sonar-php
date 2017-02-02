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
package org.sonar.php.metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.api.measures.FileLinesContext;
import org.sonar.php.compat.CompatibleInputFile;
import org.sonar.plugins.php.api.tree.CompilationUnitTree;
import org.sonar.plugins.php.api.tree.Tree;
import org.sonar.plugins.php.api.tree.Tree.Kind;
import org.sonar.plugins.php.api.visitors.PHPSubscriptionCheck;

public class MetricsVisitor extends PHPSubscriptionCheck {

  private static final Number[] LIMITS_COMPLEXITY_FUNCTIONS = {1, 2, 4, 6, 8, 10, 12};
  private static final Number[] FILES_DISTRIBUTION_BOTTOM_LIMITS = {0, 5, 10, 20, 30, 60, 90};

  private static final Kind[] FUNCTION_NODES = {
    Kind.FUNCTION_DECLARATION,
    Kind.FUNCTION_EXPRESSION,
    Kind.METHOD_DECLARATION,
  };

  private static final Kind[] CLASS_NODES = {
    Kind.CLASS_DECLARATION,
    Kind.INTERFACE_DECLARATION,
    Kind.TRAIT_DECLARATION
  };

  private FileMeasures fileMeasures;

  private FileLinesContext fileLinesContext;

  private boolean saveExecutableLines;

  public static Kind[] getClassNodes() {
    return CLASS_NODES;
  }

  public static Kind[] getFunctionNodes() {
    return FUNCTION_NODES;
  }

  @Override
  public List<Kind> nodesToVisit() {
    List<Kind> result = new ArrayList<>(Arrays.asList(FUNCTION_NODES));
    result.addAll(Arrays.asList(CLASS_NODES));
    result.add(Kind.COMPILATION_UNIT);
    return result;
  }

  @Override
  public void visitNode(Tree tree) {
    if (tree.is(Kind.COMPILATION_UNIT)) {
      fileMeasures.setFileComplexity(ComplexityVisitor.complexity(tree));

    } else if (tree.is(CLASS_NODES)) {
      fileMeasures.addClassComplexity(ComplexityVisitor.complexity(tree));

    } else if (tree.is(FUNCTION_NODES)) {
      fileMeasures.addFunctionComplexity(ComplexityVisitor.complexity(tree));
    }
  }

  public FileMeasures getFileMeasures(CompatibleInputFile file, CompilationUnitTree tree, FileLinesContext fileLinesContext, boolean saveExecutableLines) {
    this.saveExecutableLines = saveExecutableLines;
    this.fileMeasures = new FileMeasures(LIMITS_COMPLEXITY_FUNCTIONS, FILES_DISTRIBUTION_BOTTOM_LIMITS);
    this.fileLinesContext = fileLinesContext;

    super.analyze(file, tree);

    setCounterMeasures();
    setLineAndCommentMeasures(file);
    return this.fileMeasures;
  }

  private void setCounterMeasures() {
    CounterVisitor counter = new CounterVisitor(context().tree());
    fileMeasures.setClassNumber(counter.getClassNumber());
    fileMeasures.setFunctionNumber(counter.getFunctionNumber());
    fileMeasures.setStatementNumber(counter.getStatementNumber());
  }

  private void setLineAndCommentMeasures(CompatibleInputFile file) {
    LineVisitor lineVisitor = new LineVisitor(context().tree());

    CommentLineVisitor commentVisitor = new CommentLineVisitor(context().tree());

    int linesNumber = lineVisitor.getLinesNumber();

    fileMeasures.setLinesNumber(linesNumber);
    fileMeasures.setLinesOfCodeNumber(lineVisitor.getLinesOfCodeNumber());
    fileMeasures.setCommentLinesNumber(commentVisitor.commentLineNumber());
    fileMeasures.setNoSonarLines(commentVisitor.noSonarLines());

    Set<Integer> linesOfCode = lineVisitor.getLinesOfCode();
    Set<Integer> commentLines = commentVisitor.commentLines();

    for (int line = 1; line <= linesNumber; line++) {
      fileLinesContext.setIntValue(CoreMetrics.NCLOC_DATA_KEY, line, linesOfCode.contains(line) ? 1 : 0);
      fileLinesContext.setIntValue(CoreMetrics.COMMENT_LINES_DATA_KEY, line, commentLines.contains(line) ? 1 : 0);
      if (saveExecutableLines) {
        fileLinesContext.setIntValue(CoreMetrics.EXECUTABLE_LINES_DATA_KEY, line, linesOfCode.contains(line) ? 1 : 0);
      }
    }

    fileLinesContext.save();
  }

}

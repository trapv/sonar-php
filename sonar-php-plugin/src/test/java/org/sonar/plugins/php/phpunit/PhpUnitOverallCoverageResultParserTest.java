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
package org.sonar.plugins.php.phpunit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.measures.CoreMetrics;
import org.sonar.plugins.php.PhpTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

public class PhpUnitOverallCoverageResultParserTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  private SensorContext context;

  private PhpUnitCoverageResultParser parser;

  @Before
  public void setUp() throws Exception {
    parser = new PhpUnitOverallCoverageResultParser(PhpTestUtils.getDefaultFileSystem());
  }

  @Test
  public void shouldSetMetrics() {
    assertThat(parser.linesToCoverMetric).isEqualTo(CoreMetrics.OVERALL_LINES_TO_COVER);
    assertThat(parser.uncoveredLinesMetric).isEqualTo(CoreMetrics.OVERALL_UNCOVERED_LINES);
  }

}

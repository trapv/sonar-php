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
package org.sonar.plugins.php;

import org.junit.Test;
import org.sonar.api.profiles.RulesProfile;
import org.sonar.api.utils.ValidationMessages;
import org.sonar.php.checks.CheckList;
import org.sonar.plugins.php.api.Php;

import static org.assertj.core.api.Assertions.assertThat;

public class DrupalProfileTest {

  @Test
  public void profile_creation() {
    ValidationMessages validation = ValidationMessages.create();

    DrupalProfile definition = new DrupalProfile(new FakeProfileParser());
    RulesProfile profile = definition.createProfile(validation);

    assertThat(profile.getLanguage()).isEqualTo(Php.KEY);
    assertThat(profile.getName()).isEqualTo("Drupal");
    assertThat(profile.getActiveRulesByRepository(CheckList.REPOSITORY_KEY)).hasSize(20);
    assertThat(validation.hasErrors()).isFalse();
  }

}

/**
 * Copyright (C) 2007 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.inject.tools.suite.module;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.tools.suite.code.CodeRunner;
import com.google.inject.tools.suite.snippets.ModuleContextSnippet;

/**
 * A {@link CodeRunner.Runnable} that can be used by the {@link CodeRunner} to
 * run a {@link com.google.inject.tools.suite.snippets.ModuleContextSnippet} in
 * userspace for an application module context 
 * ({@link com.google.inject.tools.suite.module.ApplicationModuleContextRepresentation}).
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
class RunApplicationModuleContextSnippet extends CodeRunner.Runnable {
  private final ApplicationModuleContextRepresentation moduleContext;

  public RunApplicationModuleContextSnippet(CodeRunner codeRunner,
      ApplicationModuleContextRepresentation moduleContext) {
    super(codeRunner);
    this.moduleContext = moduleContext;
  }

  @Override
  public String label() {
    return "Running module context " + moduleContext.getName();
  }

  @Override
  protected String getFullyQualifiedSnippetClass() {
    return ModuleContextSnippet.class.getName();
  }

  @Override
  protected List<? extends Object> getSnippetArguments() {
    final List<Object> args = new ArrayList<Object>();
    args.add(moduleContext.getName());
    args.add(String.valueOf(-1));
    args.add(moduleContext.getClassName());
    return args;
  }
}

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

import com.google.inject.tools.suite.Messenger;
import com.google.inject.tools.suite.SampleModuleScenario.WorkingModule;
import com.google.inject.tools.suite.code.CodeRunner;
import com.google.inject.tools.suite.module.ModuleRepresentation;
import com.google.inject.tools.suite.module.ModuleRepresentationImpl;
import com.google.inject.tools.suite.snippets.CodeSnippetResult;
import com.google.inject.tools.suite.snippets.ModuleSnippet;
import com.google.inject.tools.suite.snippets.ModuleSnippet.DefaultConstructorRepresentation;
import com.google.inject.tools.suite.snippets.problems.CodeProblem;

import junit.framework.TestCase;

import java.util.Collections;

/**
 * Unit test the {@link ModuleRepresentationImpl}.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class ModuleRepresentationTest extends TestCase {
  public void testModuleRepresentation() throws Exception {
    ModuleRepresentation module =
        new ModuleRepresentationImpl(WorkingModule.class.getName());
    CodeRunner codeRunner = new SimulatedCodeRunner();
    module.clean(codeRunner);
    codeRunner.run("", true);
    codeRunner.waitFor();
    assertFalse(module.isDirty());
    assertTrue(module.hasDefaultConstructor());
    assertTrue(module.getName().equals(WorkingModule.class.getName()));
    assertTrue(module.getConstructors().equals(
        Collections.singleton(new DefaultConstructorRepresentation())));
  }

  private class SimulatedCodeRunner implements CodeRunner {
    private CodeRunListener listener;

    public void addListener(CodeRunListener listener) {
      this.listener = listener;
    }

    public Messenger getMessenger() {
      return new Messenger() {
        public void display(String message) {
        }

        public void logException(String label, Throwable throwable) {
        }

        public void logMessage(String message) {
        }
        
        public void logCodeRunnerMessage(String message) {
          
        }
        
        public void logCodeRunnerException(String label, Throwable throwable) {
          
        }
      };
    }

    public boolean isCancelled() {
      return false;
    }

    public boolean isDone() {
      return true;
    }

    public boolean isDone(Runnable runnable) {
      return true;
    }

    public void kill() {
    }

    public void kill(Runnable runnable) {
    }

    public void waitFor() {
    }

    public void waitFor(Runnable runnable) {
    }

    public void notifyDone(Runnable runnable) {
    }

    public void notifyResult(Runnable runnable, CodeSnippetResult result) {
      listener.acceptCodeRunResult(result);
    }

    public void queue(Runnable runnable) {
    }

    public void run(String label, boolean backgroundAutomatically) {
      notifyResult(null, simulatedSnippetResult());
    }

    public void run(String label) {
      run(label, true);
    }

    private ModuleSnippet.ModuleResult simulatedSnippetResult() {
      return new ModuleSnippet.ModuleResult(WorkingModule.class.getName(),
          Collections.<CodeProblem> emptySet(), true, Collections
              .singleton(new DefaultConstructorRepresentation()));
    }
  }
}

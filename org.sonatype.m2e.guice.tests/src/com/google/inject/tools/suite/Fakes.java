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

package com.google.inject.tools.suite;

import com.google.inject.CreationException;
import com.google.inject.spi.Message;
import com.google.inject.tools.suite.Messenger;
import com.google.inject.tools.suite.ProgressHandler.ProgressMonitor;
import com.google.inject.tools.suite.code.CodeRunner;
import com.google.inject.tools.suite.snippets.CodeSnippet;
import com.google.inject.tools.suite.snippets.CodeSnippetResult;
import com.google.inject.tools.suite.snippets.problems.CodeProblem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Fake objects for use in testing.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class Fakes {
  public static class FakeCreationException extends CreationException {
    private static final long serialVersionUID = -6889671178292449161L;
    
    private static Set<Message> messages =
        Collections.singleton(new Message("Mock Guice Message."));

    public FakeCreationException() {
      super(messages);
    }

    @Override
    public String toString() {
      return "Mock Creation Exception.";
    }
  }

  public static class TestSnippet extends CodeSnippet {
    public TestSnippet(int millisecsToTake) {
      super();
      try {
        Thread.sleep(millisecsToTake);
      } catch (Exception exception) {
        // do nothing
      }
    }

    public static class TestSnippetResult extends CodeSnippetResult {
      private static final long serialVersionUID = 7098318823969460583L;
      
      private final String blah = "blah";

      public TestSnippetResult() {
        super(Collections.<CodeProblem> emptySet());
      }

      public String getBlah() {
        return blah;
      }
    }

    @Override
    public CodeSnippetResult getResult() {
      return new TestSnippetResult();
    }

    public static void main(String[] args) {
      int millisecsToTake;
      if (args.length > 0) {
        millisecsToTake = Integer.valueOf(args[0]);
      } else {
        millisecsToTake = -1;
      }
      new TestSnippet(millisecsToTake).printResult(System.out);
    }
  }

  public static class FakeCodeRunner implements CodeRunner {
    public void addListener(CodeRunListener listener) {
    }

    public Messenger getMessenger() {
      return null;
    }

    public boolean isCancelled() {
      return false;
    }

    public boolean isDone() {
      return false;
    }

    public boolean isDone(Runnable runnable) {
      return false;
    }

    public void kill() {
    }

    public void kill(Runnable runnable) {
    }

    public void notifyResult(Runnable runnable, CodeSnippetResult result) {
    }

    public void queue(Runnable runnable) {
    }

    public void run(String label, boolean backgroundAutomatically) {
    }

    public void run(String label) {
    }

    public void waitFor() {
    }

    public void waitFor(Runnable runnable) {
    }

    public void notifyDone(Runnable runnable) {
    }
  }

  public static class FakeJavaManager implements JavaManager {
    public String getJavaCommand() throws Exception {
      return null;
    }
    
    public List<String> getJavaFlags() throws Exception {
      return null;
    }

    public String getProjectClasspath() throws Exception {
      return null;
    }
    
    public String getGuiceClasspath() throws Exception {
      return null;
    }

    public String getSnippetsClasspath() throws Exception {
      return null;
    }
    
    public String getClasspathDelimiter() {
      return null;
    }
  }
  
  public static class FakeMessenger implements Messenger {
    public void display(String message) {
    }

    public void logException(String label, Throwable throwable) {
    }

    public void logMessage(String message) {
    }
    
    public void logCodeRunnerException(String label, Throwable throwable) {
    }

    public void logCodeRunnerMessage(String message) {
    }
  }
  
  static class FakeProgressMonitor implements ProgressMonitor {
    public void begin(String label, int units) {
    }

    public void done() {
    }

    public ProgressMonitor getSubMonitor(int parentunits) {
      return this;
    }

    public void worked(int workedunits) {
    }
  }
  
  public static class FakeProgressHandler implements ProgressHandler {
    private final List<ProgressStep> steps = new ArrayList<ProgressStep>();
    private Runnable executeAfter;
    
    public void go(String label, boolean backgroundAutomatically) {
      for (ProgressStep step : steps) {
        step.run(new FakeProgressMonitor());
        step.complete();
      }
      if (executeAfter != null) executeAfter.run();
    }

    public void go(String label, boolean backgroundAutomatically, boolean cancelThread) {
      go(label, backgroundAutomatically);
    }
    
    public void waitFor() {
    }
    
    public boolean isDone() {
      return true;
    }

    public boolean isCancelled() {
      return false;
    }

    public void step(ProgressStep step) {
      steps.add(step);
    }
    
    public void executeAfter(Runnable executeAfter) {
      this.executeAfter = executeAfter;
    }
  }
}

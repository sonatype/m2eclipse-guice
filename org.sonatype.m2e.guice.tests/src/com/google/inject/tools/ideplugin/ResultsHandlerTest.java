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

package com.google.inject.tools.ideplugin;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.tools.ideplugin.Fakes.FakeActionsHandler;
import com.google.inject.tools.ideplugin.Fakes.FakeCodeLocationsResults;
import com.google.inject.tools.ideplugin.results.CodeLocationsResults;
import com.google.inject.tools.ideplugin.results.ResultsHandler;
import com.google.inject.tools.ideplugin.results.ResultsView;

import junit.framework.TestCase;

import org.easymock.EasyMock;

/**
 * Unit test the ResultsHander implementation.
 * 
 * @author Darren Creutz <dcreut@gmail.com>
 */
public class ResultsHandlerTest extends TestCase {
  /**
   * Test that Locations esults get correctly passed to the ResultsView.
   */
  public void testDisplayLocationsResults() {
    CodeLocationsResults results = new FakeCodeLocationsResults();
    ResultsView resultsView = EasyMock.createMock(ResultsView.class);
    resultsView.displayResults(results);
    EasyMock.replay(resultsView);
    Injector injector =
        Guice.createInjector(new MockingGuicePluginModule()
            .useRealResultsHandler().useResultsView(resultsView)
            .useActionsHandler(new FakeActionsHandler()));
    ResultsHandler resultsHandler = injector.getInstance(ResultsHandler.class);
    resultsHandler.displayLocationsResults(results);
    EasyMock.verify(resultsView);
  }
}

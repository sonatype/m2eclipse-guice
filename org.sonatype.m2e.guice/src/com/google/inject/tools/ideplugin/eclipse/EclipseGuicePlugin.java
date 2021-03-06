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

package com.google.inject.tools.ideplugin.eclipse;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.google.inject.tools.ideplugin.GuicePlugin;
import com.google.inject.tools.ideplugin.JavaElement;
import com.google.inject.tools.ideplugin.ModuleSelectionView;
import com.google.inject.tools.ideplugin.ProjectManager;
import com.google.inject.tools.ideplugin.bindings.BindingsEngine;
import com.google.inject.tools.ideplugin.results.ResultsView;
import com.google.inject.tools.ideplugin.results.Results;
import com.google.inject.tools.suite.GuiceToolsModule;
import com.google.inject.tools.ideplugin.JavaProject;
import com.google.inject.tools.suite.Messenger;
import com.google.inject.tools.suite.module.ModuleManager;

/*
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class EclipseGuicePlugin extends GuicePlugin {
  private static class GetToUIThread implements Runnable {
    private final Results results;
    private final Messenger messenger;

    public GetToUIThread(Results results, Messenger messenger) {
      this.results = results;
      this.messenger = messenger;
    }

    public void run() {
      try {
        IWorkbenchPage activePage = PlatformUI.getWorkbench()
            .getWorkbenchWindows()[0].getActivePage();
        IViewPart viewPart = activePage.showView(PluginDefinitionValues.RESULTS_VIEW_ID);
        ((EclipseResultsView) viewPart).displayResults(results);
      } catch (Throwable e) {
        messenger.logException("Error loading ResultsView", e);
      }
    }
  }

  @Singleton
  public static class ResultsViewImpl implements ResultsView {
    private final Messenger messenger;

    @Inject
    public ResultsViewImpl(Messenger messenger) {
      this.messenger = messenger;
    }

    public void displayResults(Results results) {
      Display.getDefault().asyncExec(new GetToUIThread(results, messenger));
    }
  }

  @Singleton
  public static class ModuleSelectionViewImpl implements ModuleSelectionView {
    private class GetToUIThread implements Runnable {
      private final JavaProject project;

      public GetToUIThread(JavaProject project) {
        this.project = project;
      }

      public void run() {
        try {
          EclipseModuleDialog.display(new Shell(), messenger, projectManager,
              projectManager.getModuleManager(project));
        } catch (Throwable t) {
          messenger.logException("Error opening ModuleSelectionView", t);
        }
      }
    }

    private final Messenger messenger;
    private final ProjectManager projectManager;

    @Inject
    public ModuleSelectionViewImpl(Messenger messenger,
        ProjectManager projectManager) {
      this.messenger = messenger;
      this.projectManager = projectManager;
    }

    public void show(JavaProject project) {
      Display.getDefault().syncExec(new GetToUIThread(project));
    }
  }

  /**
   * Create an EclipseGuicePlugin.
   * 
   * @param module the guice module to inject from
   */
  public EclipseGuicePlugin(EclipsePluginModule module,
      GuiceToolsModule toolsModule) {
    super(module, toolsModule);
  }

  @Override
  public BindingsEngine getBindingsEngine(JavaElement element, JavaProject javaProject) {
    if (javaProject instanceof EclipseJavaProject) {
      ModuleManager moduleManager = getModuleManager(javaProject);
      if (moduleManager.getModuleContext("sisu") == null) {
        moduleManager.addCustomContext("sisu", SisuIndex.class.getName(), "bindings");
      }
      moduleManager.rerunModules();
    }
    return super.getBindingsEngine(element, javaProject);
  }
}

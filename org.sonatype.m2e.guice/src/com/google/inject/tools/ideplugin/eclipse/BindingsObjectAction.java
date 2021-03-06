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

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.viewers.IStructuredSelection;

import com.google.inject.tools.ideplugin.GuicePlugin;

/**
 * The action to take when the user selects an object in the tree view (Outline)
 * and selects "Find Bindings" from the right click menu.
 * 
 * @author Darren Creutz (dcreutz@gmail.com)
 */
public class BindingsObjectAction implements IObjectActionDelegate {
  private IWorkbenchPart part;
  private GuicePlugin guicePlugin;

  public BindingsObjectAction() {
    super();
    guicePlugin = Activator.getDefault().getGuicePlugin();
  }

  public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    this.part = targetPart;
  }

  /**
   * Eclipse callback to have us run the bindings engine.
   */
  public void run(IAction action) {
    IStructuredSelection selection = (IStructuredSelection) part.getSite()
        .getSelectionProvider().getSelection();
    EclipseJavaElement javaElement = selection.getFirstElement()==null ? null :
        new EclipseJavaElement((IJavaElement) selection.getFirstElement());
    if (javaElement != null && javaElement.getType() != null) {
      guicePlugin.getBindingsEngine(javaElement,
          new EclipseJavaProject(javaElement.getIJavaElement().getJavaProject()));
    } else {
      guicePlugin.getMessenger().display(PluginTextValues.SELECTION_NO_BINDINGS);
    }
  }

  /**
   * Eclipse callback that the selected text changed.
   */
  public void selectionChanged(IAction action, ISelection selection) {
  }

}

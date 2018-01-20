package net.sf.eclipsecs.ui.actions;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkingSet;

abstract class AbstractCheckstyleAction implements IObjectActionDelegate {

  private ArrayList<Object> selected;

  private IWorkbenchPart mPart;

  /**
   * {@inheritDoc}
   */
  @Override
  final public void setActivePart(IAction action, IWorkbenchPart targetPart) {
    mPart = targetPart;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  public final void selectionChanged(IAction action, ISelection selection) {
    if (selection instanceof IStructuredSelection) {
      IStructuredSelection structuredSelection = (IStructuredSelection) selection;
      selected = new ArrayList<Object>(structuredSelection.toList());
    }
  }

  /**
   * Get the selected projects, as well as projects of selected working sets.
   * 
   * @return projects
   */
  protected Collection<IProject> getSelectedProjects() {
    ArrayList<IProject> projects = new ArrayList<>();
    for (Object object : selected) {
      if (object instanceof IWorkingSet) {
        for (IAdaptable adaptable : ((IWorkingSet) object).getElements()) {
          IProject element = adaptable.getAdapter(IProject.class);
          if (element != null) {
            projects.add(element);
          }
        }
      } else if (object instanceof IProject) {
        projects.add((IProject) object);
      }
    }
    return projects;
  }

  /**
   * Get the selected resources, and the direct resource children of selected working sets.
   * 
   * @return resources
   */
  protected Collection<IResource> getSelectedResources() {
    ArrayList<IResource> resources = new ArrayList<>();
    for (Object object : selected) {
      if (object instanceof IWorkingSet) {
        for (IAdaptable adaptable : ((IWorkingSet) object).getElements()) {
          IResource element = adaptable.getAdapter(IResource.class);
          if (element != null) {
            resources.add(element);
          }
        }
      } else if (object instanceof IResource) {
        resources.add((IResource) object);
      }
    }
    return resources;
  }

  protected IWorkbenchPart getActivePart() {
    return mPart;
  }

}

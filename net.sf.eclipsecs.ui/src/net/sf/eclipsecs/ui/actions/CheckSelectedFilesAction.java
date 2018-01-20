//============================================================================
//
// Copyright (C) 2002-2016  David Schneider, Lars Ködderitzsch
//
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//============================================================================

package net.sf.eclipsecs.ui.actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;

import net.sf.eclipsecs.core.jobs.RunCheckstyleOnFilesJob;
import net.sf.eclipsecs.ui.CheckstyleUIPlugin;

/**
 * Action to run Checkstyle on one ore more projects.
 * 
 * @author Lars Ködderitzsch
 */
public class CheckSelectedFilesAction extends AbstractCheckstyleAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public void run(IAction action) {

    List<IFile> filesToCheck = new ArrayList<IFile>();

    try {
      addFileResources(getSelectedResources(), filesToCheck);

      RunCheckstyleOnFilesJob job = new RunCheckstyleOnFilesJob(filesToCheck);
      job.setRule(job);
      job.schedule();
    } catch (CoreException e) {
      CheckstyleUIPlugin.errorDialog(getActivePart().getSite().getShell(), e, true);
    }
  }

  /**
   * Recursively add all files contained in the given resource collection to the
   * second list.
   * 
   * @param resources
   *          list of resource
   * @param files
   *          the list of files
   * @throws CoreException
   *           an unexpected exception
   */
  private void addFileResources(Collection<IResource> resources, List<IFile> files) throws CoreException {

    for (IResource resource : resources) {

      if (!resource.isAccessible()) {
        continue;
      }

      if (resource instanceof IFile) {
        files.add((IFile) resource);
      } else if (resource instanceof IContainer) {
        addFileResources(Arrays.asList(((IContainer) resource).members()), files);
      } 
    }
  }
}

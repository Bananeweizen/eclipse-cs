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

import java.util.Collection;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;

import net.sf.eclipsecs.core.builder.CheckstyleMarker;
import net.sf.eclipsecs.ui.Messages;

/**
 * Action to clear Checkstyle markers on one ore more projects.
 * 
 * @author Lars Ködderitzsch
 */
public class ClearSelectedFilesAction extends AbstractCheckstyleAction {

  /**
   * {@inheritDoc}
   */
  @Override
  public void run(IAction action) {
    ClearMarkersJob job = new ClearMarkersJob(getSelectedResources());
    job.schedule();
  }

  /**
   * Clears checkstyle markers on a collection of projects.
   * 
   * @author Lars Ködderitzsch
   */
  private class ClearMarkersJob extends WorkspaceJob {

    private Collection<IResource> mResourcesToClear;

    public ClearMarkersJob(Collection<IResource> resourcesToClear) {
      super(Messages.ClearSelectedFilesAction_title);
      this.mResourcesToClear = resourcesToClear;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {

      for (IResource resource : mResourcesToClear) {
        if (resource.isAccessible()) {
          resource.deleteMarkers(CheckstyleMarker.MARKER_ID, true, IResource.DEPTH_INFINITE);
        }
      }

      return Status.OK_STATUS;
    }
  }
}

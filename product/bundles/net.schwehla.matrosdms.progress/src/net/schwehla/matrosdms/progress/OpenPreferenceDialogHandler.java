/*******************************************************************************
 * Copyright (c) 2010, 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 ******************************************************************************/

package net.schwehla.matrosdms.progress;


import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.swt.widgets.Shell;

import net.schwehla.matrosdms.progress.internal.JobsViewPreferenceDialog;
import net.schwehla.matrosdms.progress.internal.PreferenceStore;


/**
 * Opens the progress view preference dialog.
 *
 * @noreference
 */
public class OpenPreferenceDialogHandler {

	@Execute
	public void openPreferenceDialog(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, PreferenceStore preferences) {
		new JobsViewPreferenceDialog(shell, preferences).open();
	}

}

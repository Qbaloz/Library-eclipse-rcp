package com.library.app.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.library.app.model.BookTo;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public class BookDetailsViewPart extends ViewPart {
	
	public BookDetailsViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Label lblTitle = new Label(parent, SWT.NONE);
		lblTitle.setText("Authors:");
		
		Label lblAuthors = new Label(parent, SWT.NONE);
		GridData gd_lblAuthors = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
		gd_lblAuthors.widthHint = 400;
		lblAuthors.setLayoutData(gd_lblAuthors);
		lblAuthors.setText("Authors: ");

		getSite().getPage().addSelectionListener(new ISelectionListener() {
			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (selection != null & selection instanceof IStructuredSelection) {
					IStructuredSelection strucSelection = (IStructuredSelection) selection;
					Object book = strucSelection.getFirstElement();
					if (book instanceof BookTo) {
						lblAuthors.setText(((BookTo) book).getAuthors().toString());
					}
				}
			}
		});
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

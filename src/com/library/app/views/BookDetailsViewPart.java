package com.library.app.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;

public class BookDetailsViewPart extends ViewPart {
	
	public BookDetailsViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(1, false));
		
		Label lblAuthors = new Label(parent, SWT.NONE);
		lblAuthors.setText("Authors:");

	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

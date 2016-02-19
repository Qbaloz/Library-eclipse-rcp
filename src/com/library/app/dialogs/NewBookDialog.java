package com.library.app.dialogs;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.google.gson.Gson;
import com.library.app.model.AuthorTo;
import com.library.app.model.BookTo;
import com.library.app.provider.BookProvider;
import org.eclipse.jface.fieldassist.ControlDecoration;

public class NewBookDialog extends TitleAreaDialog {

	private Text titleText;
	private Text firstNameText;
	private Text lastNameText;

	private String title;
	private String firstName;
	private String lastName;

	private Gson jsonParser = new Gson();
	private Set<AuthorTo> authors = new HashSet<>();
	private String jsonString;
	private BookTo book;

	public NewBookDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		setTitle("New book dialog");
		setMessage("Enter book title, author first name and last name", IMessageProvider.INFORMATION);
		Button okButton = getButton(IDialogConstants.OK_ID);
		okButton.setEnabled(false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createTitle(container);
		createFirstName(container);
		createLastName(container);

		return area;
	}

	private void createTitle(Composite container) {
		Label lbtTitle = new Label(container, SWT.NONE);
		lbtTitle.setText("Title:");

		GridData dataTitle = new GridData();
		dataTitle.grabExcessHorizontalSpace = true;
		dataTitle.horizontalAlignment = GridData.FILL;

		titleText = new Text(container, SWT.BORDER);
		titleText.setLayoutData(dataTitle);

		ControlDecoration controlDecoration = new ControlDecoration(titleText, SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText("Title can not be empty");
		controlDecoration
				.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
		controlDecoration.setShowOnlyOnFocus(true);
		controlDecoration.show();

		titleText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				validate();
				if (titleText.getText().length() > 0) {
					controlDecoration.hide();
				} else {
					controlDecoration.show();
				}
			}
		});
	}

	private void createFirstName(Composite container) {
		Label lbtFirstName = new Label(container, SWT.NONE);
		lbtFirstName.setText("Author first name:");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		firstNameText = new Text(container, SWT.BORDER);
		firstNameText.setLayoutData(dataFirstName);

		ControlDecoration controlDecoration = new ControlDecoration(firstNameText, SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText("First name can not be empty");
		controlDecoration
				.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
		controlDecoration.setShowOnlyOnFocus(true);
		controlDecoration.show();

		firstNameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				validate();
				if (firstNameText.getText().length() > 0) {
					controlDecoration.hide();
				} else {
					controlDecoration.show();
				}
			}
		});
	}

	private void createLastName(Composite container) {
		Label lbtLastName = new Label(container, SWT.NONE);
		lbtLastName.setText("Author last name:");

		GridData dataLastName = new GridData();
		dataLastName.grabExcessHorizontalSpace = true;
		dataLastName.horizontalAlignment = GridData.FILL;
		lastNameText = new Text(container, SWT.BORDER);
		lastNameText.setLayoutData(dataLastName);

		ControlDecoration controlDecoration = new ControlDecoration(lastNameText, SWT.LEFT | SWT.TOP);
		controlDecoration.setDescriptionText("Last name can not be empty");
		controlDecoration
				.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_DEC_FIELD_ERROR));
		controlDecoration.setShowOnlyOnFocus(true);
		controlDecoration.show();

		lastNameText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				validate();
				if (lastNameText.getText().length() > 0) {
					controlDecoration.hide();
				} else {
					controlDecoration.show();
				}
			}
		});
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	private void saveInput() {
		title = titleText.getText();
		firstName = firstNameText.getText();
		lastName = lastNameText.getText();
	}

	private String createBookJson() {
		authors.add(new AuthorTo(null, firstName, lastName));
		BookTo book = new BookTo(null, title, authors);

		jsonString = jsonParser.toJson(book);
		return jsonString;
	}

	@Override
	protected void okPressed() {
		saveInput();
		createBookJson();
		book = BookProvider.INSTANCE.addBook(jsonString);

		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Book successfully added", "Book " + book.getTitle() + " was successfully added.");
		super.okPressed();
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getTitle() {
		return title;
	}

	private void validate() {
		Button okButton = getButton(IDialogConstants.OK_ID);
		if (titleText.getText().length() == 0 || firstNameText.getText().length() == 0
				|| lastNameText.getText().length() == 0) {
			okButton.setEnabled(false);
		} else {
			setErrorMessage(null);
			okButton.setEnabled(true);
		}
	}

}

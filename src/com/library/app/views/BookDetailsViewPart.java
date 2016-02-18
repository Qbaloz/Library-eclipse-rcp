package com.library.app.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.google.gson.Gson;
import com.library.app.model.AuthorTo;
import com.library.app.model.BookTo;
import com.library.app.rest.BookRestService;

import org.eclipse.swt.widgets.Label;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.core.databinding.beans.PojoProperties;

public class BookDetailsViewPart extends ViewPart {
	
	private DataBindingContext m_bindingContext;
	private Text editBook;
	private BookRestService bookRestService = new BookRestService();
	private Gson jsonParser = new Gson();
	private Set<AuthorTo> authors = new HashSet<>();
	private String jsonString;
	private BookTo book;
	private Long id;
	private Label lblTitle;
	
	public BookDetailsViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(null);
		
		Label lblWindowTitle = new Label(parent, SWT.NONE);
		lblWindowTitle.setBounds(10, 10, 574, 20);
		lblWindowTitle.setText("Authors:");
		
		Label lblAuthors = new Label(parent, SWT.NONE);
		lblAuthors.setBounds(10, 36, 574, 20);
		lblAuthors.setText("");
		
		Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 114, 574, 2);
		
		Label lblEditBook = new Label(parent, SWT.NONE);
		lblEditBook.setBounds(10, 122, 83, 20);
		lblEditBook.setText("Change title:");
		
		editBook = new Text(parent, SWT.BORDER);
		editBook.setBounds(10, 148, 150, 26);
		
		Button btnSave = new Button(parent, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnSave.setBounds(10, 180, 90, 30);
		btnSave.setText("Save");
		
		lblTitle = new Label(parent, SWT.NONE);
		lblTitle.setBounds(10, 88, 574, 20);
		
		Label lblTitle_1 = new Label(parent, SWT.NONE);
		lblTitle_1.setBounds(10, 62, 70, 20);
		lblTitle_1.setText("Title:");
		
		
		
		btnSave.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				createBookJson();
				try {
					book = bookRestService.sendPOST(jsonString);
				} catch (ClientProtocolException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		getSite().getPage().addSelectionListener(new ISelectionListener() {
			@Override
			public void selectionChanged(IWorkbenchPart part, ISelection selection) {
				if (selection != null & selection instanceof IStructuredSelection) {
					IStructuredSelection strucSelection = (IStructuredSelection) selection;
					Object book = strucSelection.getFirstElement();
					if (book instanceof BookTo) {
						lblAuthors.setText(((BookTo) book).getAuthors().toString());
						editBook.setText(((BookTo) book).getTitle());
						authors = ((BookTo) book).getAuthors();
						id = ((BookTo) book).getId();
						lblTitle.setText(((BookTo) book).getTitle());
						
					}
				}
			}
		});
		m_bindingContext = initDataBindings();
		
	}
	
	private String createBookJson() {
		BookTo book = new BookTo(id, editBook.getText(), authors);
		jsonString = jsonParser.toJson(book);
		return jsonString;
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextLblTitleObserveWidget = WidgetProperties.text().observe(lblTitle);
		IObservableValue observeTextEditBookObserveWidget = WidgetProperties.text(SWT.Modify).observe(editBook);
		bindingContext.bindValue(observeTextLblTitleObserveWidget, observeTextEditBookObserveWidget, null, null);
		//
		return bindingContext;
	}
}

package com.library.app.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import com.google.gson.Gson;
import com.library.app.model.AuthorTo;
import com.library.app.model.BookTo;
import com.library.app.provider.BookProvider;
import org.eclipse.swt.widgets.Label;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;

public class BookDetailsViewPart extends ViewPart {
	
	private Button btnSave;
	private Text editBook;
	private Gson jsonParser = new Gson();
	private Set<AuthorTo> authors = new HashSet<>();
	private String jsonString;
	private Long id;
	private Label lblTitle;
	private Label lblWindowTitle;
	private Label lblAuthors;
	private Label label;
	private Label lblEditBook;
	
	public BookDetailsViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {		
		
		initialize(parent);
		
		btnSaveListener();
		SelectionListener();
		initDataBindings();
		
	}
	
	private void SelectionListener(){
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
	}
	
	private void btnSaveListener(){
		btnSave.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				createBookJson();
				BookProvider.INSTANCE.addBook(jsonString);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}
	
	private void initialize(Composite parent){
		parent.setLayout(null);
		
		lblWindowTitle = new Label(parent, SWT.NONE);
		lblWindowTitle.setBounds(10, 10, 574, 20);
		lblWindowTitle.setText("Authors:");
		
		lblAuthors = new Label(parent, SWT.NONE);
		lblAuthors.setBounds(10, 36, 574, 20);
		lblAuthors.setText("");
		
		label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setBounds(10, 114, 574, 2);
		
		lblEditBook = new Label(parent, SWT.NONE);
		lblEditBook.setBounds(10, 122, 83, 20);
		lblEditBook.setText("Change title:");
		
		editBook = new Text(parent, SWT.BORDER);
		editBook.setBounds(10, 148, 150, 26);
		
		btnSave = new Button(parent, SWT.NONE);
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
	}
	
	private String createBookJson() {
		BookTo book = new BookTo(id, editBook.getText(), authors);
		jsonString = jsonParser.toJson(book);
		return jsonString;
	}

	@Override
	public void setFocus() {


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

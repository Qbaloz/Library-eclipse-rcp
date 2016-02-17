package com.library.app.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.library.app.dialogs.NewBookDialog;
import com.library.app.model.BookTo;
import com.library.app.provider.BookProvider;
import com.library.app.rest.BookRestService;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.TableColumn;

public class BooksListViewPart extends ViewPart {
	private Text titlePrefix;
	private Button btnDeleteBook;
	private Button btnSearch;
	private TableViewer viewer;
	private Table table;

	private BookRestService bookRestService = new BookRestService();

	private List<BookTo> bookToList = new ArrayList<>();

	public BooksListViewPart() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setLayout(new GridLayout(3, false));

		Label lblSearchBook = new Label(parent, SWT.NONE);
		lblSearchBook.setText("Search:");

		titlePrefix = new Text(parent, SWT.BORDER);
		titlePrefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		btnSearch = new Button(parent, SWT.NONE);
		btnSearch.setText("Search");

		Button btnAddBook = new Button(parent, SWT.NONE);
		btnAddBook.setText("Add book");

		btnDeleteBook = new Button(parent, SWT.NONE);
		btnDeleteBook.setText("Delete book");

		btnAddBook.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NewBookDialog dialog = new NewBookDialog(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				dialog.open();
				updateBooksList("");
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		btnSearch.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				updateBooksList(titlePrefix.getText());
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		
		btnDeleteBook.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = viewer.getStructuredSelection();
				BookTo book = (BookTo) selection.getFirstElement();
				if(book != null){
					try {
						bookRestService.sendDELETE(book.getId());
						updateBooksList("");
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});

		createViewer(parent);
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(BookProvider.INSTANCE.getBooks());
		getSite().setSelectionProvider(viewer);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 3;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);
	}

	public TableViewer getViewer() {
		return viewer;
	}

	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "Id", "Title" };
		int[] bounds = { 50, 185 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BookTo book = (BookTo) element;
				String id = String.valueOf(book.getId());
				return id;
			}
		});

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				BookTo book = (BookTo) element;
				return book.getTitle();
			}
		});

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}
	
	private void updateBooksList(String titlePrefix) {
		try {
			bookToList = bookRestService.sendGET(titlePrefix);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		viewer.setInput(bookToList);
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

}

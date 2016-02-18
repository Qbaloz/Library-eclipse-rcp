package com.library.app.views;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import com.library.app.dialogs.NewBookDialog;
import com.library.app.model.BookTo;
import com.library.app.provider.BookProvider;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;

import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.jface.databinding.viewers.ViewerSupport;
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
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.events.SelectionAdapter;

public class BooksListViewPart extends ViewPart {

	private Text titlePrefix;
	private Button btnDeleteBook;
	private Button btnSearch;
	private Button btnAddBook;
	private TableViewer viewer;
	private Table table;

	public BooksListViewPart() {

	}

	@Override
	public void createPartControl(Composite parent) {

		initialize(parent);

		btnAddListener();
		btnSearchListener();
		btnDeleteListener();

		createViewer(parent);

	}

	private void initialize(Composite parent) {

		parent.setLayout(new GridLayout(3, false));

		Label lblSearchBook = new Label(parent, SWT.NONE);
		lblSearchBook.setText("Search:");

		titlePrefix = new Text(parent, SWT.BORDER);
		titlePrefix.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

		btnSearch = new Button(parent, SWT.NONE);
		btnSearch.setText("Search");

		btnAddBook = new Button(parent, SWT.NONE);
		btnAddBook.setText("Add book");

		btnDeleteBook = new Button(parent, SWT.NONE);
		btnDeleteBook.setText("Delete book");
	}

	private void btnAddListener() {

		btnAddBook.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NewBookDialog dialog = new NewBookDialog(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				dialog.open();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	private void btnSearchListener() {

		btnSearch.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				BookProvider.INSTANCE.getBooks(titlePrefix.getText());

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	private void btnDeleteListener() {

		btnDeleteBook.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = viewer.getStructuredSelection();
				BookTo book = (BookTo) selection.getFirstElement();
				if (book != null) {
					BookProvider.INSTANCE.deleteBook(book.getId());
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);
		table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 3, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(new ArrayContentProvider());

		try {
			ViewerSupport.bind(viewer, BookProvider.INSTANCE.getBooks(titlePrefix.getText()),
					PojoProperties.values(new String[] { "id", "title", "authors" }));
		} catch (Exception e) {
			e.printStackTrace();
		}

		getSite().setSelectionProvider(viewer);

		createMenu();

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

	private void createMenu() {
		Menu menu = new Menu(table);
		table.setMenu(menu);

		MenuItem mntmAddBook = new MenuItem(menu, SWT.NONE);
		mntmAddBook.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				NewBookDialog dialog = new NewBookDialog(
						PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell());
				dialog.open();
			}
		});
		mntmAddBook.setText("Add book");

		MenuItem mntmDelete = new MenuItem(menu, SWT.NONE);
		mntmDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = viewer.getStructuredSelection();
				BookTo book = (BookTo) selection.getFirstElement();
				if (book != null) {
					BookProvider.INSTANCE.deleteBook(book.getId());
				}
			}
		});
		mntmDelete.setText("Delete");
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

	@Override
	public void setFocus() {

	}
}

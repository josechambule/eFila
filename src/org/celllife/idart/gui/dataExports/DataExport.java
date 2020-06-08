/*
 * iDART: The Intelligent Dispensing of Antiretroviral Treatment
 * Copyright (C) 2006 Cell-Life
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License version
 * 2 for more details.
 *
 * You should have received a copy of the GNU General Public License version 2
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 */

package org.celllife.idart.gui.dataExports;

import model.manager.FileManager;
import model.manager.exports.BaseReportObject;
import model.manager.exports.ExportColumn;
import model.manager.exports.ReportObject;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.iDartProperties;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.gui.misc.iDARTChangeListener;
import org.celllife.idart.gui.platform.GenericOthersGui;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.celllife.idart.misc.SafeSaveDialog;
import org.celllife.idart.misc.SafeSaveDialog.FileType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 */
public class DataExport extends GenericOthersGui implements iDARTChangeListener {

	private Composite compDetails;

	private static Logger log = Logger.getLogger(DataExport.class);

	private final Map<ReportObject, String> exportFilenameMapping;

	private Group grpDescription;

	private Button btnCancel;

	private Button btnRunDataExport;

	private Button btnNewDataExport;

	private Button btnUpdateDataExport;

	private Button btnDeleteDataExport;

	private Label lblExistingExports;

	private ListViewer lstExistingExports;

	private Label lblExportDescription;

	private TableViewer lstExportDescription;

	private Label lblExportName;

	private Text txtExportName;

	private Label lblDescription;

	/**
	 * @param parent
	 */
	public DataExport(Shell parent) {
		super(parent, HibernateUtil.getNewSession());
		exportFilenameMapping = new HashMap<ReportObject, String>();
		activate();

	}

	@Override
	protected void createShell() {
		String shellTxt = "Relatório";
		Rectangle bounds = new Rectangle(25, 0, 900, 700);
		buildShell(shellTxt, bounds);
		createCompDetails();
		populateDataExports();
	}

	@Override
	protected void createCompButtons() {
		btnRunDataExport = new Button(getCompButtons(), SWT.NONE);
		btnRunDataExport.setText("Executar relatorio");
		btnRunDataExport.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnRunDataExport
		.setToolTipText("Clica no botão para gerar relatório.");
		btnRunDataExport
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdRunDataExportSelected();
			}
		});

		btnCancel = new Button(getCompButtons(), SWT.NONE);
		btnCancel.setText("Cancelar");
		btnCancel.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnCancel.setToolTipText("Clica no botão para limpar o ecrã.");
		btnCancel
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdCancelWidgetSelected();
			}
		});

	}

	@SuppressWarnings("unchecked")
	private boolean isReportListEmpty() {
		return ((List<BaseReportObject>) lstExistingExports.getInput()).size() == 0;
	}

	public void createCompDetails() {
		compDetails = new Composite(getShell(), SWT.NONE);
		compDetails.setBounds(new Rectangle(25, 100, 850, 500));

		lblExistingExports = new Label(compDetails, SWT.CENTER | SWT.BORDER);
		lblExistingExports.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblExistingExports.setText("Relatórios Exixtentes");
		lblExistingExports.setBounds(new org.eclipse.swt.graphics.Rectangle(
				100, 30, 250, 20));
		lstExistingExports = new ListViewer(compDetails, SWT.BORDER
				| SWT.V_SCROLL);
		lstExistingExports.getList().setFont(
				ResourceUtils.getFont(iDartFont.VERASANS_8));
		lstExistingExports.getList().setBounds(
				new org.eclipse.swt.graphics.Rectangle(100, 50, 250, 350));
		lstExistingExports
		.setContentProvider(new DataExportObjectContentProvider());
		lstExistingExports
		.setLabelProvider(new DataExportObjectLabelProvider());
		lstExistingExports.setSorter(new ViewerSorter() {
			@Override
			public int compare(Viewer viewer, Object e1, Object e2) {
				return ((BaseReportObject) e1).getName().compareTo(
						((BaseReportObject) e2).getName());
			}
		});
		lstExistingExports
		.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				lstExistingExportsWidgetSelected();
			}
		});

		lblExportDescription = new Label(compDetails, SWT.CENTER | SWT.BORDER);
		lblExportDescription.setFont(ResourceUtils
				.getFont(iDartFont.VERASANS_8));
		lblExportDescription.setText("Descrição do relatório");
		lblExportDescription.setBounds(new org.eclipse.swt.graphics.Rectangle(
				500, 30, 250, 20));
		grpDescription = new Group(compDetails, SWT.NONE);
		grpDescription.setBounds(new Rectangle(500, 50, 250, 350));
		lblExportName = new Label(grpDescription, SWT.LEFT);
		lblExportName.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblExportName.setText("Nome: ");
		lblExportName.setBounds(new org.eclipse.swt.graphics.Rectangle(10, 25,
				50, 20));

		txtExportName = new Text(grpDescription, SWT.BORDER);
		txtExportName.setBounds(new Rectangle(65, 20, 170, 20));
		txtExportName.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		txtExportName.setEditable(false);

		lblDescription = new Label(grpDescription, SWT.LEFT);
		lblDescription.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblDescription.setText("Colunas: ");
		lblDescription.setBounds(new org.eclipse.swt.graphics.Rectangle(10, 60,
				55, 20));
		lstExportDescription = new TableViewer(grpDescription, SWT.BORDER
				| SWT.V_SCROLL);
		lstExportDescription.getTable().setBounds(
				new org.eclipse.swt.graphics.Rectangle(0, 80, 250, 270));
		lstExportDescription.getTable().setFont(
				ResourceUtils.getFont(iDartFont.VERASANS_8));
		lstExportDescription
		.setContentProvider(new ExportColumnContentProvider());
		lstExportDescription.setLabelProvider(new ExportColumnLabelProvider());

		btnNewDataExport = new Button(compDetails, SWT.NONE);
		btnNewDataExport.setText("Novo Relatório");
		btnNewDataExport.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnNewDataExport.setBounds(new Rectangle(175, 440, 150, 30));
		btnNewDataExport
		.setToolTipText("Clica no botão para criar um novo relatório.");
		btnNewDataExport
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdNewDataExportWidgetSelected();
			}
		});

		btnUpdateDataExport = new Button(compDetails, SWT.NONE);
		btnUpdateDataExport.setText("Actualizar relatório");
		btnUpdateDataExport
		.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnUpdateDataExport.setBounds(new Rectangle(375, 440, 150, 30));
		btnUpdateDataExport
		.setToolTipText("Clica no botão para actualizar um relatorio existente.");
		btnUpdateDataExport
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdUpdateWidgetSelected();
			}
		});

		btnDeleteDataExport = new Button(compDetails, SWT.NONE);
		btnDeleteDataExport.setText("Delete Export");
		btnDeleteDataExport
		.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnDeleteDataExport.setBounds(new Rectangle(575, 440, 150, 30));
		btnDeleteDataExport
		.setToolTipText("Clica no botão para remover um relatório.");
		btnDeleteDataExport
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdDeleteWidgetSelected();
			}
		});
		btnUpdateDataExport.setEnabled(false);
		btnRunDataExport.setEnabled(false);
		btnDeleteDataExport.setEnabled(false);

	}

	protected void cmdUpdateWidgetSelected() {
		ReportObject deo = getSelectedExport();

		if (isReportListEmpty()) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			box.setText("Template não seleccionado");
			box
			.setMessage("Não exitem relatorios criados.");
			box.open();
			return;
		}

		if (deo == null) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			box.setText("Nenhum relatório foi seleccionado");
			box.setMessage("Por favor seleccione o relatorio.");
			box.open();
			return;
		}

		AddTemplate addTemplate = new AddTemplate(getShell());
		addTemplate.addChangeListener(this);
		addTemplate.open();
		addTemplate.load(deo, exportFilenameMapping.get(deo));

	}

	protected void cmdCancelWidgetSelected() {
		closeShell(true);
	}

	protected void populateDataExports() {
		String[] exportObjects = FileManager.getDataExportObjects("./"
				+ iDartProperties.exportDir);
		exportObjects = FileManager
		.getDataExportObjects(iDartProperties.exportDir);
		if (exportObjects == null)
			return;

		java.util.List<ReportObject> deos = new Vector<ReportObject>();
		for (int i = 0; i < exportObjects.length; i++) {
			try {
				ReportObject dero = FileManager
				.readXMLfile(exportObjects[i]);
				deos.add(dero);
				exportFilenameMapping.put(dero, exportObjects[i]);
			} catch (IOException e) {
				log.error("There was an error reading " + exportObjects[i], e);
			}
		}

		if (deos.size() == 0) {
			btnUpdateDataExport.setEnabled(false);
			btnRunDataExport.setEnabled(false);
			btnDeleteDataExport.setEnabled(false);
		}

		lstExistingExports.setInput(deos);
	}

	protected void lstExistingExportsWidgetSelected() {
		BaseReportObject deo = getSelectedExport();
		if (deo != null) {
			lstExportDescription.setInput(deo.getColumns());
			txtExportName.setText(deo.getName());
			btnUpdateDataExport.setEnabled(true);
			btnRunDataExport.setEnabled(true);
			btnDeleteDataExport.setEnabled(true);
		}
	}

	private BaseReportObject getSelectedExport() {
		IStructuredSelection selection = (IStructuredSelection) lstExistingExports
		.getSelection();
		if (selection != null) {
			BaseReportObject deo = (BaseReportObject) selection
			.getFirstElement();
			return deo;
		} else
			return null;
	}

	protected void cmdDeleteWidgetSelected() {
		BaseReportObject deoToDelete = getSelectedExport();

		if (isReportListEmpty()) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
			box.setText("Template não seleccionado");
			box
			.setMessage("Não existe nenhum relatório disponível. Por favor, crie um relatório.");
			box.open();
			return;
		}

		if (deoToDelete == null) {

			MessageBox mbox = new MessageBox(getShell(), SWT.ICON_INFORMATION
					| SWT.OK);
			mbox.setText("Remover Relatório");
			mbox
			.setMessage("Please select an export for deletion, from the list of existing exports.");

			mbox.open();
			return;
		}

		MessageBox mbox = new MessageBox(getShell(), SWT.ICON_QUESTION
				| SWT.YES | SWT.NO);
		mbox.setText("Remover Relatório");
		mbox.setMessage("Tem certeza que pretende remover o relatório? '"
				+ txtExportName.getText() + "'?");

		if (SWT.YES == mbox.open()) {

			if (FileManager.deleteFile(exportFilenameMapping.get(deoToDelete),
					iDartProperties.exportDir + File.separator)) {
				MessageBox box = new MessageBox(getShell(),
						SWT.ICON_INFORMATION | SWT.OK);
				box.setText("Relatório removido com sucesso");
				box.setMessage("O Relatório '" + txtExportName.getText()
						+ "'  removido com sucesso.");
				box.open();

				clearFields();
				populateDataExports();
			}
		}
	}

	public void clearFields() {
		txtExportName.setText("");
		lstExportDescription.getTable().clearAll();
	}

	protected void cmdNewDataExportWidgetSelected() {
		AddTemplate addTemplate = new AddTemplate(getShell());
		addTemplate.addChangeListener(this);
		addTemplate.open();
	}

	protected void cmdRunDataExportSelected() {
		BaseReportObject export = getSelectedExport();

		
		if (export == null) {
			MessageBox box = new MessageBox(getShell(), SWT.ICON_WARNING
					| SWT.OK);
			box.setText("Nenhum relatório seleccionado");
			box.setMessage("Seleccione um relatório na lista.");
			box.open();
			return;
		}

		SafeSaveDialog dlg = new SafeSaveDialog(getShell(), FileType.CSV);
		String fileName = dlg.open();
		if (fileName != null) {
			try {
				new ProgressMonitorDialog(getShell()).run(true, true,
						new DataExportJob(fileName, export));

				MessageBox success = new MessageBox(getShell(),
						SWT.ICON_INFORMATION | SWT.OK);
				success.setText("Relatório executado comm sucesso");
				success
				.setMessage("Relatório gerado comm sucesso.");
				success.open();

			} catch (InvocationTargetException e) {
				MessageDialog.openError(getShell(), "Error", e.getMessage());
			} catch (InterruptedException e) {
				MessageDialog.openInformation(getShell(), "Cancelado", e
						.getMessage());
			}
		}
	}

	@Override
	protected void createCompHeader() {
		String txt = "Relatório";
		iDartImage icoImage = iDartImage.REDOPACKAGE;
		buildCompHeader(txt, icoImage);
	}

	@Override
	protected void createCompOptions() {
		// Not Implemented
	}

	@Override
	protected void setLogger() {
		setLog(Logger.getLogger(this.getClass()));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.celllife.idart.gui.misc.iDARTChangeListner#changed(java.lang.Object)
	 */
	@Override
	public void changed(Object o) {
		if (o != null && o instanceof ReportObject) {
			populateDataExports();
			lstExistingExports.setSelection(new StructuredSelection(o));
			lstExistingExportsWidgetSelected();
		}
	}

	class DataExportObjectContentProvider implements IStructuredContentProvider {

		@Override
		@SuppressWarnings("unchecked")
		public Object[] getElements(Object arg0) {
			return ((java.util.List<ReportObject>) arg0).toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		}
	}

	class DataExportObjectLabelProvider implements ILabelProvider {

		@Override
		public Image getImage(Object arg0) {
			return null;
		}

		@Override
		public String getText(Object arg0) {
			return ((ReportObject) arg0).getName();
		}

		@Override
		public void addListener(ILabelProviderListener arg0) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
		}
	}

	/**
	 * Split complex columns into multiple columns for display purposes.
	 *
	 * @see
	 */
	class ExportColumnContentProvider implements IStructuredContentProvider {

		@Override
		@SuppressWarnings("unchecked")
		public Object[] getElements(Object arg0) {
			List<ExportColumn> ecs = (java.util.List<ExportColumn>) arg0;
			return ecs.toArray();
		}

		@Override
		public void dispose() {
		}

		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {
		}
	}

	class ExportColumnLabelProvider implements ILabelProvider {

		@Override
		public Image getImage(Object arg0) {
			return null;
		}

		@Override
		public String getText(Object arg0) {
			return ((ExportColumn) arg0).toString();
		}

		@Override
		public void addListener(ILabelProviderListener arg0) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public boolean isLabelProperty(Object arg0, String arg1) {
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener arg0) {
		}
	}

}

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

package org.celllife.idart.gui.search;

import model.manager.PatientManager;
import model.manager.SearchManager;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.iDartProperties;
import org.celllife.idart.database.hibernate.IdentifierType;
import org.celllife.idart.database.hibernate.Patient;
import org.celllife.idart.database.hibernate.PatientIdentifier;
import org.celllife.idart.gui.patient.tabs.IPatientTab;
import org.celllife.idart.gui.platform.GenericOthersGui;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.hibernate.Session;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PatientSearch extends GenericOthersGui {

	private TableViewer tblViewer;
	private String searchString;
	private Text searchBar;
	protected PatientIdentifier selectedIdentifier;
	private IPatientTab[] groupTabs;
	private Patient localPatient;
	
	/**
	 * Flag to determine whether to show inactive patients
	 */
	private boolean showInactive = false;
	
	/**
	 * Flag to determine whether to show only those patients with 
	 * packages awaiting pickup
	 */
	private boolean showPatientsWithPackagesAwaiting = false;
	
	/**
	 * The list of search resutls
	 */
	private List<PatientIdentifier> identifiers;
	
	/**
	 * This is the set of individual patients. It is only populated when
	 * there is a chance that there is only one patient in the identifiers list
	 * i.e. identifiers.size() <= maxNumberOfIdentifiersPerPatient
	 */
	private Set<Integer> patientids = new HashSet<Integer>();
	
	/**
	 * This is the maximum number of identifiers each patient can have. It is used
	 * to determine when to check for unique patients. 
	 */
	private int maxNumberOfIdentifiersPerPatient;
	private boolean disableSelection = false;
	
	private List<IdentifierType> types;

	// FIXME: (simon - multi ids) show alternate patients
	// see model.manager.PatientSearchManager.getSelectedPatient(boolean)
	public PatientSearch(Shell parent, Session session) {
		super(parent, session);
		types = PatientManager.getAllIdentifierTypes(session);
		maxNumberOfIdentifiersPerPatient = types.size();
	}
	
	public PatientIdentifier search(String patientId) {
		this.searchString = patientId == null ? "" : patientId;
		loadPatientIdentifiers();
		
		/*
		 * if (selectedIdentifier== null) { RestClient restClient = new RestClient();
		 * 
		 * String openMrsResource =
		 * restClient.getOpenMRSResource("patient?q="+StringUtils.replace(patientId,
		 * " ", "%20"));
		 * 
		 * Transaction tx = null;
		 * 
		 * try { tx = getHSession().beginTransaction();
		 * 
		 * for (IPatientTab tab : groupTabs) { tab.submit(localPatient); }
		 * 
		 * PatientManager.savePatient(getHSession(), localPatient);
		 * 
		 * getHSession().flush(); tx.commit();
		 * 
		 * } catch (Exception he) { if (tx != null) { tx.rollback(); }
		 * getLog().error("Error saving patient to the database.", he); } }
		 */
		
		if (patientids.size() == 1){
			return identifiers.get(0);
		}
		activate();
		updateTable();
		waitForClose();
		return selectedIdentifier;
	}
	
	public PatientIdentifier searchByName(String patientId) {
		this.searchString = patientId == null ? "" : patientId;
		//loadPatientIdentifiersOpenMRS();
		if (patientids.size() == 1){
			return identifiers.get(0);
		}
		activate();
		updateTable();
		waitForClose();
		return selectedIdentifier;
	}
	
	private void loadPatientIdentifiers() {
		BusyIndicator.showWhile(getParent().getDisplay(), 
			new Runnable() {
				@Override
				public void run() {
					if (showPatientsWithPackagesAwaiting){
						identifiers = SearchManager.getPatientIdentifiersWithAwiatingPackages(getHSession(), searchString);	
					} else {
						identifiers = SearchManager.getPatientIdentifiers(getHSession(), searchString, showInactive);	
					}
					
					if (identifiers.size() <= maxNumberOfIdentifiersPerPatient){
						populatePatientIds();
					} else {
						patientids.clear();
					}
				}
			});
	}
	
	/*
	 * private void loadPatientIdentifiersOpenMRS() {
	 * BusyIndicator.showWhile(getParent().getDisplay(), new Runnable() {
	 * 
	 * @Override public void run() {
	 * 
	 * identifiers = SearchManager.getPatientIdentifiersByName(getHSession(),
	 * searchString, showInactive, types);
	 * 
	 * if (identifiers.size() <= maxNumberOfIdentifiersPerPatient){
	 * populatePatientIds(); } else { patientids.clear(); } } }); }
	 */
	
	private void updateTable(){
		tblViewer.setInput(identifiers);
		selectFirstExactMatch();
	}
	
	private void selectFirstExactMatch() {
		for (PatientIdentifier id : identifiers) {
			if (id.getValue().equalsIgnoreCase(searchString)){
				disableSelection  = true;
				tblViewer.setSelection(new StructuredSelection(id), true);
				selectedIdentifier = id;
				disableSelection = false;
				return;
			}
		}
	}

	private void populatePatientIds() {
		for (PatientIdentifier identifier : identifiers) {
			patientids.add(identifier.getPatient().getId());
		}
	}

	/**
	 * This method initializes newPrintBlankLabel
	 */
	@Override
	protected void createShell() {
		buildShell("Identificação de Pacientes", new Rectangle(0, 0, 600, 570));
	}

	/**
	 * This method initializes compHeader
	 * 
	 */
	@Override
	protected void createCompHeader() {
		buildCompHeader("Identificação do Paciente", iDartImage.PATIENTINFOLABEL);
	}

	/**
	 * This method initializes compOptions
	 * 
	 */
	@Override
	protected void createCompOptions() {
		Composite composite = new Composite(getShell(), SWT.BORDER);
		composite.setLayout(new GridLayout(1,true));
		Rectangle b = getShell().getBounds();
		composite.setBounds(0, 80, b.width, b.height-160);
		
		tblViewer = new TableViewer(composite, SWT.BORDER | SWT.V_SCROLL
				| SWT.FULL_SELECTION | SWT.VIRTUAL);
		tblViewer.getTable().setLayoutData(new GridData(GridData.FILL_BOTH));
		tblViewer.getTable().setFont(
				ResourceUtils.getFont(iDartFont.VERASANS_8));
		tblViewer.setContentProvider(new ArrayContentProvider());
		tblViewer.getTable().setHeaderVisible(true);
		tblViewer.getTable().setLinesVisible(true);
		tblViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent arg0) {
				if (!disableSelection) {
					StructuredSelection selection = (StructuredSelection) arg0.getSelection();
					selectedIdentifier = (PatientIdentifier) selection.getFirstElement();
					cmdItemSelected();
				}
			}
		});
		createColumns(tblViewer);
		
		searchBar = new Text(composite, SWT.BORDER);
		searchBar.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		searchBar.setFocus();
		searchBar.setText(null == searchString ? "" : searchString);
		searchBar.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent arg0) {
				searchString = searchBar.getText().trim();
			}
		});
		
		searchBar.addKeyListener(new KeyAdapter() {
			private Runnable runnable;

			@Override
			public void keyReleased(KeyEvent e) {
				if (runnable == null){
					runnable = new Runnable(){
						@Override
						public void run() {
							if (searchBar.isDisposed()){
								return;
							}
							searchBar.setEnabled(false);
							loadPatientIdentifiers();
							updateTable();
							searchBar.setEnabled(true);
							searchBar.setFocus();
						}
					};
				}
				if ((e.character == SWT.CR)
						|| (e.character == (char) iDartProperties.intValueOfAlternativeBarcodeEndChar)) {

					getShell().getDisplay().timerExec(-1, runnable);
					runnable.run();
					
					if (patientids.size() == 1){
						selectedIdentifier = identifiers.get(0);
						cmdItemSelected();
					} else {
						selectFirstExactMatch();
						if (selectedIdentifier != null)
							cmdItemSelected();
					}
				} else {
					getShell().getDisplay().timerExec(500, runnable);
				}

			}
		});
		composite.layout();
	}

	protected void cmdItemSelected() {
		closeShell(false);
	}

	/**
	 * Creates the table layout
	 * 
	 * @param viewer
	 */
	public void createColumns(final TableViewer viewer) {
		TableViewerColumn name = new TableViewerColumn(viewer, SWT.NONE);
		name.getColumn().setWidth(250);
		name.getColumn().setText("Nome do Paciente");
		name.getColumn().setMoveable(true);
		name.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				PatientIdentifier content = (PatientIdentifier) element;
				return content.getPatient().toString();
			}
		});
		
		TableViewerColumn id = new TableViewerColumn(viewer, SWT.NONE);
		id.getColumn().setWidth(150);
		id.getColumn().setText("Tipo de Identificador");
		id.getColumn().setMoveable(true);
		id.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				PatientIdentifier content = (PatientIdentifier) element;
				return content.getType().getName();
			}
		});

		TableViewerColumn value = new TableViewerColumn(viewer, SWT.NONE);
		value.getColumn().setWidth(100);
		value.getColumn().setText("Número");
		value.getColumn().setMoveable(true);
		value.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				PatientIdentifier content = (PatientIdentifier) element;
				return content.getValue();
			}
		});
	}

	@Override
	protected void setLogger() {
		setLog(Logger.getLogger(this.getClass()));
	}
	
	private void waitForClose() {
		while (!getShell().isDisposed()) {
			if (!getShell().getDisplay().readAndDispatch()) {
				getShell().getDisplay().sleep();
			}
		}
	}

	@Override
	protected void createCompButtons() {
		Button btnCancel = new Button(getCompButtons(), SWT.NONE);
		btnCancel.setText("Cancelar");
		btnCancel.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnCancel.setToolTipText("Clique aqui para fechar esta janela.");
		btnCancel
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				closeShell(false);
			}
		});
	}

	/**
	 * If true the search list will include inactive patients
	 * 
	 * Defaults to false
	 * 
	 * Can not be used in conjunction with showPatientsWithPackagesAwaiting
	 * @param showInactive
	 */
	public void setShowInactive(boolean showInactive) {
		this.showInactive = showInactive;
		
	}

	/**
	 * If true the search list will only show patients that have
	 * packages awaiting collection.
	 * 
	 * Defaults to false
	 * 
	 * Can not be used in conjunction with showInactive
	 * @param showPatientsWithPackagesAwaiting
	 */
	public void setShowPatientsWithPackagesAwaiting(boolean showPatientsWithPackagesAwaiting) {
		this.showPatientsWithPackagesAwaiting = showPatientsWithPackagesAwaiting;
	}
}

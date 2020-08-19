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

/**
 * created on 08/05/2007
 * @author Rashid
 * gui class for stock take
 *
 * 2 functions
 * 	1) print out a form of all the stock in the clinic
 * 	2) allows users to capture the stock date information.
 */

package org.celllife.idart.gui.stockTake;

import model.manager.AdministrationManager;
import model.manager.DrugManager;
import model.manager.StockManager;
import model.manager.reports.StockTakeReport;
import model.manager.reports.StockTakeSheet;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.database.hibernate.*;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.gui.platform.GenericOthersGui;
import org.celllife.idart.gui.search.Search;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartColor;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.celllife.idart.gui.welcome.GenericWelcome;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 */
public class StockTakeGui extends GenericOthersGui {

	private static final String VALUE_CHANGED = "Value Changed";
	private static final String STOCK_ID = "stockID";
	private static final String UNITS_REMAINING = "unitsRemaining";

	private Button btnPrintForm;

	private Button btnStockReport;

	private Group grpManageStockTake;

	private Group grpDrugInfo;

	private Label lblPharmacy;

	private CCombo cmbPharmacy;

	private Label lblDrugSearch;

	private Text txtDrugSearch;

	private Text txtDrugForm;

	private Text txtPackSize;

	private Button btnSearch;

	private Button btnClose;

	private Button btnClear;

	private Button btnUpdate;

	private Group grpStockDetailsInfo;

	private Label lblDrugName;

	private Button btnNext;

	private Button btnImgNext;

	private Button btnPrevious;

	private Button btnImgPrevious;

	private Label lblPackSize;

	private Button btnZeroBatchSearch;

	private Table tblStockTake;

	private TableColumn clmDateReceived;

	private TableColumn clmBatchNumber;

	private TableColumn clmExpiryDate;

	private TableColumn clmQtyCountedPacks;

	private TableColumn clmQtyCountedPills;

	private TableColumn clmNotes;

	private Drug localDrug; // @jve:decl-index=0:

	private final List<Drug> drugList;

	private int currentDrugIndex = 0;

	private int sizeOfDrugList = 0;

	private TableEditor editor;

	private final boolean includeZeroBatches = false;

	private StockTake currentStockTake;

	private List<StockAdjustment> stockAdjustmentInStockTake;

	private final Date captureDate;

	private boolean isdisposed = false;

	private Button btnOpenStockTake;

	private Label lblStockTake;

	private StockCenter localStockCenter;

	/**
	 * @param parent
	 */
	public StockTakeGui(Shell parent) {
		super(parent, HibernateUtil.getNewSession());
		StockManager.clearStockTakes(getHSession());
		activate();
		GenericWelcome.timer.setDisabled(true);
		currentStockTake = StockManager.getStockTake(getHSession());
		if (currentStockTake != null) {
			stockAdjustmentInStockTake = StockManager
			.getStockAdjustmentsInStockTake(getHSession(),
					currentStockTake);
		} else {
			stockAdjustmentInStockTake = new ArrayList<StockAdjustment>();
		}
		captureDate = new Date();
		drugList = DrugManager.getDrugsListForStockTake(getHSession(),
				includeZeroBatches);
		sizeOfDrugList = drugList.size();
		// Collections.sort(drugList);
		if (currentStockTake == null) {
			enableFields(false);
		} else {
			enableFields(true);
		}
	}

	/**
	 * This method initialises getShell()
	 */
	@Override
	protected void createShell() {
		String shellTxt = "inventário de Stock";
		Rectangle bounds = new Rectangle(50, 50, 900, 700);

		buildShell(shellTxt, bounds);

		getShell().addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event e) {
				cmdEndStockTakeSelected();
			}
		});
		getShell().addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent event) {
				// When the shell is disposed, close the session and commit the
				// stock take
			}
		});

		// create the composites
		createCompSelectPharmacyAndStockTake();
		createGrpManageStockTake();
		createGrpDrugInfo();
		createGrpStockDetails();

		if (currentStockTake != null) {
			// Populate the table.
			localDrug = drugList.get(0);
			txtDrugSearch.setText(localDrug.getName());
			lblDrugName.setText(localDrug.getName() + "\t(Medicamento "
					+ (currentDrugIndex + 1) + " de " + sizeOfDrugList + ")");
			txtPackSize.setText(String.valueOf(localDrug.getPackSize()));
			txtDrugForm.setText(localDrug.getForm().getFormLanguage1());
			cmbPharmacy.setEnabled(false);
			populateStockGUI();
		}
	}

	/**
	 * This method initialises getCompHeader()
	 * 
	 */
	@Override
	protected void createCompHeader() {

		// getCompHeader()
		setCompHeader(new Composite(getShell(), SWT.NONE));
		getCompHeader().setBounds(new Rectangle(172, 0, 570, 50));

		// lblIcon
		lblIcon = new Label(getCompHeader(), SWT.NONE);
		lblIcon
		.setBounds(new org.eclipse.swt.graphics.Rectangle(50, 10, 50,
				43));
		lblIcon.setText("");
		lblIcon.setImage(ResourceUtils.getImage(iDartImage.PRESCRIPTIONNEW));

		// lblHeader
		lblHeader = new Label(getCompHeader(), SWT.CENTER | SWT.SHADOW_IN);
		lblHeader.setBackground(ResourceUtils
				.getColor(iDartColor.WIDGET_NORMAL_SHADOW_BACKGROUND));

		lblHeader.setFont(ResourceUtils.getFont(iDartFont.VERASANS_14));
		lblHeader.setBounds(new Rectangle(110, 20, 410, 30));
		lblHeader.setText("inventário de Stock");

	}

	private void createCompSelectPharmacyAndStockTake() {
		Composite compSelectPharmacyAndStockTake = new Composite(getShell(),
				SWT.NONE);
		compSelectPharmacyAndStockTake.setBounds(new Rectangle(172, 55, 570,
				125));
		compSelectPharmacyAndStockTake.setFont(ResourceUtils
				.getFont(iDartFont.VERASANS_8));

		lblStockTake = new Label(compSelectPharmacyAndStockTake, SWT.CENTER);
		lblStockTake.setBounds(new Rectangle(0, 5, 570, 41));
		if (currentStockTake == null) {
			lblStockTake
			.setText("O inventário de Stock ainda não está iniciado. Por favor inicie o inventário antes de "
					+ "lançar os dados.");
			lblStockTake.setForeground(ResourceUtils.getColor(iDartColor.RED));
		} else {
			lblStockTake
			.setText("inventário em progresso.\nO inventário actual foi aberto em "
					+ new SimpleDateFormat("dd-MM-yyyy")
					.format(currentStockTake.getStartDate())
					+ " as "
					+ new SimpleDateFormat("HH:mm")
					.format(currentStockTake.getStartDate()));
			lblStockTake
			.setForeground(ResourceUtils.getColor(iDartColor.BLACK));
		}
		lblStockTake.setFont(ResourceUtils
				.getFont(iDartFont.VERASANS_10_ITALIC));
		lblStockTake.setAlignment(SWT.CENTER);

		btnOpenStockTake = new Button(compSelectPharmacyAndStockTake, SWT.NONE);
		btnOpenStockTake.setBounds(new Rectangle(230, 86, 110, 30));
		btnOpenStockTake.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnOpenStockTake.setFocus();
		if (currentStockTake == null) {
			btnOpenStockTake.setText("Iniciar o inventário");
		} else {
			btnOpenStockTake.setText("Encerar o inventário");
		}
		btnOpenStockTake.setAlignment(SWT.CENTER);
		btnOpenStockTake
		.setToolTipText("Pressione este botão para ver a fichar de inventário.");
		btnOpenStockTake.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				if (currentStockTake == null) {
					cmdOpenStockTakeSelected();
				} else {
					cmdEndStockTakeSelected();
				}
			}
		});

		lblPharmacy = new Label(compSelectPharmacyAndStockTake, SWT.NONE);
		lblPharmacy.setBounds(new Rectangle(120, 51, 149, 20));
		lblPharmacy.setText("Farmácia:");
		lblPharmacy.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		cmbPharmacy = new CCombo(compSelectPharmacyAndStockTake, SWT.BORDER);
		cmbPharmacy.setBounds(new Rectangle(275, 51, 180, 20));
		cmbPharmacy.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbPharmacy.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		cmbPharmacy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent se) {
				localStockCenter = AdministrationManager.getStockCenter(
						getHSession(), cmbPharmacy.getText().trim());

			}
		});

		populatePharmacyCombo();
		localStockCenter = AdministrationManager.getStockCenter(getHSession(),
				cmbPharmacy.getText().trim());

	}

	private void createGrpManageStockTake() {
		if (grpManageStockTake != null) {
			grpManageStockTake.dispose();
		}

		grpManageStockTake = new Group(getShell(), SWT.NONE);
		grpManageStockTake.setText("Folhas da Contagem de Stock e Relatório de inventário");
		grpManageStockTake.setBounds(171, 180, 558, 55);
		grpManageStockTake.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		btnPrintForm = new Button(grpManageStockTake, SWT.NONE);
		btnPrintForm.setBounds(new Rectangle(10, 15, 265, 30));
		btnPrintForm.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnPrintForm.setText("Gerar Folha da Contagem de para inventário");
		btnPrintForm
		.setToolTipText("Pressione este botão para exibir a folha de lançamento do inventário.");
		btnPrintForm.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				StockTakeSheet report = new StockTakeSheet(getShell(),
						localStockCenter, false);
				viewReport(report);
			}
		});

		btnStockReport = new Button(grpManageStockTake, SWT.NONE);
		btnStockReport.setBounds(new Rectangle(285, 15, 265, 30));
		btnStockReport.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnStockReport.setText("Visualizar o Relatório do inventário");
		btnStockReport
		.setToolTipText("Pressione este botão para visualizar o Relatório do inventário.");
		btnStockReport.setEnabled(false);
		btnStockReport.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				StockTakeSheet report = new StockTakeSheet(getShell(),
						localStockCenter, false);
				viewReport(report);
			}
		});

	}

	/**
	 * This method initialises grpDrugInfo
	 * 
	 */
	private void createGrpDrugInfo() {

		if (grpDrugInfo != null) {
			grpDrugInfo.dispose();
		}

		grpDrugInfo = new Group(getShell(), SWT.NONE);
		grpDrugInfo.setText("Detalhes do Medicamento");
		grpDrugInfo.setBounds(new Rectangle(210, 245, 480, 110));
		grpDrugInfo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblDrugSearch = new Label(grpDrugInfo, SWT.NONE);
		lblDrugSearch.setBounds(new Rectangle(15, 20, 130, 20));
		lblDrugSearch.setText("Medicamento:");
		lblDrugSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		txtDrugSearch = new Text(grpDrugInfo, SWT.BORDER);
		txtDrugSearch.setBounds(new Rectangle(155, 18, 200, 20));
		txtDrugSearch.setEditable(false);
		txtDrugSearch.setEnabled(false);
		txtDrugSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		btnSearch = new Button(grpDrugInfo, SWT.NONE);
		btnSearch.setBounds(new Rectangle(365, 12, 100, 28));
		btnSearch.setText("Procurar");
		btnSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnSearch
		.setToolTipText("Pressione este botão para procurar por um medicamento.");

		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
				cmdDrugSearchWidgetSelected();
			}
		});

		if (currentStockTake == null) {
			btnSearch.setEnabled(false);
			lblDrugSearch.setEnabled(false);
			txtDrugSearch.setEnabled(false);
		} else {
			btnSearch.setEnabled(true);
			lblDrugSearch.setEnabled(true);
			txtDrugSearch.setEnabled(true);
		}
		lblPackSize = new Label(grpDrugInfo, SWT.NONE);
		lblPackSize.setBounds(new Rectangle(15, 45, 130, 20));
		lblPackSize.setText("Conteúdo/Frasco:");
		lblPackSize.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		txtPackSize = new Text(grpDrugInfo, SWT.BORDER);
		txtPackSize.setBounds(new Rectangle(155, 44, 200, 20));
		txtPackSize.setEditable(false);
		txtPackSize.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		txtDrugForm = new Text(grpDrugInfo, SWT.BORDER);
		txtDrugForm.setBounds(new Rectangle(365, 44, 100, 20));
		txtDrugForm.setEditable(false);
		txtDrugForm.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		btnImgPrevious = new Button(grpDrugInfo, SWT.NONE);
		btnImgPrevious.setBounds(15, 70, 30, 30);
		btnImgPrevious.setImage(ResourceUtils
				.getImage(iDartImage.LEFTARROW_30X26));
		btnImgPrevious
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {

				cmdPreviousWidgetSelected();
			}
		});

		btnPrevious = new Button(grpDrugInfo, SWT.NONE);
		btnPrevious.setText("Anterior");
		btnPrevious.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnPrevious.setBounds(new Rectangle(55, 70, 150, 30));
		btnPrevious
		.setToolTipText("Pressione este botão para carregar informações do medicamento anterior.");
		btnPrevious
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdPreviousWidgetSelected();
			}
		});

		btnNext = new Button(grpDrugInfo, SWT.NONE);
		btnNext.setText("Próximo");
		btnNext.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnNext.setBounds(new Rectangle(280, 70, 150, 30));
		btnNext
		.setToolTipText("Pressione este botão para carregar informações do próximo medicamento.");
		btnNext
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdNextWidgetSelected();
			}
		});

		btnImgNext = new Button(grpDrugInfo, SWT.NONE);
		btnImgNext.setBounds(440, 70, 30, 30);
		btnImgNext
		.setImage(ResourceUtils.getImage(iDartImage.RIGHTARROW_30X26));

		btnImgNext
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {

				cmdNextWidgetSelected();
			}
		});

	}

	/**
	 * This method initialises createGrpDrugInfo
	 * 
	 */
	private void createGrpStockDetails() {

		if (grpStockDetailsInfo != null) {
			grpStockDetailsInfo.dispose();
		}
		grpStockDetailsInfo = new Group(getShell(), SWT.NONE);
		grpStockDetailsInfo.setText("Informação de Lote" + "");
		grpStockDetailsInfo.setBounds(new Rectangle(54, 363, 783, 250));
		grpStockDetailsInfo
		.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		lblDrugName = new Label(grpStockDetailsInfo, SWT.NONE);
		lblDrugName.setBounds(new Rectangle(20, 20, 310, 20));
		lblDrugName.setText("Med.:");
		lblDrugName.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		btnZeroBatchSearch = new Button(grpStockDetailsInfo, SWT.NONE);
		btnZeroBatchSearch.setBounds(new Rectangle(557, 14, 194, 30));
		btnZeroBatchSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnZeroBatchSearch.setText("Procurar por lotes vazios");
		btnZeroBatchSearch
		.setToolTipText("Pressione este botão para procurar um lote que não tem unidade em stock.");
		btnZeroBatchSearch
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdZeroBatchWidgetSelected();
			}
		});
		btnZeroBatchSearch.setEnabled(false);

		tblStockTake = new Table(grpStockDetailsInfo, SWT.FULL_SELECTION
				| SWT.BORDER);
		tblStockTake.setBounds(new Rectangle(13, 51, 752, 182));
		tblStockTake.setHeaderVisible(true);
		tblStockTake.setLinesVisible(true);
		// tblStockTake
		// .setToolTipText("Click a row to view additional dispensing
		// information");

		tblStockTake.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		clmDateReceived = new TableColumn(tblStockTake, SWT.NONE);
		clmDateReceived.setText("Data de Recepção");
		clmDateReceived.setWidth(100);
		clmDateReceived.setResizable(true);

		clmBatchNumber = new TableColumn(tblStockTake, SWT.NONE);
		clmBatchNumber.setText("No de Lote.");
		clmBatchNumber.setWidth(70);
		clmBatchNumber.setResizable(true);

		clmExpiryDate = new TableColumn(tblStockTake, SWT.NONE);
		clmExpiryDate.setText("Data Expiração");
		clmExpiryDate.setWidth(100);
		clmExpiryDate.setResizable(true);

		clmQtyCountedPacks = new TableColumn(tblStockTake, SWT.NONE);
		clmQtyCountedPacks.setText("Frascos Contados");
		clmQtyCountedPacks.setWidth(125);
		clmQtyCountedPacks.setResizable(true);

		clmQtyCountedPills = new TableColumn(tblStockTake, SWT.NONE);
		clmQtyCountedPills.setText("Unidades Contadas");
		clmQtyCountedPills.setWidth(100);
		clmQtyCountedPills.setResizable(true);

		clmNotes = new TableColumn(tblStockTake, SWT.NONE);
		clmNotes.setText("Notas");
		clmNotes.setWidth(250);
		clmNotes.setResizable(true);

		editor = new TableEditor(tblStockTake);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		tblStockTake.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent event) {
				// Dispose any existing editor
				Control old = editor.getEditor();
				if (old != null) {
					old.dispose();
				}

				// Determine where the mouse was clicked
				Point pt = new Point(event.x, event.y);

				// Determine which row was selected
				final TableItem item = tblStockTake.getItem(pt);
				if (item != null) {
					// Determine which column was selected
					int column = -1;
					for (int i = 0, n = tblStockTake.getColumnCount(); i < n; i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							// This is the selected column
							column = i;
							break;
						}

					}

					if (column == 3 || column == 4 || column == 5) {
						// Create the Text Object for your editor
						final Text text = new Text(tblStockTake, SWT.None);
						text.setForeground(item.getForeground());

						// Transfer any text from the cell to the text control
						// and store a copy of the original text
						text.setText(item.getText(column));
						text.setForeground(item.getForeground());
						text.setFont(ResourceUtils
								.getFont(iDartFont.VERASANS_8));
						text.selectAll();
						text.setFocus();

						// Recalculate the minimum width for the editor
						editor.minimumWidth = text.getBounds().width;

						// Set the control into the editor
						editor.setEditor(text, item, column);

						// Add a handler to transfer the text back to the cell
						// any time its modified
						final int col = column;
						text.addModifyListener(new ModifyListener() {
							@Override
							public void modifyText(ModifyEvent event1) {
								// User finished updating cell, we now check if
								// the data entered is in the correct form
								item.setText(col, text.getText());
								// check if user modified text
								if (!item.getText().equals("-")) {
									item.setData(VALUE_CHANGED, "true");
									btnUpdate.setEnabled(true);
								}
							}
						});
					}
				}
			}
		});
	}

	/**
	 * This method initialises compButtons
	 * 
	 */
	@Override
	protected void createCompButtons() {

		btnUpdate = new Button(getCompButtons(), SWT.NONE);
		btnUpdate.setText("Gravar");
		btnUpdate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		// btnUpdate.setBounds(new Rectangle(24, 5, 100, 30));
		btnUpdate
		.setToolTipText("Pressione este botão para actualizar  o stock a partir da tabela");
		btnUpdate
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdUpdateWidgetSelected();
			}
		});
		btnUpdate.setEnabled(false);

		btnClear = new Button(getCompButtons(), SWT.NONE);
		btnClear.setText("Limpar");
		btnClear.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnClear.setBounds(new Rectangle(161, 6, 100, 30));
		btnClear
		.setToolTipText("Pressione este botão para limpar a tela.\nA informação que inseriu será perdida.");
		btnClear
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdClearWidgetSelected();
			}
		});

		btnClose = new Button(getCompButtons(), SWT.NONE);
		btnClose.setText("Fechar");
		btnClose.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		// btnClose.setBounds(new Rectangle(300, 5, 100, 30));
		btnClose
		.setToolTipText("Pressione este botão para fechar a tela.\nA informação que inseriu será perdida.");
		btnClose
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdEndStockTakeSelected();
			}
		});

	}

	private void cmdUpdateWidgetSelected() {

		if (fieldsOk()) {
			boolean showMessage = false;
			// Check for non captured drugs
			boolean nonCapturedBatches = false;
			Transaction tx = null;

			try {

				tx = getHSession().beginTransaction();
				// Check if any batches were not captured
				for (int i = 0; i < tblStockTake.getItemCount(); i++) {
					TableItem ti = tblStockTake.getItem(i);
					// only update entries which have changed
					if (((String) ti.getData(VALUE_CHANGED)).equals("false")
							&& ti.getText(3).equals("-")
							&& ti.getText(4).equals("-")) {
						// Show message to ask user if all batches were counted
						MessageBox mbox = new MessageBox(getShell(), SWT.YES
								| SWT.NO);
						mbox.setText("Medicamento actualizado");
						mbox
						.setMessage("De acordo com o que você preencheu nesta tabela, "
								+ "nem todos os lotes para este medicamento foram contados. \nO valor que inseriu é o TOTAL DE CONTAGEM DE TODOS LOTES?");

						if (mbox.open() == SWT.YES) {
							nonCapturedBatches = true;
						}

						break;
					}
				}
				// Iterate through all batches in table
				for (int i = 0; i < tblStockTake.getItemCount(); i++) {
					TableItem ti = tblStockTake.getItem(i);
					// only update entries which have changed
					String packsTxt = ti.getText(3);
					String pillsTxt = ti.getText(4);
					if (((String) ti.getData(VALUE_CHANGED)).equals("true")) {
						// First check if an entry for this batch is in the
						// stockAdjustment table
						StockAdjustment stockAdjustment = StockManager.getAdjustment(
								getHSession(),
								((Integer) ti.getData(STOCK_ID)).intValue(),
								currentStockTake.getId());
						// No entry exists, so we create a new one
						if (stockAdjustment == null) {
							stockAdjustment = new StockAdjustment();
						}
						stockAdjustment.setCaptureDate(captureDate);
						Integer stockId = (Integer) ti.getData(STOCK_ID);
						stockAdjustment.setStock(StockManager.getStock(
								getHSession(),	stockId));
						// Note adjusted value is in pills
						// Need to consider if user only edited 1 of the 2
						// values
						boolean hasData = !packsTxt.equals("-") || !pillsTxt.equals("-");  
						
						int packs = 0;
						int pills = 0;
						if (!packsTxt.equals("-")){
							packs = Integer.parseInt(packsTxt);
						}
						if (!pillsTxt.equals("-")){
							pills = Integer.parseInt(pillsTxt);
						}
						
						int count = (localDrug.getPackSize() * packs) + pills;
						stockAdjustment.setStockCount(count);
						stockAdjustment.setNotes(ti.getText(5));
						stockAdjustment.setStockTake(currentStockTake);
						Integer unitsRemaining = (Integer) ti.getData(UNITS_REMAINING);
						stockAdjustment.setAdjustedValue(unitsRemaining
								- stockAdjustment.getStockCount());

						if (hasData || nonCapturedBatches){
							
							StockManager.saveStockAdjustment(getHSession(),
									stockAdjustment);
						} else {
							removeAdjustmentFromDatabase(stockId);
						}
						showMessage = true;
					}
				}
				if (showMessage) {
					// Show message to say that drug was updated
					MessageBox mb = new MessageBox(getShell(), SWT.OK);
					mb.setText("Medicamento Actualizado");
					mb.setMessage("Medicamento '" + localDrug.getName()
							+ "' foi actualizado com Sucesso");
					mb.open();
				}
				// After saving, we reload the new values into the table and
				// reset
				// the indexes etc
				clearForm();

				// load batches
				txtDrugSearch.setText(localDrug.getName());
				lblDrugName.setText(localDrug.getName() + "\t(Medicamento "
						+ (currentDrugIndex + 1) + " de " + sizeOfDrugList
						+ ")");
				resetValuesChanged();

				getHSession().flush();
				tx.commit();

				// After each update, we update the list of adjustments
				stockAdjustmentInStockTake = StockManager
				.getStockAdjustmentsInStockTake(getHSession(),
						currentStockTake);

				populateStockGUI();

				// now we remove the focus to point to a component other than
				// the table
				// We do this because the user may have left the focus on a
				// particular
				// entry. When the table gets updated, this entry will appear in
				// the newly

				// constructed table.
				cmbPharmacy.forceFocus();
			} catch (HibernateException he) {
				MessageBox m = new MessageBox(getShell(), SWT.OK
						| SWT.ICON_INFORMATION);
				m.setText("Problemas ao salvar na base de dados");
				m
				.setMessage("Houvem um problema ao salvar os ajustes na base de dados. Tente de novo");
				m.open();

			}
		}
	}

	private void cmdPreviousWidgetSelected() {

		// First check if the current drug batches were changed.
		// if so, we ask the user if he/she would like to save it
		if (isUpdateRequired()) {
			MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO
					| SWT.CANCEL);
			mb.setText("Gravaçao das quantidades de medicamentos");
			mb.setMessage("Gostaria de salvar os detalhes deste medicamento?");
			int selection = mb.open();
			if (selection == SWT.YES) {
				cmdUpdateWidgetSelected();
			} else if (selection == SWT.CANCEL)
				return;
		}

		// only update the screen if the drug displayed is not the first
		// one in the list.
		if (currentDrugIndex != 0) {
			clearForm();

			// load batches
			localDrug = drugList.get(currentDrugIndex - 1);
			txtDrugSearch.setText(localDrug.getName());
			updateCurrentDruxIndex();
			lblDrugName.setText(localDrug.getName() + "\t(Medicamento "
					+ (currentDrugIndex + 1) + " de " + sizeOfDrugList + ")");
			txtPackSize.setText(String.valueOf(localDrug.getPackSize()));
			txtDrugForm.setText(localDrug.getForm().getFormLanguage1());
			populateStockGUI();
		}
	}

	private void cmdNextWidgetSelected() {

		// First check if the current drug batches were changed.
		// if so, we ask the user if he/she would like to save it
		if (isUpdateRequired()) {
			MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO
					| SWT.CANCEL);
			mb.setText("Gravação das quantidades de medicamentos");
			mb.setMessage("Gostaria de salvar os detalhes deste medicamento?");
			int selection = mb.open();
			if (selection == SWT.YES) {
				cmdUpdateWidgetSelected();
			} else if (selection == SWT.CANCEL)
				return;

		}
		// only update the screen if the drug displayed is not the last
		// one in the list.
		if (currentDrugIndex != drugList.size() - 1) {
			clearForm();

			// load batches
			localDrug = drugList.get(currentDrugIndex + 1);
			txtDrugSearch.setText(localDrug.getName());
			updateCurrentDruxIndex();
			lblDrugName.setText(localDrug.getName() + "\t(Drug "
					+ (currentDrugIndex + 1) + " of " + sizeOfDrugList + ")");
			txtPackSize.setText(String.valueOf(localDrug.getPackSize()));
			txtDrugForm.setText(localDrug.getForm().getFormLanguage1());
			populateStockGUI();
		}
	}

	private void cmdDrugSearchWidgetSelected() {

		// First check if the current drug batches were changed.
		// if so, we ask the user if he/she would like to save it
		if (isUpdateRequired()) {
			MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO
					| SWT.CANCEL);
			mb.setText("Gravação das quantidades de medicamentos");
			mb.setMessage("Gostaria de salvar os detalhes deste medicamento?");
			int selection = mb.open();
			if (selection == SWT.YES) {
				cmdUpdateWidgetSelected();
			} else if (selection == SWT.CANCEL)
				return;
		}

		Search drugSearch = new Search(getHSession(), getShell(),
				CommonObjects.DRUG, includeZeroBatches);

		if (drugSearch.getValueSelected() != null) {

			clearForm();

			// load batches
			localDrug = DrugManager.getDrug(getHSession(), drugSearch
					.getValueSelected()[0]);
			txtDrugSearch.setText(localDrug.getName());
			txtDrugForm.setText(localDrug.getForm().getFormLanguage1());
			updateCurrentDruxIndex();
			txtPackSize.setText(String.valueOf(localDrug.getPackSize()));
			lblDrugName.setText(localDrug.getName() + "\t(Medicamento "
					+ (currentDrugIndex + 1) + " de " + sizeOfDrugList + ")");

			populateStockGUI();

		}

	}

	private void cmdZeroBatchWidgetSelected() {

		Search stockSearch = new Search(getHSession(), getShell(), true,
				localDrug);

		if (stockSearch.getValueSelected() != null) {

			int stockId = (Integer.parseInt((String)stockSearch.getData()));
			Stock localBatch = StockManager.getStock(getHSession(), stockId);
			addItemToTable(localBatch);
		}
	}

	private void populatePharmacyCombo() {

		CommonObjects.populateStockCenters(getHSession(), cmbPharmacy);
		cmbPharmacy.setEnabled(true);
	}

	private void populateStockGUI() {

		if (localStockCenter == null) {
			getLog().info(
			"Tried to retrieve stock list, but localPharmay is null");
			return;
		} else if (localDrug == null) {
			getLog()
			.info("Tried to retrieve stock list, but localDrug is null");
			return;

		}

		java.util.List<Stock> batchList = null;

		// Clear the table of all previous rows
		tblStockTake.removeAll();
		tblStockTake.clearAll();

		try {

			batchList = StockManager.getStockForStockTake(getHSession(),
					localDrug, localStockCenter, includeZeroBatches);
			Iterator<Stock> iter = batchList.iterator();

			while (iter.hasNext()) {
				Stock thisStock = iter.next();
				addItemToTable(thisStock);
			}
		}

		catch (HibernateException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			batchList = null;
		}
	}

	private void clearForm() {

		// Clear the table of all previous rows
		tblStockTake.removeAll();
		btnUpdate.setEnabled(false);
		Control old = editor.getEditor();
		if (old != null) {
			old.dispose();
		}
	}

	/**
	 * Method fieldsOk.
	 * 
	 * @return boolean
	 */
	private boolean fieldsOk() {
		if (txtDrugSearch.getText().equals("")) {
			MessageBox mb = new MessageBox(getShell());
			mb.setText("Nenhum medicamento seleccionado");
			mb.setMessage("Nenhum medicamento foi seleccionado. Por favor "
					+ "procura por um medicamento");
			mb.open();
			return false;
		} else if (captureDate.after(new Date())) {
			MessageBox mb = new MessageBox(getShell());
			mb.setText("Data incorrecta");
			mb
			.setMessage("A data de lançamento é incorrecta. Por favor seleccione uma data correcta");
			mb.open();
			return false;
		}

		else
			return true;
	}

	private void updateCurrentDruxIndex() {

		for (int i = 0; i < drugList.size(); i++) {
			Drug drug = drugList.get(i);
			if (drug.getId() == localDrug.getId()) {
				currentDrugIndex = i;
				if (currentDrugIndex == 0) {
					btnPrevious.setEnabled(false);
					btnImgPrevious.setEnabled(false);
				} else {
					btnPrevious.setEnabled(true);
					btnImgPrevious.setEnabled(true);
				}

				if (currentDrugIndex == sizeOfDrugList - 1) {
					btnNext.setEnabled(false);
					btnImgNext.setEnabled(false);
				} else {
					btnNext.setEnabled(true);
					btnImgNext.setEnabled(true);
				}
				return;
			}
		}

	}

	/**
	 * Method isUpdateRequired.
	 * 
	 * @return boolean
	 */
	private boolean isUpdateRequired() {
		// Iterate through all batches in table
		for (int i = 0; i < tblStockTake.getItemCount(); i++) {
			TableItem ti = tblStockTake.getItem(i);
			// only update entries which have changed
			if (((String) ti.getData(VALUE_CHANGED)).equals("true"))
				return true;

		}
		return false;
	}

	private void resetValuesChanged() {
		// Iterate through all batches in table
		for (int i = 0; i < tblStockTake.getItemCount(); i++) {
			TableItem ti = tblStockTake.getItem(i);
			// only update entries which have changed
			if (((String) ti.getData(VALUE_CHANGED)).equals("true")) {
				ti.setData(VALUE_CHANGED, "false");
			}
		}
	}

	// Method to add a single batch to the table
	/**
	 * Method addItemToTable.
	 * 
	 * @param thisStock
	 *            Stock
	 */
	private void addItemToTable(Stock thisStock) {

		StockLevel sl = StockManager.getCurrentStockLevel(
				getHSession(), thisStock);
		if (sl == null || (sl.getFullContainersRemaining() == 0
				&& sl.getLoosePillsRemaining() == 0)) {
			return;
		}

		final TableItem ti = new TableItem(tblStockTake, SWT.NONE);
		ti.setBackground(ResourceUtils.getColor(iDartColor.WHITE));

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

		ti.setText(0, sdf.format(thisStock.getDateReceived()));
		ti.setText(1, thisStock.getBatchNumber());
		ti.setText(2, sdf.format(thisStock.getExpiryDate()));
		// try to prepopulate values if this batch count
		// was entered already
		boolean set = false;
		for (int i = 0; i < stockAdjustmentInStockTake.size(); i++) {
			StockAdjustment sa = stockAdjustmentInStockTake.get(i);
			if (thisStock == sa.getStock()) {
				int count = sa.getStockCount();
				int packs = count / thisStock.getDrug().getPackSize();
				int pills = count % thisStock.getDrug().getPackSize();

				ti.setText(3, String.valueOf(packs));
				ti.setText(4, String.valueOf(pills));
				ti.setText(5, sa.getNotes());
				set = true;
			}
		}
		if (!set) {
			ti.setText(3, "-");
			ti.setText(4, "-");
			ti.setText(5, "");
		}

		ti.setData(VALUE_CHANGED, "false");
		ti.setData(STOCK_ID, thisStock.getId());
		int unitsRemaining = localDrug.getPackSize()
						* sl.getFullContainersRemaining() + sl
						.getLoosePillsRemaining();
		ti.setData(UNITS_REMAINING, unitsRemaining);

		// set the editable cells background colour to grey
		ti
		.setBackground(3, ResourceUtils
				.getColor(iDartColor.GRAY));
		ti
		.setBackground(4, ResourceUtils
				.getColor(iDartColor.GRAY));
		ti
		.setBackground(5, ResourceUtils
				.getColor(iDartColor.GRAY));
	}

	/**
	 * Method enableFields.
	 * 
	 * @param enable
	 *            boolean
	 */
	private void enableFields(boolean enable) {

		btnPrintForm.setEnabled(enable);
		btnSearch.setEnabled(enable);
		btnPrevious.setEnabled(enable);
		btnNext.setEnabled(enable);
		btnImgNext.setEnabled(enable);
		btnImgPrevious.setEnabled(enable);
		//btnZeroBatchSearch.setEnabled(enable);
		btnClear.setEnabled(enable);
		// btnUpdate.setEnabled(enable);
		tblStockTake.setEnabled(enable);
		txtPackSize.setEnabled(enable);
		txtDrugForm.setEnabled(enable);

		if (currentDrugIndex == 0) {
			btnPrevious.setEnabled(false);
			btnImgPrevious.setEnabled(false);
		}
		if (currentDrugIndex == sizeOfDrugList - 1) {
			btnNext.setEnabled(false);
			btnImgNext.setEnabled(false);
		}
	}

	private void cmdOpenStockTakeSelected() {
		if (localStockCenter == null) {
			MessageBox mPharmNull = new MessageBox(getShell(), SWT.OK
					| SWT.ICON_WARNING);
			mPharmNull.setText("Nenhuma Farmácia seleccionada");
			mPharmNull
			.setMessage("Seleccione a Farmácia a realizar o inventário");
			mPharmNull.open();
			return;

		}

		MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO
				| SWT.ICON_INFORMATION);
		mb.setText("Open Stock Take?");
		mb.setMessage("Quer iniciar novo inventário para a farmácia '"
				+ localStockCenter.getStockCenterName() + "'?");

		switch (mb.open()) {
		case SWT.NO: {
			return;
		}
		case SWT.YES: {
			Transaction tx = null;

			try {
				tx = getHSession().beginTransaction();
				currentStockTake = StockManager.createStockTake(getHSession(),
						new Date());
				getHSession().flush();
				tx.commit();
			} catch (HibernateException he) {

				MessageBox m = new MessageBox(getShell(), SWT.OK
						| SWT.ICON_WARNING);
				m.setText("Problemas ao gravar na base de dados");
				m
				.setMessage("Hou problemas ao criar o inventário. Tente de novo.");
				m.open();
				getLog().error("Hibernate Exception while opening stock take",
						he);
				if (tx != null) {
					tx.rollback();
				}
				cmdEndStockTakeSelected();

			}
			// Populate the table.
			localDrug = drugList.get(0);
			txtDrugSearch.setText(localDrug.getName());
			lblDrugName.setText(localDrug.getName() + "\t(Medicamento "
					+ (currentDrugIndex + 1) + " de " + sizeOfDrugList + ")");
			txtPackSize.setText(String.valueOf(localDrug.getPackSize()));
			txtDrugForm.setText(localDrug.getForm().getFormLanguage1());
			populateStockGUI();
			enableFields(true);
			cmbPharmacy.setEnabled(false);
			btnOpenStockTake.setText("Encerar o inventário");
			lblStockTake
			.setText("inventário em progresso. O inventário actual foi iniciado em "
					+ new SimpleDateFormat("dd-MM-yyyy")
					.format(currentStockTake.getStartDate())
					+ " as "
					+ new SimpleDateFormat("HH:mm")
					.format(currentStockTake.getStartDate()));
			lblStockTake
			.setForeground(ResourceUtils.getColor(iDartColor.BLACK));

		}
		}

	}

	private void cmdEndStockTakeSelected() {
		if (isdisposed)
			return;
		if (currentStockTake == null) {
			getHSession().close();
			isdisposed = true;
			getShell().dispose();
			return;
		}
		// check if any adjustments were made
		if (stockAdjustmentInStockTake.size() == 0) {
			MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO);
			mb.setText("Fecho do inventário");
			mb
			.setMessage("O inventário actual não tem dados.\n\n Tem certeza que quer fechar?");
			if (mb.open() == SWT.YES) {
				Transaction tx = null;

				try {

					tx = getHSession().beginTransaction();
					StockManager.deleteStockTake(getHSession(),
							currentStockTake);
					getHSession().flush();
					tx.commit();
					GenericWelcome.timer.setDisabled(false);
					getHSession().close();
					isdisposed = true;
					getShell().dispose();

				} catch (HibernateException he) {
					MessageBox m = new MessageBox(getShell(), SWT.OK
							| SWT.ICON_INFORMATION);
					m.setText("Problemas ao remover dados da base de dados");
					m
					.setMessage("Houve problemas ao apagar o inventário da base de dados. Tente de novo.");
					m.open();

				}
			} else
				return;
		} else {

			MessageBox mbox = new MessageBox(getShell(), SWT.YES | SWT.NO);
			mbox.setText("Fim do inventário: Aceita o cálculo da variancia?");
			mbox
			.setMessage("Com base no stock contado, a variação actual dentro do stock é "
					 
					+ StockManager.getVariance(getHSession())
					+ "\n\nSe você optar por aceitar essa variancia, clique Yes. Os níveis de stock, então, serão ajustados em conformidade\n"
					+ "Se você optar por não aceitar esta variação, clique No. Então será capaz de recuperar todas contagens de stock novamente. "
					+ "");
			if (mbox.open() == SWT.YES) {
				Transaction tx = null;

				try {

					tx = getHSession().beginTransaction();
					StockManager.endStockTake(getHSession(), new Date());
					// commit and flush so that the report can see the values
					// which were finalised
					getHSession().flush();
					// tx.commit();
					MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO);
					mb.setText("Relatório de inventário de Stock");
					mb
					.setMessage("Gostaria de visualizaer o Relatório final de inventário de stock?");
					if (mb.open() == SWT.YES) {
						StockTakeReport report = new StockTakeReport(
								getShell(), localStockCenter,
								currentStockTake.getStockTakeNumber());
						viewReport(report);
					}
					// After we save the stock, we need to check if their are
					// units remaining
					// for the stock which was adjusted
					for (int i = 0; i < stockAdjustmentInStockTake.size(); i++) {
						StockManager.updateStockLevel(getHSession(),
								stockAdjustmentInStockTake.get(i).getStock());
					}
					getHSession().flush();
					tx.commit();
					GenericWelcome.timer.setDisabled(false);
					getHSession().close();
					isdisposed = true;
					getShell().dispose();

				} catch (HibernateException he) {
					MessageBox m = new MessageBox(getShell(), SWT.OK
							| SWT.ICON_INFORMATION);
					m.setText("Problemas ao graavar na base de dados");
					m
					.setMessage("Houve um problema ao gravar o inventário na base de dados. Por favor, tente novamente.");
					m.open();

				}
			} else
				return;
		}
	}

	private void cmdClearWidgetSelected() {
		// Check if any batches for this drug has been saved already
		if (isBatchSavedInTable()) {

			MessageBox mb = new MessageBox(getShell(), SWT.YES | SWT.NO);
			mb.setText("Limpar a contagem de stock para o medicamento corrente");
			mb
			.setMessage("Gostaria de limpar a contagem de stock para o medicamento actual?");
			if (mb.open() == SWT.YES) {
				for (int i = 0; i < tblStockTake.getItemCount(); i++) {
					TableItem ti = tblStockTake.getItem(i);
					removeAdjustmentFromDatabase(((Integer) ti
							.getData(STOCK_ID)).intValue());
				}
				MessageBox mb1 = new MessageBox(getShell(),
						SWT.ICON_INFORMATION);
				mb1.setText("Contagem de Stock limpado com successo");
				mb1
				.setMessage("A contagem de stock para o medicamento corrente foi limpo com sucesso.");
				mb1.open();
				resetValuesChanged();
				stockAdjustmentInStockTake = StockManager
				.getStockAdjustmentsInStockTake(getHSession(),
						currentStockTake);
			} else
				return;
		}
		// After saving, we reload the new values into the table and
		// reset
		// the indexes etc
		clearForm();

		// load batches
		txtDrugSearch.setText(localDrug.getName());
		lblDrugName.setText(localDrug.getName() + "\t(Medicamento "
				+ (currentDrugIndex + 1) + " de " + sizeOfDrugList + ")");
		resetValuesChanged();

		getHSession().flush();

		// After each update, we update the list of adjustments
		stockAdjustmentInStockTake = StockManager
		.getStockAdjustmentsInStockTake(getHSession(), currentStockTake);
		populateStockGUI();
		// now we remove the focus to point to a component other than
		// the table
		// We do this because the user may have left the focus on a
		// particular
		// entry. When the table gets updated, this entry will appear in
		// the newly
		// constructed table.
		cmbPharmacy.forceFocus();

	}

	/**
	 * Method isBatchSavedInTable.
	 * 
	 * @return boolean
	 */
	private boolean isBatchSavedInTable() {
		for (int i = 0; i < tblStockTake.getItemCount(); i++) {
			TableItem ti = tblStockTake.getItem(i);

			for (int j = 0; j < stockAdjustmentInStockTake.size(); j++) {
				StockAdjustment stock = stockAdjustmentInStockTake.get(j);
				if (((Integer) ti.getData(STOCK_ID)).intValue() == stock
						.getStock().getId())
					return true;
			}
		}
		return false;
	}

	/**
	 * Method removeAdjustmentFromDatabase.
	 * 
	 * @param id
	 *            int
	 * @return boolean
	 */
	private boolean removeAdjustmentFromDatabase(int id) {

		for (int j = 0; j < stockAdjustmentInStockTake.size(); j++) {
			StockAdjustment stock = stockAdjustmentInStockTake.get(j);
			if (id == stock.getStock().getId()) {
				StockManager.deleteStockAdjustment(getHSession(), stock);
				return true;
			}
		}
		getHSession().flush();
		return false;
	}

	@Override
	protected void createCompOptions() {
	}

	@Override
	protected void setLogger() {
		setLog(Logger.getLogger(this.getClass()));
	}
}

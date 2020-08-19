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

package org.celllife.idart.gui.stockArrives;

import model.manager.AdministrationManager;
import model.manager.DrugManager;
import model.manager.StockManager;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.commonobjects.iDartProperties;
import org.celllife.idart.database.hibernate.Drug;
import org.celllife.idart.database.hibernate.Stock;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.gui.misc.iDARTChangeListener;
import org.celllife.idart.gui.platform.GenericFormGui;
import org.celllife.idart.gui.search.Search;
import org.celllife.idart.gui.utils.*;
import org.celllife.idart.gui.widget.DateButton;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 */
public class DeliveryDetails extends GenericFormGui {

	private CCombo cmbStockCenter;

	private Group grpDrugDetails;

	private Text txtDrugName;

	private Text txtManufacturer;

	private Text txtDescription;

	private Text txtPackSize;

	private Group grpBatchDetails;

	private Text txtBatchNumber;

	private Combo cmbExpiryMonth;

	private Combo cmbExpiryYear;

	private Text txtUnitsReceived;

	private TableItem tableItem;

	private iDARTChangeListener changeListener;

	private Drug theDrug;

	private Text txtShelfNo;

	private Button btnDrugSearch;

	private DateButton btnArrivalDatePicker;

	private Text txtUnitPrice;

	private final StockArrives parentStockScreen;

	/**
	 * Constructor method
	 * 
	 * @param parentStockScreen
	 *            Shell
	 * @param ti
	 *            TableItem
	 */
	public DeliveryDetails(StockArrives parentStockScreen, TableItem ti) {
		super(parentStockScreen.getShell(), HibernateUtil.getNewSession());
		this.tableItem = ti;
		this.parentStockScreen = parentStockScreen;

		Search drugSearch = new Search(getHSession(), getShell(),
				CommonObjects.DRUG);

		if (drugSearch.getValueSelected() != null) {
			btnDrugSearch.setEnabled(false);
			loadDrugDetails(drugSearch.getValueSelected()[0]);
			txtDescription.setEnabled(false);
			txtPackSize.setEnabled(false);
		} else {
			enableFields(false);
		}
	}

	/**
	 * This method initializes newDeliveryDetails
	 */
	@Override
	protected void createShell() {
		//String shellTxt = "Delivery Details";
		String shellTxt = "Detalhes da Entrada";
		Rectangle bounds = new Rectangle(200, 100, 600, 630);
		buildShell(shellTxt, bounds);
		getShell().addListener(SWT.Close, new Listener() {
			@Override
			public void handleEvent(Event e) {
				cmdCancelWidgetSelected();
			}
		});
	}

	/**
	 * This method initializes compHeader
	 */
	@Override
	protected void createCompHeader() {
		String headerTxt = "Detalhes da Entrada";
		iDartImage icoImage = iDartImage.PACKAGESARRIVE;
		buildCompHeader(headerTxt, icoImage);
	}

	@Override
	protected void createContents() {
		createGrpDrugDetails();
		createGrpBatchDetails();
		enableFields(false);

	}

	/**
	 * This method initializes grpDrugDetails
	 * 
	 */
	private void createGrpDrugDetails() {

		grpDrugDetails = new Group(getShell(), SWT.NONE);
		grpDrugDetails.setText("Detalhes do Medicamento");
		grpDrugDetails.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grpDrugDetails.setBounds(new Rectangle(25, 100, 600, 108));

		Label lblDrugName = new Label(grpDrugDetails, SWT.NONE);
		lblDrugName.setBounds(new org.eclipse.swt.graphics.Rectangle(20, 25,
				90, 20));
		lblDrugName.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblDrugName.setText("Nome");

		txtDrugName = new Text(grpDrugDetails, SWT.BORDER);
		txtDrugName.setBounds(new org.eclipse.swt.graphics.Rectangle(150, 25,
				190, 20));
		txtDrugName.setEditable(false);
		txtDrugName.setEnabled(false);
		txtDrugName.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		btnDrugSearch = new Button(grpDrugDetails, SWT.NONE);
		btnDrugSearch.setBounds(new org.eclipse.swt.graphics.Rectangle(340, 22,
				135, 26));
		btnDrugSearch.setText("Procurar Medicamento");
		btnDrugSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		btnDrugSearch
		.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
			@Override
			public void widgetSelected(
					org.eclipse.swt.events.SelectionEvent e) {
				cmdSearchWidgetSelected();
			}
		});

		Label lblForm = new Label(grpDrugDetails, SWT.NONE);
		lblForm.setBounds(new Rectangle(20, 50, 90, 20));
		lblForm.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblForm.setText("Forma");

		txtDescription = new Text(grpDrugDetails, SWT.BORDER);
		txtDescription.setBounds(new Rectangle(150, 50, 90, 20));
		txtDescription.setEditable(false);
		txtDescription.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		Label lblPackSize = new Label(grpDrugDetails, SWT.NONE);
		lblPackSize.setBounds(new Rectangle(20, 75, 125, 20));
		lblPackSize.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblPackSize.setText("Tamanho do Frasco");

		txtPackSize = new Text(grpDrugDetails, SWT.BORDER);
		txtPackSize.setBounds(new Rectangle(150, 75, 60, 20));
		txtPackSize.setEditable(false);
		txtPackSize.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

	}

	/**
	 * This method initializes grpBatchDetails
	 * 
	 */
	private void createGrpBatchDetails() {

		grpBatchDetails = new Group(getShell(), SWT.NONE);
		grpBatchDetails.setText("Detalhes do Lote");
		grpBatchDetails.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		grpBatchDetails.setBounds(new Rectangle(25, 226, 600, 280));

		// Manufacturer
		Label lblManufacturer = new Label(grpBatchDetails, SWT.NONE);
		lblManufacturer.setBounds(new Rectangle(20, 30, 90, 20));
		lblManufacturer.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblManufacturer.setText("Fabricante");

		txtManufacturer = new Text(grpBatchDetails, SWT.BORDER);
		txtManufacturer.setBounds(new Rectangle(150, 30, 185, 20));
		txtManufacturer.setEditable(true);
		txtManufacturer.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		txtManufacturer.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.character == SWT.CR) {
					btnSave.setFocus();
				}
			}
		});

		// Batch number
		Label lblBatchNumber = new Label(grpBatchDetails, SWT.NONE);
		lblBatchNumber.setBounds(new Rectangle(20, 60, 100, 20));
		lblBatchNumber.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblBatchNumber.setText("Número do Lote");

		txtBatchNumber = new Text(grpBatchDetails, SWT.BORDER);
		txtBatchNumber.setBounds(new Rectangle(150, 59, 90, 20));
		txtBatchNumber.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		txtBatchNumber.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.character == SWT.CR) {
					btnSave.setFocus();
				}
			}
		});

		Label lblExpiryDate = new Label(grpBatchDetails, SWT.NONE);
		lblExpiryDate.setBounds(new Rectangle(20, 90, 90, 20));
		lblExpiryDate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblExpiryDate.setText("*Data de Validade");

		cmbExpiryMonth = new Combo(grpBatchDetails, SWT.BORDER);
		cmbExpiryMonth.setBounds(new Rectangle(150, 85, 106, 13));
		cmbExpiryMonth.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbExpiryMonth.setVisibleItemCount(13);
		cmbExpiryMonth.setBackground(ResourceUtils.getColor(iDartColor.WHITE));

		cmbExpiryMonth.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				String theText = cmbExpiryMonth.getText();

				if (theText.length() > 2) {

					String s = theText.substring(0, 1);
					String t = theText.substring(1, theText.length());
					theText = s.toUpperCase() + t;

					String[] items = cmbExpiryMonth.getItems();

					for (int i = 0; i < items.length; i++) {
						if (items[i].substring(0, 3).equalsIgnoreCase(theText)) {
							cmbExpiryMonth.setText(items[i]);
							cmbExpiryYear.setFocus();
						}
					}
				}

				if (e.character == SWT.CR) {
					btnSave.setFocus();
				}

			}
		});

		String months[] = { "January", "February", "March", "April", "May",
				"June", "July", "August", "September", "October", "November",
		"December" };
		
	
		
		
		for (int i = 0; i < 12; i++) {
			cmbExpiryMonth.add(months[i]);
		}

		cmbExpiryYear = new Combo(grpBatchDetails, SWT.BORDER);
		cmbExpiryYear.setBounds(new Rectangle(255, 85, 65, 20));
		cmbExpiryYear.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbExpiryYear.setVisibleItemCount(10);

		// get the current date
		Calendar rightNow = Calendar.getInstance();
		final int currentYear = rightNow.get(Calendar.YEAR);

		cmbExpiryYear.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String theText = cmbExpiryYear.getText();

				if ((cmbExpiryYear.indexOf(theText) == -1)
						&& (theText.length() >= 4)) {
					cmbExpiryYear.setText(String.valueOf(currentYear));
				}
			}
		});

		for (int i = 0; i < 50; i++) {
			cmbExpiryYear.add(Integer.toString(currentYear - 1 + i));
		}
		cmbExpiryYear.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		cmbExpiryYear.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.character == SWT.CR) {
					btnSave.setFocus();
				}
			}
		});

		cmbExpiryYear.setVisibleItemCount(15);

		// Units Received
		Label lblUnitsReceived = new Label(grpBatchDetails, SWT.NONE);
		lblUnitsReceived.setBounds(new Rectangle(20, 120, 100, 20));
		lblUnitsReceived.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblUnitsReceived.setText("*Unidades Recebidas");

		txtUnitsReceived = new Text(grpBatchDetails, SWT.BORDER);
		txtUnitsReceived.setBounds(new Rectangle(150, 119, 90, 20));
		txtUnitsReceived.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

		// put enter shortcut here
		txtUnitsReceived.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.character == SWT.CR) {
					btnSave.setFocus();
				}
			}
		});

		// Shelf Number
		Label lblShelfNo = new Label(grpBatchDetails, SWT.NONE);
		lblShelfNo.setBounds(new Rectangle(20, 150, 90, 20));
		lblShelfNo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblShelfNo.setText("Número da Prateleira");

		txtShelfNo = new Text(grpBatchDetails, SWT.BORDER);
		txtShelfNo.setBounds(new Rectangle(150, 149, 90, 20));
		txtShelfNo.setText("0");
		txtShelfNo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		txtShelfNo.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.character == SWT.CR) {
					btnSave.setFocus();
				}
			}
		});

		// Stock Center
		Label lblStockCenter = new Label(grpBatchDetails, SWT.NONE);
		lblStockCenter.setText("Farmácia");
		lblStockCenter.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblStockCenter.setBounds(new Rectangle(20, 180, 110, 20));
		cmbStockCenter = new CCombo(grpBatchDetails, SWT.BORDER | SWT.READ_ONLY);
		cmbStockCenter.setBounds(new Rectangle(150, 175, 220, 20));
		cmbStockCenter.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		cmbStockCenter.setBackground(ResourceUtils.getColor(iDartColor.WHITE));

		CommonObjects.populateStockCenters(getHSession(), cmbStockCenter);

		cmbStockCenter.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.character == SWT.CR) {
					btnSave.setFocus();
				}
			}
		});

		// Capture Date
		Label lblCaptureDate = new Label(grpBatchDetails, SWT.NONE);
		lblCaptureDate.setBounds(new Rectangle(20, 210, 84, 20));
		lblCaptureDate.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblCaptureDate.setText("Data de Entrada");

		btnArrivalDatePicker = new DateButton(grpBatchDetails,
				DateButton.ZERO_TIMESTAMP,
				null);
		btnArrivalDatePicker.setFont(ResourceUtils
				.getFont(iDartFont.VERASANS_8));
		btnArrivalDatePicker.setBounds(new Rectangle(150, 208, 210, 26));
		btnArrivalDatePicker.setDate(new Date());

		// Unit Price
		Label lblUnitPrice = new Label(grpBatchDetails, SWT.NONE);
		lblUnitPrice.setBounds(new Rectangle(20, 242, 84, 20));
		lblUnitPrice.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblUnitPrice.setText("Preço Por Unidade ");

		Label lblCurrency = new Label(grpBatchDetails, SWT.RIGHT);
		lblCurrency.setBounds(new Rectangle(110, 242, 23, 20));
		lblCurrency.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		lblCurrency.setText(iDartProperties.currency);

		txtUnitPrice = new Text(grpBatchDetails, SWT.BORDER);
		txtUnitPrice.setBounds(new Rectangle(150, 239, 90, 20));
		txtUnitPrice.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
		txtUnitPrice.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {

				if (e.character == SWT.CR) {
					btnSave.setFocus();
				}
			}
		});

	}

	/**
	 * This method initializes compButtons
	 * 
	 */
	@Override
	protected void createCompButtons() {
		buildCompButtons();
		btnSave.setText("Adicionar Este Medicamento");
		btnSave.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
	}

	/**
	 * This method loads information of the drug
	 * (stoResourceUtils.getColor(iDartColor.RED) in the global 'theDrug'
	 * variable)
	 * 
	 * @param drugName
	 *            String
	 */
	private void loadDrugDetails(String drugName) {
		// Load the item
		theDrug = DrugManager.getDrug(getHSession(), drugName);
		if (theDrug == null) {
			MessageBox feedback = new MessageBox(getShell(), SWT.OK);
			feedback
			.setMessage("O Medicamento '"
					.concat(drugName)
					.concat(
					"' não foi encontrado na base de dados. Por favor adiciona este medicamento na base de dados, usando a tela de  'Administração Geral'."));
			feedback.setText("Medicamento não Encontrado");
			feedback.open();

		} else {

			txtDrugName.setText(theDrug.getName());
			txtDescription.setText(theDrug.getForm().getForm());
			txtPackSize.setText(String.valueOf(theDrug.getPackSize()));
			txtUnitPrice.setText(getPreviousUnitPrice(theDrug));

			enableFields(true);

			String manu = getPreviousManufacturer(theDrug);
			if (manu == null) {
				txtManufacturer.setFocus();
			} else {
				txtBatchNumber.setFocus();
				txtManufacturer.setText(manu);
			}

		}
	}

	/**
	 * This method looks for a previously entered unit price. It does this by
	 * first looking in the current table and then by searching the database
	 * 
	 * @param drug
	 * @return
	 */
	private String getPreviousUnitPrice(Drug drug) {
		BigDecimal unitPrice = null;

		// first, check any unsaved stock batches the user has just added
		for (Stock s : parentStockScreen.getStockBatches()) {
			if ((s.getDrug().getId() == drug.getId())
					&& s.getStockCenter().getStockCenterName()
					.equalsIgnoreCase(cmbStockCenter.getText())
					&& (s.getUnitPrice() != null)) {
				unitPrice = s.getUnitPrice();
			}

		}

		// if not found, check the database
		if (unitPrice == null) {
			unitPrice = StockManager.getLastUnitPriceForDrug(getHSession(),
					drug, cmbStockCenter.getText());
		}

		String unitPriceStr = "";
		if (unitPrice != null) {
			try {
				NumberFormat currencyNumberFormatter = NumberFormat
				.getNumberInstance(iDartProperties.currentLocale);
				unitPriceStr = currencyNumberFormatter.format(unitPrice);

			} catch (NumberFormatException ne) {
				getLog().warn(
						"Autocomplete unit price found but could not format",
						ne);
				unitPriceStr = "";
			}
		}
		return unitPriceStr;
	}

	/**
	 * This method looks for a previously entered manufacturer. It does this by
	 * first looking in the current table and then by searching the database
	 * 
	 * @param drug
	 * @return
	 */
	private String getPreviousManufacturer(Drug drug) {
		String manufacturer = "";

		// first, check any unsaved stock batches the user has just added
		for (Stock s : parentStockScreen.getStockBatches()) {
			if ((s.getDrug().getId() == drug.getId())
					&& s.getStockCenter().getStockCenterName()
					.equalsIgnoreCase(cmbStockCenter.getText())
					&& (s.getManufacturer() != null)
					|| "".equalsIgnoreCase(s.getManufacturer())) {
				manufacturer = s.getManufacturer();
			}

		}

		// if not found, check the database
		if ("".equals(manufacturer)) {
			manufacturer = DrugManager.getLatestDrugManufacturer(getHSession(),
					drug.getName(), cmbStockCenter.getText());
		}
		return manufacturer;
	}

	/**
	 * Check if the form is completed before proceeding
	 * 
	 * @return true if all fields are correctly filled in
	 */
	@Override
	protected boolean fieldsOk() {
		boolean result = true;
		if (txtDrugName.getText().equals("")) {
			MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
			m
			.setMessage("Por favor, carregue o medicamento apresentado na lista de medicamentos (Utilize o botão 'Pesquisar medicamento').");
			m.setText("O campo  Nome do medicamento não pode ser vazio");
			m.open();
			txtDrugName.setFocus();
			result = false;
		} else if (cmbExpiryMonth.getText().equals("")) {
			MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
			m.setMessage("Por favor, introduza o mes de validade.");
			m.setText("O campo Mes de Validade não pode ser vazio");
			m.open();
			cmbExpiryMonth.setFocus();
			result = false;
		} else if (cmbExpiryYear.getText().equals("")) {
			MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
			m.setMessage("Por favor, introduza o ano de validade.");
			m.setText("O campo ano de Validade não pode ser vazio");
			m.open();
			cmbExpiryYear.setFocus();
			result = false;
		} else if (txtUnitsReceived.getText().equals("")) {
			MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
			m.setMessage("Por favor, introduza a quantidade recebida.");
			m.setText("O campo quantidade recebida não pode ser vazio");
			m.open();
			txtUnitsReceived.setFocus();
			result = false;
		} else if (!InputVerificationUtils
				.checkPositiveIntegerValue(txtUnitsReceived)) {
			MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
			m
			.setMessage("Introduza um valor maior que zero (Ex. '1000').");
			m.setText("Valor incorrecto no campo quantidade recebida");
			m.open();
			txtUnitsReceived.setText("");
			txtUnitsReceived.setFocus();
			result = false;
		} else if ((!txtUnitPrice.getText().trim().equals(""))
				&& (!InputVerificationUtils
						.checkPositiveNumericValue(txtUnitPrice))) {
			MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
			m
			.setMessage("TIntroduza um valor maior que zero (Ex. '5.00'). Caso nao queira especificar o valor, pode deixar este campo em branco.");
			m.setText("Valor incorrecto");
			m.open();
			txtUnitPrice.setText("");
			txtUnitPrice.setFocus();
			result = false;
		} else {
			result = checkExpiryDate();
		}

		return result;

	}

	/**
	 * This method is invoked when user presses cancel button It closes the
	 * newPatientAdmin
	 */
	@Override
	protected void cmdCancelWidgetSelected() {
		tableItem.dispose();
		fireChangeEvent("Cancel");
		closeShell(false);
	}

	@Override
	protected void cmdClearWidgetSelected() {
		clearForm();
	}

	/**
	 * Clears the patientForm and sets the default values
	 */
	@Override
	public void clearForm() {

		// txtBarcode.setText("");
		txtDrugName.setText("");
		txtManufacturer.setText("");
		txtDescription.setText("");
		txtPackSize.setText("");
		txtBatchNumber.setText("");
		cmbExpiryMonth.setText("");
		cmbExpiryYear.setText("");
		txtUnitsReceived.setText("");
		txtShelfNo.setText("");
		txtUnitPrice.setText("");
		enableFields(false);
		btnDrugSearch.setEnabled(true);
		btnArrivalDatePicker.setDate(new Date());

	}

	/*
	 * This method called a "Search" GUI. Upon successful selection from the
	 * user, the PackageManager loads the selected patient's information
	 */
	private void cmdSearchWidgetSelected() {

		Search drugSearch = new Search(getHSession(), getShell(),
				CommonObjects.DRUG);

		if (drugSearch.getValueSelected() != null) {

			btnDrugSearch.setEnabled(false);

			String manu = getPreviousManufacturer(DrugManager.getDrug(
					getHSession(), drugSearch.getValueSelected()[0]));
			if (manu == null) {
				txtManufacturer.setFocus();
			} else {
				txtManufacturer.setText(manu);
				txtBatchNumber.setFocus();
			}

			txtUnitPrice.setText(getPreviousUnitPrice(DrugManager.getDrug(
					getHSession(), drugSearch.getValueSelected()[0])));
			loadDrugDetails(drugSearch.getValueSelected()[0]);

		}
	}

	/**
	 * Method enableFields.
	 * 
	 * @param enable
	 *            boolean
	 */
	@Override
	protected void enableFields(boolean enable) {

		txtDescription.setEnabled(enable);
		txtPackSize.setEnabled(enable);
		txtManufacturer.setEnabled(enable);
		txtBatchNumber.setEnabled(enable);
		cmbExpiryMonth.setEnabled(enable);
		cmbExpiryYear.setEnabled(enable);
		txtUnitsReceived.setEnabled(enable);
		txtShelfNo.setEnabled(enable);
		txtUnitPrice.setEnabled(enable);

		cmbStockCenter.setBackground(ResourceUtils.getColor(iDartColor.WHITE));
		cmbStockCenter.setEnabled(enable);

		btnArrivalDatePicker.setEnabled(enable);

		cmbExpiryMonth.setBackground(enable ? ResourceUtils
				.getColor(iDartColor.WHITE) : ResourceUtils
				.getColor(iDartColor.GRAY));

		cmbExpiryYear.setBackground(enable ? ResourceUtils
				.getColor(iDartColor.WHITE) : ResourceUtils
				.getColor(iDartColor.GRAY));

		cmbStockCenter.setBackground(enable ? ResourceUtils
				.getColor(iDartColor.WHITE) : ResourceUtils
				.getColor(iDartColor.GRAY));

		btnSave.setEnabled(enable);

	}

	/**
	 * @param tableItem
	 *            The tableItem to set.
	 */
	public void setTableItem(TableItem tableItem) {
		this.tableItem = tableItem;
	}

	/**
	 * Method checkExpiryDate.
	 * 
	 * @return boolean
	 */
	private boolean checkExpiryDate() {

		boolean expiryDateOkay = false;
		boolean match = false;
		// checks that data is entered correctly
		for (int i = 0; i < cmbExpiryMonth.getItemCount(); i++) {
			if (cmbExpiryMonth.getText().equals(cmbExpiryMonth.getItem(i))) {
				match = true;
			}
		}

		if (!match) {
			MessageBox feedback = new MessageBox(getShell(), SWT.OK);
			feedback.setMessage("A data de Validade invalida.");
			feedback.setText("A data de Validade invalida.");
			feedback.open();
			expiryDateOkay = false;
			return expiryDateOkay;
		}

		else {

			Calendar expiryDate = Calendar.getInstance();

			try {
				expiryDate.setTime(new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
				.parse("01 " + cmbExpiryMonth.getText() + " "
						+ cmbExpiryYear.getText()));

				Calendar today = Calendar.getInstance();
				today.setTime(new Date());

				Calendar sixMonthsFromNow = Calendar.getInstance();
				sixMonthsFromNow.setTime(new Date());
				sixMonthsFromNow.add(Calendar.MONTH, 6);

				if (expiryDate.before(today)) {
					MessageBox m = new MessageBox(getShell(), SWT.ICON_QUESTION
							| SWT.YES | SWT.NO);
					m.setText("O Stock ja esta expirado");
					m
					.setMessage("Este stock ja esta expirado. Tem a certeza que pretende adicionar?");

					switch (m.open()) {

					case SWT.YES:
						expiryDateOkay = true;
						break;
					case SWT.NO:
						expiryDateOkay = false;
						cmbExpiryMonth.setFocus();
						break;
					}
				}

				else if (expiryDate.before(sixMonthsFromNow)) {
					MessageBox m = new MessageBox(getShell(), SWT.ICON_QUESTION
							| SWT.YES | SWT.NO);
					m.setText("Este Stock expira dentro de alguns meses");
					m
					.setMessage("Este stock expira dentro de 6 meses. Tem a certeza que pretende adicionar?");

					switch (m.open()) {

					case SWT.YES:
						expiryDateOkay = true;
						break;
					case SWT.NO:
						expiryDateOkay = false;
						cmbExpiryMonth.setFocus();
						break;
					}
				} else {
					expiryDateOkay = true;
				}
			} catch (ParseException p) {
				getLog().error("Expiry date did not parse.", p);
				MessageBox feedback = new MessageBox(getShell(), SWT.OK);
				feedback.setMessage("Expiry Date is invalid.");
				feedback.setText("Invalid Expiry Date");
				feedback.open();
				expiryDateOkay = false;

			}

			return expiryDateOkay;

		}
	}

	@Override
	protected void cmdSaveWidgetSelected() {
		if (fieldsOk()) {

			if (submitForm()) {
				closeShell(false);
			} else {
				MessageBox m = new MessageBox(getShell(), SWT.ICON_ERROR
						| SWT.OK);
				m.setText("Erro ao gravar a entrada do stock");
				m.setMessage("Erro ao gravar a entrada do stock. Tenta novamente.");
				clearForm();

			}
		}

	}

	/**
	 * Method submitForm.
	 * 
	 * @return boolean
	 */
	@Override
	protected boolean submitForm() {

		// create the table item
		Stock newStock = new Stock();
		tableItem.setData(newStock);

		newStock.setDrug(DrugManager.getDrug(getHSession(), txtDrugName
				.getText()));
		tableItem.setText(1, newStock.getDrug().getName());

		tableItem.setText(2, txtPackSize.getText());
		tableItem.setText(3, txtDescription.getText());

		newStock.setUnitsReceived(Integer.parseInt(txtUnitsReceived.getText()));
		tableItem.setText(4, newStock.getUnitsReceived() + "");

		newStock.setStockCenter(AdministrationManager.getStockCenter(
				getHSession(), cmbStockCenter.getText()));
		tableItem.setText(5, newStock.getStockCenter().getStockCenterName());

		newStock.setManufacturer(txtManufacturer.getText());
		tableItem.setText(6, newStock.getManufacturer());

		newStock.setBatchNumber(txtBatchNumber.getText());
		tableItem.setText(7, newStock.getBatchNumber());

		Date arrivalDate = btnArrivalDatePicker.getDate();
		newStock
		.setDateReceived(arrivalDate == null ? new Date() : arrivalDate);

		try {
			newStock.setExpiryDate(new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
			.parse("01 " + cmbExpiryMonth.getText() + " "
					+ cmbExpiryYear.getText()));
			tableItem.setText(8, new SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
			.format(newStock.getExpiryDate()));
		} catch (ParseException p) {
			getLog().warn("Parse Exception setting stock expiry date.", p);
			return false;
		}

		newStock.setShelfNumber(txtShelfNo.getText());
		tableItem.setText(9, newStock.getShelfNumber());

		newStock.setModified('T');
		newStock.setHasUnitsRemaining('T');

		try {
			Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
			newStock.setUnitPrice(new BigDecimal(unitPrice.doubleValue()));

			NumberFormat currencyFormatter = NumberFormat
			.getCurrencyInstance(iDartProperties.currentLocale);
			tableItem.setText(10, currencyFormatter.format(unitPrice));

		} catch (NumberFormatException ne) {
			getLog().debug("unit price is null");
			tableItem.setText(10, "");
			newStock.setUnitPrice(null);

		}
		return true;
	}

	/**
	 * Method addChangeListener.
	 * 
	 * @param listener
	 *            iDARTChangeListener
	 */
	public void addChangeListener(iDARTChangeListener listener) {
		this.changeListener = listener;
	}

	/**
	 * Method fireChangeEvent.
	 * 
	 * @param o
	 *            Object
	 */
	private void fireChangeEvent(Object o) {
		if (changeListener != null) {
			changeListener.changed(o);
		}
	}

	@Override
	protected void setLogger() {
		setLog(Logger.getLogger(this.getClass()));
	}
}
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
package org.celllife.idart.gui.reportParameters;

import model.manager.AdministrationManager;
import model.manager.reports.LinhaTerapeutica;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.database.hibernate.StockCenter;
import org.celllife.idart.gui.platform.GenericReportGui;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartColor;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

/**
 */
public class RegimeTerapeuticoReport extends GenericReportGui {

    private Group grpDateRange;

    private Group grpPharmacySelection;

    private CCombo cmbStockCenter;

    /**
     * Constructor
     *
     * @param parent Shell
     * @param activate boolean
     */
    public RegimeTerapeuticoReport(Shell parent, boolean activate) {
        super(parent, REPORTTYPE_MIA, activate);
    }

    /**
     * This method initializes newMonthlyStockOverview
     */
    @Override
    protected void createShell() {
        Rectangle bounds = new Rectangle(100, 50, 600, 510);
        buildShell(REPORT_LINHAS_TERAPEUTICAS, bounds);
        // create the composites
        createMyGroups();
    }

    private void createMyGroups() {
        createGrpClinicSelection();

    }

    /**
     * This method initializes compHeader
     *
     */
    @Override
    protected void createCompHeader() {
        iDartImage icoImage = iDartImage.REPORT_STOCKCONTROLPERCLINIC;
        buildCompdHeader("Linhas Terapeuticas", icoImage);
    }

    /**
     * This method initializes grpClinicSelection
     *
     */
    private void createGrpClinicSelection() {

        grpPharmacySelection = new Group(getShell(), SWT.NONE);
        grpPharmacySelection.setText("Farmácia");
        grpPharmacySelection.setFont(ResourceUtils
                .getFont(iDartFont.VERASANS_8));
        grpPharmacySelection.setBounds(new org.eclipse.swt.graphics.Rectangle(
                140, 90, 320, 65));

        Label lblPharmacy = new Label(grpPharmacySelection, SWT.NONE);
        lblPharmacy.setBounds(new Rectangle(10, 25, 140, 20));
        lblPharmacy.setText("Selecione a farmácia");
        lblPharmacy.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        cmbStockCenter = new CCombo(grpPharmacySelection, SWT.BORDER);
        cmbStockCenter.setBounds(new Rectangle(156, 24, 160, 20));
        cmbStockCenter.setEditable(false);
        cmbStockCenter.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        cmbStockCenter.setBackground(ResourceUtils.getColor(iDartColor.WHITE));

        CommonObjects.populateStockCenters(getHSession(), cmbStockCenter);

    }


    /**
     * This method initializes compButtons
     *
     */
    @Override
    protected void createCompButtons() {
    }

    @SuppressWarnings("unused")
    @Override
    protected void cmdViewReportWidgetSelected() {

        StockCenter pharm = AdministrationManager.getStockCenter(getHSession(),
                cmbStockCenter.getText());

        if (cmbStockCenter.getText().equals("")) {

            MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
                    | SWT.OK);
            missing.setText("No Pharmacy Was Selected");
            missing
                    .setMessage("No pharmacy was selected. Please select a pharmacy by looking through the list of available pharmacies.");
            missing.open();

        } else if (pharm == null) {

            MessageBox missing = new MessageBox(getShell(), SWT.ICON_ERROR
                    | SWT.OK);
            missing.setText("Pharmacy not found");
            missing
                    .setMessage("There is no pharmacy called '"
                            + cmbStockCenter.getText()
                            + "' in the database. Please select a pharmacy by looking through the list of available pharmacies.");
            missing.open();

        }else {
            try {

                //theStartDate = sdf.parse(strTheDate);
                LinhaTerapeutica report = new LinhaTerapeutica(
                        getShell());
        
                viewReport(report);
            } catch (Exception e) {
                getLog()
                        .error(
                                "Exception while running Monthly Receipts and Issues report",
                                e);
           
            }
        }

    }

    @Override
    protected void cmdViewReportXlsWidgetSelected() {

    }

    /**
     * This method is called when the user presses "Close" button
     *
     */
    @Override
    protected void cmdCloseWidgetSelected() {
        cmdCloseSelected();
    }

    @Override
    protected void setLogger() {
        setLog(Logger.getLogger(this.getClass()));
    }


}

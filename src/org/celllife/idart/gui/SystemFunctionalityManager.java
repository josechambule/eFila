package org.celllife.idart.gui;

import model.manager.AdministrationManager;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.database.hibernate.SystemFunctionality;
import org.celllife.idart.database.hibernate.util.HibernateUtil;
import org.celllife.idart.gui.platform.GenericFormGui;
import org.celllife.idart.gui.search.Search;
import org.celllife.idart.gui.utils.ResourceUtils;
import org.celllife.idart.gui.utils.iDartFont;
import org.celllife.idart.gui.utils.iDartImage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;
import org.hibernate.HibernateException;
import org.hibernate.Transaction;

public class SystemFunctionalityManager extends GenericFormGui {

    private Group grpFunctionalityInfo;

    private Text txtFuncionality;
    private Text txtCodigo;
    private Button btnSearch;

    private SystemFunctionality currFunctionality;

    /**
     * Constructor for GenericFormGui.
     *
     * @param parent   Shell
     */
    public SystemFunctionalityManager(Shell parent) {
        super(parent, HibernateUtil.getNewSession());
    }

    @Override
    protected void clearForm() {
        txtFuncionality.setText("");
        txtCodigo.setText("");
        this.currFunctionality = null;
        btnSearch.setEnabled(true);
    }

    @Override
    protected boolean submitForm() {
        return false;
    }

    @Override
    protected boolean fieldsOk() {
        return (txtFuncionality.getText() != null && txtFuncionality.getText().length() > 2) &&
                (txtCodigo.getText() != null && txtCodigo.getText().length() > 2);
    }

    @Override
    protected void createCompHeader() {
        String headerTxt = ("Gestão de funcionalidades do sistema");
        iDartImage icoImage = iDartImage.PHARMACYUSER;
        buildCompHeader(headerTxt, icoImage);
    }

    @Override
    protected void createCompButtons() {
        buildCompButtons();
    }

    @Override
    protected void cmdSaveWidgetSelected() {
        if (fieldsOk()) {

            //First we check access selected
            int option = SWT.YES;


            MessageBox msg = new MessageBox(getShell(), SWT.YES | SWT.NO | SWT.ICON_QUESTION);
            msg.setText("Adicção de nova funcionalidade");
            msg.setMessage("Tem certeza de que deseja adicionar a funcionalidade ["+txtFuncionality.getText()+"] ao sistema?");
            option = msg.open();

            if(option == SWT.YES)
            {
                Transaction tx = null;

                try {
                    tx = getHSession().beginTransaction();

                    if (this.currFunctionality == null || this.currFunctionality.getId() <= 0) {
                        this.initNewRecord();
                    }else {
                        this.updateRecord();
                    }
                    AdministrationManager.saveSystemFuntionality(getHSession(), currFunctionality);

                    getHSession().flush();
                    tx.commit();
                    MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_INFORMATION);
                    m.setText("Nova funcionalidade gravada");
                    m.setMessage("A funcionalidade '".concat(currFunctionality.getDescription()).concat( "' foi gravada com sucesso."));
                    m.open();
                    cmdCancelWidgetSelected();

                }

                catch (HibernateException he) {
                    if (tx != null) {
                        tx.rollback();
                    }
                    MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
                    m.setText("Problem Saving To Database");
                    m.setMessage("A funcionalidade não foi gravada. Por favor, tente novamente");
                    m.open();
                    getLog().error(he);
                }
            }
        }else {
            MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
            m.setText("Preenchimento dos campos");
            m.setMessage("Os campos [Perfil] e [Codigo] devem estar preenchidos.");
            m.open();
        }

    }

    private void updateRecord() {
        this.currFunctionality.setDescription(txtFuncionality.getText());
        this.currFunctionality.setCode(txtCodigo.getText());
    }

    @Override
    protected void cmdClearWidgetSelected() {
        clearForm();
    }

    @Override
    protected void cmdCancelWidgetSelected() {
        this.currFunctionality = null;
        cmdCloseSelected();
    }

    @Override
    protected void enableFields(boolean enable) {
        txtFuncionality.setEnabled(enable);
        txtCodigo.setEnabled(enable);
    }

    @Override
    protected void createContents() {
        createGrpSysFunctionalityInfo();
    }

    @Override
    protected void setLogger() {
        Logger log = Logger.getLogger(this.getClass());
        setLog(log);
    }

    @Override
    protected void createShell() {
        Rectangle bounds = new Rectangle(25, 0, 800, 300);
        buildShell("Gestão de funcionalidades do sistema", bounds);
    }

    private void initNewRecord(){
        currFunctionality = new SystemFunctionality(txtFuncionality.getText(), txtCodigo.getText());
    }
    private void createGrpSysFunctionalityInfo() {

        if (grpFunctionalityInfo != null) {
            grpFunctionalityInfo.dispose();
        }
        // grpUserInfo
        grpFunctionalityInfo = new Group(getShell(), SWT.NONE);
        grpFunctionalityInfo.setBounds(new Rectangle(100, 100, 600, 100));



        // lblUser & txtUser
        Label lblFunctionality = new Label(grpFunctionalityInfo, SWT.NONE);
        lblFunctionality.setBounds(new Rectangle(30, 40, 90, 20));
        lblFunctionality.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblFunctionality.setText("* Funcionalidade:");
        txtFuncionality = new Text(grpFunctionalityInfo, SWT.BORDER);
        txtFuncionality.setBounds(new Rectangle(150, 40, 130, 20));
        txtFuncionality.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        /*txtRole.setText(localUser.getUsername());
        txtRole.setEditable(false);
        txtRole.setEnabled(false);*/

        Label lblCodigo= new Label(grpFunctionalityInfo, SWT.NONE);
        lblCodigo.setBounds(new Rectangle(30, 70, 90, 20));
        lblCodigo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblCodigo.setText("* Código:");
        txtCodigo= new Text(grpFunctionalityInfo, SWT.BORDER);
        txtCodigo.setBounds(new Rectangle(150, 70, 130, 20));
        txtCodigo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        btnSearch = new Button(grpFunctionalityInfo, SWT.NONE);
        btnSearch.setBounds(new org.eclipse.swt.graphics.Rectangle(300, 38, 120, 30));
        btnSearch.setToolTipText("Pressione este botão para procurar uma funcionalidade.");
        btnSearch.setText("Procurar"); //$NON-NLS-1$
        btnSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(
                    org.eclipse.swt.events.SelectionEvent e) {
                cmdSearchWidgetSelected();
            }
        });


    }

    private void cmdSearchWidgetSelected() {

        Search sysFunctionalitySearch = new Search(getHSession(), getShell(), CommonObjects.FUNCTIONALITY);

        if (sysFunctionalitySearch.getValueSelected() != null) {

            currFunctionality = AdministrationManager.getFunctionalityByDescription(getHSession(), sysFunctionalitySearch.getValueSelected()[0]);

            txtFuncionality.setText(currFunctionality.getDescription());
            txtCodigo.setText(currFunctionality.getCode());

            btnSearch.setEnabled(false);

            enableFields(true);

            btnSave.setEnabled(true);

        } else {
            btnSearch.setEnabled(true);

        }

    }
}

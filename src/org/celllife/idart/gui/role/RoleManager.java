package org.celllife.idart.gui.role;

import model.manager.AdministrationManager;
import org.apache.log4j.Logger;
import org.celllife.idart.commonobjects.CommonObjects;
import org.celllife.idart.commonobjects.LocalObjects;
import org.celllife.idart.database.hibernate.Role;
import org.celllife.idart.database.hibernate.SystemFunctionality;
import org.celllife.idart.database.hibernate.User;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RoleManager extends GenericFormGui {

    private Text txtRole;

    private Button btnProcurar;

    private Table tblFunctionalities;

    private Text txtCodigo;

    private Role currentRole;

    private User localUser;

    private Group grpRoleInfo;

    private SystemFunctionality currentFunctionality;

    private Button btnSearch;

    private Set<SystemFunctionality> currFunctionalities;
    /**
     * Constructor for GenericFormGui.
     *
     * @param parent   Shell
     */
    public RoleManager(Shell parent) {
        super(parent, HibernateUtil.getNewSession());
    }

    @Override
    protected void clearForm() {
        txtRole.setText("");
        txtCodigo.setText("");
        this.currentRole = null;
        btnSearch.setEnabled(true);

        for (TableItem ti : tblFunctionalities.getItems()) {
            ti.setChecked(false);
        }
        currFunctionalities = new HashSet<>();
    }

    @Override
    protected boolean submitForm() {
        return false;
    }

    private void loadSelectedFuncionalities(){
        currFunctionalities = new HashSet<>();
        for (TableItem ti : tblFunctionalities.getItems()) {
            if (ti.getChecked()) {
                currFunctionalities.add((SystemFunctionality) ti.getData());
            }
        }
    }

    @Override
    protected boolean fieldsOk() {

        boolean checkedClinic = false;
        for (TableItem ti : tblFunctionalities.getItems()) {
            if (ti.getChecked()) {
                checkedClinic = true;
                break;
            }
        }

        if (!checkedClinic) {
            MessageBox b = new MessageBox(getShell(), SWT.ICON_ERROR | SWT.OK);
            b.setMessage("Todos os perfies precisam ter acesso a pelo menos uma funcionalidade associada. \n\n"
                    + "Por favor, selecione pelo menos uma funcionalidade e tente salvar novamente.");
            b.setText("Nenhuma funcionalidade selecionada");

            b.open();
            return false;
        }

        return (txtRole.getText() != null && txtRole.getText().length() > 2) &&
                (txtCodigo.getText() != null && txtCodigo.getText().length() > 2);
    }

    @Override
    protected void createCompHeader() {
        String headerTxt = ("Gestão de perfis");
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
            msg.setText("Adicção de novo perfil");
            msg.setMessage("Tem certeza de que deseja adicionar o perfil ["+txtRole.getText()+"] ao sistema?");
            option = msg.open();

            if(option == SWT.YES)
            {
                Transaction tx = null;

                try {
                    tx = getHSession().beginTransaction();

                    loadSelectedFuncionalities();

                    if (this.currentRole == null || this.currentRole.getId() <= 0) {
                        this.initNewRecord();
                    }else {
                        this.updateRecord();
                    }
                    AdministrationManager.saveRole(getHSession(), currentRole);

                    getHSession().flush();
                    tx.commit();
                    MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_INFORMATION);
                    m.setText("Novo perfil gravado");
                    m.setMessage("O perfil'".concat(currentRole.getDescription()).concat( "' foi gravado com sucesso."));
                    m.open();
                    cmdCancelWidgetSelected();

                }

                catch (HibernateException he) {
                    if (tx != null) {
                        tx.rollback();
                    }
                    MessageBox m = new MessageBox(getShell(), SWT.OK | SWT.ICON_WARNING);
                    m.setText("Problem Saving To Database");
                    m.setMessage("O registo não foi gravado. Por favor, tente novamente");
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
        this.currentRole.setDescription(txtRole.getText());
        this.currentRole.setCode(txtCodigo.getText());
        this.currentRole.setSysFunctions(this.currFunctionalities);
    }

    private void initNewRecord() {
        this.currentRole = new Role(txtRole.getText(), txtCodigo.getText(), this.currFunctionalities);
    }

    @Override
    protected void cmdClearWidgetSelected() {
        clearForm();

    }

    @Override
    protected void cmdCancelWidgetSelected() {
        this.currentRole = null;
        cmdCloseSelected();
    }

    @Override
    protected void enableFields(boolean enable) {
        txtRole.setEnabled(enable);
        txtCodigo.setEnabled(enable);
    }

    @Override
    protected void createContents() {
        localUser = LocalObjects.getUser(getHSession());
        createCompInstructions();
        createGrpRoleInfo();
    }

    private void createGrpRoleInfo() {

        if (grpRoleInfo != null) {
            grpRoleInfo.dispose();
        }
        // grpUserInfo
        grpRoleInfo = new Group(getShell(), SWT.NONE);
        grpRoleInfo.setBounds(new Rectangle(100, 110, 600, 280));



            // lblUser & txtUser
        Label lblRole = new Label(grpRoleInfo, SWT.NONE);
        lblRole.setBounds(new Rectangle(30, 20, 50, 20));
        lblRole.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblRole.setText("* Perfil:");
        txtRole = new Text(grpRoleInfo, SWT.BORDER);
        txtRole.setBounds(new Rectangle(80, 20, 130, 20));
        txtRole.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        Label lblCodigo= new Label(grpRoleInfo, SWT.NONE);
        lblCodigo.setBounds(new Rectangle(30, 50, 50, 20));
        lblCodigo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblCodigo.setText("* Código:");
        txtCodigo = new Text(grpRoleInfo, SWT.BORDER);
        txtCodigo.setBounds(new Rectangle(80, 50, 130, 20));
        txtCodigo.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        btnSearch = new Button(grpRoleInfo, SWT.NONE);
        btnSearch.setBounds(new org.eclipse.swt.graphics.Rectangle(220, 18, 110, 30));
        btnSearch.setToolTipText("Pressione este botão para procurar um perfil.");
        btnSearch.setText("Procurar"); //$NON-NLS-1$
        btnSearch.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        btnSearch.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {
            @Override
            public void widgetSelected(
                    org.eclipse.swt.events.SelectionEvent e) {
                cmdSearchWidgetSelected();
            }
        });


        Label lblFuncionalities = new Label(grpRoleInfo, SWT.BORDER);
        lblFuncionalities.setBounds(new Rectangle(370, 20, 200, 20));

        lblFuncionalities.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));
        lblFuncionalities.setText("Funcionalidades");

        lblFuncionalities.setAlignment(SWT.CENTER);

        tblFunctionalities = new Table(grpRoleInfo, SWT.CHECK | SWT.BORDER | SWT.FULL_SELECTION);
        tblFunctionalities.setBounds(370, 40, 200, 220);
        tblFunctionalities.setFont(ResourceUtils.getFont(iDartFont.VERASANS_8));

        TableColumn tblColClinicName = new TableColumn(tblFunctionalities, SWT.NONE);
        tblColClinicName.setText("Funcionalidade");
        tblColClinicName.setWidth(195);
        populateClinicAccessList();

    }

    private void cmdSearchWidgetSelected() {

        Search roleSearch = new Search(getHSession(), getShell(), CommonObjects.ROLE);

        if (roleSearch.getValueSelected() != null) {

            currentRole = AdministrationManager.getRoleByDescription(getHSession(), roleSearch.getValueSelected()[0]);

            txtRole.setText(currentRole.getDescription());
            txtCodigo.setText(currentRole.getCode());
            btnSearch.setEnabled(false);

            for (SystemFunctionality functionality : currentRole.getSysFunctions()){
                for (TableItem ti : tblFunctionalities.getItems()) {
                    if (functionality.equals(ti.getData())){
                        ti.setChecked(true);
                    }
                }
            }


            enableFields(true);

            btnSave.setEnabled(true);

        } else {
            btnSearch.setEnabled(true);

        }

    }

    private void populateClinicAccessList() {
        for (SystemFunctionality functionality : AdministrationManager.getSystemFunctionalities(getHSession())) {
            TableItem ti = new TableItem(tblFunctionalities, SWT.None);
            ti.setText(0, functionality.getDescription());
            ti.setData(functionality);

            /*if ((!isAddNotUpdate) && localUser.getClinics().contains(clinic)) {
                ti.setChecked(true);
            } */

        }
    }

    private void createCompInstructions() {
        Composite compInstructions = new Composite(getShell(), SWT.NONE);
        compInstructions.setLayout(null);
        compInstructions.setBounds(new Rectangle(200, 79, 530, 25));

        Label lblInstructions = new Label(compInstructions, SWT.CENTER);
        lblInstructions.setBounds(new Rectangle(0, 0, 600, 25));
        lblInstructions.setText("Todos campos com * são de preenchimento obrigatório");
        lblInstructions.setFont(ResourceUtils.getFont(iDartFont.VERASANS_10_ITALIC));
    }

    @Override
    protected void setLogger() {
        Logger log = Logger.getLogger(this.getClass());
        setLog(log);
    }

    @Override
    protected void createShell() {
        Rectangle bounds = new Rectangle(25, 0, 800, 500);
        buildShell("Gestão de perfies", bounds);
    }
}

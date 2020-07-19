package model.manager.reports;

import model.manager.excel.conversion.exceptions.ReportException;

import org.eclipse.swt.widgets.Shell;

import java.util.HashMap;
import java.util.Map;

public class FarmaciasRegistadas extends AbstractJasperReport {

    public FarmaciasRegistadas(Shell parent) {
        super(parent);
    }

    @Override
    protected void generateData() throws ReportException {

    }

    @Override
    protected String getReportFileName() {
        return "FarmaciasRegistadas";
    }

    @Override
    protected Map<String, Object> getParameterMap() throws ReportException {

        // Set the parameters for the report
        Map<String, Object> map = new HashMap<>();

        map.put("path", getReportPath());

        return map;
    }

}

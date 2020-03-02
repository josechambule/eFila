package org.celllife.idart.commonobjects;

import org.apache.log4j.Logger;
import org.celllife.idart.misc.PatientBarcodeParser;
import org.celllife.idart.misc.PropertiesEncrypter;
import org.celllife.idart.misc.iDARTRuntimeException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.Properties;

public class PrinterProperties {

    public static String labelprintWidth="216.0";
    public static String labelprintHeight="135.0";
    public static String labelBarCodeScalax="0.7";
    public static String labelBarCodeScalay="1";
    public static String labelprintPageFormat="1";
    public static final String FILE = "printer.properties";

    /**
     */
    public enum LabelType {
        EKAPA, IDART
    }

    public static LabelType labelType = LabelType.IDART;

    private static Logger log = null;

    private static Properties loadedProperties;

    /**
     * private constructor to prevent instantiation
     */
    private PrinterProperties() {
    }

    public static void setPrinterProperties() throws UnsupportedEncodingException, FileNotFoundException, IOException {
        log = Logger.getLogger(PrinterProperties.class);
        log.info("Loading Encrypted System Properties");

        PropertiesEncrypter pe = new PropertiesEncrypter();
        try {
            pe.loadPropertiesFromFile(FILE);
        } catch (IOException e) {
            throw new iDARTRuntimeException("Failed to load properties");
        }
        pe.decryptProperties();
        loadedProperties = pe.getProperties();

        labelprintPageFormat = setStringProperty("labelprintPageFormat");
        labelprintWidth = setStringProperty("labelprintWidth");
        labelprintHeight = setStringProperty("labelprintHeight");
        labelBarCodeScalax = setStringProperty("labelBarCodeScalax");
        labelBarCodeScalay = setStringProperty("labelBarCodeScalay");

        String labelTypeString = setStringProperty("labelType");
        // Should paeds labels have quantities blank by default?
        if (labelTypeString != null) {
            if (labelTypeString.equalsIgnoreCase("idart")) {
                labelType = PrinterProperties.LabelType.IDART;
            } else if (labelTypeString.equalsIgnoreCase("ekapa")) {
                labelType = PrinterProperties.LabelType.EKAPA;
            }
        }

    }

    public static String getPropertiesString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        sb.append("labelprintPageFormat=" + labelprintPageFormat);
        sb.append("\n");
        sb.append("labelprintWidth=" + labelprintWidth);
        sb.append("\n");
        sb.append("labelprintHeight=" + labelprintHeight);
        sb.append("\n");
        sb.append("labelBarCodeScalax=" + labelBarCodeScalax);
        sb.append("\n");
        sb.append("labelBarCodeScalay=" + labelBarCodeScalay);
        sb.append("\n");

        switch (labelType) {
            case EKAPA:
                sb.append("labelType=ekapa");
                break;
            case IDART:
                sb.append("labelType=idart");
                break;
        }
        return sb.toString();
    }


    /**
     * Method setStringProperty.
     *
     * @param propertyName
     *            String
     * @return String
     */
    private static String setStringProperty(String propertyName) {
        String theSetting = loadedProperties.getProperty(propertyName);
        if (theSetting != null)
            return theSetting.trim();
        else {
            log.warn("Property: " + propertyName + " is null");
            return "";
        }
    }

    /**
     * Generates a string of name value pairs for the fields in this class.
     *
     * @return String listing all the values of the properties in this class.
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    public static String printProperties() throws IllegalArgumentException,
            IllegalAccessException, ClassNotFoundException {
        Field[] fields = PrinterProperties.class.getDeclaredFields();
        StringBuffer props = new StringBuffer();
        for (int i = 0; i < fields.length; i++) {
            props.append(fields[i].getName());
            props.append(" : '");
            Object value = fields[i].get(Class.forName(PrinterProperties.class
                    .getName()));
            String val = "";
            if (value != null) {
                val = value.getClass().cast(value).toString();
            }
            props.append(val);
            props.append("'\n");
        }
        return props.toString();
    }

}

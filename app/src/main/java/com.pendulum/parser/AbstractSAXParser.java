/*
 *
 *  Proprietary and confidential. Property of Kellton Tech Solutions Ltd. Do not disclose or distribute.
 *  You must have written permission from Kellton Tech Solutions Ltd. to use this code.
 *
 */

package com.pendulum.parser;

import com.taxi.model.IModel;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public abstract class AbstractSAXParser extends DefaultHandler implements IParser<IModel> {
    protected StringBuffer sb = null;
    protected String parent;

    protected SAXParserFactory factory;
    protected SAXParser saxParser;

    public void init() throws ParserConfigurationException, SAXException {
        factory = SAXParserFactory.newInstance();
        saxParser = factory.newSAXParser();
    }

    // abstract methods
    public abstract void onStartElement(String namespaceURI, String localName, String qName, Attributes atts)
            throws SAXException, IllegalArgumentException;

    public abstract void onEndElement(String namespaceURI, String localName, String qName) throws SAXException;

    // public methods.
    public void warning(SAXParseException e) {
        System.err.println("warning:" + e.getMessage());
    }

    public void error(SAXParseException e) {
        System.err.println("error:" + e.getMessage());
    }

    public void fatalError(SAXParseException e) {
        System.err.println("fatalError:" + e.getMessage());
    }

    public void startDocument() throws SAXException {
        sb = new StringBuffer();
        parent = "";
    }

    public void endDocument() throws SAXException {
        super.endDocument();
    }

    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException,
            IllegalArgumentException {

        // call to abstract method
        onStartElement(namespaceURI, localName, qName, atts);

        sb = new StringBuffer();

        // Keep the trace of the parent nodes
        if (parent.length() > 0) {
            parent = parent.concat(",");
        }
        parent = parent.concat(localName);
    }

    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        // remove the end element from stack of parent elements
        int index = parent.lastIndexOf(',');
        if (index > 0) {
            parent = parent.substring(0, index);
        }
        onEndElement(namespaceURI, localName, qName);
    }

    public void characters(char ch[], int start, int length) {
        String theString = new String(ch, start, length);
        sb.append(theString);
    }

    /**
     * ************************* Helper Methods ****************************************
     */

    public String getNodeValue() {
        return sb.toString();
    }

    protected boolean getBoolean(String value) throws IllegalArgumentException {
        value = value.trim();

        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("yes"))
            return true;
        else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("0") || value.equalsIgnoreCase("no"))
            return false;
        else
            throw new IllegalArgumentException(value + " is not a valid boolean value");

    }

    protected boolean getBoolean(String value, boolean optValue) {
        if (value == null) {
            return optValue;
        }
        value = value.trim();
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1") || value.equalsIgnoreCase("yes"))
            return true;
        else if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("0") || value.equalsIgnoreCase("no"))
            return false;
        else
            return optValue;

    }

    protected long getLong(String value) throws IllegalArgumentException {
        try {
            long lg = Long.parseLong(value);
            return lg;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(value + " is not a valid long value");
        }
    }

    protected long getLong(String value, long optValue) {
        if (value == null) {
            return optValue;
        }
        value = value.trim();

        try {
            long lg = Long.parseLong(value);
            return lg;
        } catch (NumberFormatException ex) {
            return optValue;
        }
    }

    protected float getFloat(String value) throws IllegalArgumentException {
        value = value.trim();

        try {
            float lg = Float.parseFloat(value);
            return lg;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(value + " is not a valid float value");
        }
    }

    protected float getFloat(String value, float optValue) {
        value = value.trim();
        if (value == null || value.length() < 0) {
            return optValue;
        }
        try {
            float lg = Float.parseFloat(value);
            return lg;
        } catch (NumberFormatException ex) {
            return optValue;
        }
    }

    protected double getDouble(String value) throws IllegalArgumentException {
        value = value.trim();

        try {
            double lg = Double.parseDouble(value);
            return lg;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(value + " is not a valid double value");
        }
    }

    protected double getDouble(String value, double optValue) {
        if (value == null || value.length() < 0) {
            return optValue;
        }
        value = value.trim();
        try {
            double lg = Double.parseDouble(value);
            return lg;
        } catch (NumberFormatException ex) {
            return optValue;
        }
    }

    protected int getInt(String value) throws IllegalArgumentException {
        value = value.trim();

        try {
            int it = Integer.parseInt(value);
            return it;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(value + " is not a valid int value");
        }
    }

    protected int getInt(String value, int optValue) {
        value = value.trim();
        if (value == null || value.length() < 0) {
            return optValue;
        }
        try {
            int it = Integer.parseInt(value);
            return it;
        } catch (NumberFormatException ex) {
            return optValue;
        }
    }
}

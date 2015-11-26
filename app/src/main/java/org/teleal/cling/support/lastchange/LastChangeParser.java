/*
 * Copyright (C) 2010 Teleal GmbH, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.teleal.cling.support.lastchange;

import org.teleal.cling.model.XMLUtil;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.support.shared.AbstractMap;
import org.teleal.common.io.IO;
import org.teleal.common.util.Exceptions;
import org.teleal.common.xml.SAXParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.teleal.cling.model.XMLUtil.appendNewElement;

/**
 * Reads and writes the "LastChange" XML content.
 * <p>
 * Validates against a schema if the {@link #getSchemaSources()} method
 * doesn't return <code>null</code>.
 * </p>
 *
 * @author Christian Bauer
 */
public abstract class LastChangeParser extends SAXParser {

    final private static Logger log = Logger.getLogger(LastChangeParser.class.getName());

    public enum CONSTANTS {
        Event,
        InstanceID,
        val;

        public boolean equals(String s) {
            return this.name().equals(s);
        }
    }

    abstract protected String getNamespace();

    protected Set<Class<? extends EventedValue>> getEventedVariables() {
        return Collections.EMPTY_SET;
    }

    protected EventedValue createValue(String name, Map.Entry<String, String>[] attributes) throws Exception {
        for (Class<? extends EventedValue> evType : getEventedVariables()) {
            if (evType.getSimpleName().equals(name)) {
                Constructor<? extends EventedValue> ctor = evType.getConstructor(Map.Entry[].class);
                return ctor.newInstance(new Object[]{attributes});
            }
        }
        return null;
    }

    /**
     * Uses the current thread's context classloader to read and unmarshall the given resource.
     *
     * @param resource The resource on the classpath.
     * @return The unmarshalled Event model.
     * @throws Exception
     */
    public Event parseResource(String resource) throws Exception {
        InputStream is = null;
        try {
            is = Thread.currentThread().getContextClassLoader().getResourceAsStream(resource);
            return parse(IO.readLines(is));
        } finally {
            if (is != null) is.close();
        }
    }

    public Event parse(String xml) throws Exception {

        if (xml == null || xml.length() == 0) {
            throw new RuntimeException("Null or empty XML");
        }

        Event event = new Event();
        new RootHandler(event, this);

        log.fine("Parsing 'LastChange' event XML content");
        parse(new InputSource(new StringReader(xml)));

        log.fine("Parsed event with instances IDs: " + event.getInstanceIDs().size());
        if (log.isLoggable(Level.FINEST)) {
            for (InstanceID instanceID : event.getInstanceIDs()) {
                log.finest("InstanceID '" + instanceID.getId() + "' has values: " + instanceID.getValues().size());
                for (EventedValue eventedValue : instanceID.getValues()) {
                    log.finest(eventedValue.getName() + " => " + eventedValue.getValue());
                }
            }
        }

        return event;
    }

    class RootHandler extends SAXParser.Handler<Event> {

        RootHandler(Event instance, SAXParser parser) {
            super(instance, parser);
        }

        RootHandler(Event instance) {
            super(instance);
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if (CONSTANTS.InstanceID.equals(localName)) {
                String valAttr = attributes.getValue(CONSTANTS.val.name());
                if (valAttr != null) {
                    InstanceID instanceID = new InstanceID(new UnsignedIntegerFourBytes(valAttr));
                    getInstance().getInstanceIDs().add(instanceID);
                    new InstanceIDHandler(instanceID, this);
                }
            }
        }
    }

    class InstanceIDHandler extends SAXParser.Handler<InstanceID> {

        InstanceIDHandler(InstanceID instance, SAXParser.Handler parent) {
            super(instance, parent);
        }

        @Override
        public void startElement(String uri, String localName, String qName, final Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            Map.Entry[] attributeMap = new Map.Entry[attributes.getLength()];
            for (int i = 0; i < attributeMap.length; i++) {
                attributeMap[i] =
                        new AbstractMap.SimpleEntry<String, String>(
                                attributes.getLocalName(i),
                                attributes.getValue(i)
                        );
            }
            try {
                EventedValue esv = createValue(localName, attributeMap);
                if (esv != null)
                    getInstance().getValues().add(esv);
            } catch (Exception ex) {
                // Don't exit, just log a warning
                log.warning("Error reading event XML, ignoring value: " + Exceptions.unwrap(ex));
            }
        }

        @Override
        protected boolean isLastElement(String uri, String localName, String qName) {
            return CONSTANTS.InstanceID.equals(localName);
        }
    }

    public String generate(Event event) throws Exception {
        return XMLUtil.documentToFragmentString(buildDOM(event));
    }

    protected Document buildDOM(Event event) throws Exception {

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        Document d = factory.newDocumentBuilder().newDocument();
        generateRoot(event, d);

        return d;
    }

    protected void generateRoot(Event event, Document descriptor) {
        Element eventElement = descriptor.createElementNS(getNamespace(), CONSTANTS.Event.name());
        descriptor.appendChild(eventElement);
        generateInstanceIDs(event, descriptor, eventElement);
    }

    protected void generateInstanceIDs(Event event, Document descriptor, Element rootElement) {
        for (InstanceID instanceID : event.getInstanceIDs()) {
            if (instanceID.getId() == null) continue;
            Element instanceIDElement = appendNewElement(descriptor, rootElement, CONSTANTS.InstanceID.name());
            instanceIDElement.setAttribute(CONSTANTS.val.name(), instanceID.getId().toString());

            for (EventedValue eventedValue : instanceID.getValues()) {
                generateEventedValue(eventedValue, descriptor, instanceIDElement);
            }
        }
    }

    protected void generateEventedValue(EventedValue eventedValue, Document descriptor, Element parentElement) {
        String name = eventedValue.getName();
        Map.Entry<String, String>[] attributes = eventedValue.getAttributes();
        if (attributes != null && attributes.length > 0) {
            Element evElement = appendNewElement(descriptor, parentElement, name);
            for (Map.Entry<String, String> attr : attributes) {
                evElement.setAttribute(attr.getKey(), attr.getValue());
            }
        }
    }

}

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

import org.teleal.cling.model.types.UnsignedIntegerFourBytes;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Collects all state changes per logical instance.
 * <p>
 * This class is supposed to be used on a UPnP state variable field,
 * on a RenderingControl or AVTransport service. The service then
 * sets evented values whenever its state changes, and periodically
 * (e.g. in a background loop) fires the "LastChange" XML content
 * through its PropertyChangeSupport. (Where the ServiceManager picks
 * it up and sends it to all subscribers.)
 * </p>
 * <p>
 * The event subscriber can use this class to marshall the "LastChange"
 * content, when the event XML is received.
 * </p>
 * <p>
 * This class is thread-safe.
 * </p>
 *
 * @author Christian Bauer
 */
public class LastChange {

    final private Event event;
    final private LastChangeParser parser;
    private String previousValue;

    public LastChange(String s) {
        throw new UnsupportedOperationException("This constructor is only for service binding detection");
    }

    public LastChange(LastChangeParser parser, Event event) {
        this.parser = parser;
        this.event = event;
    }

    public LastChange(LastChangeParser parser) {
        this(parser, new Event());
    }

    public LastChange(LastChangeParser parser, String xml) throws Exception {
        if (xml != null && xml.length() > 0) {
            this.event = parser.parse(xml);
        } else {
            this.event = new Event();
        }
        this.parser = parser;
    }

    synchronized public void reset() {
        previousValue = toString();
        event.clear();
    }

    synchronized public void setEventedValue(int instanceID, EventedValue... ev) {
        setEventedValue(new UnsignedIntegerFourBytes(instanceID), ev);
    }

    synchronized public void setEventedValue(UnsignedIntegerFourBytes instanceID, EventedValue... ev) {
        for (EventedValue eventedValue : ev) {
            if (eventedValue != null)
                event.setEventedValue(instanceID, eventedValue);

        }
    }

    synchronized public UnsignedIntegerFourBytes[] getInstanceIDs() {
        List<UnsignedIntegerFourBytes> list = new ArrayList();
        for (InstanceID instanceID : event.getInstanceIDs()) {
            list.add(instanceID.getId());
        }
        return list.toArray(new UnsignedIntegerFourBytes[list.size()]);
    }

    synchronized EventedValue[] getEventedValues(UnsignedIntegerFourBytes instanceID) {
        InstanceID inst = event.getInstanceID(instanceID);
        return inst != null ? inst.getValues().toArray(new EventedValue[inst.getValues().size()]) : null;
    }

    synchronized public <EV extends EventedValue> EV getEventedValue(int instanceID, Class<EV> type) {
        return getEventedValue(new UnsignedIntegerFourBytes(instanceID), type);
    }

    synchronized public <EV extends EventedValue> EV getEventedValue(UnsignedIntegerFourBytes id, Class<EV> type) {
        return event.getEventedValue(id, type);
    }

    synchronized public void fire(PropertyChangeSupport propertyChangeSupport) {
        String lastChanges = toString();
        if (lastChanges != null && lastChanges.length() > 0) {
            propertyChangeSupport.firePropertyChange("LastChange", previousValue, lastChanges);
            reset();
        }
    }

    @Override
    synchronized public String toString() {
        if (!event.hasChanges()) return "";
        try {
            return parser.generate(event);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

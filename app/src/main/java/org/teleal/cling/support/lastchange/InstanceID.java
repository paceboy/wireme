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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public class InstanceID {

    protected UnsignedIntegerFourBytes id;
    protected List<EventedValue> values = new ArrayList();

    public InstanceID(UnsignedIntegerFourBytes id) {
        this(id, new ArrayList());
    }

    public InstanceID(UnsignedIntegerFourBytes id, List<EventedValue> values) {
        this.id = id;
        this.values = values;
    }

    public UnsignedIntegerFourBytes getId() {
        return id;
    }

    public List<EventedValue> getValues() {
        return values;
    }
}

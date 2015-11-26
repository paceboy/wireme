/*
 * Copyright (C) 2011 Teleal GmbH, Switzerland
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

import org.teleal.cling.model.types.Datatype;

import java.util.Map;

/**
 * @author Christian Bauer
 */
public class EventedValueShort extends EventedValue<Short> {

    public EventedValueShort(Short value) {
        super(value);
    }

    public EventedValueShort(Map.Entry<String, String>[] attributes) {
        super(attributes);
    }

    @Override
    protected Datatype getDatatype() {
        return Datatype.Builtin.I2_SHORT.getDatatype();
    }
}

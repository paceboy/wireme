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

package org.teleal.cling.support.model;

import org.teleal.cling.model.ModelUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public enum TransportAction {

    Play,
    Stop,
    Pause,
    Seek,
    Next,
    Previous,
    Record;

    public static TransportAction[] valueOfCommaSeparatedList(String s) {
        String[] strings = ModelUtil.fromCommaSeparatedList(s);
        if (strings == null) return new TransportAction[0];
        List<TransportAction> result = new ArrayList();
        for (String taString : strings) {
            for (TransportAction ta : values()) {
                if (ta.name().equals(taString)) {
                    result.add(ta);
                }
            }

        }
        return result.toArray(new TransportAction[result.size()]);
    }
}

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

package org.teleal.cling.support.avtransport.impl.state;

import org.teleal.cling.support.avtransport.lastchange.AVTransportVariable;
import org.teleal.cling.support.model.AVTransport;
import org.teleal.cling.support.model.TransportAction;
import org.teleal.cling.support.model.TransportInfo;
import org.teleal.cling.support.model.TransportState;

import java.net.URI;
import java.util.logging.Logger;

/**
 * @author Christian Bauer
 */
public abstract class PausedPlay<T extends AVTransport> extends AbstractState {

    final private static Logger log = Logger.getLogger(PausedPlay.class.getName());

    public PausedPlay(T transport) {
        super(transport);
    }

    public void onEntry() {
        log.fine("Setting transport state to PAUSED_PLAYBACK");
        getTransport().setTransportInfo(
                new TransportInfo(
                        TransportState.PAUSED_PLAYBACK,
                        getTransport().getTransportInfo().getCurrentTransportStatus(),
                        getTransport().getTransportInfo().getCurrentSpeed()
                )
        );
        getTransport().getLastChange().setEventedValue(
                getTransport().getInstanceId(),
                new AVTransportVariable.TransportState(TransportState.PAUSED_PLAYBACK),
                new AVTransportVariable.CurrentTransportActions(getCurrentTransportActions())
        );
    }

    public abstract Class<? extends AbstractState> setTransportURI(URI uri, String metaData);
    public abstract Class<? extends AbstractState> stop();
    public abstract Class<? extends AbstractState> play(String speed);

    public TransportAction[] getCurrentTransportActions() {
        return new TransportAction[] {
                TransportAction.Stop,
                TransportAction.Play
        };
    }

}

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

package org.teleal.cling.support.shared;

import org.teleal.common.swingfwk.logging.LogCategory;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * @author Christian Bauer
 */
public class LogCategories extends ArrayList<LogCategory> {

    public LogCategories() {
        super(10);

        add(new LogCategory("Network", new LogCategory.Group[]{

                new LogCategory.Group(
                        "UDP communication",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.spi.DatagramIO.class.getName(), Level.FINE),
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.spi.MulticastReceiver.class.getName(), Level.FINE),
                        }
                ),

                new LogCategory.Group(
                        "UDP datagram processing and content",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.spi.DatagramProcessor.class.getName(), Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "TCP communication",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.spi.UpnpStream.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.spi.StreamServer.class.getName(), Level.FINE),
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.spi.StreamClient.class.getName(), Level.FINE),
                        }
                ),

                new LogCategory.Group(
                        "SOAP action message processing and content",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.spi.SOAPActionProcessor.class.getName(), Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "GENA event message processing and content",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.spi.GENAEventProcessor.class.getName(), Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "HTTP header processing",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.model.message.UpnpHeaders.class.getName(), Level.FINER)
                        }
                ),
        }));


        add(new LogCategory("UPnP Protocol", new LogCategory.Group[]{

                new LogCategory.Group(
                        "Discovery (Notification & Search)",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.ProtocolFactory.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.cling.protocol.async", Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "Description",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.ProtocolFactory.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.RetrieveRemoteDescriptors.class.getName(), Level.FINE),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.ReceivingRetrieval.class.getName(), Level.FINE),
                                new LogCategory.LoggerLevel(org.teleal.cling.binding.xml.DeviceDescriptorBinder.class.getName(), Level.FINE),
                                new LogCategory.LoggerLevel(org.teleal.cling.binding.xml.ServiceDescriptorBinder.class.getName(), Level.FINE),
                        }
                ),

                new LogCategory.Group(
                        "Control",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.ProtocolFactory.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.ReceivingAction.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.SendingAction.class.getName(), Level.FINER),
                        }
                ),

                new LogCategory.Group(
                        "GENA ",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.cling.model.gena", Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.ProtocolFactory.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.ReceivingEvent.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.ReceivingSubscribe.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.ReceivingUnsubscribe.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.SendingEvent.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.SendingSubscribe.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.SendingUnsubscribe.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.protocol.sync.SendingRenewal.class.getName(), Level.FINER),
                        }
                ),
        }));

        add(new LogCategory("Core", new LogCategory.Group[]{

                new LogCategory.Group(
                        "Router",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.transport.Router.class.getName(), Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "Registry",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel(org.teleal.cling.registry.Registry.class.getName(), Level.FINER),
                        }
                ),

                new LogCategory.Group(
                        "Local service binding & invocation",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.cling.binding.annotations", Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.model.meta.LocalService.class.getName(), Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.cling.model.action", Level.FINER),
                                new LogCategory.LoggerLevel("org.teleal.cling.model.state", Level.FINER),
                                new LogCategory.LoggerLevel(org.teleal.cling.model.DefaultServiceManager.class.getName(), Level.FINER)
                        }
                ),

                new LogCategory.Group(
                        "Control Point interaction",
                        new LogCategory.LoggerLevel[]{
                                new LogCategory.LoggerLevel("org.teleal.cling.controlpoint", Level.FINER),
                        }
                ),
        }));

    }

}

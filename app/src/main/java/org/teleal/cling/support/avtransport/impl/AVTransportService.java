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

package org.teleal.cling.support.avtransport.impl;

import org.teleal.cling.model.ModelUtil;
import org.teleal.cling.model.types.ErrorCode;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.support.avtransport.AVTransportErrorCode;
import org.teleal.cling.support.avtransport.AVTransportException;
import org.teleal.cling.support.avtransport.AbstractAVTransportService;
import org.teleal.cling.support.avtransport.impl.state.AbstractState;
import org.teleal.cling.support.lastchange.LastChange;
import org.teleal.cling.support.model.AVTransport;
import org.teleal.cling.support.model.DeviceCapabilities;
import org.teleal.cling.support.model.MediaInfo;
import org.teleal.cling.support.model.PlayMode;
import org.teleal.cling.support.model.PositionInfo;
import org.teleal.cling.support.model.RecordQualityMode;
import org.teleal.cling.support.model.SeekMode;
import org.teleal.cling.support.model.StorageMedium;
import org.teleal.cling.support.model.TransportAction;
import org.teleal.cling.support.model.TransportInfo;
import org.teleal.cling.support.model.TransportSettings;
import org.teleal.common.statemachine.StateMachineBuilder;
import org.teleal.common.statemachine.TransitionException;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * State-machine based implementation of AVTransport service.
 * <p>
 * One logical AVTransport is represented by:
 * </p>
 * <ul>
 * <li>
 * One {@link org.teleal.cling.support.avtransport.impl.AVTransportStateMachine}
 * instance that accepts the action method call as a proxy.
 * </li>
 * <li>
 * Each state machine holds several instances of
 * {@link org.teleal.cling.support.avtransport.impl.state.AbstractState}, created on
 * instantation of the state machine. The "current" state will be the target of
 * the action call. It is the state implementation that decides how to handle the
 * call and what the next state is after a possible transition.
 * </li>
 * <li>
 * Each state has a reference to an implementation of
 * {@link org.teleal.cling.support.model.AVTransport}, where the state can hold
 * information about well, the state.
 * </li>
 * </ul>
 * <p>
 * Simplified, this means that each AVTransport instance ID is typically handled by
 * one state machine, and the internal state of that machine is stored in an
 * <code>AVTransport</code>.
 * </p>
 * <p>
 * Override the {@link #createTransport(org.teleal.cling.model.types.UnsignedIntegerFourBytes, org.teleal.cling.support.lastchange.LastChange)}
 * method to utilize a subclass of <code>AVTransport</code> as your internal state holder.
 * </p>
 *
 * @author Christian Bauer
 */
public class AVTransportService<T extends AVTransport> extends AbstractAVTransportService {

    final private static Logger log = Logger.getLogger(AVTransportService.class.getName());

    final private Map<Long, AVTransportStateMachine> stateMachines = new ConcurrentHashMap();

    final Class<? extends AVTransportStateMachine> stateMachineDefinition;
    final Class<? extends AbstractState> initialState;
    final Class<? extends AVTransport> transportClass;

    public AVTransportService(Class<? extends AVTransportStateMachine> stateMachineDefinition,
                              Class<? extends AbstractState> initialState) {
        this(stateMachineDefinition, initialState, (Class<T>)AVTransport.class);
    }

    public AVTransportService(Class<? extends AVTransportStateMachine> stateMachineDefinition,
                              Class<? extends AbstractState> initialState,
                              Class<T> transportClass) {
        this.stateMachineDefinition = stateMachineDefinition;
        this.initialState = initialState;
        this.transportClass = transportClass;
    }

    public void setAVTransportURI(UnsignedIntegerFourBytes instanceId,
                                  String currentURI,
                                  String currentURIMetaData) throws AVTransportException {

        URI uri;
        try {
            uri = new URI(currentURI);
        } catch (Exception ex) {
            throw new AVTransportException(
                    ErrorCode.INVALID_ARGS, "CurrentURI can not be null or malformed"
            );
        }

        try {
            AVTransportStateMachine transportStateMachine = findStateMachine(instanceId, true);
            transportStateMachine.setTransportURI(uri, currentURIMetaData);
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    public void setNextAVTransportURI(UnsignedIntegerFourBytes instanceId,
                                      String nextURI,
                                      String nextURIMetaData) throws AVTransportException {

        URI uri;
        try {
            uri = new URI(nextURI);
        } catch (Exception ex) {
            throw new AVTransportException(
                    ErrorCode.INVALID_ARGS, "NextURI can not be null or malformed"
            );
        }

        try {
            AVTransportStateMachine transportStateMachine = findStateMachine(instanceId, true);
            transportStateMachine.setNextTransportURI(uri, nextURIMetaData);
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    public void setPlayMode(UnsignedIntegerFourBytes instanceId, String newPlayMode) throws AVTransportException {
        AVTransport transport = findStateMachine(instanceId).getCurrentState().getTransport();
        try {
            transport.setTransportSettings(
                    new TransportSettings(
                            PlayMode.valueOf(newPlayMode),
                            transport.getTransportSettings().getRecQualityMode()
                    )
            );
        } catch (IllegalArgumentException ex) {
            throw new AVTransportException(
                    AVTransportErrorCode.PLAYMODE_NOT_SUPPORTED, "Unsupported play mode: " + newPlayMode
            );
        }
    }

    public void setRecordQualityMode(UnsignedIntegerFourBytes instanceId, String newRecordQualityMode) throws AVTransportException {
        AVTransport transport = findStateMachine(instanceId).getCurrentState().getTransport();
        try {
            transport.setTransportSettings(
                    new TransportSettings(
                            transport.getTransportSettings().getPlayMode(),
                            RecordQualityMode.valueOrExceptionOf(newRecordQualityMode)
                    )
            );
        } catch (IllegalArgumentException ex) {
            throw new AVTransportException(
                    AVTransportErrorCode.RECORDQUALITYMODE_NOT_SUPPORTED, "Unsupported record quality mode: " + newRecordQualityMode
            );
        }
    }

    public MediaInfo getMediaInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return findStateMachine(instanceId).getCurrentState().getTransport().getMediaInfo();
    }

    public TransportInfo getTransportInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return findStateMachine(instanceId).getCurrentState().getTransport().getTransportInfo();
    }

    public PositionInfo getPositionInfo(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return findStateMachine(instanceId).getCurrentState().getTransport().getPositionInfo();
    }

    public DeviceCapabilities getDeviceCapabilities(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return findStateMachine(instanceId).getCurrentState().getTransport().getDeviceCapabilities();
    }

    public TransportSettings getTransportSettings(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return findStateMachine(instanceId).getCurrentState().getTransport().getTransportSettings();
    }

    public String getCurrentTransportActions(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        AVTransportStateMachine stateMachine = findStateMachine(instanceId);
        try {
            TransportAction[] actions = stateMachine.getCurrentState().getCurrentTransportActions();
            return ModelUtil.toCommaSeparatedList(actions);
        } catch (TransitionException ex) {
            return ""; // TODO: Empty string is not defined in spec but seems reasonable for no available action?
        }
    }

    public void stop(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        try {
            findStateMachine(instanceId).stop();
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    public void play(UnsignedIntegerFourBytes instanceId, String speed) throws AVTransportException {
        try {
            findStateMachine(instanceId).play(speed);
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    public void pause(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        try {
            findStateMachine(instanceId).pause();
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    public void record(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        try {
            findStateMachine(instanceId).record();
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    public void seek(UnsignedIntegerFourBytes instanceId, String unit, String target) throws AVTransportException {
        SeekMode seekMode;
        try {
             seekMode = SeekMode.valueOrExceptionOf(unit);
        } catch (IllegalArgumentException ex) {
            throw new AVTransportException(
                    AVTransportErrorCode.SEEKMODE_NOT_SUPPORTED, "Unsupported seek mode: " + unit
            );
        }

        try {
            findStateMachine(instanceId).seek(seekMode, target);
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    public void next(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        try {
            findStateMachine(instanceId).next();
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    public void previous(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        try {
            findStateMachine(instanceId).previous();
        } catch (TransitionException ex) {
            throw new AVTransportException(AVTransportErrorCode.TRANSITION_NOT_AVAILABLE, ex.getMessage());
        }
    }

    protected AVTransportStateMachine findStateMachine(UnsignedIntegerFourBytes instanceId) throws AVTransportException {
        return findStateMachine(instanceId, true);
    }

    protected AVTransportStateMachine findStateMachine(UnsignedIntegerFourBytes instanceId, boolean createDefaultTransport) throws AVTransportException {
        synchronized (stateMachines) {
            long id = instanceId.getValue();
            AVTransportStateMachine stateMachine = stateMachines.get(id);
            if (stateMachine == null && id == 0 && createDefaultTransport) {
                log.fine("Creating default transport instance with ID '0'");
                stateMachine = createStateMachine(instanceId);
                stateMachines.put(id, stateMachine);
            } else if (stateMachine == null) {
                throw new AVTransportException(AVTransportErrorCode.INVALID_INSTANCE_ID);
            }
            log.fine("Found transport control with ID '" + id + "'");
            return stateMachine;
        }
    }

    protected AVTransportStateMachine createStateMachine(UnsignedIntegerFourBytes instanceId) {
        // Create a proxy that delegates all calls to the right state implementation, working on the T state
        return StateMachineBuilder.build(
                stateMachineDefinition,
                initialState,
                new Class[]{transportClass},
                new Object[]{createTransport(instanceId, getLastChange())}
        );
    }

    protected AVTransport createTransport(UnsignedIntegerFourBytes instanceId, LastChange lastChange) {
        return new AVTransport(instanceId, lastChange, StorageMedium.NETWORK);
    }

}

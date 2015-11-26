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

package org.teleal.cling.support.avtransport.lastchange;

import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.support.model.PlayMode;
import org.teleal.cling.support.model.RecordQualityMode;
import org.teleal.cling.support.model.TransportAction;
import org.teleal.cling.support.model.StorageMedium;
import org.teleal.cling.support.lastchange.EventedValue;
import org.teleal.cling.support.lastchange.EventedValueEnum;
import org.teleal.cling.support.lastchange.EventedValueEnumArray;
import org.teleal.cling.support.lastchange.EventedValueString;
import org.teleal.cling.support.lastchange.EventedValueURI;
import org.teleal.cling.support.lastchange.EventedValueUnsignedIntegerFourBytes;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Christian Bauer
 */
public class AVTransportVariable {

    public static Set<Class<? extends EventedValue>> ALL = new HashSet<Class<? extends EventedValue>>() {{
        add(TransportState.class);
        add(TransportStatus.class);
        add(RecordStorageMedium.class);
        add(PossibleRecordStorageMedia.class);
        add(PossiblePlaybackStorageMedia.class);
        add(CurrentPlayMode.class);
        add(TransportPlaySpeed.class);
        add(RecordMediumWriteStatus.class);
        add(CurrentRecordQualityMode.class);
        add(PossibleRecordQualityModes.class);
        add(NumberOfTracks.class);
        add(CurrentTrack.class);
        add(CurrentTrackDuration.class);
        add(CurrentMediaDuration.class);
        add(CurrentTrackMetaData.class);
        add(CurrentTrackURI.class);
        add(AVTransportURI.class);
        add(NextAVTransportURI.class);
        add(AVTransportURIMetaData.class);
        add(NextAVTransportURIMetaData.class);
        add(CurrentTransportActions.class);
    }};

    public static class TransportState extends EventedValueEnum<org.teleal.cling.support.model.TransportState> {
        public TransportState(org.teleal.cling.support.model.TransportState avTransportState) {
            super(avTransportState);
        }

        public TransportState(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected org.teleal.cling.support.model.TransportState enumValueOf(String s) {
            return org.teleal.cling.support.model.TransportState.valueOf(s);
        }
    }

    public static class TransportStatus extends EventedValueEnum<org.teleal.cling.support.model.TransportStatus> {
        public TransportStatus(org.teleal.cling.support.model.TransportStatus transportStatus) {
            super(transportStatus);
        }

        public TransportStatus(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected org.teleal.cling.support.model.TransportStatus enumValueOf(String s) {
            return org.teleal.cling.support.model.TransportStatus.valueOf(s);
        }
    }

    public static class RecordStorageMedium extends EventedValueEnum<StorageMedium> {

        public RecordStorageMedium(StorageMedium storageMedium) {
            super(storageMedium);
        }

        public RecordStorageMedium(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected StorageMedium enumValueOf(String s) {
            return StorageMedium.valueOf(s);
        }
    }

    public static class PossibleRecordStorageMedia extends EventedValueEnumArray<StorageMedium> {
        public PossibleRecordStorageMedia(StorageMedium[] e) {
            super(e);
        }

        public PossibleRecordStorageMedia(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected StorageMedium[] enumValueOf(String[] names) {
            List<StorageMedium> list = new ArrayList();
            for (String s : names) {
                list.add(StorageMedium.valueOf(s));
            }
            return list.toArray(new StorageMedium[list.size()]);
        }
    }

    public static class PossiblePlaybackStorageMedia extends PossibleRecordStorageMedia {
        public PossiblePlaybackStorageMedia(StorageMedium[] e) {
            super(e);
        }

        public PossiblePlaybackStorageMedia(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class CurrentPlayMode extends EventedValueEnum<PlayMode> {
        public CurrentPlayMode(PlayMode playMode) {
            super(playMode);
        }

        public CurrentPlayMode(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected PlayMode enumValueOf(String s) {
            return PlayMode.valueOf(s);
        }
    }

    public static class TransportPlaySpeed extends EventedValueString {
        public TransportPlaySpeed(String value) {
            super(value);
        }

        public TransportPlaySpeed(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class RecordMediumWriteStatus extends EventedValueEnum<org.teleal.cling.support.model.RecordMediumWriteStatus> {
        public RecordMediumWriteStatus(org.teleal.cling.support.model.RecordMediumWriteStatus recordMediumWriteStatus) {
            super(recordMediumWriteStatus);
        }

        public RecordMediumWriteStatus(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected org.teleal.cling.support.model.RecordMediumWriteStatus enumValueOf(String s) {
            return org.teleal.cling.support.model.RecordMediumWriteStatus.valueOf(s);
        }
    }

    public static class CurrentRecordQualityMode extends EventedValueEnum<RecordQualityMode> {
        public CurrentRecordQualityMode(RecordQualityMode recordQualityMode) {
            super(recordQualityMode);
        }

        public CurrentRecordQualityMode(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected RecordQualityMode enumValueOf(String s) {
            return RecordQualityMode.valueOf(s);
        }
    }

    public static class PossibleRecordQualityModes extends EventedValueEnumArray<RecordQualityMode> {
        public PossibleRecordQualityModes(RecordQualityMode[] e) {
            super(e);
        }

        public PossibleRecordQualityModes(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected RecordQualityMode[] enumValueOf(String[] names) {
            List<RecordQualityMode> list = new ArrayList();
            for (String s : names) {
                list.add(RecordQualityMode.valueOf(s));
            }
            return list.toArray(new RecordQualityMode[list.size()]);
        }
    }

    public static class NumberOfTracks extends EventedValueUnsignedIntegerFourBytes {
        public NumberOfTracks(UnsignedIntegerFourBytes value) {
            super(value);
        }

        public NumberOfTracks(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class CurrentTrack extends EventedValueUnsignedIntegerFourBytes {
        public CurrentTrack(UnsignedIntegerFourBytes value) {
            super(value);
        }

        public CurrentTrack(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class CurrentTrackDuration extends EventedValueString {
        public CurrentTrackDuration(String value) {
            super(value);
        }

        public CurrentTrackDuration(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class CurrentMediaDuration extends EventedValueString {
        public CurrentMediaDuration(String value) {
            super(value);
        }

        public CurrentMediaDuration(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class CurrentTrackMetaData extends EventedValueString {
        public CurrentTrackMetaData(String value) {
            super(value);
        }

        public CurrentTrackMetaData(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class CurrentTrackURI extends EventedValueURI {
        public CurrentTrackURI(URI value) {
            super(value);
        }

        public CurrentTrackURI(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class AVTransportURI extends EventedValueURI {
        public AVTransportURI(URI value) {
            super(value);
        }

        public AVTransportURI(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class NextAVTransportURI extends EventedValueURI {
        public NextAVTransportURI(URI value) {
            super(value);
        }

        public NextAVTransportURI(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class AVTransportURIMetaData extends EventedValueString {
        public AVTransportURIMetaData(String value) {
            super(value);
        }

        public AVTransportURIMetaData(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class NextAVTransportURIMetaData extends EventedValueString {
        public NextAVTransportURIMetaData(String value) {
            super(value);
        }

        public NextAVTransportURIMetaData(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }
    }

    public static class CurrentTransportActions extends EventedValueEnumArray<TransportAction>{
        public CurrentTransportActions(TransportAction[] e) {
            super(e);
        }

        public CurrentTransportActions(Map.Entry<String, String>[] attributes) {
            super(attributes);
        }

        @Override
        protected TransportAction[] enumValueOf(String[] names) {
            if (names == null) return new TransportAction[0];
            List<TransportAction> list = new ArrayList();
            for (String s : names) {
                list.add(TransportAction.valueOf(s));
            }
            return list.toArray(new TransportAction[list.size()]);
        }
    }

}

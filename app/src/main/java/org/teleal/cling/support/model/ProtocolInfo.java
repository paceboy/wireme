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

import org.teleal.cling.model.types.InvalidValueException;
import org.teleal.common.util.MimeType;

/**
 * Encaspulates a MIME type (content format) and transport, protocol, additional information.
 *
 * @author Christian Bauer
 */
public class ProtocolInfo {

    public static final String WILDCARD = "*";
    public static final String TRAILING_ZEROS = "000000000000000000000000";

    public static final class DLNAFlags {
        public static final int SENDER_PACED = (1 << 31);
        public static final int TIME_BASED_SEEK = (1 << 30);
        public static final int BYTE_BASED_SEEK = (1 << 29);
        public static final int FLAG_PLAY_CONTAINER = (1 << 28);
        public static final int S0_INCREASE = (1 << 27);
        public static final int SN_INCREASE = (1 << 26);
        public static final int RTSP_PAUSE = (1 << 25);
        public static final int STREAMING_TRANSFER_MODE = (1 << 24);
        public static final int INTERACTIVE_TRANSFERT_MODE = (1 << 23);
        public static final int BACKGROUND_TRANSFERT_MODE = (1 << 22);
        public static final int CONNECTION_STALL = (1 << 21);
        public static final int DLNA_V15 = (1 << 20);
    }

    protected Protocol protocol = Protocol.ALL;
    protected String network = WILDCARD;
    protected String contentFormat = WILDCARD;
    protected String additionalInfo = WILDCARD;

    public ProtocolInfo(String s) throws InvalidValueException {
        if (s == null) throw new NullPointerException();
        s = s.trim();
        String[] split = s.split(":");
        if (split.length != 4) {
            throw new InvalidValueException("Can't parse ProtocolInfo string: " + s);
        }
        this.protocol = Protocol.valueOrNullOf(split[0]);
        this.network = split[1];
        this.contentFormat = split[2];
        this.additionalInfo = split[3];
    }

    public ProtocolInfo(MimeType contentFormatMimeType) {
        this.protocol = Protocol.HTTP_GET;
        this.contentFormat = contentFormatMimeType.toString();
    }

    public ProtocolInfo(Protocol protocol, String network, String contentFormat, String additionalInfo) {
        this.protocol = protocol;
        this.network = network;
        this.contentFormat = contentFormat;
        this.additionalInfo = additionalInfo;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public String getNetwork() {
        return network;
    }

    public String getContentFormat() {
        return contentFormat;
    }

    public MimeType getContentFormatMimeType() throws IllegalArgumentException {
        return MimeType.valueOf(contentFormat);
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProtocolInfo that = (ProtocolInfo) o;

        if (!additionalInfo.equals(that.additionalInfo)) return false;
        if (!contentFormat.equals(that.contentFormat)) return false;
        if (!network.equals(that.network)) return false;
        if (protocol != that.protocol) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = protocol.hashCode();
        result = 31 * result + network.hashCode();
        result = 31 * result + contentFormat.hashCode();
        result = 31 * result + additionalInfo.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return protocol.toString() + ":" +
                network + ":" +
                contentFormat + ":" +
                additionalInfo;

    }
}

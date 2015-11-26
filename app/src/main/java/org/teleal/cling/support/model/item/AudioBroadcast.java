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

package org.teleal.cling.support.model.item;

import org.teleal.cling.support.model.Res;

import static org.teleal.cling.support.model.DIDLObject.Property.UPNP;

/**
 * @author Christian Bauer
 */
public class AudioBroadcast extends AudioItem {

    public static final Class CLASS = new Class("object.item.audioItem.audioBroadcast");

    public AudioBroadcast() {
        setClazz(CLASS);
    }

    public AudioBroadcast(Item other) {
        super(other);
    }

    public AudioBroadcast(String id, String parentID, String title, String creator, Res... resource) {
        super(id, parentID, title, creator, resource);
        setClazz(CLASS);
    }

    public String getRegion() {
        return getFirstPropertyValue(UPNP.REGION.class);
    }

    public AudioBroadcast setRegion(String region) {
        replaceFirstProperty(new UPNP.REGION(region));
        return this;
    }

    public String getRadioCallSign() {
        return getFirstPropertyValue(UPNP.RADIO_CALL_SIGN.class);
    }

    public AudioBroadcast setRadioCallSign(String radioCallSign) {
        replaceFirstProperty(new UPNP.RADIO_CALL_SIGN(radioCallSign));
        return this;
    }

    public String getRadioStationID() {
        return getFirstPropertyValue(UPNP.RADIO_STATION_ID.class);
    }

    public AudioBroadcast setRadioStationID(String radioStationID) {
        replaceFirstProperty(new UPNP.RADIO_STATION_ID(radioStationID));
        return this;
    }

    public String getRadioBand() {
        return getFirstPropertyValue(UPNP.RADIO_BAND.class);
    }

    public AudioBroadcast setRadioBand(String radioBand) {
        replaceFirstProperty(new UPNP.RADIO_BAND(radioBand));
        return this;
    }

    public Integer getChannelNr() {
        return getFirstPropertyValue(UPNP.CHANNEL_NR.class);
    }

    public AudioBroadcast setChannelNr(Integer channelNr) {
        replaceFirstProperty(new UPNP.CHANNEL_NR(channelNr));
        return this;
    }
}

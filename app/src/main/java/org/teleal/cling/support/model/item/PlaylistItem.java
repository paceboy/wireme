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

import org.teleal.cling.support.model.Person;
import org.teleal.cling.support.model.PersonWithRole;
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.StorageMedium;
import org.teleal.cling.support.model.container.Container;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.teleal.cling.support.model.DIDLObject.Property.DC;
import static org.teleal.cling.support.model.DIDLObject.Property.UPNP;

/**
 * @author Christian Bauer
 */
public class PlaylistItem extends Item {

    public static final Class CLASS = new Class("object.item.playlistItem");

    public PlaylistItem() {
        setClazz(CLASS);
    }

    public PlaylistItem(Item other) {
        super(other);
    }

    public PlaylistItem(String id, Container parent, String title, String creator, Res... resource) {
        this(id, parent.getId(), title, creator, resource);
    }

    public PlaylistItem(String id, String parentID, String title, String creator, Res... resource) {
        super(id, parentID, title, creator, CLASS);
        if (resource != null) {
            getResources().addAll(Arrays.asList(resource));
        }
    }

    public PersonWithRole getFirstArtist() {
        return getFirstPropertyValue(UPNP.ARTIST.class);
    }

    public PersonWithRole[] getArtists() {
        List<PersonWithRole> list = getPropertyValues(UPNP.ARTIST.class);
        return list.toArray(new PersonWithRole[list.size()]);
    }

    public PlaylistItem setArtists(PersonWithRole[] artists) {
        removeProperties(UPNP.ARTIST.class);
        for (PersonWithRole artist : artists) {
            addProperty(new UPNP.ARTIST(artist));
        }
        return this;
    }

    public String getFirstGenre() {
        return getFirstPropertyValue(UPNP.GENRE.class);
    }

    public String[] getGenres() {
        List<String> list = getPropertyValues(UPNP.GENRE.class);
        return list.toArray(new String[list.size()]);
    }

    public PlaylistItem setGenres(String[] genres) {
        removeProperties(UPNP.GENRE.class);
        for (String genre : genres) {
            addProperty(new UPNP.GENRE(genre));
        }
        return this;
    }

    public String getDescription() {
        return getFirstPropertyValue(DC.DESCRIPTION.class);
    }

    public PlaylistItem setDescription(String description) {
        replaceFirstProperty(new DC.DESCRIPTION(description));
        return this;
    }

    public String getLongDescription() {
        return getFirstPropertyValue(UPNP.LONG_DESCRIPTION.class);
    }

    public PlaylistItem setLongDescription(String description) {
        replaceFirstProperty(new UPNP.LONG_DESCRIPTION(description));
        return this;
    }

    public String getLanguage() {
        return getFirstPropertyValue(DC.LANGUAGE.class);
    }

    public PlaylistItem setLanguage(String language) {
        replaceFirstProperty(new DC.LANGUAGE(language));
        return this;
    }

    public StorageMedium getStorageMedium() {
        return getFirstPropertyValue(UPNP.STORAGE_MEDIUM.class);
    }

    public PlaylistItem setStorageMedium(StorageMedium storageMedium) {
        replaceFirstProperty(new UPNP.STORAGE_MEDIUM(storageMedium));
        return this;
    }

    public String getDate() {
        return getFirstPropertyValue(DC.DATE.class);
    }

    public PlaylistItem setDate(String date) {
        replaceFirstProperty(new DC.DATE(date));
        return this;
    }

}

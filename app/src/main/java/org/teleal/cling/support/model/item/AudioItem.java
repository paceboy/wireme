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
import org.teleal.cling.support.model.Res;
import org.teleal.cling.support.model.container.Container;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.teleal.cling.support.model.DIDLObject.Property.DC;
import static org.teleal.cling.support.model.DIDLObject.Property.UPNP;

/**
 * @author Christian Bauer
 */
public class AudioItem extends Item {

    public static final Class CLASS = new Class("object.item.audioItem");

    public AudioItem() {
        setClazz(CLASS);
    }

    public AudioItem(Item other) {
        super(other);
    }

    public AudioItem(String id, Container parent, String title, String creator, Res... resource) {
        this(id, parent.getId(), title, creator, resource);
    }

    public AudioItem(String id, String parentID, String title, String creator, Res... resource) {
        super(id, parentID, title, creator, CLASS);
        if (resource != null) {
            getResources().addAll(Arrays.asList(resource));
        }
    }

    public String getFirstGenre() {
        return getFirstPropertyValue(UPNP.GENRE.class);
    }

    public String[] getGenres() {
        List<String> list = getPropertyValues(UPNP.GENRE.class);
        return list.toArray(new String[list.size()]);
    }

    public AudioItem setGenres(String[] genres) {
        removeProperties(UPNP.GENRE.class);
        for (String genre : genres) {
            addProperty(new UPNP.GENRE(genre));
        }
        return this;
    }

    public String getDescription() {
        return getFirstPropertyValue(DC.DESCRIPTION.class);
    }

    public AudioItem setDescription(String description) {
        replaceFirstProperty(new DC.DESCRIPTION(description));
        return this;
    }

    public String getLongDescription() {
        return getFirstPropertyValue(UPNP.LONG_DESCRIPTION.class);
    }

    public AudioItem setLongDescription(String description) {
        replaceFirstProperty(new UPNP.LONG_DESCRIPTION(description));
        return this;
    }

    public Person getFirstPublisher() {
        return getFirstPropertyValue(DC.PUBLISHER.class);
    }

    public Person[] getPublishers() {
        List<Person> list = getPropertyValues(DC.PUBLISHER.class);
        return list.toArray(new Person[list.size()]);
    }

    public AudioItem setPublishers(Person[] publishers) {
        removeProperties(DC.PUBLISHER.class);
        for (Person publisher : publishers) {
            addProperty(new DC.PUBLISHER(publisher));
        }
        return this;
    }

    public URI getFirstRelation() {
        return getFirstPropertyValue(DC.RELATION.class);
    }

    public URI[] getRelations() {
        List<URI> list = getPropertyValues(DC.RELATION.class);
        return list.toArray(new URI[list.size()]);
    }

    public AudioItem setRelations(URI[] relations) {
        removeProperties(DC.RELATION.class);
        for (URI relation : relations) {
            addProperty(new DC.RELATION(relation));
        }
        return this;
    }

    public String getLanguage() {
        return getFirstPropertyValue(DC.LANGUAGE.class);
    }

    public AudioItem setLanguage(String language) {
        replaceFirstProperty(new DC.LANGUAGE(language));
        return this;
    }

    public String getFirstRights() {
        return getFirstPropertyValue(DC.RIGHTS.class);
    }

    public String[] getRights() {
        List<String> list = getPropertyValues(DC.RIGHTS.class);
        return list.toArray(new String[list.size()]);
    }

    public AudioItem setRights(String[] rights) {
        removeProperties(DC.RIGHTS.class);
        for (String right : rights) {
            addProperty(new DC.RIGHTS(right));
        }
        return this;
    }
}


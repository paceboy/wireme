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

import org.teleal.cling.support.model.container.Album;
import org.teleal.cling.support.model.container.Container;
import org.teleal.cling.support.model.container.GenreContainer;
import org.teleal.cling.support.model.container.MovieGenre;
import org.teleal.cling.support.model.container.MusicAlbum;
import org.teleal.cling.support.model.container.MusicArtist;
import org.teleal.cling.support.model.container.MusicGenre;
import org.teleal.cling.support.model.container.PersonContainer;
import org.teleal.cling.support.model.container.PhotoAlbum;
import org.teleal.cling.support.model.container.PlaylistContainer;
import org.teleal.cling.support.model.container.StorageFolder;
import org.teleal.cling.support.model.container.StorageSystem;
import org.teleal.cling.support.model.container.StorageVolume;
import org.teleal.cling.support.model.item.AudioBook;
import org.teleal.cling.support.model.item.AudioBroadcast;
import org.teleal.cling.support.model.item.AudioItem;
import org.teleal.cling.support.model.item.ImageItem;
import org.teleal.cling.support.model.item.Item;
import org.teleal.cling.support.model.item.Movie;
import org.teleal.cling.support.model.item.MusicTrack;
import org.teleal.cling.support.model.item.MusicVideoClip;
import org.teleal.cling.support.model.item.Photo;
import org.teleal.cling.support.model.item.PlaylistItem;
import org.teleal.cling.support.model.item.TextItem;
import org.teleal.cling.support.model.item.VideoBroadcast;
import org.teleal.cling.support.model.item.VideoItem;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Christian Bauer
 */
public class DIDLContent {

    public static final String NAMESPACE_URI = "urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/";
    public static final String DESC_WRAPPER_NAMESPACE_URI = "urn:teleal-org:cling:support:content-directory-desc-1-0";

    protected List<Container> containers = new ArrayList();
    protected List<Item> items = new ArrayList();
    protected List<DescMeta> descMetadata = new ArrayList();

    public Container getFirstContainer() {
        return getContainers().get(0);
    }

    public DIDLContent addContainer(Container container) {
        getContainers().add(container);
        return this;
    }

    public List<Container> getContainers() {
        return containers;
    }

    public void setContainers(List<Container> containers) {
        this.containers = containers;
    }

    public DIDLContent addItem(Item item) {
        getItems().add(item);
        return this;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public DIDLContent addDescMetadata(DescMeta descMetadata) {
        getDescMetadata().add(descMetadata);
        return this;
    }

    public List<DescMeta> getDescMetadata() {
        return descMetadata;
    }

    public void setDescMetadata(List<DescMeta> descMetadata) {
        this.descMetadata = descMetadata;
    }

    public void replaceGenericContainerAndItems() {
        setItems(replaceGenericItems(getItems()));
        setContainers(replaceGenericContainers(getContainers()));
    }

    protected List<Item> replaceGenericItems(List<Item> genericItems) {
        List<Item> specificItems = new ArrayList();

        for (Item genericItem : genericItems) {
            String genericType = genericItem.getClazz().getValue();

            if (AudioItem.CLASS.getValue().equals(genericType)) {
                specificItems.add(new AudioItem(genericItem));
            } else if (MusicTrack.CLASS.getValue().equals(genericType)) {
                specificItems.add(new MusicTrack(genericItem));
            } else if (AudioBook.CLASS.getValue().equals(genericType)) {
                specificItems.add(new AudioBook(genericItem));
            } else if (AudioBroadcast.CLASS.getValue().equals(genericType)) {
                specificItems.add(new AudioBroadcast(genericItem));

            } else if (VideoItem.CLASS.getValue().equals(genericType)) {
                specificItems.add(new VideoItem(genericItem));
            } else if (Movie.CLASS.getValue().equals(genericType)) {
                specificItems.add(new Movie(genericItem));
            } else if (VideoBroadcast.CLASS.getValue().equals(genericType)) {
                specificItems.add(new VideoBroadcast(genericItem));
            } else if (MusicVideoClip.CLASS.getValue().equals(genericType)) {
                specificItems.add(new MusicVideoClip(genericItem));

            } else if (ImageItem.CLASS.getValue().equals(genericType)) {
                specificItems.add(new ImageItem(genericItem));
            } else if (Photo.CLASS.getValue().equals(genericType)) {
                specificItems.add(new Photo(genericItem));

            } else if (PlaylistItem.CLASS.getValue().equals(genericType)) {
                specificItems.add(new PlaylistItem(genericItem));

            } else if (TextItem.CLASS.getValue().equals(genericType)) {
                specificItems.add(new TextItem(genericItem));

            } else {
                specificItems.add(genericItem);
            }
        }

        return specificItems;
    }

    protected List<Container> replaceGenericContainers(List<Container> genericContainers) {
        List<Container> specificContainers = new ArrayList();

        for (Container genericContainer : genericContainers) {
            String genericType = genericContainer.getClazz().getValue();

            Container specific;

            if (Album.CLASS.getValue().equals(genericType)) {
                specific = new Album(genericContainer);

            } else if (MusicAlbum.CLASS.getValue().equals(genericType)) {
                specific = new MusicAlbum(genericContainer);

            } else if (PhotoAlbum.CLASS.getValue().equals(genericType)) {
                specific = new PhotoAlbum(genericContainer);

            } else if (GenreContainer.CLASS.getValue().equals(genericType)) {
                specific = new GenreContainer(genericContainer);

            } else if (MusicGenre.CLASS.getValue().equals(genericType)) {
                specific = new MusicGenre(genericContainer);

            } else if (MovieGenre.CLASS.getValue().equals(genericType)) {
                specific = new MovieGenre(genericContainer);

            } else if (PlaylistContainer.CLASS.getValue().equals(genericType)) {
                specific = new PlaylistContainer(genericContainer);

            } else if (PersonContainer.CLASS.getValue().equals(genericType)) {
                specific = new PersonContainer(genericContainer);

            } else if (MusicArtist.CLASS.getValue().equals(genericType)) {
                specific = new MusicArtist(genericContainer);

            } else if (StorageSystem.CLASS.getValue().equals(genericType)) {
                specific = new StorageSystem(genericContainer);

            } else if (StorageVolume.CLASS.getValue().equals(genericType)) {
                specific = new StorageVolume(genericContainer);

            } else if (StorageFolder.CLASS.getValue().equals(genericType)) {
                specific = new StorageFolder(genericContainer);

            } else {
                specific = genericContainer;
            }

            specific.setItems(replaceGenericItems(genericContainer.getItems()));
            specificContainers.add(specific);
        }

        return specificContainers;
    }
}

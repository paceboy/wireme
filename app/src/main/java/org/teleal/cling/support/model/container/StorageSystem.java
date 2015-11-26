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

package org.teleal.cling.support.model.container;

import org.teleal.cling.support.model.StorageMedium;

import static org.teleal.cling.support.model.DIDLObject.Property.UPNP;

/**
 * @author Christian Bauer
 */
public class StorageSystem extends Container {

    public static final Class CLASS = new Class("object.container.storageSystem");

    public StorageSystem() {
        setClazz(CLASS);
    }

    public StorageSystem(Container other) {
        super(other);
    }

    public StorageSystem(String id, Container parent, String title, String creator, Integer childCount,
                         Long storageTotal, Long storageUsed, Long storageFree, Long storageMaxPartition, StorageMedium storageMedium) {
        this(id, parent.getId(), title, creator, childCount, storageTotal, storageUsed, storageFree, storageMaxPartition, storageMedium);
    }

    public StorageSystem(String id, String parentID, String title, String creator, Integer childCount,
                         Long storageTotal, Long storageUsed, Long storageFree, Long storageMaxPartition, StorageMedium storageMedium) {
        super(id, parentID, title, creator, CLASS, childCount);
        if (storageTotal != null)
            setStorageTotal(storageTotal);
        if (storageUsed!= null)
            setStorageUsed(storageUsed);
        if (storageFree != null)
            setStorageFree(storageFree);
        if (storageMaxPartition != null)
            setStorageMaxPartition(storageMaxPartition);
        if (storageMedium != null)
            setStorageMedium(storageMedium);
    }

    public Long getStorageTotal() {
        return getFirstPropertyValue(UPNP.STORAGE_TOTAL.class);
    }

    public StorageSystem setStorageTotal(Long l) {
        replaceFirstProperty(new UPNP.STORAGE_TOTAL(l));
        return this;
    }

    public Long getStorageUsed() {
        return getFirstPropertyValue(UPNP.STORAGE_USED.class);
    }

    public StorageSystem setStorageUsed(Long l) {
        replaceFirstProperty(new UPNP.STORAGE_USED(l));
        return this;
    }

    public Long getStorageFree() {
        return getFirstPropertyValue(UPNP.STORAGE_FREE.class);
    }

    public StorageSystem setStorageFree(Long l) {
        replaceFirstProperty(new UPNP.STORAGE_FREE(l));
        return this;
    }

    public Long getStorageMaxPartition() {
        return getFirstPropertyValue(UPNP.STORAGE_MAX_PARTITION.class);
    }

    public StorageSystem setStorageMaxPartition(Long l) {
        replaceFirstProperty(new UPNP.STORAGE_MAX_PARTITION(l));
        return this;
    }

    public StorageMedium getStorageMedium() {
        return getFirstPropertyValue(UPNP.STORAGE_MEDIUM.class);
    }

    public StorageSystem setStorageMedium(StorageMedium storageMedium) {
        replaceFirstProperty(new UPNP.STORAGE_MEDIUM(storageMedium));
        return this;
    }

}

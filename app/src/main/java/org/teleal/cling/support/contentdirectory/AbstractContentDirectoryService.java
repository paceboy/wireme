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
package org.teleal.cling.support.contentdirectory;

import org.teleal.cling.binding.annotations.UpnpAction;
import org.teleal.cling.binding.annotations.UpnpInputArgument;
import org.teleal.cling.binding.annotations.UpnpOutputArgument;
import org.teleal.cling.binding.annotations.UpnpService;
import org.teleal.cling.binding.annotations.UpnpServiceId;
import org.teleal.cling.binding.annotations.UpnpServiceType;
import org.teleal.cling.binding.annotations.UpnpStateVariable;
import org.teleal.cling.binding.annotations.UpnpStateVariables;
import org.teleal.cling.model.types.ErrorCode;
import org.teleal.cling.model.types.UnsignedIntegerFourBytes;
import org.teleal.cling.model.types.csv.CSV;
import org.teleal.cling.model.types.csv.CSVString;
import org.teleal.cling.support.model.BrowseFlag;
import org.teleal.cling.support.model.BrowseResult;
import org.teleal.cling.support.model.DIDLContent;
import org.teleal.cling.support.model.SortCriterion;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple ContentDirectory service skeleton.
 * <p>
 * Only state variables and actions required by <em>ContentDirectory:1</em>
 * (not the optional ones) are implemented.
 * </p>
 *
 * @author Alessio Gaeta
 * @author Christian Bauer
 */

@UpnpService(
        serviceId = @UpnpServiceId("ContentDirectory"),
        serviceType = @UpnpServiceType(value = "ContentDirectory", version = 1)
)

@UpnpStateVariables({
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_ObjectID",
                                    sendEvents = false,
                                    datatype = "string"),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_Result",
                                    sendEvents = false,
                                    datatype = "string"),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_BrowseFlag",
                                    sendEvents = false,
                                    datatype = "string",
                                    allowedValuesEnum = BrowseFlag.class),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_Filter",
                                    sendEvents = false,
                                    datatype = "string"),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_SortCriteria",
                                    sendEvents = false,
                                    datatype = "string"),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_Index",
                                    sendEvents = false,
                                    datatype = "ui4"),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_Count",
                                    sendEvents = false,
                                    datatype = "ui4"),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_UpdateID",
                                    sendEvents = false,
                                    datatype = "ui4"),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_URI",
                                    sendEvents = false,
                                    datatype = "uri"),
                            @UpnpStateVariable(
                                    name = "A_ARG_TYPE_SearchCriteria",
                                    sendEvents = false,
                                    datatype = "string")
                    })
public abstract class AbstractContentDirectoryService {

    public static final String CAPS_WILDCARD = "*";

    @UpnpStateVariable(sendEvents = false)
    final private CSV<String> searchCapabilities;

    @UpnpStateVariable(sendEvents = false)
    final private CSV<String> sortCapabilities;

    @UpnpStateVariable(
            sendEvents = true,
            defaultValue = "0",
            eventMaximumRateMilliseconds = 200
    )
    private UnsignedIntegerFourBytes systemUpdateID = new UnsignedIntegerFourBytes(0);

    final protected PropertyChangeSupport propertyChangeSupport;

    protected AbstractContentDirectoryService() {
        this(new ArrayList(), new ArrayList(), null);
    }

    protected AbstractContentDirectoryService(List<String> searchCapabilities, List<String> sortCapabilities) {
        this(searchCapabilities, sortCapabilities, null);
    }

    protected AbstractContentDirectoryService(List<String> searchCapabilities, List<String> sortCapabilities,
                                              PropertyChangeSupport propertyChangeSupport) {
        this.propertyChangeSupport = propertyChangeSupport != null ? propertyChangeSupport : new PropertyChangeSupport(this);
        this.searchCapabilities = new CSVString();
        this.searchCapabilities.addAll(searchCapabilities);
        this.sortCapabilities = new CSVString();
        this.sortCapabilities.addAll(sortCapabilities);
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "SearchCaps"))
    public CSV<String> getSearchCapabilities() {
        return searchCapabilities;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "SortCaps"))
    public CSV<String> getSortCapabilities() {
        return sortCapabilities;
    }

    @UpnpAction(out = @UpnpOutputArgument(name = "Id"))
    synchronized public UnsignedIntegerFourBytes getSystemUpdateID() {
        return systemUpdateID;
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    /**
     * Call this method after making changes to your content directory.
     * <p>
     * This will notify clients that their view of the content directory is potentially
     * outdated and has to be refreshed.
     * </p>
     */
    synchronized protected void changeSystemUpdateID() {
        Long oldUpdateID = getSystemUpdateID().getValue();
        systemUpdateID.increment(true);
        getPropertyChangeSupport().firePropertyChange(
                "SystemUpdateID",
                oldUpdateID,
                getSystemUpdateID().getValue()
        );
    }

    @UpnpAction(out = {
            @UpnpOutputArgument(name = "Result",
                                stateVariable = "A_ARG_TYPE_Result",
                                getterName = "getResult"),
            @UpnpOutputArgument(name = "NumberReturned",
                                stateVariable = "A_ARG_TYPE_Count",
                                getterName = "getCount"),
            @UpnpOutputArgument(name = "TotalMatches",
                                stateVariable = "A_ARG_TYPE_Count",
                                getterName = "getTotalMatches"),
            @UpnpOutputArgument(name = "UpdateID",
                                stateVariable = "A_ARG_TYPE_UpdateID",
                                getterName = "getContainerUpdateID")
    })
    public BrowseResult browse(
            @UpnpInputArgument(name = "ObjectID", aliases = "ContainerID") String objectId,
            @UpnpInputArgument(name = "BrowseFlag") String browseFlag,
            @UpnpInputArgument(name = "Filter") String filter,
            @UpnpInputArgument(name = "StartingIndex", stateVariable = "A_ARG_TYPE_Index") UnsignedIntegerFourBytes firstResult,
            @UpnpInputArgument(name = "RequestedCount", stateVariable = "A_ARG_TYPE_Count") UnsignedIntegerFourBytes maxResults,
            @UpnpInputArgument(name = "SortCriteria") String orderBy)
            throws ContentDirectoryException {

        SortCriterion[] orderByCriteria;
        try {
            orderByCriteria = SortCriterion.valueOf(orderBy);
        } catch (Exception ex) {
            throw new ContentDirectoryException(ContentDirectoryErrorCode.UNSUPPORTED_SORT_CRITERIA, ex.toString());
        }

        try {
            return browse(
                    objectId,
                    BrowseFlag.valueOrNullOf(browseFlag),
                    filter,
                    firstResult.getValue(), maxResults.getValue(),
                    orderByCriteria
            );
        } catch (ContentDirectoryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ContentDirectoryException(ErrorCode.ACTION_FAILED, ex.toString());
        }
    }

    /**
     * Implement this method to implement browsing of your content.
     * <p>
     * This is a required action defined by <em>ContentDirectory:1</em>.
     * </p>
     * <p>
     * You should wrap any exception into a {@link ContentDirectoryException}, so a propery
     * error message can be returned to control points.
     * </p>
     */
    public abstract BrowseResult browse(String objectID, BrowseFlag browseFlag,
                                        String filter,
                                        long firstResult, long maxResults,
                                        SortCriterion[] orderby) throws ContentDirectoryException;


    @UpnpAction(out = {
            @UpnpOutputArgument(name = "Result",
                                stateVariable = "A_ARG_TYPE_Result",
                                getterName = "getResult"),
            @UpnpOutputArgument(name = "NumberReturned",
                                stateVariable = "A_ARG_TYPE_Count",
                                getterName = "getCount"),
            @UpnpOutputArgument(name = "TotalMatches",
                                stateVariable = "A_ARG_TYPE_Count",
                                getterName = "getTotalMatches"),
            @UpnpOutputArgument(name = "UpdateID",
                                stateVariable = "A_ARG_TYPE_UpdateID",
                                getterName = "getContainerUpdateID")
    })
    public BrowseResult search(
            @UpnpInputArgument(name = "ContainerID", stateVariable = "A_ARG_TYPE_ObjectID") String containerId,
            @UpnpInputArgument(name = "SearchCriteria") String searchCriteria,
            @UpnpInputArgument(name = "Filter") String filter,
            @UpnpInputArgument(name = "StartingIndex", stateVariable = "A_ARG_TYPE_Index") UnsignedIntegerFourBytes firstResult,
            @UpnpInputArgument(name = "RequestedCount", stateVariable = "A_ARG_TYPE_Count") UnsignedIntegerFourBytes maxResults,
            @UpnpInputArgument(name = "SortCriteria") String orderBy)
            throws ContentDirectoryException {

        SortCriterion[] orderByCriteria;
        try {
            orderByCriteria = SortCriterion.valueOf(orderBy);
        } catch (Exception ex) {
            throw new ContentDirectoryException(ContentDirectoryErrorCode.UNSUPPORTED_SORT_CRITERIA, ex.toString());
        }

        try {
            return search(
                    containerId,
                    searchCriteria,
                    filter,
                    firstResult.getValue(), maxResults.getValue(),
                    orderByCriteria
            );
        } catch (ContentDirectoryException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ContentDirectoryException(ErrorCode.ACTION_FAILED, ex.toString());
        }
    }

    /**
     * Override this method to implement searching of your content.
     * <p>
     * The default implementation returns an empty result.
     * </p>
     */
    public BrowseResult search(String containerId, String searchCriteria, String filter,
                               long firstResult, long maxResults, SortCriterion[] orderBy) throws ContentDirectoryException {

        try {
            return new BrowseResult(new DIDLParser().generate(new DIDLContent()), 0, 0);
        } catch (Exception ex) {
            throw new ContentDirectoryException(ErrorCode.ACTION_FAILED, ex.toString());
        }
    }
}

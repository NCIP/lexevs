
package org.LexGrid.LexBIG.LexBIGService;

import java.io.Serializable;

import org.LexGrid.LexBIG.DataModel.Collections.ConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.LocalNameList;
import org.LexGrid.LexBIG.DataModel.Collections.NameAndValueList;
import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.ConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBInvocationException;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.LexGrid.LexBIG.Utility.Iterators.ResolvedConceptReferencesIterator;
import org.LexGrid.commonTypes.types.PropertyTypes;

/**
 * A coded node set represents a flat list of coded entries.
 */
public interface CodedNodeSet extends Serializable {

    public enum ActiveOption { ACTIVE_ONLY, INACTIVE_ONLY, ALL }
    
    public enum AnonymousOption { ANONYMOUS_ONLY, NON_ANONYMOUS_ONLY, ALL }
    
    public enum PropertyType { 
    	COMMENT(PropertyTypes.COMMENT.toString()), 
    	DEFINITION(PropertyTypes.DEFINITION.toString()), 
    	PRESENTATION(PropertyTypes.PRESENTATION.toString()), 
    	GENERIC(PropertyTypes.PROPERTY.toString());
    	
    	private String value;
    	
    	private PropertyType(String value){
    		this.value = value;
    	}

		@Override
		public String toString() {
			return this.value;
		}
    }
    
    public enum SearchDesignationOption { PREFERRED_ONLY, NON_PREFERRED_ONLY, ALL }

	/**
	 * Return a coded node set that represents the set of nodes in this
	 * set that are not included by the given set.
	 * 
	 * @param codesToRemove
	 *            List of codes to remove from the surrounding set.
	 * @return A new CodedNodeSet representing the difference.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet difference(CodedNodeSet codesToRemove)
			throws LBInvocationException,LBParameterException;

	/**
	 * Return a coded node set that represents the set of nodes
	 * this set and the provided node set have in common.
	 * 
	 * @param codes
	 *            Set of codes to intersect.
	 * @return A new CodedNodeSet representing the intersection result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet intersect(CodedNodeSet codes)
			throws LBInvocationException,LBParameterException;

	/**
	 * Return true if the supplied code reference is contained within
	 * this coded node set.
	 * 
	 * @param code
	 *            Coding scheme and code to test.
	 * @throws LBInvocationException,LBParameterException
	 */
	Boolean isCodeInSet(ConceptReference code)
			throws LBInvocationException,LBParameterException;

	/**
	 * Resolve an iterator over nodes matching the given criteria.
	 * 
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @return An iterator over matching entries.  Each entry will include
	 *            basic information for the node along with an embedded
	 *            object (e.g. concept) populated with requested properties.  
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the set.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferencesIterator resolve(
			SortOptionList sortOptions, LocalNameList propertyNames, PropertyType[] propertyTypes)
			throws LBInvocationException,LBParameterException;

	/**
	 * Resolve an iterator over nodes matching the given criteria,
	 * allowing for additional filters to be applied against the returned items.
	 * 
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param filterOptions
	 *            List of Filter extensions to apply during resolution. If
	 *            supplied, filters are applied in the order provided. Each name
	 *            in the list must correspond to the name of a Filter
	 *            description as registered to the associated service. Available
	 *            Filter descriptions can be retrieved through the LexBIGService
	 *            getFilterExtensions() method after being defined to the
	 *            LexBIGServiceManager extension registry.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @return An iterator over matching entries.  Each entry will include
	 *            basic information for the node along with an embedded
	 *            object (e.g. concept) populated with requested properties.
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the set.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferencesIterator resolve(
			SortOptionList sortOptions, LocalNameList filterOptions,
			LocalNameList propertyNames, PropertyType[] propertyTypes)
			throws LBInvocationException,LBParameterException;

	/**
	 * Resolve an iterator over nodes matching the given criteria,
	 * allowing for additional filters and optionally populating
	 * full objects (e.g. concept or instance) for each returned
	 * reference.
	 * 
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param filterOptions
	 *            List of Filter extensions to apply during resolution. If
	 *            supplied, filters are applied in the order provided. Each name
	 *            in the list must correspond to the name of a Filter
	 *            description as registered to the associated service. Available
	 *            Filter descriptions can be retrieved through the LexBIGService
	 *            getFilterExtensions() method after being defined to the
	 *            LexBIGServiceManager extension registry.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @param resolveObjects
	 *            True to build and embed a full object (e.g. concept) for each
	 *            referenced node in the returned results; false to return only
	 *            basic identifying information (e.g. code, coding scheme, 
	 *            and description). If false, additional properties for referenced 
	 *            entries can be resolved on an item-by-item basis as controlled 
	 *            by the application.     
	 * @return An iterator over matching entries.  Each entry will include
	 *            basic information for the node, such as code and coding scheme.
	 *            An embedded object (e.g. concept) will optionally be populated
	 *            for each item, based on the resolveObjects setting.  
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the set.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferencesIterator resolve(
			SortOptionList sortOptions, LocalNameList filterOptions,
			LocalNameList propertyNames, PropertyType[] propertyTypes, boolean resolveObjects)
			throws LBInvocationException,LBParameterException;
	
	/**
	 * Resolve the set to a list of nodes sorted by the supplied parameters,
	 * resolving all of the properties named in the list.
	 * 
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @param maxToReturn
	 *            Maximum number of entries to return.
	 * @return A list of node references, up to the maximum number specified.
	 *            Note that in the event that a maximum number 'n' is specified and
	 *            exactly 'n' items are resolved, there is currently no flag or
	 *            notification provided to indicate the requested list is fully
	 *            resolved.
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the set.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferenceList resolveToList(
			SortOptionList sortOptions, LocalNameList propertyNames,
			PropertyType[] propertyTypes, int maxToReturn)
			throws LBInvocationException,LBParameterException;

	/**
	 * Resolve the set to a list of nodes sorted by the supplied parameters,
	 * resolving all of the properties named in the list, and allowing for
	 * additional filters to be applied against the returned items.
	 * 
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param filterOptions
	 *            List of Filter extensions to apply during resolution. If
	 *            supplied, filters are applied in the order provided. Each name
	 *            in the list must correspond to the name of a Filter
	 *            description as registered to the associated service. Available
	 *            Filter descriptions can be retrieved through the LexBIGService
	 *            getFilterExtensions() method after being defined to the
	 *            LexBIGServiceManager extension registry.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @param maxToReturn
	 *            Maximum number of entries to return.
	 * @return A list of node references, up to the maximum number specified.
	 *            Note that in the event that a maximum number 'n' is specified and
	 *            exactly 'n' items are resolved, there is currently no flag or
	 *            notification provided to indicate the requested list is fully
	 *            resolved.
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the set.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferenceList resolveToList(SortOptionList sortOptions,
			LocalNameList filterOptions, LocalNameList propertyNames,
			PropertyType[] propertyTypes, int maxToReturn)
			throws LBInvocationException,LBParameterException;

	/**
	 * Resolve the set to a list of nodes sorted by the supplied parameters,
	 * resolving all of the properties named in the list, and allowing for
	 * additional filters to be applied against the returned items.
	 * 
	 * @param sortOptions
	 *            List of sort options to apply during resolution. If supplied,
	 *            the sort algorithms will be applied in the order provided. Any
	 *            algorithms not valid to be applied in context of node set
	 *            iteration, as specified in the sort extension description,
	 *            will result in a parameter exception. Available algorithms can
	 *            be retrieved through the LexBIGService getSortExtensions()
	 *            method after being defined to the LexBIGServiceManager
	 *            extension registry.
	 * @param filterOptions
	 *            List of Filter extensions to apply during resolution. If
	 *            supplied, filters are applied in the order provided. Each name
	 *            in the list must correspond to the name of a Filter
	 *            description as registered to the associated service. Available
	 *            Filter descriptions can be retrieved through the LexBIGService
	 *            getFilterExtensions() method after being defined to the
	 *            LexBIGServiceManager extension registry.
	 * @param propertyNames
	 *            Local names of properties to resolve.  If not empty and not
	 *            null, only properties matching the given names are included
	 *            for resolved nodes. 
     * @param propertyTypes
	 *            Indicates whether to resolve only specific property categories,
	 *            regardless of the assigned name.  Any of the enumerated PropertyType
	 *            values can be specified.  If not empty and not null, only
	 *            properties matching the given types are included for resolved
	 *            nodes.
	 * @param resolveObjects
	 *            True to build and embed a full object (e.g. concept) for each
	 *            referenced node in the returned results; false to return only
	 *            basic identifying information (e.g. code, coding scheme, 
	 *            and description). If false, additional properties for referenced 
	 *            entries can be resolved on an item-by-item basis as controlled 
	 *            by the application.     
	 * @param maxToReturn
	 *            Maximum number of entries to return.
	 * @return A list of node references, up to the maximum number specified.
	 *            Note that in the event that a maximum number 'n' is specified and
	 *            exactly 'n' items are resolved, there is currently no flag or
	 *            notification provided to indicate the requested list is fully
	 *            resolved.
	 *            <p>
	 *            Note that while the class of the returned value appears
	 *            to imply concepts only, each contained reference inherits
	 *            from the more general CodedNodeReference and is capable
	 *            of representing any type of node contained by the set.
	 * @throws LBInvocationException,LBParameterException
	 */
	ResolvedConceptReferenceList resolveToList(SortOptionList sortOptions,
			LocalNameList filterOptions, LocalNameList propertyNames,
			PropertyType[] propertyTypes, boolean resolveObjects, int maxToReturn)
			throws LBInvocationException,LBParameterException;	
	
	/**
	 * Restrict the set to the list of codes in the supplied reference
	 * list
	 * 
	 * @param codeList
	 *            The list of codes to filter on.
	 * @return A new CodedNodeSet representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet restrictToCodes(ConceptReferenceList codeList)
			throws LBInvocationException,LBParameterException;

	/**
	 * Restrict the list to the set of nodes with designations matching
	 * the supplied text, interpreted using the supplied matching algorithm
	 * and language.
	 * 
	 * @param matchText
	 *            Filter String - syntax is determined by the match algorithm
	 * @param preferredOnly
	 *            True means only use preferred designations, false means all.
	 * @param matchAlgorithm
	 *            Local name of the match algorithm - possible algorithms are
	 *            returned in LexBigService.getMatchAlgorithms().
	 * @param language
	 *            Language of search string. If missing, use the default
	 *            language specified in the context.
	 * @return A new CodedNodeSet representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 * @deprecated see restrictToMatchingDesignations(String, SearchDesignationOption, String, String)
	 */
	CodedNodeSet restrictToMatchingDesignations(String matchText,
			boolean preferredOnly, String matchAlgorithm, String language)
			throws LBInvocationException,LBParameterException;

	/**
	 * Restrict the list to the set of nodes with designations matching
	 * the supplied text, interpreted using the supplied matching algorithm
	 * and language.
	 * 
	 * @param matchText
	 *            Filter String - syntax is determined by the match algorithm
	 * @param option
	 *            Indicates the designations to search (one of the enumerated
	 *            type SearchDesignationOption).
	 * @param matchAlgorithm
	 *            Local name of the match algorithm - possible algorithms are
	 *            returned in LexBigService.getMatchAlgorithms().
	 * @param language
	 *            Language of search string. If missing, use the default
	 *            language specified in the context.
	 * @return A new CodedNodeSet representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet restrictToMatchingDesignations(String matchText,
			SearchDesignationOption option, String matchAlgorithm, String language)
			throws LBInvocationException,LBParameterException;

	/**
	 * Remove all elements from the set that do not have one or more properties
	 * that match the supplied property names, type, and text.  Text values are
	 * compared using the provided matching algorithm and language.
	 * <p>
	 * Note that while property name and type can be the same, the API allows
	 * for them to differ.  While property name can vary from source to source,
	 * all properties are mapped to normalized property types or categories as
	 * established by the LexGrid model ('Presentations', 'Definitions',
	 * 'Comments', and 'Generic' properties).  As an example, a Presentation
	 * property may be named 'displayText').
	 * <p>
	 * This method allows for query based on property name, type, or both.
	 * However, at least one name or type must be specified.
	 * 
	 * @param propertyNames
	 *            Indicates the local names of properties to match.  To be recognized,
	 *            each provided name must be defined in the coding scheme metadata as
	 *            part of the registered supported properties.  If empty or null,
	 *            all names are evaluated for the specified property types.
	 *            <p>
	 *            Note that the meta-property 'conceptCode' can be specified
	 *            in addition to specific named properties defined by the code
	 *            system.
	 *            <p>
	 *            If 'conceptCode' is specified, the matchAlgorithms 'exactMatch', 
	 *            'contains' and 'luceneQuery' are allowed.  Any other request 
	 *            results in 'luceneQuery' being used.<p>
	 * @param propertyTypes
	 *            Indicates whether to match specific property categories, regardless
	 *            of the assigned name.  Any of the enumerated PropertyType values
	 *            can be specified.  If empty or null, properties of all types are
	 *            evaluated.
	 * @param matchText
	 *            Property text to match - syntax is determined by the
	 *            algorithm.
	 * @param matchAlgorithm
	 *            Local name of the match algorithm - possible algorithms are
	 *            returned in LexBigService.getMatchAlgorithms().
	 * @param language
	 *            Language of search string. If missing, use the default
	 *            language specified in the context.
	 * @return A new CodedNodeSet representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet restrictToMatchingProperties(
			LocalNameList propertyNames, PropertyType[] propertyTypes,
			String matchText, String matchAlgorithm, String language)
			throws LBInvocationException,LBParameterException;

	/**
	 * Remove all elements from the set that do not have one or more properties
	 * that match the given criteria.
	 * <p>
	 * Note that while property name and type can be the same, the API allows
	 * for them to differ.  While property name can vary from source to source,
	 * all properties are mapped to normalized property types or categories as
	 * established by the LexGrid model ('Presentations', 'Definitions',
	 * 'Comments', and 'Generic' properties).  As an example, a Presentation
	 * property may be named 'displayText').
	 * <p>
	 * This method allows for query based on property name, type, or both.
	 * However, at least one name or type must be specified.
	 * 
	 * @param propertyNames
	 *            Indicates the local names of properties to match.  To be recognized,
	 *            each provided name must be defined in the coding scheme metadata as
	 *            part of the registered supported properties.  If empty or null,
	 *            all names are evaluated for the specified property types.
	 *            <p>
	 *            Note that the meta-property 'conceptCode' can be specified
	 *            in addition to specific named properties defined by the code
	 *            system.
	 *            <p>
	 *            If 'conceptCode' is specified, the matchAlgorithms 'exactMatch', 
	 *            'contains' and 'luceneQuery' and 'RegExp' are allowed.  Any other request 
	 *            results in 'luceneQuery' being used.<p>
	 * @param propertyTypes
	 *            Indicates whether to match specific property categories, regardless
	 *            of the assigned name.  Any of the enumerated PropertyType values
	 *            can be specified.  If empty or null, properties of all types are
	 *            evaluated.
	 * @param sourceList
	 *            Local names of sources to match; each must be defined in the
	 *            supported sources for the coding scheme. Returned values must
	 *            match at least one of the specified values. A null or empty
	 *            value indicates to match against all available sources.
	 * @param contextList
	 *            Local names of usage contexts to match; each must be defined
	 *            in the supported contexts for the coding scheme. Returned
	 *            values must match at least one of the specified values. A null
	 *            or empty value indicates to match against all available
	 *            contexts.
	 * @param qualifierList
	 *            Name/value pairings of property qualifiers to match. Each name
	 *            must be defined in the supported property qualifiers for the
	 *            coding scheme. Returned values must match at least one of the
	 *            name/value combinations. A null or empty value indicates to
	 *            match against all property qualifiers.
	 * @param matchText
	 *            Property text to match - syntax is determined by the
	 *            algorithm.
	 * @param matchAlgorithm
	 *            Local name of the match algorithm - possible algorithms are
	 *            returned in LexBigService.getMatchAlgorithms().
	 * @param language
	 *            Language of search string. If missing, use the default
	 *            language specified in the context.
	 * @return A new CodedNodeSet representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet restrictToMatchingProperties(
			LocalNameList propertyNames, PropertyType[] propertyTypes,
			LocalNameList sourceList, LocalNameList contextList, NameAndValueList qualifierList,
			String matchText, String matchAlgorithm, String language)
			throws LBInvocationException,LBParameterException;

	/**
	 * Remove all elements from the set that do not have one or more properties
	 * that match the supplied property list.
	 * <p>
	 * Note that while property name and type can be the same, the API allows
	 * for them to differ.  While property name can vary from source to source,
	 * all properties are mapped to normalized property types or categories as
	 * established by the LexGrid model ('Presentations', 'Definitions',
	 * 'Comments', and 'Generic' properties).  As an example, a Presentation
	 * property may be named 'displayText').
     * <p>
     * This method allows for query based on property name, type, or both.
     * However, at least one name or type must be specified.
	 * 
	 * @param propertyList
	 *            Local names of properties to use in restriction; each must be
	 *            defined in the supported properties for the coding scheme.
     * @param propertyTypes
     *            Indicates whether to match specific property categories, regardless
     *            of the assigned name.  Any of the enumerated PropertyType values
     *            can be specified.  If empty or null, properties of all types are
     *            evaluated.                 
	 *
	 * @return A new CodedNodeSet representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet restrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes)
			throws LBInvocationException,LBParameterException;

	/**
	 * Remove all elements from the set that do not have one or more properties
	 * that match the given criteria.
	 * <p>
	 * Note that while property name and type can be the same, the API allows
	 * for them to differ.  While property name can vary from source to source,
	 * all properties are mapped to normalized property types or categories as
	 * established by the LexGrid model ('Presentations', 'Definitions',
	 * 'Comments', and 'Generic' properties).  As an example, a Presentation
	 * property may be named 'displayText').
     * <p>
     * This method allows for query based on property name, type, or both.
     * However, at least one name or type must be specified.
	 * 
	 * @param propertyList
	 *            Local names of properties to use in restriction; each must be
	 *            defined in the supported properties for the coding scheme.
     * @param propertyTypes
     *            Indicates whether to match specific property categories, regardless
     *            of the assigned name.  Any of the enumerated PropertyType values
     *            can be specified.  If empty or null, properties of all types are
     *            evaluated.   
	 * @param sourceList
	 *            Local names of sources to match; each must be defined in the
	 *            supported sources for the coding scheme. Returned values must
	 *            match at least one of the specified values. A null or empty
	 *            value indicates to match against all available sources.
	 * @param contextList
	 *            Local names of usage contexts to match; each must be defined
	 *            in the supported contexts for the coding scheme. Returned
	 *            values must match at least one of the specified values. A null
	 *            or empty value indicates to match against all available
	 *            contexts.
	 * @param qualifierList
	 *            Name/value pairings of property qualifiers to match. Each name
	 *            must be defined in the supported property qualifiers for the
	 *            coding scheme. Returned values must match at least one of the
	 *            name/value combinations. A null or empty value indicates to
	 *            match against all property qualifiers.
	 * @return A new CodedNodeSet representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet restrictToProperties(LocalNameList propertyList, PropertyType[] propertyTypes,
			LocalNameList sourceList, LocalNameList contextList,
			NameAndValueList qualifierList) throws LBInvocationException,LBParameterException;

	/**
	 * Restrict the set to nodes matching the given status criteria.
	 * 
	 * @param activeOption
	 *            Indicates whether to include active codes, inactive codes,
	 *            or both in the resolved result set (one of the enumerated type
	 *            ActiveOption).  This is matched against the 'isActive' field for
	 *            CodedEntry instances in the code system.
	 * @param status
	 *            Indicates zero or more status values to match.  Provided
	 *            values are compared using an exact match algorithm against
	 *            the 'status' field for matching entities. If null or
	 *            empty, the restriction is evaluated based only on the
	 *            specified activeOption.
	 * @return A new CodedNodeSet representing the filtered result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet restrictToStatus(ActiveOption activeOption, String[] status)
			throws LBInvocationException,LBParameterException;
	
	/**
	 * Restrict the set to anonymous (or non-anonymous) nodes.
	 * 
	 * @param anonymousOption
	 * 			AnonymousOption.ANONYMOUS_ONLY		: Only Anonymous nodes
	 * 			AnonymousOption.NON_ANONYMOUS_ONLY  : Only nodes that are either not anonymous, or unspecified
	 * 			AnonymousOption.ALL					: All nodes
	 * 
	 * @return A new CodedNodeSet representing the filtered result.
	 * 
	 * @throws LBInvocationException the LB invocation exception
	 * @throws LBParameterException the LB parameter exception
	 */
	CodedNodeSet restrictToAnonymous(AnonymousOption anonymousOption) throws LBInvocationException, LBParameterException;

	/**
	 * Return the set union of all of the codes in the containing or the
	 * referenced set
	 * 
	 * @param codes
	 *            Codes to add to the union
	 * @return A new CodedNodeSet representing the merged result.
	 * @throws LBInvocationException,LBParameterException
	 */
	CodedNodeSet union(CodedNodeSet codes) throws LBInvocationException,LBParameterException;

	/**
	 * @param crl is the list of concepts to restrict to
	 * @return A new CodedNodeSet restriction based on non entity mapping nodes
	 * @throws LBParameterException
	 * @throws LBInvocationException
	 */
	CodedNodeSet restrictToMappingCodes(ConceptReferenceList crl) throws LBParameterException, LBInvocationException;

}
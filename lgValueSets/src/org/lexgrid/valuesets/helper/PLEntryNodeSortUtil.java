
package org.lexgrid.valuesets.helper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.lexgrid.valuesets.dto.ResolvedPickListEntry;

public class PLEntryNodeSortUtil {
	
	public static final int ASCENDING = 1;
	public static final int DESCENDING = 2;
	public static final int CUSTOM = 3;

/**
	 * comparator for sorting in descending order of the pick text.
	 */
@SuppressWarnings("unchecked")
	static Comparator<ResolvedPickListEntry> SortAscending = new Comparator() {

		public int compare(Object o1, Object o2) {
			if (o1 instanceof ResolvedPickListEntry && o2 instanceof ResolvedPickListEntry) {
				ResolvedPickListEntry entry1 = (ResolvedPickListEntry) o1;
				ResolvedPickListEntry entry2 = (ResolvedPickListEntry) o2;

				String pickText1 = entry1.getPickText();
				String pickText2 = entry2.getPickText();
				
				if( pickText1 == null || pickText2 == null )
					return -1;
				
				return pickText1.compareTo(pickText2);
			}
			return 1;
		}
	};
	
	/**
	 * comparator for sorting in descending order of the pick text.
	 */
	@SuppressWarnings("unchecked")
	static Comparator<ResolvedPickListEntry> SortDescending = new Comparator() {

		public int compare(Object o1, Object o2) {
			if (o1 instanceof ResolvedPickListEntry && o2 instanceof ResolvedPickListEntry) {
				ResolvedPickListEntry entry1 = (ResolvedPickListEntry) o1;
				ResolvedPickListEntry entry2 = (ResolvedPickListEntry) o2;

				String pickText1 = entry1.getPickText();
				String pickText2 = entry2.getPickText();
				
				if( pickText1 == null || pickText2 == null )
					return -1;
				
				return pickText2.compareTo(pickText1);
			}
			return 1;
		}
	};
	
	/**
	 * comparator for sorting in custom order.
	 */
	@SuppressWarnings("unchecked")
	static Comparator<ResolvedPickListEntry> CustomSort = new Comparator() {

		public int compare(Object o1, Object o2) {
			if (o1 instanceof ResolvedPickListEntry && o2 instanceof ResolvedPickListEntry) {
				ResolvedPickListEntry entry1 = (ResolvedPickListEntry) o1;
				ResolvedPickListEntry entry2 = (ResolvedPickListEntry) o2;

				Integer order1 = entry1.getEntryOrder();
				Integer order2 = entry2.getEntryOrder();
				
				if( order1 == null || order2 == null )
					return -1;
				
				return order1.compareTo(order2);
			}
			return 1;
		}
	};
	
	public static ResolvedPickListEntry[] sort(List<ResolvedPickListEntry> pickListEntryList, Integer sortType ) {
		
		if( pickListEntryList != null ) {
			
			Comparator<ResolvedPickListEntry> comparator = null;
			
			if( sortType == null || sortType == PLEntryNodeSortUtil.ASCENDING) {
				comparator = SortAscending;
			} else if( sortType == PLEntryNodeSortUtil.DESCENDING) {
				comparator = SortDescending;
			} else if( sortType == PLEntryNodeSortUtil.CUSTOM) {
				comparator = CustomSort;
			}
			
			ResolvedPickListEntry[] plEntries = new ResolvedPickListEntry[pickListEntryList.size()];
			Arrays.sort(pickListEntryList.toArray(plEntries), comparator);
			
			return plEntries;
		}
		
		return null;
	}
	
	public static ResolvedPickListEntry[] sort(ResolvedPickListEntry[] pickListEntryList, Integer sortType) {
		
		Comparator<ResolvedPickListEntry> comparator = null;
		
		if( sortType == null || sortType == PLEntryNodeSortUtil.ASCENDING) {
			comparator = SortAscending;
		} else if( sortType == PLEntryNodeSortUtil.DESCENDING) {
			comparator = SortDescending;
		} else if( sortType == PLEntryNodeSortUtil.CUSTOM) {
			comparator = CustomSort;
		}
		
		Arrays.sort(pickListEntryList, comparator);
		
		return pickListEntryList;
	}
}
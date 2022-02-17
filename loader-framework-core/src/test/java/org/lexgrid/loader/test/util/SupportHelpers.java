
package org.lexgrid.loader.test.util;

import org.lexgrid.loader.data.codingScheme.CodingSchemeIdSetter;
import org.lexgrid.loader.processor.support.RelationContainerResolver;

public class SupportHelpers {

	public static class TestCodingSchemeIdSetter 
	implements CodingSchemeIdSetter {
		public String getCodingSchemeName() {
			return "testCodingSchemeName";
		}

		public String getCodingSchemeUri() {
			return "testCodingSchemeUri";
		}

		public String getCodingSchemeVersion() {
			return "testCodingSchemeVersion";
		}
	}
	
	public static class TestRelationContainerResolver 
	implements RelationContainerResolver {
		public String getRelationContainer(Object item) {
			return "testContainerName";
		}	
	}
}
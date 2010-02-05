package test.util;

import org.lexgrid.loader.data.codingScheme.CodingSchemeNameSetter;
import org.lexgrid.loader.processor.support.RelationContainerResolver;

public class SupportHelpers {

	public static class TestCodingSchemeNameSetter 
	implements CodingSchemeNameSetter {
		public String getCodingSchemeName() {
			return "testCodingSchemeName";
		}
	}
	
	public static class TestRelationContainerResolver 
	implements RelationContainerResolver {
		public String getRelationContainer(Object item) {
			return "testContainerName";
		}	
	}
}

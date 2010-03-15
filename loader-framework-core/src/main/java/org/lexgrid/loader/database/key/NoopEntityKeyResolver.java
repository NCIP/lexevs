package org.lexgrid.loader.database.key;

public class NoopEntityKeyResolver implements EntityKeyResolver {

	public String resolveKey(String codingSchemeId, String entityCode,
			String entityCodeNamespace) {
		return null;
	}
}

package org.cts2.profile.read;

import org.cts2.service.core.NameOrURI;
import org.cts2.service.core.QueryControl;
import org.cts2.service.core.ReadContext;

public interface BaseReadService<T> {

	public T read(NameOrURI id, QueryControl queryControl, ReadContext readContext);
	
	public boolean exists(NameOrURI id, QueryControl queryControl, ReadContext readContext);
}
	
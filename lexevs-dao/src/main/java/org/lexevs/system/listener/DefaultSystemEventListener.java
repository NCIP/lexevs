
package org.lexevs.system.listener;

import org.lexevs.system.event.SystemEventListener;

public class DefaultSystemEventListener implements SystemEventListener {

	@Override
	public void onRemoveCodingSchemeResourceFromSystemEvent(String uri,
			String version) {
		//pass-through
	}
}
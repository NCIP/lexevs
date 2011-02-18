package org.cts2.test;

import org.LexGrid.LexBIG.test.BaseContentLoadingInMemoryTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration( 
		value = "/cts2-application-config-test.xml"
)
public class BaseCts2IntegrationTest extends BaseContentLoadingInMemoryTest {

}

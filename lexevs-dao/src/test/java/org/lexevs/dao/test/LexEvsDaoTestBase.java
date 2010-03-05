package org.lexevs.dao.test;

import static org.junit.Assert.assertNotNull;

import org.LexGrid.commonTypes.Text;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-test.xml"})
public class LexEvsDaoTestBase {

	@Test
	public void testConfig(){
		assertNotNull(this);
	}
	
	protected Text buildText(String content){
		return this.buildText(content, null);
	}
	
	protected Text buildText(String content, String format){
		Text text = new Text();
		text.setContent(content);
		text.setDataType(format);
		return text;
	}
	
}

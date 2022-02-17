
package org.lexevs.dao.test;

import static org.junit.Assert.assertNotNull;

import org.LexGrid.commonTypes.Text;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * The Class LexEvsDaoTestBase.
 * 
 * @author <a href="mailto:kevin.peterson@mayo.edu">Kevin Peterson</a>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/lexevsDao-test.xml"})
public class LexEvsDaoTestBase {

	/**
	 * Test config.
	 */
	@Test
	public void testConfig(){
		assertNotNull(this);
	}
	
	/**
	 * Builds the text.
	 * 
	 * @param content the content
	 * 
	 * @return the text
	 */
	protected Text buildText(String content){
		return this.buildText(content, null);
	}
	
	/**
	 * Builds the text.
	 * 
	 * @param content the content
	 * @param format the format
	 * 
	 * @return the text
	 */
	protected Text buildText(String content, String format){
		Text text = new Text();
		text.setContent(content);
		text.setDataType(format);
		return text;
	}
	
}
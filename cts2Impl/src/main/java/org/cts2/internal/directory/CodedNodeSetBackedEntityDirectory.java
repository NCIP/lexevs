package org.cts2.internal.directory;

import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.cts2.entity.EntityDirectory;
import org.cts2.internal.mapper.BeanMapper;

public class CodedNodeSetBackedEntityDirectory extends EntityDirectory {

	private static final long serialVersionUID = -235309050016482174L;
	
	private CodedNodeSet codedNodeSet;
	private BeanMapper beanMapper;
	
	public CodedNodeSetBackedEntityDirectory(CodedNodeSet codedNodeSet, BeanMapper beanMapper){
		this.codedNodeSet = codedNodeSet;
		this.beanMapper = beanMapper;
	}
}

package org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator;

import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.Impl.LexBIGServiceImpl;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTree;
import org.LexGrid.LexBIG.Impl.Extensions.tree.model.LexEvsTreeNode;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeService;
import org.LexGrid.LexBIG.Impl.Extensions.tree.service.TreeServiceFactory;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;


public class TestTreeExtension {
	LexBIGService lbSvc = null;
	public TestTreeExtension(LexBIGService lbSvc) {
		this.lbSvc = lbSvc;
	}

    public List<LexEvsTreeNode> getChildren(String codingScheme, String version, String code, String namespace) {
        List<LexEvsTreeNode> list = new ArrayList<LexEvsTreeNode>();
        try {
			CodingSchemeVersionOrTag versionOrTag = new CodingSchemeVersionOrTag();
			if (version != null) versionOrTag.setVersion(version);
			TreeService treeService = TreeServiceFactory.getInstance().getTreeService(lbSvc);
			LexEvsTree lexEvsTree = null;
			try {
				lexEvsTree = treeService.getTree(codingScheme, versionOrTag, code);
			} catch (Exception ex) {
				System.out.println(	"treeService.getTree failed.");
				return null;
			}
			LexEvsTreeNode focus_node = null;
			focus_node = lexEvsTree.findNodeInTree(code);
			if (focus_node == null) {
				return null;
			}
			LexEvsTreeNode.ExpandableStatus focus_node_status = focus_node.getExpandableStatus();
			if (focus_node_status == LexEvsTreeNode.ExpandableStatus.IS_EXPANDABLE) {
				ChildTreeNodeIterator itr = focus_node.getChildIterator();
				if (itr == null) {
					System.out.println(	"itr == null???");
				} else {
					try {
						while(itr.hasNext()){
							LexEvsTreeNode child = itr.next();
							list.add(child);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						System.out.println("WARNING: ChildTreeNodeIterator throws exception...");
					}
			    }
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
    }

    public static void main(String[] args) {
		String service_URL = "http://lexevsapi6.nci.nih.gov/lexevsapi63";
		LexBIGService lbSvc = LexBIGServiceImpl.defaultInstance();
		String codingScheme = "NCI_Thesaurus";
		String version = "15.12d";
		String code = "C1404";
		String namespace = "NCI_Thesaurus";
		List<LexEvsTreeNode> list = new TestTreeExtension(lbSvc).getChildren(codingScheme, version, code, namespace);
	}
}


/*
Registration of SecurityToken was successful.
LB_WARN_LOGGER WARN - 2016-01-19 08:55:54,661 - [LOG_ID 10] No user defined Cach
e Settings available, using defaults.
SLF4J: This version of SLF4J requires log4j version 1.2.12 or later. See also ht
tp://www.slf4j.org/codes.html#log4j_version
[main] WARN  config.ConfigurationFactory  - No configuration found. Configuring
ehcache from ehcache-failsafe.xml  found in the classpath: jar:file:/G:/LexEVSAP
I/LexEVSAPI_Client/TEST/lib/ehcache-core-2.0.1.jar!/ehcache-failsafe.xml
net.sf.ehcache.config.ConfigurationFactory WARN - 2016-01-19 08:55:57,267 - No c
onfiguration found. Configuring ehcache from ehcache-failsafe.xml  found in the
classpath: jar:file:/G:/LexEVSAPI/LexEVSAPI_Client/TEST/lib/ehcache-core-2.0.1.j
ar!/ehcache-failsafe.xml
LB_WARN_LOGGER WARN - 2016-01-19 08:56:02,729 - [LOG_ID 12] tree-utility is not
on the classpath or could not be loaded as an Extension.
org.LexGrid.LexBIG.Exceptions.LBParameterException: A Generic Extension by that
name is already registered
        at org.LexGrid.LexBIG.Impl.Extensions.ExtensionRegistryImpl.registerGene
ricExtension(ExtensionRegistryImpl.java:337)
        at org.LexGrid.LexBIG.Impl.LexBIGServiceImpl.registerExtensions(LexBIGSe
rviceImpl.java:689)
        at org.LexGrid.LexBIG.Impl.LexBIGServiceImpl.<init>(LexBIGServiceImpl.ja
va:198)
        at org.LexGrid.LexBIG.Impl.LexBIGServiceImpl.defaultInstance(LexBIGServi
ceImpl.java:162)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.pathtoroot.LexEvsPathToRo
otResolver.<init>(LexEvsPathToRootResolver.java:48)
        at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)

        at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstruct
orAccessorImpl.java:57)
        at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingC
onstructorAccessorImpl.java:45)
        at java.lang.reflect.Constructor.newInstance(Constructor.java:525)
        at org.springframework.beans.BeanUtils.instantiateClass(BeanUtils.java:1
47)
        at org.springframework.beans.factory.support.SimpleInstantiationStrategy
.instantiate(SimpleInstantiationStrategy.java:76)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBean
Factory.instantiateBean(AbstractAutowireCapableBeanFactory.java:990)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBean
Factory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:943)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBean
Factory.doCreateBean(AbstractAutowireCapableBeanFactory.java:485)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBean
Factory.createBean(AbstractAutowireCapableBeanFactory.java:456)
        at org.springframework.beans.factory.support.AbstractBeanFactory$1.getOb
ject(AbstractBeanFactory.java:294)
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistr
y.getSingleton(DefaultSingletonBeanRegistry.java:225)
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBe
an(AbstractBeanFactory.java:291)
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean
(AbstractBeanFactory.java:193)
        at org.springframework.beans.factory.support.BeanDefinitionValueResolver
.resolveReference(BeanDefinitionValueResolver.java:322)
        at org.springframework.beans.factory.support.BeanDefinitionValueResolver
.resolveValueIfNecessary(BeanDefinitionValueResolver.java:106)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBean
Factory.applyPropertyValues(AbstractAutowireCapableBeanFactory.java:1360)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBean
Factory.populateBean(AbstractAutowireCapableBeanFactory.java:1118)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBean
Factory.doCreateBean(AbstractAutowireCapableBeanFactory.java:517)
        at org.springframework.beans.factory.support.AbstractAutowireCapableBean
Factory.createBean(AbstractAutowireCapableBeanFactory.java:456)
        at org.springframework.beans.factory.support.AbstractBeanFactory$1.getOb
ject(AbstractBeanFactory.java:294)
        at org.springframework.beans.factory.support.DefaultSingletonBeanRegistr
y.getSingleton(DefaultSingletonBeanRegistry.java:225)
        at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBe
an(AbstractBeanFactory.java:291)
        at org.springframework.beans.factory.support.AbstractBeanFactory.getBean
(AbstractBeanFactory.java:193)
        at org.springframework.beans.factory.support.DefaultListableBeanFactory.
preInstantiateSingletons(DefaultListableBeanFactory.java:585)
        at org.springframework.context.support.AbstractApplicationContext.finish
BeanFactoryInitialization(AbstractApplicationContext.java:913)
        at org.springframework.context.support.AbstractApplicationContext.refres
h(AbstractApplicationContext.java:464)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.service.ApplicationContextFac
tory.<init>(ApplicationContextFactory.java:66)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.service.ApplicationContextFac
tory.getInstance(ApplicationContextFactory.java:51)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.JdbcLexEvsTreeDao.setData
Source(JdbcLexEvsTreeDao.java:246)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.JdbcLexEvsTreeDao.getChil
dren(JdbcLexEvsTreeDao.java:225)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeI
terator.pageChildren(PagingChildNodeIterator.java:161)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeI
terator.pageIfNecessary(PagingChildNodeIterator.java:226)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeI
terator.hasNext(PagingChildNodeIterator.java:195)
        at TestTreeExtension.getChildren(TestTreeExtension.java:67)
        at TestTreeExtension.main(TestTreeExtension.java:90)
java.lang.RuntimeException: org.LexGrid.LexBIG.Exceptions.LBParameterException:
No Coding Scheme Found for Name: NCI_Thesaurus Version 15.12d.
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.sqlbuilder.GetChildrenSql
Builder.doBuildSql(GetChildrenSqlBuilder.java:111)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.sqlbuilder.GetChildrenSql
Builder.buildSql(GetChildrenSqlBuilder.java:73)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.JdbcLexEvsTreeDao.getChil
dren(JdbcLexEvsTreeDao.java:227)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeI
terator.pageChildren(PagingChildNodeIterator.java:161)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeI
terator.pageIfNecessary(PagingChildNodeIterator.java:226)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.iterator.PagingChildNodeI
terator.hasNext(PagingChildNodeIterator.java:195)
        at TestTreeExtension.getChildren(TestTreeExtension.java:67)
        at TestTreeExtension.main(TestTreeExtension.java:90)
Caused by: org.LexGrid.LexBIG.Exceptions.LBParameterException: No Coding Scheme
Found for Name: NCI_Thesaurus Version 15.12d.
        at org.LexGrid.LexBIG.Utility.ServiceUtility.getAbsoluteCodingSchemeVers
ionReference(ServiceUtility.java:173)
        at org.LexGrid.LexBIG.Impl.Extensions.tree.dao.sqlbuilder.GetChildrenSql
Builder.doBuildSql(GetChildrenSqlBuilder.java:100)
        ... 7 more
WARNING: ChildTreeNodeIterator throws exception...

*/
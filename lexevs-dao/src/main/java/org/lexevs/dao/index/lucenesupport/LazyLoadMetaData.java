package org.lexevs.dao.index.lucenesupport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Core.AbsoluteCodingSchemeVersionReference;
import org.LexGrid.LexBIG.Exceptions.LBParameterException;
import org.lexevs.dao.index.lucenesupport.LuceneDirectoryFactory.NamedDirectory;
import org.lexevs.dao.indexer.utility.CodingSchemeMetaData;
import org.lexevs.dao.indexer.utility.ConcurrentMetaData;
import org.lexevs.dao.indexer.utility.Utility;
import org.lexevs.locator.LexEvsServiceLocator;
import org.lexevs.registry.model.RegistryEntry;
import org.lexevs.registry.service.Registry.ResourceType;
import org.lexevs.system.constants.SystemVariables;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class LazyLoadMetaData implements
		ApplicationListener<ContextRefreshedEvent> {

	private LexEvsServiceLocator locator;
	private SystemVariables systemVariables;
	private LuceneDirectoryCreator directoryCreator;
	private LuceneMultiDirectoryFactory multiDirectoryFactory;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		this.locator = LexEvsServiceLocator.getInstance();
		try {
			lazyLoadMetadata();
		} catch (LBParameterException | IOException e) {
			throw new RuntimeException(e);
		}
	}



	public void lazyLoadMetadata() throws LBParameterException, IOException {
		ConcurrentMetaData concurrentMetaData = ConcurrentMetaData
				.getInstance();
		List<RegistryEntry> registeredSchemes = locator.getRegistry()
				.getAllRegistryEntriesOfType(ResourceType.CODING_SCHEME);
		List<NamedDirectory> namedDirectories = new ArrayList<NamedDirectory>();
		File indexDir = new File(systemVariables.getAutoLoadIndexLocation());
		for (File f : indexDir.listFiles()) {
			if (f.exists() && f.isDirectory()) {

				for (RegistryEntry re : registeredSchemes) {
					if (fileMatch(re, f)) {
						concurrentMetaData.add(reconciliateDbToIndex(re, f,
								namedDirectories, directoryCreator));
					}
				}
			}
		}

	}

	private CodingSchemeMetaData reconciliateDbToIndex(RegistryEntry re,
			File f, List<NamedDirectory> namedDirectories,
			LuceneDirectoryCreator directoryCreator)
			throws LBParameterException, IOException {
		//TODO do not build indexes if not there, but flag in the metadat object.
		CodingSchemeMetaData csMetaData = null;
		csMetaData = new CodingSchemeMetaData(re.getResourceUri(),
				re.getResourceVersion(), locator.getSystemResourceService()
						.getInternalCodingSchemeNameForUserCodingSchemeName(
								re.getResourceUri(), re.getResourceVersion()),
				makeNewDirectoryIfNone(re));

		return csMetaData;
	}

	private NamedDirectory makeNewDirectoryIfNone(RegistryEntry re)
			throws IOException {
		AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
		reference.setCodingSchemeURN(re.getResourceUri());
		reference.setCodingSchemeVersion(re.getResourceVersion());
		return multiDirectoryFactory.getNamedDirectory(Utility
				.getIndexName(reference));
	}

	private boolean fileMatch(RegistryEntry re, File f) {
		AbsoluteCodingSchemeVersionReference reference = new AbsoluteCodingSchemeVersionReference();
		reference.setCodingSchemeURN(re.getResourceUri());
		reference.setCodingSchemeVersion(re.getResourceVersion());
		if (Utility.getIndexName(reference).equals(f.getName())) {
			return true;
		}
		return false;
	}

	public SystemVariables getSystemVariables() {
		return systemVariables;
	}

	public void setSystemVariables(SystemVariables systemVariables) {
		this.systemVariables = systemVariables;
	}

	public LuceneDirectoryCreator getDirectoryCreator() {
		return directoryCreator;
	}

	public void setDirectoryCreator(LuceneDirectoryCreator directoryCreator) {
		this.directoryCreator = directoryCreator;
	}

	public LuceneMultiDirectoryFactory getMultiDirectoryFactory() {
		return multiDirectoryFactory;
	}

	public void setMultiDirectoryFactory(
			LuceneMultiDirectoryFactory multiDirectoryFactory) {
		this.multiDirectoryFactory = multiDirectoryFactory;
	}
	
	public LexEvsServiceLocator getLocator() {
		return locator;
	}

	public void setLocator(LexEvsServiceLocator locator) {
		this.locator = locator;
	}

}

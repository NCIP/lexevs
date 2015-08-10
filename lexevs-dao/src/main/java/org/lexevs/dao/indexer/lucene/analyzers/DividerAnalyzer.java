package org.lexevs.dao.indexer.lucene.analyzers;

import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.pattern.PatternTokenizer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.util.AttributeFactory;

public class DividerAnalyzer extends Analyzer {

	private static final String STRING_TOKENIZER_TOKEN = "<:>";

	public DividerAnalyzer() {
		// TODO Auto-generated constructor stub
	}

	public DividerAnalyzer(ReuseStrategy reuseStrategy) {
		super(reuseStrategy);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected TokenStreamComponents createComponents(String arg0) {
		Pattern pattern = Pattern.compile(STRING_TOKENIZER_TOKEN);
		final PatternTokenizer source = new PatternTokenizer(AttributeFactory.DEFAULT_ATTRIBUTE_FACTORY, pattern, -1);
		TokenStream filter = new StandardFilter(source);
		System.out.println("In pattern matching analyzer");
		return new TokenStreamComponents(source, filter);
	}

}

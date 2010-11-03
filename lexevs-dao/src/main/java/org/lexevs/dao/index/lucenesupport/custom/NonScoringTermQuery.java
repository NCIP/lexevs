/*
 * Copyright: (c) 2004-2010 Mayo Foundation for Medical Education and 
 * Research (MFMER). All rights reserved. MAYO, MAYO CLINIC, and the
 * triple-shield Mayo logo are trademarks and service marks of MFMER.
 *
 * Except as contained in the copyright notice above, or as used to identify 
 * MFMER as the author of this software, the trade names, trademarks, service
 * marks, or product names of the copyright holder shall not be used in
 * advertising, promotion or otherwise in connection with this software without
 * prior written authorization of the copyright holder.
 * 
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 * 		http://www.eclipse.org/legal/epl-v10.html
 * 
 */
package org.lexevs.dao.index.lucenesupport.custom;

import java.util.Set;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.ComplexExplanation;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.Similarity;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.Weight;
import org.apache.lucene.util.ToStringUtils;

public class NonScoringTermQuery extends TermQuery {

	private static final long serialVersionUID = 6122671428507075176L;

	public NonScoringTermQuery(Term t) {
		super(t);

	}

	private class MatchAllScorer extends Scorer {

		final IndexReader reader;
		int id;
		final int maxId;
		final float score;

		MatchAllScorer(IndexReader reader, Similarity similarity, Weight w) {
			super(similarity);
			this.reader = reader;
			id = -1;
			maxId = reader.maxDoc() - 1;
			score = w.getValue();
		}

		public Explanation explain(int doc) {
			return null;
		}

		public int doc() {
			return id;
		}

		public boolean next() {
			while (id < maxId) {
				id++;
				if (!reader.isDeleted(id)) {
					return true;
				}
			}
			return false;
		}

		public float score() {
			return score;
		}

		public boolean skipTo(int target) {
			id = target - 1;
			return next();
		}
	}

	private class MatchAllDocsWeight implements Weight {
		
		private static final long serialVersionUID = 8245890839430555571L;
		
		private Similarity similarity;
		private float queryWeight;
		private float queryNorm;

		public MatchAllDocsWeight(Searcher searcher) {
			this.similarity = searcher.getSimilarity();
		}

		public String toString() {
			return "MatchAllDocsWeight";
		}

		public Query getQuery() {
			return NonScoringTermQuery.this;
		}

		public float getValue() {
			return queryWeight;
		}

		public float sumOfSquaredWeights() {
			queryWeight = getBoost();
			return queryWeight * queryWeight;
		}

		public void normalize(float queryNorm) {
			this.queryNorm = queryNorm;
			queryWeight *= this.queryNorm;
		}

		public Scorer scorer(IndexReader reader) {
			return new MatchAllScorer(reader, similarity, this);
		}

		public Explanation explain(IndexReader reader, int doc) {
			// explain query weight
			Explanation queryExpl = new ComplexExplanation
			(true, getValue(), "MatchAllDocsQuery, product of:");
			if (getBoost() != 1.0f) {
				queryExpl.addDetail(new Explanation(getBoost(),"boost"));
			}
			queryExpl.addDetail(new Explanation(queryNorm,"queryNorm"));

			return queryExpl;
		}
	}

	protected Weight createWeight(Searcher searcher) {
		return new MatchAllDocsWeight(searcher);
	}

	public void extractTerms(Set terms) {
	}

	public String toString(String field) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("MatchAllDocsQuery");
		buffer.append(ToStringUtils.boost(getBoost()));
		return buffer.toString();
	}

	public boolean equals(Object o) {
		return super.equals(o);
	}

	public int hashCode() {
		return Float.floatToIntBits(getBoost()) ^ 0x1AA71190;
	}
}
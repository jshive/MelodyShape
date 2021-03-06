// Copyright (C) 2013, 2015-2016  Julián Urbano <urbano.julian@gmail.com>
// Distributed under the terms of the MIT License.

package jurbano.melodyshape.comparison.alignment;

import java.util.ArrayList;

import jurbano.melodyshape.comparison.NGram;
import jurbano.melodyshape.comparison.NGramComparer;

/**
 * An implementation of the Smith-Waterman alignment algorithm for sequences of {@link NGram}s.
 * 
 * @author Julián Urbano
 * @see NGram
 * @see NGramComparer
 */
public class LocalAligner implements MelodyAligner
{	
	protected NGramComparer comparer;
		
	/**
	 * Constructs a new {@code LocalAligner} with the specified {@link NGramComparer}. 
	 * 
	 * @param comparer the n-gram comparer to use.
	 */
	public LocalAligner(NGramComparer comparer) {
		this.comparer = comparer;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the {@link String} {@code "Local(comparer)"}, where
	 *         {@code comparer} is the name of the underlying
	 *         {@link NGramComparer}.
	 */
	@Override
	public String getName() {
		return "Local("+this.comparer.getName()+")";
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double align(ArrayList<NGram> s1, ArrayList<NGram> s2) {
		double[][] matrix = new double[s1.size() + 1][s2.size() + 1];
		
		double max = Double.NEGATIVE_INFINITY;
		for (int i = 1; i <= s1.size(); i++) {
			for (int j = 1; j <= s2.size(); j++) {
				double left = matrix[i - 1][j] + this.comparer.compare(s1.get(i - 1), null);
				double up = matrix[i][j - 1] + this.comparer.compare(null, s2.get(j - 1));
				double diag = matrix[i - 1][j - 1] + this.comparer.compare(s1.get(i - 1), s2.get(j - 1));
				matrix[i][j] = Math.max(0, Math.max(left, Math.max(up, diag)));
				if (max < matrix[i][j])
					max = matrix[i][j];
			}
		}
		
		return max / Math.min(s1.size(), s2.size());  // TODO: move normalization to algorithm?
	}
	
}

package org.cts2.internal.model.uri.restrict;

import com.google.common.collect.Iterables;

public class RestrictionUtils {
	
	@SuppressWarnings("unchecked")
	public static <T extends IterableRestriction<?>> T combineIterableRestrictions(Iterable<T> restrictions){
		return (T) combineRestrictions(Iterables.toArray(restrictions, IterableRestriction.class));
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends Restriction<?>> T combineRestrictions(Iterable<T> restrictions){
		return (T) combineRestrictions(Iterables.toArray(restrictions, Restriction.class));
	}

	public static <T> IterableRestriction<T> combineRestrictions(final IterableRestriction<T>... restrictions){
		return new IterableRestriction<T>(){

			@Override
			public Iterable<T> processRestriction(Iterable<T> state) {
				for(IterableRestriction<T> restriction : restrictions){
					state = restriction.processRestriction(state);
				}

				return state;
			}
		};
	}
	
	public static <T> Restriction<T> combineRestrictions(final Restriction<T>... restrictions){
		return new Restriction<T>(){

			@Override
			public T processRestriction(T state) {
				for(Restriction<T> restriction : restrictions){
					state = restriction.processRestriction(state);
				}

				return state;
			}
		};
	}
}

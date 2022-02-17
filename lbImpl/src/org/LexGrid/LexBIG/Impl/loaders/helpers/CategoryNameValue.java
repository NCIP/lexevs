
package org.LexGrid.LexBIG.Impl.loaders.helpers;

/**
 * @author m029206
 *
 */
public class CategoryNameValue<C, V> {
    
        private final C code;
        private final V categoryValue;

        public CategoryNameValue(C code, V category) {
          this.code = code;
          this.categoryValue = category;
        }

        public C getCode() { return code; }
        public V getCategoryValue() { return categoryValue; }

        @Override
        public int hashCode() { return code.hashCode() ^ categoryValue.hashCode(); }

        @SuppressWarnings("rawtypes")
        @Override
        public boolean equals(Object o) {
          if (!(o instanceof CategoryNameValue)) return false;
          CategoryNameValue catnv = (CategoryNameValue) o;
          return this.code.equals(catnv.getCode()) &&
                 this.categoryValue.equals(catnv.getCategoryValue());
        }

}
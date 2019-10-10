 package org.lexevs.dao.database.access.association.model;

import com.arangodb.entity.DocumentField;
import com.arangodb.entity.DocumentField.Type;

public class LexVertex {
		
		@DocumentField(Type.ID)
		private String id;

		@DocumentField(Type.KEY)
		private String code;

		@DocumentField(Type.REV)
		private String revision;

		private String namespace;
		
		public LexVertex(){
			super();
		}

		public LexVertex(String code, String namespace) {
			this.code = code;
			this.namespace = namespace;
		}

		/**
		 * @return the id
		 */
		public String getId() {
			return id;
		}

		/**
		 * @param id the id to set
		 */
		public void setId(String id) {
			this.id = id;
		}

		/**
		 * @return the code
		 */
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the revision
		 */
		public String getRevision() {
			return revision;
		}

		/**
		 * @param revision the revision to set
		 */
		public void setRevision(String revision) {
			this.revision = revision;
		}

		/**
		 * @return the namespace
		 */
		public String getNamespace() {
			return namespace;
		}

		/**
		 * @param namespace the namespace to set
		 */
		public void setNamespace(String namespace) {
			this.namespace = namespace;
		}

	}
package br.com.temasistemas.workshop.testes.utils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;

public class Query implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sql;

	private final Map<String, Integer> indexParameter;

	private String sqlParse;

	public Query() {
		super();
		this.indexParameter = new HashMap<>();
	}

	public Query(final String sql) {
		this();
		this.sql = sql;
	}

	@XmlElement(required = true, nillable = false, name = "sql")
	public String getSql() {
		return this.sql;
	}

	public void setSql(final String sql) {
		this.sql = sql;
	}

	public synchronized String getSqlParse() {
		if (this.sqlParse == null) {
			this.sqlParse = this.parse();
		}
		return this.sqlParse;
	}

	private String parse() {
		if (this.sql == null || this.sql.trim().isEmpty()) {
			throw new GenericException("Sql da query não foi informado");
		}
		final String query = this.sql;

		final int length = query.length();

		final StringBuilder parsedQuery = new StringBuilder(length);

		boolean inSingleQuote = false;

		boolean inDoubleQuote = false;

		int index = 1;

		for (int i = 0; i < length; i++) {
			char c = query.charAt(i);

			if (inSingleQuote) {
				if (c == '\'') {
					inSingleQuote = false;
				}
			} else if (inDoubleQuote) {
				if (c == '"') {
					inDoubleQuote = false;
				}
			} else {
				if (c == '\'') {
					inSingleQuote = true;
				} else if (c == '"') {
					inDoubleQuote = true;
				} else if (c == ':' && i + 1 < length && Character.isJavaIdentifierStart(query.charAt(i + 1))) {
					int j = i + 2;
					while (j < length && Character.isJavaIdentifierPart(query.charAt(j))) {
						j++;
					}
					final String name = query.substring(i + 1, j);
					c = '?';
					i += name.length();

					this.indexParameter.put(name, index);

					index++;
				}
			}
			parsedQuery.append(c);
		}

		return parsedQuery.toString();
	}

	public Map<String, Integer> getIndexParameter() {
		return this.indexParameter;
	}

}

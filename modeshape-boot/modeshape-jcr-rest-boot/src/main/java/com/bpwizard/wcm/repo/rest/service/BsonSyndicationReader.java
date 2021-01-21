package com.bpwizard.wcm.repo.rest.service;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.modeshape.jcr.cache.document.DocumentConstants;
import org.modeshape.schematic.document.Bson;
import org.modeshape.schematic.document.Document;
import org.modeshape.schematic.internal.document.BsonReader;
import org.modeshape.schematic.internal.document.DefaultDocumentValueFactory;
import org.modeshape.schematic.internal.document.DocumentValueFactory;
import org.modeshape.schematic.internal.document.MutableDocument;
import org.modeshape.schematic.internal.io.BsonDataInput;
import org.springframework.util.StringUtils;

import com.bpwizard.wcm.repo.rest.modeshape.model.RestNode;

public class BsonSyndicationReader extends BsonReader {
	protected static final DocumentValueFactory VALUE_FACTORY = new DefaultDocumentValueFactory();

    private static List<String> idFileds = new ArrayList<>();
    static {
    	idFileds.add(DocumentConstants.PARENT);
    	idFileds.add(DocumentConstants.KEY);
    	idFileds.add(RestNode.ID_FIELD_NAME);
    	idFileds.add(DocumentConstants.SIMPLE_REFERENCE_FIELD);
    	idFileds.add(DocumentConstants.REFERENCE_FIELD);
    	idFileds.add(DocumentConstants.WEAK_REFERENCE_FIELD);
    }
    
	public Document read(InputStream stream, Map<String, String> rootNodeKeyMap) throws IOException {
		// Create an object so that this reader is thread safe ...
		DocumentValueFactory valueFactory = VALUE_FACTORY;
		SyndicationReader reader = new SyndicationReader(
				new BsonDataInput(new DataInputStream(stream)), 
				valueFactory, 
				rootNodeKeyMap);
		reader.startDocument();
		return reader.endDocument();
	}

	private static boolean isIdField(String name) {
		return idFileds.contains(name);
		
	}
	protected static class SyndicationReader extends BsonReader.Reader {
		private Map<String, String> rootNodeKeyMap;
		protected SyndicationReader(BsonDataInput data, DocumentValueFactory valueFactory, Map<String, String> rootNodeKeyMap) {
			super(data, valueFactory);
			this.rootNodeKeyMap = rootNodeKeyMap;
		}
		
		protected void startDocument() throws IOException {
            super.startDocument();
        }

		protected Document endDocument() {
            return super.endDocument();
        }
		
		protected void readElement(byte type, MutableDocument bson) throws IOException {
			if (type != Bson.Type.STRING) {
				super.readElement(type, bson);
			} else {
				String name = readCString();
				String value = readString();
				if (isIdField(name) && StringUtils.hasText(value)) {
					value = syndicateNodeKey(value);
				}
				bson.put(name, value);
			}
		}
		
		private String syndicateNodeKey(String value) {
			for (String key: rootNodeKeyMap.keySet()) {
				if (value.startsWith(key)) {
					value = String.format("%s%s", rootNodeKeyMap.get(key), value.substring(key.length())); 
				}
			}
			return value;
		}
	}
}

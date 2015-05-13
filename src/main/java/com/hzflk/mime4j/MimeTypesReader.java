package com.hzflk.mime4j;

import org.w3c.dom.Document;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MimeTypesReader extends DefaultHandler implements MimeTypesReaderMetKeys {
    protected final MimeTypes types;

    /** Current type */
    protected MimeType type = null;

    protected int priority;

    protected StringBuilder characters = null;

    protected MimeTypesReader(MimeTypes types) {
        this.types = types;
    }

    public void read(InputStream stream) throws IOException, MimeTypeException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            SAXParser parser = factory.newSAXParser();
            parser.parse(stream, this);
        } catch (ParserConfigurationException e) {
            throw new MimeTypeException("Unable to create an XML parser", e);
        } catch (SAXException e) {
            throw new MimeTypeException("Invalid type configuration", e);
        }
    }

    public void read(Document document) throws MimeTypeException {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(new DOMSource(document), new SAXResult(this));
        } catch (TransformerException e) {
            throw new MimeTypeException("Failed to parse type registry", e);
        }
    }

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(new ByteArrayInputStream(new byte[0]));
    }

    @Override
    public void startElement(
            String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
        if (type == null) {
            if (MIME_TYPE_TAG.equals(qName)) {
                String name = attributes.getValue(MIME_TYPE_TYPE_ATTR);
                try {
                    type = types.forName(name);
                } catch (MimeTypeException e) {
                    handleMimeError(name, e, qName, attributes);
                }
            }
        } else if (ALIAS_TAG.equals(qName)) {
            String alias = attributes.getValue(ALIAS_TYPE_ATTR);
            types.addAlias(type, MediaType.parse(alias));
        } else if (SUB_CLASS_OF_TAG.equals(qName)) {
            String parent = attributes.getValue(SUB_CLASS_TYPE_ATTR);
            types.setSuperType(type, MediaType.parse(parent));
        } else if (ACRONYM_TAG.equals(qName)||
                   COMMENT_TAG.equals(qName)||
                   TIKA_LINK_TAG.equals(qName)||
                   TIKA_UTI_TAG.equals(qName)) {
            characters = new StringBuilder();
        } else if (GLOB_TAG.equals(qName)) {
            String pattern = attributes.getValue(PATTERN_ATTR);
            String isRegex = attributes.getValue(ISREGEX_ATTR);
            if (pattern != null) {
                try {
                    types.addPattern(type, pattern, Boolean.valueOf(isRegex));
                } catch (MimeTypeException e) {
                  handleGlobError(type, pattern, e, qName, attributes);
                }
            }
        } else if (ROOT_XML_TAG.equals(qName)) {
            String namespace = attributes.getValue(NS_URI_ATTR);
            String name = attributes.getValue(LOCAL_NAME_ATTR);
            type.addRootXML(namespace, name);
        } else if (MATCH_TAG.equals(qName)) {
            String kind = attributes.getValue(MATCH_TYPE_ATTR);
            String offset = attributes.getValue(MATCH_OFFSET_ATTR);
            String value = attributes.getValue(MATCH_VALUE_ATTR);
            String mask = attributes.getValue(MATCH_MASK_ATTR);
            if (kind == null) {
                kind = "string";
            }
            current = new ClauseRecord(
                    new MagicMatch(type.getType(), kind, offset, value, mask));
        } else if (MAGIC_TAG.equals(qName)) {
            String value = attributes.getValue(MAGIC_PRIORITY_ATTR);
            if (value != null && value.length() > 0) {
                priority = Integer.parseInt(value);
            } else {
                priority = 50;
            }
            current = new ClauseRecord(null);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) {
        if (type != null) {
            if (MIME_TYPE_TAG.equals(qName)) {
                type = null;
            } else if (COMMENT_TAG.equals(qName)) {
                type.setDescription(characters.toString().trim());
                characters = null;
            } else if (ACRONYM_TAG.equals(qName)) {
                type.setAcronym(characters.toString().trim());
                characters = null;
            } else if (TIKA_UTI_TAG.equals(qName)) {
                type.setUniformTypeIdentifier(characters.toString().trim());
                characters = null;
            } else if (TIKA_LINK_TAG.equals(qName)) {
                try {
                    type.addLink(new URI(characters.toString().trim()));
                } 
                catch (URISyntaxException e) {
                    throw new IllegalArgumentException("unable to parse link: "+characters, e);
                }
                characters = null;
            } else if (MATCH_TAG.equals(qName)) {
                current.stop();
            } else if (MAGIC_TAG.equals(qName)) {
                for (Clause clause : current.getClauses()) {
                    type.addMagic(new Magic(type, priority, clause));
                }
                current = null;
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        if (characters != null) {
            characters.append(ch, start, length);
        }
    }

    protected void handleMimeError(String input, MimeTypeException ex, String qName, Attributes attributes) throws SAXException {
      throw new SAXException(ex);
    }
    
    protected void handleGlobError(MimeType type, String pattern, MimeTypeException ex, String qName, Attributes attributes) throws SAXException {
      throw new SAXException(ex);
    }

    private ClauseRecord current = new ClauseRecord(null);

    private class ClauseRecord {

        private ClauseRecord parent;

        private Clause clause;

        private List<Clause> subclauses = null;

        public ClauseRecord(Clause clause) {
            this.parent = current;
            this.clause = clause;
        }

        public void stop() {
            if (subclauses != null) {
                Clause subclause;
                if (subclauses.size() == 1) {
                    subclause = subclauses.get(0);
                } else {
                    subclause = new OrClause(subclauses);
                }
                clause = new AndClause(clause, subclause);
            }
            if (parent.subclauses == null) {
                parent.subclauses = Collections.singletonList(clause);
            } else {
                if (parent.subclauses.size() == 1) {
                    parent.subclauses = new ArrayList<Clause>(parent.subclauses);
                }
                parent.subclauses.add(clause);
            }

            current = current.parent;
        }
 
        public List<Clause> getClauses() {
            return subclauses;
        }

    }

}
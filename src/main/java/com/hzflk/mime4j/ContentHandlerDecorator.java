package com.hzflk.mime4j;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ContentHandlerDecorator extends DefaultHandler {

    /**
     * Decorated SAX event handler.
     */
    private ContentHandler handler;

    /**
     * Creates a decorator for the given SAX event handler.
     *
     * @param handler SAX event handler to be decorated
     */
    public ContentHandlerDecorator(ContentHandler handler) {
        assert handler != null;
        this.handler = handler;
    }

    /**
     * Creates a decorator that by default forwards incoming SAX events to
     * a dummy content handler that simply ignores all the events. Subclasses
     * should use the {@link #setContentHandler(ContentHandler)} method to
     * switch to a more usable underlying content handler.
     */
    protected ContentHandlerDecorator() {
        this(new DefaultHandler());
    }

    /**
     * Sets the underlying content handler. All future SAX events will be
     * directed to this handler instead of the one that was previously used.
     *
     * @param handler content handler
     */
    protected void setContentHandler(ContentHandler handler) {
        assert handler != null;
        this.handler = handler;
    }

    @Override
    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        try {
            handler.startPrefixMapping(prefix, uri);
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void endPrefixMapping(String prefix) throws SAXException {
        try {
            handler.endPrefixMapping(prefix);
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void processingInstruction(String target, String data)
            throws SAXException {
        try {
            handler.processingInstruction(target, data);
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void setDocumentLocator(Locator locator) {
        handler.setDocumentLocator(locator);
    }

    @Override
    public void startDocument() throws SAXException {
        try {
            handler.startDocument();
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void endDocument() throws SAXException {
        try {
            handler.endDocument();
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void startElement(
            String uri, String localName, String name, Attributes atts)
            throws SAXException {
        try {
            handler.startElement(uri, localName, name, atts);
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void endElement(String uri, String localName, String name)
            throws SAXException {
        try {
            handler.endElement(uri, localName, name);
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void characters(char[] ch, int start, int length)
            throws SAXException {
        try {
            handler.characters(ch, start, length);
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void ignorableWhitespace(char[] ch, int start, int length)
            throws SAXException {
        try {
            handler.ignorableWhitespace(ch, start, length);
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
        try {
            handler.skippedEntity(name);
        } catch (SAXException e) {
            handleException(e);
        }
    }

    @Override
    public String toString() {
        return handler.toString();
    }

    /**
     * Handle any exceptions thrown by methods in this class. This method
     * provides a single place to implement custom exception handling. The
     * default behaviour is simply to re-throw the given exception, but
     * subclasses can also provide alternative ways of handling the situation.
     *
     * @param exception the exception that was thrown
     * @throws SAXException the exception (if any) thrown to the client
     */
    protected void handleException(SAXException exception) throws SAXException {
        throw exception;
    }

}
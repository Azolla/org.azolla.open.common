//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.07.08 at 07:15:45 ���� CST 
//


package org.azolla.open.common.cfg.test;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.azolla.open.common.cfg.test package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _TestElementNode_QNAME = new QName("", "testElementNode");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.azolla.open.common.cfg.test
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TestRootNode }
     * 
     */
    public TestRootNode createTestRootNode() {
        return new TestRootNode();
    }

    /**
     * Create an instance of {@link TestRefElementNode }
     * 
     */
    public TestRefElementNode createTestRefElementNode() {
        return new TestRefElementNode();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "testElementNode")
    public JAXBElement<String> createTestElementNode(String value) {
        return new JAXBElement<String>(_TestElementNode_QNAME, String.class, null, value);
    }

}

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.08.04 at 01:49:18 PM EDT 
//


package io.apiman.test.common.plan;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the io.apiman.test.common.plan package. 
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


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: io.apiman.test.common.plan
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link TestPlan }
     * 
     */
    public TestPlan createTestPlan() {
        return new TestPlan();
    }

    /**
     * Create an instance of {@link TestGroupType }
     * 
     */
    public TestGroupType createTestGroupType() {
        return new TestGroupType();
    }

    /**
     * Create an instance of {@link TestType }
     * 
     */
    public TestType createTestType() {
        return new TestType();
    }

}

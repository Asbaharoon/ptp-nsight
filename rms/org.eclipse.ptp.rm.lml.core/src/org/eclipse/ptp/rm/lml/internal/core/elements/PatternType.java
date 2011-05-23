//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.05.23 at 02:42:42 PM CEST 
//


package org.eclipse.ptp.rm.lml.internal.core.elements;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;


/**
 * A pattern element consists of include and exclude tags. Values of the corresponding column
 * can be checked by the defined regular expressions. They must pass the regexp-checks in
 * the following way: Go through all include/exclude tags. A value must be valid against the 
 * include-regexps and must not match with the exclude-regexps. Therefore the order of tags
 * is important.
 * 
 * 
 * <p>Java class for pattern_type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="pattern_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded">
 *         &lt;element name="include" type="{http://www.llview.de}pattern_match_type" minOccurs="0"/>
 *         &lt;element name="exclude" type="{http://www.llview.de}pattern_match_type" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "pattern_type", propOrder = {
    "includeAndExclude"
})
public class PatternType {

    @XmlElementRefs({
        @XmlElementRef(name = "include", type = JAXBElement.class),
        @XmlElementRef(name = "exclude", type = JAXBElement.class)
    })
    protected List<JAXBElement<PatternMatchType>> includeAndExclude;

    /**
     * Gets the value of the includeAndExclude property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the includeAndExclude property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIncludeAndExclude().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link PatternMatchType }{@code >}
     * {@link JAXBElement }{@code <}{@link PatternMatchType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<PatternMatchType>> getIncludeAndExclude() {
        if (includeAndExclude == null) {
            includeAndExclude = new ArrayList<JAXBElement<PatternMatchType>>();
        }
        return this.includeAndExclude;
    }

}

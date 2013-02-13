//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.02.13 at 04:49:42 PM EST 
//


package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for browse-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="browse-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="text-layout-data" type="{http://eclipse.org/ptp/schemas}layout-data-type" minOccurs="0"/>
 *         &lt;element name="button-layout-data" type="{http://eclipse.org/ptp/schemas}layout-data-type" minOccurs="0"/>
 *         &lt;element name="font" type="{http://eclipse.org/ptp/schemas}font-type" minOccurs="0"/>
 *         &lt;element name="tooltip" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="text-control-state" type="{http://eclipse.org/ptp/schemas}control-state-type" minOccurs="0"/>
 *         &lt;element name="button-control-state" type="{http://eclipse.org/ptp/schemas}control-state-type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="title" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="textStyle" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="directory" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="uri" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="localOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="readOnly" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="attribute" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="foreground" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="background" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "browse-type", propOrder = {
    "textLayoutData",
    "buttonLayoutData",
    "font",
    "tooltip",
    "textControlState",
    "buttonControlState"
})
public class BrowseType {

    @XmlElement(name = "text-layout-data")
    protected LayoutDataType textLayoutData;
    @XmlElement(name = "button-layout-data")
    protected LayoutDataType buttonLayoutData;
    protected FontType font;
    protected String tooltip;
    @XmlElement(name = "text-control-state")
    protected ControlStateType textControlState;
    @XmlElement(name = "button-control-state")
    protected ControlStateType buttonControlState;
    @XmlAttribute(name = "title")
    protected String title;
    @XmlAttribute(name = "textStyle")
    protected String textStyle;
    @XmlAttribute(name = "directory")
    protected Boolean directory;
    @XmlAttribute(name = "uri")
    protected Boolean uri;
    @XmlAttribute(name = "localOnly")
    protected Boolean localOnly;
    @XmlAttribute(name = "readOnly")
    protected Boolean readOnly;
    @XmlAttribute(name = "attribute")
    protected String attribute;
    @XmlAttribute(name = "foreground")
    protected String foreground;
    @XmlAttribute(name = "background")
    protected String background;

    /**
     * Gets the value of the textLayoutData property.
     * 
     * @return
     *     possible object is
     *     {@link LayoutDataType }
     *     
     */
    public LayoutDataType getTextLayoutData() {
        return textLayoutData;
    }

    /**
     * Sets the value of the textLayoutData property.
     * 
     * @param value
     *     allowed object is
     *     {@link LayoutDataType }
     *     
     */
    public void setTextLayoutData(LayoutDataType value) {
        this.textLayoutData = value;
    }

    /**
     * Gets the value of the buttonLayoutData property.
     * 
     * @return
     *     possible object is
     *     {@link LayoutDataType }
     *     
     */
    public LayoutDataType getButtonLayoutData() {
        return buttonLayoutData;
    }

    /**
     * Sets the value of the buttonLayoutData property.
     * 
     * @param value
     *     allowed object is
     *     {@link LayoutDataType }
     *     
     */
    public void setButtonLayoutData(LayoutDataType value) {
        this.buttonLayoutData = value;
    }

    /**
     * Gets the value of the font property.
     * 
     * @return
     *     possible object is
     *     {@link FontType }
     *     
     */
    public FontType getFont() {
        return font;
    }

    /**
     * Sets the value of the font property.
     * 
     * @param value
     *     allowed object is
     *     {@link FontType }
     *     
     */
    public void setFont(FontType value) {
        this.font = value;
    }

    /**
     * Gets the value of the tooltip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * Sets the value of the tooltip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTooltip(String value) {
        this.tooltip = value;
    }

    /**
     * Gets the value of the textControlState property.
     * 
     * @return
     *     possible object is
     *     {@link ControlStateType }
     *     
     */
    public ControlStateType getTextControlState() {
        return textControlState;
    }

    /**
     * Sets the value of the textControlState property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlStateType }
     *     
     */
    public void setTextControlState(ControlStateType value) {
        this.textControlState = value;
    }

    /**
     * Gets the value of the buttonControlState property.
     * 
     * @return
     *     possible object is
     *     {@link ControlStateType }
     *     
     */
    public ControlStateType getButtonControlState() {
        return buttonControlState;
    }

    /**
     * Sets the value of the buttonControlState property.
     * 
     * @param value
     *     allowed object is
     *     {@link ControlStateType }
     *     
     */
    public void setButtonControlState(ControlStateType value) {
        this.buttonControlState = value;
    }

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the textStyle property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTextStyle() {
        return textStyle;
    }

    /**
     * Sets the value of the textStyle property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTextStyle(String value) {
        this.textStyle = value;
    }

    /**
     * Gets the value of the directory property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDirectory() {
        if (directory == null) {
            return false;
        } else {
            return directory;
        }
    }

    /**
     * Sets the value of the directory property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDirectory(Boolean value) {
        this.directory = value;
    }

    /**
     * Gets the value of the uri property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isUri() {
        if (uri == null) {
            return false;
        } else {
            return uri;
        }
    }

    /**
     * Sets the value of the uri property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUri(Boolean value) {
        this.uri = value;
    }

    /**
     * Gets the value of the localOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isLocalOnly() {
        if (localOnly == null) {
            return false;
        } else {
            return localOnly;
        }
    }

    /**
     * Sets the value of the localOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLocalOnly(Boolean value) {
        this.localOnly = value;
    }

    /**
     * Gets the value of the readOnly property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isReadOnly() {
        if (readOnly == null) {
            return false;
        } else {
            return readOnly;
        }
    }

    /**
     * Sets the value of the readOnly property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setReadOnly(Boolean value) {
        this.readOnly = value;
    }

    /**
     * Gets the value of the attribute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Sets the value of the attribute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute(String value) {
        this.attribute = value;
    }

    /**
     * Gets the value of the foreground property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForeground() {
        return foreground;
    }

    /**
     * Sets the value of the foreground property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForeground(String value) {
        this.foreground = value;
    }

    /**
     * Gets the value of the background property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBackground() {
        return background;
    }

    /**
     * Sets the value of the background property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBackground(String value) {
        this.background = value;
    }

}

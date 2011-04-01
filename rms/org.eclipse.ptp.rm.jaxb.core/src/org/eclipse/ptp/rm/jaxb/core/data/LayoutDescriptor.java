//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.04.01 at 01:23:48 PM CDT 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for layout-descriptor complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="layout-descriptor">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="fill-layout" type="{http://org.eclipse.ptp/rm}fill-layout-descriptor"/>
 *         &lt;element name="row-layout" type="{http://org.eclipse.ptp/rm}row-layout-descriptor"/>
 *         &lt;element name="grid-layout" type="{http://org.eclipse.ptp/rm}grid-layout-descriptor"/>
 *         &lt;element name="form-layout" type="{http://org.eclipse.ptp/rm}form-layout-descriptor"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "layout-descriptor", propOrder = { "fillLayout", "rowLayout", "gridLayout", "formLayout" })
public class LayoutDescriptor {

	@XmlElement(name = "fill-layout")
	protected FillLayoutDescriptor fillLayout;
	@XmlElement(name = "row-layout")
	protected RowLayoutDescriptor rowLayout;
	@XmlElement(name = "grid-layout")
	protected GridLayoutDescriptor gridLayout;
	@XmlElement(name = "form-layout")
	protected FormLayoutDescriptor formLayout;

	/**
	 * Gets the value of the fillLayout property.
	 * 
	 * @return possible object is {@link FillLayoutDescriptor }
	 * 
	 */
	public FillLayoutDescriptor getFillLayout() {
		return fillLayout;
	}

	/**
	 * Gets the value of the formLayout property.
	 * 
	 * @return possible object is {@link FormLayoutDescriptor }
	 * 
	 */
	public FormLayoutDescriptor getFormLayout() {
		return formLayout;
	}

	/**
	 * Gets the value of the gridLayout property.
	 * 
	 * @return possible object is {@link GridLayoutDescriptor }
	 * 
	 */
	public GridLayoutDescriptor getGridLayout() {
		return gridLayout;
	}

	/**
	 * Gets the value of the rowLayout property.
	 * 
	 * @return possible object is {@link RowLayoutDescriptor }
	 * 
	 */
	public RowLayoutDescriptor getRowLayout() {
		return rowLayout;
	}

	/**
	 * Sets the value of the fillLayout property.
	 * 
	 * @param value
	 *            allowed object is {@link FillLayoutDescriptor }
	 * 
	 */
	public void setFillLayout(FillLayoutDescriptor value) {
		this.fillLayout = value;
	}

	/**
	 * Sets the value of the formLayout property.
	 * 
	 * @param value
	 *            allowed object is {@link FormLayoutDescriptor }
	 * 
	 */
	public void setFormLayout(FormLayoutDescriptor value) {
		this.formLayout = value;
	}

	/**
	 * Sets the value of the gridLayout property.
	 * 
	 * @param value
	 *            allowed object is {@link GridLayoutDescriptor }
	 * 
	 */
	public void setGridLayout(GridLayoutDescriptor value) {
		this.gridLayout = value;
	}

	/**
	 * Sets the value of the rowLayout property.
	 * 
	 * @param value
	 *            allowed object is {@link RowLayoutDescriptor }
	 * 
	 */
	public void setRowLayout(RowLayoutDescriptor value) {
		this.rowLayout = value;
	}

}

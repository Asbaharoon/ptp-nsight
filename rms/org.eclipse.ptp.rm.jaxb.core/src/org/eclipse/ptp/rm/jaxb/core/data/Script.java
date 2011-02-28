//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.02.27 at 07:28:19 PM CST 
//

package org.eclipse.ptp.rm.jaxb.core.data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Java class for anonymous complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}directive-definitions" minOccurs="0"/>
 *         &lt;element ref="{}environment-variables" minOccurs="0"/>
 *         &lt;element ref="{}pre-execute-commands" minOccurs="0"/>
 *         &lt;element ref="{}execute-command"/>
 *         &lt;element ref="{}post-execute-commands" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="shell" use="required" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "directiveDefinitions", "environmentVariables", "preExecuteCommands", "executeCommand",
		"postExecuteCommands" })
@XmlRootElement(name = "script")
public class Script {

	@XmlElement(name = "directive-definitions")
	protected DirectiveDefinitions directiveDefinitions;
	@XmlElement(name = "environment-variables")
	protected EnvironmentVariables environmentVariables;
	@XmlElement(name = "pre-execute-commands")
	protected PreExecuteCommands preExecuteCommands;
	@XmlElement(name = "execute-command", required = true)
	protected ExecuteCommand executeCommand;
	@XmlElement(name = "post-execute-commands")
	protected PostExecuteCommands postExecuteCommands;
	@XmlAttribute(required = true)
	protected String shell;

	/**
	 * Gets the value of the directiveDefinitions property.
	 * 
	 * @return possible object is {@link DirectiveDefinitions }
	 * 
	 */
	public DirectiveDefinitions getDirectiveDefinitions() {
		return directiveDefinitions;
	}

	/**
	 * Gets the value of the environmentVariables property.
	 * 
	 * @return possible object is {@link EnvironmentVariables }
	 * 
	 */
	public EnvironmentVariables getEnvironmentVariables() {
		return environmentVariables;
	}

	/**
	 * Gets the value of the executeCommand property.
	 * 
	 * @return possible object is {@link ExecuteCommand }
	 * 
	 */
	public ExecuteCommand getExecuteCommand() {
		return executeCommand;
	}

	/**
	 * Gets the value of the postExecuteCommands property.
	 * 
	 * @return possible object is {@link PostExecuteCommands }
	 * 
	 */
	public PostExecuteCommands getPostExecuteCommands() {
		return postExecuteCommands;
	}

	/**
	 * Gets the value of the preExecuteCommands property.
	 * 
	 * @return possible object is {@link PreExecuteCommands }
	 * 
	 */
	public PreExecuteCommands getPreExecuteCommands() {
		return preExecuteCommands;
	}

	/**
	 * Gets the value of the shell property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getShell() {
		return shell;
	}

	/**
	 * Sets the value of the directiveDefinitions property.
	 * 
	 * @param value
	 *            allowed object is {@link DirectiveDefinitions }
	 * 
	 */
	public void setDirectiveDefinitions(DirectiveDefinitions value) {
		this.directiveDefinitions = value;
	}

	/**
	 * Sets the value of the environmentVariables property.
	 * 
	 * @param value
	 *            allowed object is {@link EnvironmentVariables }
	 * 
	 */
	public void setEnvironmentVariables(EnvironmentVariables value) {
		this.environmentVariables = value;
	}

	/**
	 * Sets the value of the executeCommand property.
	 * 
	 * @param value
	 *            allowed object is {@link ExecuteCommand }
	 * 
	 */
	public void setExecuteCommand(ExecuteCommand value) {
		this.executeCommand = value;
	}

	/**
	 * Sets the value of the postExecuteCommands property.
	 * 
	 * @param value
	 *            allowed object is {@link PostExecuteCommands }
	 * 
	 */
	public void setPostExecuteCommands(PostExecuteCommands value) {
		this.postExecuteCommands = value;
	}

	/**
	 * Sets the value of the preExecuteCommands property.
	 * 
	 * @param value
	 *            allowed object is {@link PreExecuteCommands }
	 * 
	 */
	public void setPreExecuteCommands(PreExecuteCommands value) {
		this.preExecuteCommands = value;
	}

	/**
	 * Sets the value of the shell property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setShell(String value) {
		this.shell = value;
	}

}

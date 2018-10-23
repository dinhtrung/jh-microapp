
package com.ft.soap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tid" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="imsi" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cdpa" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cgpa" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "tid",
    "imsi",
    "cdpa",
    "cgpa"
})
@XmlRootElement(name = "sendLU")
public class SendLU {

    @XmlElement(required = true)
    protected String tid;
    @XmlElement(required = true)
    protected String imsi;
    @XmlElement(required = true)
    protected String cdpa;
    @XmlElement(required = true)
    protected String cgpa;

    /**
     * Gets the value of the tid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTid() {
        return tid;
    }

    /**
     * Sets the value of the tid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTid(String value) {
        this.tid = value;
    }

    /**
     * Gets the value of the imsi property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getImsi() {
        return imsi;
    }

    /**
     * Sets the value of the imsi property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setImsi(String value) {
        this.imsi = value;
    }

    /**
     * Gets the value of the cdpa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCdpa() {
        return cdpa;
    }

    /**
     * Sets the value of the cdpa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCdpa(String value) {
        this.cdpa = value;
    }

    /**
     * Gets the value of the cgpa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCgpa() {
        return cgpa;
    }

    /**
     * Sets the value of the cgpa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCgpa(String value) {
        this.cgpa = value;
    }

}

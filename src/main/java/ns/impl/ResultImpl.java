/*
 * XML Type:  result
 * Namespace: tns:ns
 * Java type: ns.Result
 *
 * Automatically generated - do not modify.
 */
package ns.impl;
/**
 * An XML result(@tns:ns).
 *
 * This is a complex type.
 */
public class ResultImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.Result
{
    private static final long serialVersionUID = 1L;
    
    public ResultImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName RESPONSECODE$0 = 
        new javax.xml.namespace.QName("tns:ns", "responseCode");
    
    
    /**
     * Gets the "responseCode" element
     */
    public int getResponseCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESPONSECODE$0, 0);
            if (target == null)
            {
                return 0;
            }
            return target.getIntValue();
        }
    }
    
    /**
     * Gets (as xml) the "responseCode" element
     */
    public org.apache.xmlbeans.XmlInt xgetResponseCode()
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(RESPONSECODE$0, 0);
            return target;
        }
    }
    
    /**
     * Sets the "responseCode" element
     */
    public void setResponseCode(int responseCode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.SimpleValue target = null;
            target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(RESPONSECODE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(RESPONSECODE$0);
            }
            target.setIntValue(responseCode);
        }
    }
    
    /**
     * Sets (as xml) the "responseCode" element
     */
    public void xsetResponseCode(org.apache.xmlbeans.XmlInt responseCode)
    {
        synchronized (monitor())
        {
            check_orphaned();
            org.apache.xmlbeans.XmlInt target = null;
            target = (org.apache.xmlbeans.XmlInt)get_store().find_element_user(RESPONSECODE$0, 0);
            if (target == null)
            {
                target = (org.apache.xmlbeans.XmlInt)get_store().add_element_user(RESPONSECODE$0);
            }
            target.set(responseCode);
        }
    }
}

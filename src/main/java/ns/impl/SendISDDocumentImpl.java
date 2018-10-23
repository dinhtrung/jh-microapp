/*
 * An XML document type.
 * Localname: sendISD
 * Namespace: tns:ns
 * Java type: ns.SendISDDocument
 *
 * Automatically generated - do not modify.
 */
package ns.impl;
/**
 * A document containing one sendISD(@tns:ns) element.
 *
 * This is a complex type.
 */
public class SendISDDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendISDDocument
{
    private static final long serialVersionUID = 1L;
    
    public SendISDDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDISD$0 = 
        new javax.xml.namespace.QName("tns:ns", "sendISD");
    
    
    /**
     * Gets the "sendISD" element
     */
    public ns.SendISDDocument.SendISD getSendISD()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendISDDocument.SendISD target = null;
            target = (ns.SendISDDocument.SendISD)get_store().find_element_user(SENDISD$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sendISD" element
     */
    public void setSendISD(ns.SendISDDocument.SendISD sendISD)
    {
        generatedSetterHelperImpl(sendISD, SENDISD$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "sendISD" element
     */
    public ns.SendISDDocument.SendISD addNewSendISD()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendISDDocument.SendISD target = null;
            target = (ns.SendISDDocument.SendISD)get_store().add_element_user(SENDISD$0);
            return target;
        }
    }
    /**
     * An XML sendISD(@tns:ns).
     *
     * This is a complex type.
     */
    public static class SendISDImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendISDDocument.SendISD
    {
        private static final long serialVersionUID = 1L;
        
        public SendISDImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName TID$0 = 
            new javax.xml.namespace.QName("tns:ns", "tid");
        private static final javax.xml.namespace.QName MSISDN$2 = 
            new javax.xml.namespace.QName("tns:ns", "msisdn");
        
        
        /**
         * Gets the "tid" element
         */
        public java.lang.String getTid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TID$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "tid" element
         */
        public org.apache.xmlbeans.XmlString xgetTid()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TID$0, 0);
                return target;
            }
        }
        
        /**
         * Sets the "tid" element
         */
        public void setTid(java.lang.String tid)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(TID$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(TID$0);
                }
                target.setStringValue(tid);
            }
        }
        
        /**
         * Sets (as xml) the "tid" element
         */
        public void xsetTid(org.apache.xmlbeans.XmlString tid)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(TID$0, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(TID$0);
                }
                target.set(tid);
            }
        }
        
        /**
         * Gets the "msisdn" element
         */
        public java.lang.String getMsisdn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MSISDN$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "msisdn" element
         */
        public org.apache.xmlbeans.XmlString xgetMsisdn()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MSISDN$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "msisdn" element
         */
        public void setMsisdn(java.lang.String msisdn)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(MSISDN$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(MSISDN$2);
                }
                target.setStringValue(msisdn);
            }
        }
        
        /**
         * Sets (as xml) the "msisdn" element
         */
        public void xsetMsisdn(org.apache.xmlbeans.XmlString msisdn)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(MSISDN$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(MSISDN$2);
                }
                target.set(msisdn);
            }
        }
    }
}

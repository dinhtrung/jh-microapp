/*
 * An XML document type.
 * Localname: sendLU
 * Namespace: tns:ns
 * Java type: ns.SendLUDocument
 *
 * Automatically generated - do not modify.
 */
package ns.impl;
/**
 * A document containing one sendLU(@tns:ns) element.
 *
 * This is a complex type.
 */
public class SendLUDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendLUDocument
{
    private static final long serialVersionUID = 1L;
    
    public SendLUDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDLU$0 = 
        new javax.xml.namespace.QName("tns:ns", "sendLU");
    
    
    /**
     * Gets the "sendLU" element
     */
    public ns.SendLUDocument.SendLU getSendLU()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendLUDocument.SendLU target = null;
            target = (ns.SendLUDocument.SendLU)get_store().find_element_user(SENDLU$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sendLU" element
     */
    public void setSendLU(ns.SendLUDocument.SendLU sendLU)
    {
        generatedSetterHelperImpl(sendLU, SENDLU$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "sendLU" element
     */
    public ns.SendLUDocument.SendLU addNewSendLU()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendLUDocument.SendLU target = null;
            target = (ns.SendLUDocument.SendLU)get_store().add_element_user(SENDLU$0);
            return target;
        }
    }
    /**
     * An XML sendLU(@tns:ns).
     *
     * This is a complex type.
     */
    public static class SendLUImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendLUDocument.SendLU
    {
        private static final long serialVersionUID = 1L;
        
        public SendLUImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName TID$0 = 
            new javax.xml.namespace.QName("tns:ns", "tid");
        private static final javax.xml.namespace.QName IMSI$2 = 
            new javax.xml.namespace.QName("tns:ns", "imsi");
        private static final javax.xml.namespace.QName CDPA$4 = 
            new javax.xml.namespace.QName("tns:ns", "cdpa");
        private static final javax.xml.namespace.QName CGPA$6 = 
            new javax.xml.namespace.QName("tns:ns", "cgpa");
        
        
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
         * Gets the "imsi" element
         */
        public java.lang.String getImsi()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMSI$2, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "imsi" element
         */
        public org.apache.xmlbeans.XmlString xgetImsi()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMSI$2, 0);
                return target;
            }
        }
        
        /**
         * Sets the "imsi" element
         */
        public void setImsi(java.lang.String imsi)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(IMSI$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(IMSI$2);
                }
                target.setStringValue(imsi);
            }
        }
        
        /**
         * Sets (as xml) the "imsi" element
         */
        public void xsetImsi(org.apache.xmlbeans.XmlString imsi)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(IMSI$2, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(IMSI$2);
                }
                target.set(imsi);
            }
        }
        
        /**
         * Gets the "cdpa" element
         */
        public java.lang.String getCdpa()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CDPA$4, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "cdpa" element
         */
        public org.apache.xmlbeans.XmlString xgetCdpa()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CDPA$4, 0);
                return target;
            }
        }
        
        /**
         * Sets the "cdpa" element
         */
        public void setCdpa(java.lang.String cdpa)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CDPA$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CDPA$4);
                }
                target.setStringValue(cdpa);
            }
        }
        
        /**
         * Sets (as xml) the "cdpa" element
         */
        public void xsetCdpa(org.apache.xmlbeans.XmlString cdpa)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CDPA$4, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CDPA$4);
                }
                target.set(cdpa);
            }
        }
        
        /**
         * Gets the "cgpa" element
         */
        public java.lang.String getCgpa()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CGPA$6, 0);
                if (target == null)
                {
                    return null;
                }
                return target.getStringValue();
            }
        }
        
        /**
         * Gets (as xml) the "cgpa" element
         */
        public org.apache.xmlbeans.XmlString xgetCgpa()
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CGPA$6, 0);
                return target;
            }
        }
        
        /**
         * Sets the "cgpa" element
         */
        public void setCgpa(java.lang.String cgpa)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.SimpleValue target = null;
                target = (org.apache.xmlbeans.SimpleValue)get_store().find_element_user(CGPA$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.SimpleValue)get_store().add_element_user(CGPA$6);
                }
                target.setStringValue(cgpa);
            }
        }
        
        /**
         * Sets (as xml) the "cgpa" element
         */
        public void xsetCgpa(org.apache.xmlbeans.XmlString cgpa)
        {
            synchronized (monitor())
            {
                check_orphaned();
                org.apache.xmlbeans.XmlString target = null;
                target = (org.apache.xmlbeans.XmlString)get_store().find_element_user(CGPA$6, 0);
                if (target == null)
                {
                    target = (org.apache.xmlbeans.XmlString)get_store().add_element_user(CGPA$6);
                }
                target.set(cgpa);
            }
        }
    }
}

/*
 * An XML document type.
 * Localname: sendCL
 * Namespace: tns:ns
 * Java type: ns.SendCLDocument
 *
 * Automatically generated - do not modify.
 */
package ns.impl;
/**
 * A document containing one sendCL(@tns:ns) element.
 *
 * This is a complex type.
 */
public class SendCLDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendCLDocument
{
    private static final long serialVersionUID = 1L;
    
    public SendCLDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDCL$0 = 
        new javax.xml.namespace.QName("tns:ns", "sendCL");
    
    
    /**
     * Gets the "sendCL" element
     */
    public ns.SendCLDocument.SendCL getSendCL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendCLDocument.SendCL target = null;
            target = (ns.SendCLDocument.SendCL)get_store().find_element_user(SENDCL$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sendCL" element
     */
    public void setSendCL(ns.SendCLDocument.SendCL sendCL)
    {
        generatedSetterHelperImpl(sendCL, SENDCL$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "sendCL" element
     */
    public ns.SendCLDocument.SendCL addNewSendCL()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendCLDocument.SendCL target = null;
            target = (ns.SendCLDocument.SendCL)get_store().add_element_user(SENDCL$0);
            return target;
        }
    }
    /**
     * An XML sendCL(@tns:ns).
     *
     * This is a complex type.
     */
    public static class SendCLImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendCLDocument.SendCL
    {
        private static final long serialVersionUID = 1L;
        
        public SendCLImpl(org.apache.xmlbeans.SchemaType sType)
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

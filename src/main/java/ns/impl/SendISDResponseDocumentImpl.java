/*
 * An XML document type.
 * Localname: sendISDResponse
 * Namespace: tns:ns
 * Java type: ns.SendISDResponseDocument
 *
 * Automatically generated - do not modify.
 */
package ns.impl;
/**
 * A document containing one sendISDResponse(@tns:ns) element.
 *
 * This is a complex type.
 */
public class SendISDResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendISDResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public SendISDResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDISDRESPONSE$0 = 
        new javax.xml.namespace.QName("tns:ns", "sendISDResponse");
    
    
    /**
     * Gets the "sendISDResponse" element
     */
    public ns.SendISDResponseDocument.SendISDResponse getSendISDResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendISDResponseDocument.SendISDResponse target = null;
            target = (ns.SendISDResponseDocument.SendISDResponse)get_store().find_element_user(SENDISDRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sendISDResponse" element
     */
    public void setSendISDResponse(ns.SendISDResponseDocument.SendISDResponse sendISDResponse)
    {
        generatedSetterHelperImpl(sendISDResponse, SENDISDRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "sendISDResponse" element
     */
    public ns.SendISDResponseDocument.SendISDResponse addNewSendISDResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendISDResponseDocument.SendISDResponse target = null;
            target = (ns.SendISDResponseDocument.SendISDResponse)get_store().add_element_user(SENDISDRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML sendISDResponse(@tns:ns).
     *
     * This is a complex type.
     */
    public static class SendISDResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendISDResponseDocument.SendISDResponse
    {
        private static final long serialVersionUID = 1L;
        
        public SendISDResponseImpl(org.apache.xmlbeans.SchemaType sType)
        {
            super(sType);
        }
        
        private static final javax.xml.namespace.QName RESPONSE$0 = 
            new javax.xml.namespace.QName("tns:ns", "response");
        
        
        /**
         * Gets the "response" element
         */
        public ns.Result getResponse()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ns.Result target = null;
                target = (ns.Result)get_store().find_element_user(RESPONSE$0, 0);
                if (target == null)
                {
                    return null;
                }
                return target;
            }
        }
        
        /**
         * Tests for nil "response" element
         */
        public boolean isNilResponse()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ns.Result target = null;
                target = (ns.Result)get_store().find_element_user(RESPONSE$0, 0);
                if (target == null) return false;
                return target.isNil();
            }
        }
        
        /**
         * Sets the "response" element
         */
        public void setResponse(ns.Result response)
        {
            generatedSetterHelperImpl(response, RESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
        }
        
        /**
         * Appends and returns a new empty "response" element
         */
        public ns.Result addNewResponse()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ns.Result target = null;
                target = (ns.Result)get_store().add_element_user(RESPONSE$0);
                return target;
            }
        }
        
        /**
         * Nils the "response" element
         */
        public void setNilResponse()
        {
            synchronized (monitor())
            {
                check_orphaned();
                ns.Result target = null;
                target = (ns.Result)get_store().find_element_user(RESPONSE$0, 0);
                if (target == null)
                {
                    target = (ns.Result)get_store().add_element_user(RESPONSE$0);
                }
                target.setNil();
            }
        }
    }
}

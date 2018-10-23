/*
 * An XML document type.
 * Localname: sendLUResponse
 * Namespace: tns:ns
 * Java type: ns.SendLUResponseDocument
 *
 * Automatically generated - do not modify.
 */
package ns.impl;
/**
 * A document containing one sendLUResponse(@tns:ns) element.
 *
 * This is a complex type.
 */
public class SendLUResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendLUResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public SendLUResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDLURESPONSE$0 = 
        new javax.xml.namespace.QName("tns:ns", "sendLUResponse");
    
    
    /**
     * Gets the "sendLUResponse" element
     */
    public ns.SendLUResponseDocument.SendLUResponse getSendLUResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendLUResponseDocument.SendLUResponse target = null;
            target = (ns.SendLUResponseDocument.SendLUResponse)get_store().find_element_user(SENDLURESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sendLUResponse" element
     */
    public void setSendLUResponse(ns.SendLUResponseDocument.SendLUResponse sendLUResponse)
    {
        generatedSetterHelperImpl(sendLUResponse, SENDLURESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "sendLUResponse" element
     */
    public ns.SendLUResponseDocument.SendLUResponse addNewSendLUResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendLUResponseDocument.SendLUResponse target = null;
            target = (ns.SendLUResponseDocument.SendLUResponse)get_store().add_element_user(SENDLURESPONSE$0);
            return target;
        }
    }
    /**
     * An XML sendLUResponse(@tns:ns).
     *
     * This is a complex type.
     */
    public static class SendLUResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendLUResponseDocument.SendLUResponse
    {
        private static final long serialVersionUID = 1L;
        
        public SendLUResponseImpl(org.apache.xmlbeans.SchemaType sType)
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

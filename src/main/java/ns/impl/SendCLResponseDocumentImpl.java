/*
 * An XML document type.
 * Localname: sendCLResponse
 * Namespace: tns:ns
 * Java type: ns.SendCLResponseDocument
 *
 * Automatically generated - do not modify.
 */
package ns.impl;
/**
 * A document containing one sendCLResponse(@tns:ns) element.
 *
 * This is a complex type.
 */
public class SendCLResponseDocumentImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendCLResponseDocument
{
    private static final long serialVersionUID = 1L;
    
    public SendCLResponseDocumentImpl(org.apache.xmlbeans.SchemaType sType)
    {
        super(sType);
    }
    
    private static final javax.xml.namespace.QName SENDCLRESPONSE$0 = 
        new javax.xml.namespace.QName("tns:ns", "sendCLResponse");
    
    
    /**
     * Gets the "sendCLResponse" element
     */
    public ns.SendCLResponseDocument.SendCLResponse getSendCLResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendCLResponseDocument.SendCLResponse target = null;
            target = (ns.SendCLResponseDocument.SendCLResponse)get_store().find_element_user(SENDCLRESPONSE$0, 0);
            if (target == null)
            {
                return null;
            }
            return target;
        }
    }
    
    /**
     * Sets the "sendCLResponse" element
     */
    public void setSendCLResponse(ns.SendCLResponseDocument.SendCLResponse sendCLResponse)
    {
        generatedSetterHelperImpl(sendCLResponse, SENDCLRESPONSE$0, 0, org.apache.xmlbeans.impl.values.XmlObjectBase.KIND_SETTERHELPER_SINGLETON);
    }
    
    /**
     * Appends and returns a new empty "sendCLResponse" element
     */
    public ns.SendCLResponseDocument.SendCLResponse addNewSendCLResponse()
    {
        synchronized (monitor())
        {
            check_orphaned();
            ns.SendCLResponseDocument.SendCLResponse target = null;
            target = (ns.SendCLResponseDocument.SendCLResponse)get_store().add_element_user(SENDCLRESPONSE$0);
            return target;
        }
    }
    /**
     * An XML sendCLResponse(@tns:ns).
     *
     * This is a complex type.
     */
    public static class SendCLResponseImpl extends org.apache.xmlbeans.impl.values.XmlComplexContentImpl implements ns.SendCLResponseDocument.SendCLResponse
    {
        private static final long serialVersionUID = 1L;
        
        public SendCLResponseImpl(org.apache.xmlbeans.SchemaType sType)
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

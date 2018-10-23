/*
 * An XML document type.
 * Localname: sendISD
 * Namespace: tns:ns
 * Java type: ns.SendISDDocument
 *
 * Automatically generated - do not modify.
 */
package ns;


/**
 * A document containing one sendISD(@tns:ns) element.
 *
 * This is a complex type.
 */
public interface SendISDDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SendISDDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s1B4F01AAF40BE2A0C7761392184F7CCA").resolveHandle("sendisdb08ddoctype");
    
    /**
     * Gets the "sendISD" element
     */
    ns.SendISDDocument.SendISD getSendISD();
    
    /**
     * Sets the "sendISD" element
     */
    void setSendISD(ns.SendISDDocument.SendISD sendISD);
    
    /**
     * Appends and returns a new empty "sendISD" element
     */
    ns.SendISDDocument.SendISD addNewSendISD();
    
    /**
     * An XML sendISD(@tns:ns).
     *
     * This is a complex type.
     */
    public interface SendISD extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SendISD.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s1B4F01AAF40BE2A0C7761392184F7CCA").resolveHandle("sendisd980felemtype");
        
        /**
         * Gets the "tid" element
         */
        java.lang.String getTid();
        
        /**
         * Gets (as xml) the "tid" element
         */
        org.apache.xmlbeans.XmlString xgetTid();
        
        /**
         * Sets the "tid" element
         */
        void setTid(java.lang.String tid);
        
        /**
         * Sets (as xml) the "tid" element
         */
        void xsetTid(org.apache.xmlbeans.XmlString tid);
        
        /**
         * Gets the "msisdn" element
         */
        java.lang.String getMsisdn();
        
        /**
         * Gets (as xml) the "msisdn" element
         */
        org.apache.xmlbeans.XmlString xgetMsisdn();
        
        /**
         * Sets the "msisdn" element
         */
        void setMsisdn(java.lang.String msisdn);
        
        /**
         * Sets (as xml) the "msisdn" element
         */
        void xsetMsisdn(org.apache.xmlbeans.XmlString msisdn);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static ns.SendISDDocument.SendISD newInstance() {
              return (ns.SendISDDocument.SendISD) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static ns.SendISDDocument.SendISD newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (ns.SendISDDocument.SendISD) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ns.SendISDDocument newInstance() {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ns.SendISDDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ns.SendISDDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ns.SendISDDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ns.SendISDDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ns.SendISDDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ns.SendISDDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ns.SendISDDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ns.SendISDDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ns.SendISDDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ns.SendISDDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ns.SendISDDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ns.SendISDDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ns.SendISDDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ns.SendISDDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ns.SendISDDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ns.SendISDDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ns.SendISDDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ns.SendISDDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

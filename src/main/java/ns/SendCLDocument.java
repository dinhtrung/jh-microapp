/*
 * An XML document type.
 * Localname: sendCL
 * Namespace: tns:ns
 * Java type: ns.SendCLDocument
 *
 * Automatically generated - do not modify.
 */
package ns;


/**
 * A document containing one sendCL(@tns:ns) element.
 *
 * This is a complex type.
 */
public interface SendCLDocument extends org.apache.xmlbeans.XmlObject
{
    public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
        org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SendCLDocument.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s1B4F01AAF40BE2A0C7761392184F7CCA").resolveHandle("sendcl0cfcdoctype");
    
    /**
     * Gets the "sendCL" element
     */
    ns.SendCLDocument.SendCL getSendCL();
    
    /**
     * Sets the "sendCL" element
     */
    void setSendCL(ns.SendCLDocument.SendCL sendCL);
    
    /**
     * Appends and returns a new empty "sendCL" element
     */
    ns.SendCLDocument.SendCL addNewSendCL();
    
    /**
     * An XML sendCL(@tns:ns).
     *
     * This is a complex type.
     */
    public interface SendCL extends org.apache.xmlbeans.XmlObject
    {
        public static final org.apache.xmlbeans.SchemaType type = (org.apache.xmlbeans.SchemaType)
            org.apache.xmlbeans.XmlBeans.typeSystemForClassLoader(SendCL.class.getClassLoader(), "schemaorg_apache_xmlbeans.system.s1B4F01AAF40BE2A0C7761392184F7CCA").resolveHandle("sendcl9f89elemtype");
        
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
         * Gets the "imsi" element
         */
        java.lang.String getImsi();
        
        /**
         * Gets (as xml) the "imsi" element
         */
        org.apache.xmlbeans.XmlString xgetImsi();
        
        /**
         * Sets the "imsi" element
         */
        void setImsi(java.lang.String imsi);
        
        /**
         * Sets (as xml) the "imsi" element
         */
        void xsetImsi(org.apache.xmlbeans.XmlString imsi);
        
        /**
         * Gets the "cdpa" element
         */
        java.lang.String getCdpa();
        
        /**
         * Gets (as xml) the "cdpa" element
         */
        org.apache.xmlbeans.XmlString xgetCdpa();
        
        /**
         * Sets the "cdpa" element
         */
        void setCdpa(java.lang.String cdpa);
        
        /**
         * Sets (as xml) the "cdpa" element
         */
        void xsetCdpa(org.apache.xmlbeans.XmlString cdpa);
        
        /**
         * Gets the "cgpa" element
         */
        java.lang.String getCgpa();
        
        /**
         * Gets (as xml) the "cgpa" element
         */
        org.apache.xmlbeans.XmlString xgetCgpa();
        
        /**
         * Sets the "cgpa" element
         */
        void setCgpa(java.lang.String cgpa);
        
        /**
         * Sets (as xml) the "cgpa" element
         */
        void xsetCgpa(org.apache.xmlbeans.XmlString cgpa);
        
        /**
         * A factory class with static methods for creating instances
         * of this type.
         */
        
        public static final class Factory
        {
            public static ns.SendCLDocument.SendCL newInstance() {
              return (ns.SendCLDocument.SendCL) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
            
            public static ns.SendCLDocument.SendCL newInstance(org.apache.xmlbeans.XmlOptions options) {
              return (ns.SendCLDocument.SendCL) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
            
            private Factory() { } // No instance of this class allowed
        }
    }
    
    /**
     * A factory class with static methods for creating instances
     * of this type.
     */
    
    public static final class Factory
    {
        public static ns.SendCLDocument newInstance() {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, null ); }
        
        public static ns.SendCLDocument newInstance(org.apache.xmlbeans.XmlOptions options) {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newInstance( type, options ); }
        
        /** @param xmlAsString the string value to parse */
        public static ns.SendCLDocument parse(java.lang.String xmlAsString) throws org.apache.xmlbeans.XmlException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, null ); }
        
        public static ns.SendCLDocument parse(java.lang.String xmlAsString, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xmlAsString, type, options ); }
        
        /** @param file the file from which to load an xml document */
        public static ns.SendCLDocument parse(java.io.File file) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, null ); }
        
        public static ns.SendCLDocument parse(java.io.File file, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( file, type, options ); }
        
        public static ns.SendCLDocument parse(java.net.URL u) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, null ); }
        
        public static ns.SendCLDocument parse(java.net.URL u, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( u, type, options ); }
        
        public static ns.SendCLDocument parse(java.io.InputStream is) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, null ); }
        
        public static ns.SendCLDocument parse(java.io.InputStream is, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( is, type, options ); }
        
        public static ns.SendCLDocument parse(java.io.Reader r) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, null ); }
        
        public static ns.SendCLDocument parse(java.io.Reader r, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, java.io.IOException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( r, type, options ); }
        
        public static ns.SendCLDocument parse(javax.xml.stream.XMLStreamReader sr) throws org.apache.xmlbeans.XmlException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, null ); }
        
        public static ns.SendCLDocument parse(javax.xml.stream.XMLStreamReader sr, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( sr, type, options ); }
        
        public static ns.SendCLDocument parse(org.w3c.dom.Node node) throws org.apache.xmlbeans.XmlException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, null ); }
        
        public static ns.SendCLDocument parse(org.w3c.dom.Node node, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( node, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ns.SendCLDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static ns.SendCLDocument parse(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return (ns.SendCLDocument) org.apache.xmlbeans.XmlBeans.getContextTypeLoader().parse( xis, type, options ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, null ); }
        
        /** @deprecated {@link org.apache.xmlbeans.xml.stream.XMLInputStream} */
        public static org.apache.xmlbeans.xml.stream.XMLInputStream newValidatingXMLInputStream(org.apache.xmlbeans.xml.stream.XMLInputStream xis, org.apache.xmlbeans.XmlOptions options) throws org.apache.xmlbeans.XmlException, org.apache.xmlbeans.xml.stream.XMLStreamException {
          return org.apache.xmlbeans.XmlBeans.getContextTypeLoader().newValidatingXMLInputStream( xis, type, options ); }
        
        private Factory() { } // No instance of this class allowed
    }
}

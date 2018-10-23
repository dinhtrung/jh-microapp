
package com.ft.soap;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.ft.soap package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.ft.soap
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendLU }
     * 
     */
    public SendLU createSendLU() {
        return new SendLU();
    }

    /**
     * Create an instance of {@link SendCL }
     * 
     */
    public SendCL createSendCL() {
        return new SendCL();
    }

    /**
     * Create an instance of {@link SendISD }
     * 
     */
    public SendISD createSendISD() {
        return new SendISD();
    }

    /**
     * Create an instance of {@link SendLUResponse }
     * 
     */
    public SendLUResponse createSendLUResponse() {
        return new SendLUResponse();
    }

    /**
     * Create an instance of {@link Result }
     * 
     */
    public Result createResult() {
        return new Result();
    }

    /**
     * Create an instance of {@link SendCLResponse }
     * 
     */
    public SendCLResponse createSendCLResponse() {
        return new SendCLResponse();
    }

    /**
     * Create an instance of {@link SendISDResponse }
     * 
     */
    public SendISDResponse createSendISDResponse() {
        return new SendISDResponse();
    }

}

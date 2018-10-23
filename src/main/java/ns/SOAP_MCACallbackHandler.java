/**
 * SOAP_MCACallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.8  Built on : May 19, 2018 (07:06:11 BST)
 */
package ns;


/**
 *  SOAP_MCACallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class SOAP_MCACallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public SOAP_MCACallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public SOAP_MCACallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for sendLU method
     * override this method for handling normal response from sendLU operation
     */
    public void receiveResultsendLU(ns.SendLUResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sendLU operation
     */
    public void receiveErrorsendLU(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sendCL method
     * override this method for handling normal response from sendCL operation
     */
    public void receiveResultsendCL(ns.SendCLResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sendCL operation
     */
    public void receiveErrorsendCL(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sendISD method
     * override this method for handling normal response from sendISD operation
     */
    public void receiveResultsendISD(ns.SendISDResponseDocument result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sendISD operation
     */
    public void receiveErrorsendISD(java.lang.Exception e) {
    }
}

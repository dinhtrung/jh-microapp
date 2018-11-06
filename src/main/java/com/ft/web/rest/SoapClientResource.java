package com.ft.web.rest;

import java.net.URI;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ft.soap.AddToneBoxEvt;
import com.ft.soap.AddToneBoxResp;
import com.ft.soap.DelInboxToneEvt;
import com.ft.soap.DelInboxToneResponse;
import com.ft.soap.OrderToneEvt;
import com.ft.soap.QuerySettingEvt;
import com.ft.soap.QuerySettingResp;
import com.ft.soap.QueryToneEvt;
import com.ft.soap.QueryUserEvt;
import com.ft.soap.SubscribeEvt;
import com.ft.soap.ToneProvideService;
import com.ft.soap.ToneProvideServiceLocator;
import com.ft.soap.ToneProvide_PortType;
import com.ft.soap.UserManageServiceLocator;
import com.ft.soap.UserManageSoapBindingStub;
import com.ft.soap.UserManage_PortType;
import com.ft.soap.UserToneManageServiceLocator;
import com.ft.soap.UserToneManage_PortType;
	
/**
 * REST controller for managing Cdr.
 */
@RestController
@RequestMapping("/api/web-service")
public class SoapClientResource {

	private final Logger log = LoggerFactory.getLogger(SoapClientResource.class);
	
	@Autowired
	ObjectMapper mapper;
	/**
	 * #1 - Query the status of current user
	 * @param msisdn
	 * @return
	 * @throws Exception
	 * 
	 * [ {
  "accountType" : null,
  "bgTone" : "0",
  "brand" : "105",
  "brandName" : "STATUS RBT",
  "chargeMonthfeeAmount" : null,
  "chargeMonthfeeBeginTime" : null,
  "chargeMonthfeeEndTime" : null,
  "city" : null,
  "corpCode" : null,
  "corpID" : null,
  "corpName" : null,
  "currentLocal" : "-1",
  "dateOfBirth" : null,
  "email" : null,
  "firstFailTime" : null,
  "firstName" : null,
  "groupID" : null,
  "httpCallBackFlag" : null,
  "idCard" : null,
  "initialCreateTime" : "2018-10-30 11:06:09",
  "isArrear" : "0",
  "isInvalidUser" : "0",
  "isLocked" : "0",
  "isUseCopy" : "1",
  "languageID" : "4",
  "lastName" : null,
  "lockReason" : null,
  "lockType" : null,
  "lockedTime" : null,
  "lrn" : null,
  "monthFeeEndDay" : "2003-01-01 00:00:00",
  "moodModeID" : "-1",
  "multiAccountValue" : null,
  "musicClubId" : null,
  "musicClubPoints" : null,
  "name" : null,
  "nationality" : null,
  "netKind" : "1",
  "operator" : "34",
  "phoneNumber" : "8099444088",
  "playMode" : "2",
  "preNoticeFlag" : "1",
  "pwd" : "123456",
  "region" : "3",
  "regionName" : "",
  "registerTime" : "2018-10-30 11:06:09",
  "renewMode" : "1",
  "reserveTime" : "2018-11-26 23:59:59",
  "serviceOrders" : null,
  "sex" : null,
  "status" : "2",
  "subCosID" : "-1",
  "totalLoyaltyPonints" : null,
  "type" : "1",
  "unlimitFlag" : null,
  "updateStatusTime" : "2018-10-30 11:06:09"
} ]
	 * 
	 */
    @GetMapping("/user-manage/query")
    @Timed
    public ResponseEntity<Object> userManageQuery(@RequestParam("m") String msisdn) throws Exception {
    	UserManage_PortType stub = new UserManageServiceLocator()
    			.getUserManage(new URI("http://localhost:28080/jboss-net/services/UserManage").toURL())
    			;
    	QueryUserEvt event = new QueryUserEvt();
    	event.setPortalAccount("lcfglobal");
    	event.setPortalPwd("lcfglobal");
    	event.setPortalType("34");
    	event.setQueryType("2");
    	event.setPhoneNumber(msisdn);
		return ResponseEntity.ok(mapper.writeValueAsString(stub.query(event).getUserInfos()));
    }
    
    /**
     * #2 - Query Tone 
     * @param msisdn
     * @return
     * @throws Exception
     */
    @GetMapping("/tone-provide/query")
    @Timed
    public ResponseEntity<Object> userToneManageQuery() throws Exception {
    	ToneProvideService toneProvideService = new ToneProvideServiceLocator();
		ToneProvide_PortType stub = toneProvideService .getToneProvide(new URI("http://localhost:28080/jboss-net/services/ToneProvide").toURL());
    	QueryToneEvt event = new QueryToneEvt();
    	event.setPortalAccount("lcfglobal");
    	event.setPortalPwd("lcfglobal");
    	event.setPortalType("34");
		return ResponseEntity.ok(mapper.writeValueAsString(stub.queryTone(event).getQueryToneInfos()));
    }
    
    @GetMapping("/user-tone-manage/subscribe")
    @Timed
    public ResponseEntity<Object> userToneManageSubscribe(@RequestParam("m") String msisdn) throws Exception {
    	UserToneManage_PortType stub = new UserToneManageServiceLocator()
    			.getUserToneManage(new URI("http://localhost:28080/jboss-net/services/UserToneManage").toURL());
    	
		OrderToneEvt event = new OrderToneEvt();
		event.setPortalAccount("lcfglobal");
    	event.setPortalPwd("lcfglobal");
    	event.setPortalType("34");
		return ResponseEntity.ok().body(mapper.writeValueAsString(stub.orderTone(event)));
    }
    

    /**
	 * #1 - Query the status of current user
	 * @param msisdn
	 * @return
	 * @throws Exception
	 */
    @GetMapping("/user-manage/subscribe")
    @Timed
    public ResponseEntity<Object> userManageSubscribe(@RequestParam("m") String msisdn) throws Exception {
    	UserManage_PortType stub = new UserManageServiceLocator()
    			.getUserManage(new URI("http://localhost:28080/jboss-net/services/UserManage").toURL())
    			;
    	SubscribeEvt event = new SubscribeEvt();
    	event.setPortalAccount("lcfglobal");
    	event.setPortalPwd("lcfglobal");
    	event.setPortalType("34");
    	event.setRole("99");
    	event.setRoleCode("lcfglobal");
    	event.setPhoneNumber(msisdn);
    	event.setModuleCode("234105");
    	event.setPwd("123456");
    	event.setTradeMark("105");
		return ResponseEntity.ok(mapper.writeValueAsString(stub.subscribe(event)));
    }
    
    @GetMapping("/user-tone-manage/query-settings")
    @Timed
    public ResponseEntity<Object> userToneManage_QuerySettings(@RequestParam("m") String msisdn) throws Exception {
    	UserToneManage_PortType stub = new UserToneManageServiceLocator()
    			.getUserToneManage(new URI("http://localhost:28080/jboss-net/services/UserToneManage").toURL());
    	
		QuerySettingEvt event = new QuerySettingEvt();
		event.setPortalAccount("lcfglobal");
    	event.setPortalPwd("lcfglobal");
    	event.setPortalType("34");
    	event.setCalledUserType("1");
    	event.setCalledUserID(msisdn);
    	QuerySettingResp result = stub.querySetting(event);
    	
		return ResponseEntity.ok().body(mapper.writeValueAsString(result.getSettingInfos()));
    }
    
    @GetMapping("/user-tone-manage/del-inbox-tone")
    @Timed
    public ResponseEntity<Object> userToneManage_DelInboxTone(@RequestParam("m") String msisdn) throws Exception {
    	UserToneManage_PortType stub = new UserToneManageServiceLocator()
    			.getUserToneManage(new URI("http://localhost:28080/jboss-net/services/UserToneManage").toURL());
    	
    	DelInboxToneEvt event = new DelInboxToneEvt();
		event.setPortalAccount("lcfglobal");
    	event.setPortalPwd("lcfglobal");
    	event.setPortalType("34");
    	event.setPhoneNumber(msisdn);
    	event.setResourceCode("9008");
    	event.setResourceType("1");
    	DelInboxToneResponse result = stub.delInboxTone(event);
		return ResponseEntity.ok().body(mapper.writeValueAsString(result));
    }
    
    @GetMapping("/user-tone-manage/add-tone-box")
    @Timed
    public ResponseEntity<Object> userToneManage_AddInboxTone(@RequestParam("m") String msisdn) throws Exception {
    	UserToneManage_PortType stub = new UserToneManageServiceLocator()
    			.getUserToneManage(new URI("http://localhost:28080/jboss-net/services/UserToneManage").toURL());
    	
    	AddToneBoxEvt event = new AddToneBoxEvt();
		event.setPortalAccount("lcfglobal");
    	event.setPortalPwd("lcfglobal");
    	event.setPortalType("34");
    	event.setRole("99");
    	event.setRoleCode(msisdn);
    	event.setName("srbt1");
    	event.setToneCode("757138".split(""));
    	AddToneBoxResp result = stub.addToneBox(event);
		return ResponseEntity.ok().body(mapper.writeValueAsString(result));
    }
    
    
}

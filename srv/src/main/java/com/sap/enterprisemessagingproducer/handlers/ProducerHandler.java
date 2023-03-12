package com.sap.enterprisemessagingproducer.handlers;

import java.util.List;
import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject; 
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;


import com.sap.cds.services.cds.CdsService;
import com.sap.cds.services.handler.EventHandler;
import com.sap.cds.services.handler.annotations.On;
import com.sap.cds.services.handler.annotations.After;
import com.sap.cds.services.handler.annotations.ServiceName;
import com.sap.cds.services.messaging.MessagingService;

import cds.gen.enterprisemessagingproducerservice.EnterpriseMessagingProducerService_;
import cds.gen.enterprisemessagingproducerservice.Students;
import cds.gen.sap.capire.enterprisemessagingproducer.Student;

@Component
//@Profile("cloud")
@ServiceName("EnterpriseMessagingProducerService")
public class ProducerHandler implements EventHandler {

	private static final Logger logger = LoggerFactory.getLogger(ProducerHandler.class);

	@Autowired
	@Qualifier("taskmanager-events")
	MessagingService messagingService;

	@On(event = CdsService.EVENT_CREATE, entity = "EnterpriseMessagingProducerService.Students")
	public void produceStudentEnrollementEvent(List<Students> studentlists) throws Exception {

		JSONObject payload = new JSONObject();

		JSONArray jsonArray = new JSONArray();

		for (Students students : studentlists) {
			JSONObject jsonObject = new JSONObject();

			jsonObject.put(Student.FIRST_NAME, students.getFirstName());
			jsonObject.put(Student.LAST_NAME, students.getLastName());
			jsonObject.put(Student.CURRENT_CLASS, students.getCurrentClass());

			jsonArray.add(jsonObject);

		}

		payload.put("data", jsonArray);

		logger.info("Data Emitted to the topic  {}", payload.toJSONString());
		messagingService.emit("sap/taskmanager-events/event-mesh/Topic1", payload);

	}

/* 	@On(service = "taskmanager-events", event = "sap/taskmanager-events/event-mesh/user-registration-topic")
    public void listen(TopicMessageEventContext context) {
        
        logger.info("---------------------------Reading Payload Emitted by the Event----------------------------------------------------");
         logger.info("checking if the message if read from SAP Event Mesh {}",context.getIsInbound());
        logger.info("reading event id{}",context.getMessageId());
        logger.info("reading event data{}", context.getData());
    } */

}

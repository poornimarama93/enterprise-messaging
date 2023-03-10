using { sap.capire.enterpriseMessagingProducer as db}  from '../db/schema';

@path: 'enterprise'
service EnterpriseMessagingProducerService {

    entity Students as projection on db.student;


}
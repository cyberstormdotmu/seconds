ACCOUNTING
==========

(TODO: please update this and the other README's, especially test/README.TXT)

A short note on the module names:

 * "core" - This is the main entity and logic module, it is wrapped by 
   the "view" layers "service" or "service-protobuf".
   
The following modules participate in the protobuf API:

 * "core" - See above...
 * "protobuf" - Message/API definition
 * "service-protobuf" - Message driven wrapper around "core"
 * "service-protobuf-web" - Message driven JMS web application 
   using "service-protobuf
 
And, non-crucial modules:
 
 * "protobuf-bots" - Simple behaviour bots that uses JMS
 * "service-protobuf-intergation-test" - Simple tests, again over JMS
 
The following modules contains the SOAP web service:

 * "api" - Service interface and DTO
 * "service" - Service wrapper around "core"
 * "service-web" - SOAP web service implementation around "service"

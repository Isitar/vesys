cd C:\Documents\Kurse\DistributedSystems\Teaching\06_WebServices2\06_WebServices2

REM wsgen ist nicht noetig
wsgen -cp bin -keep -s src-gen -d bin ch.fhnw.ds.ws.temp.server.TemperatureConversionsImpl

wsimport -keep -p ch.fhnw.ds.ws.temp.client.jaxws -d bin -s src-gen http://localhost:19876/temp?wsdl


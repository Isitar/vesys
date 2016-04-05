cd C:\Documents\Kurse\DistributedSystems\Teaching\06_WebServices2\06_WebServices2

REM wsgen ist nicht noetig
wsgen -cp bin -keep -s src-gen -d bin ch.fhnw.ds.ws.graph.server.GraphServiceImpl

wsimport -keep -p ch.fhnw.ds.ws.graph.client.jaxws -d bin -s src-gen http://localhost:9877/graph?wsdl

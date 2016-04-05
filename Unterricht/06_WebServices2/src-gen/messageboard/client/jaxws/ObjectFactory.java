
package messageboard.client.jaxws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the messageboard.client.jaxws package. 
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

    private final static QName _Create_QNAME = new QName("http://server.messageboard/", "create");
    private final static QName _CreateResponse_QNAME = new QName("http://server.messageboard/", "createResponse");
    private final static QName _GetMessage_QNAME = new QName("http://server.messageboard/", "getMessage");
    private final static QName _Delete_QNAME = new QName("http://server.messageboard/", "delete");
    private final static QName _GetAllMessagesFromResponse_QNAME = new QName("http://server.messageboard/", "getAllMessagesFromResponse");
    private final static QName _GetMessageResponse_QNAME = new QName("http://server.messageboard/", "getMessageResponse");
    private final static QName _GetAllMessagesFrom_QNAME = new QName("http://server.messageboard/", "getAllMessagesFrom");
    private final static QName _DeleteResponse_QNAME = new QName("http://server.messageboard/", "deleteResponse");
    private final static QName _LengthExceededException_QNAME = new QName("http://server.messageboard/", "LengthExceededException");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: messageboard.client.jaxws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetAllMessagesFrom }
     * 
     */
    public GetAllMessagesFrom createGetAllMessagesFrom() {
        return new GetAllMessagesFrom();
    }

    /**
     * Create an instance of {@link DeleteResponse }
     * 
     */
    public DeleteResponse createDeleteResponse() {
        return new DeleteResponse();
    }

    /**
     * Create an instance of {@link LengthExceededException }
     * 
     */
    public LengthExceededException createLengthExceededException() {
        return new LengthExceededException();
    }

    /**
     * Create an instance of {@link Create }
     * 
     */
    public Create createCreate() {
        return new Create();
    }

    /**
     * Create an instance of {@link CreateResponse }
     * 
     */
    public CreateResponse createCreateResponse() {
        return new CreateResponse();
    }

    /**
     * Create an instance of {@link GetMessage }
     * 
     */
    public GetMessage createGetMessage() {
        return new GetMessage();
    }

    /**
     * Create an instance of {@link Delete }
     * 
     */
    public Delete createDelete() {
        return new Delete();
    }

    /**
     * Create an instance of {@link GetAllMessagesFromResponse }
     * 
     */
    public GetAllMessagesFromResponse createGetAllMessagesFromResponse() {
        return new GetAllMessagesFromResponse();
    }

    /**
     * Create an instance of {@link GetMessageResponse }
     * 
     */
    public GetMessageResponse createGetMessageResponse() {
        return new GetMessageResponse();
    }

    /**
     * Create an instance of {@link Message }
     * 
     */
    public Message createMessage() {
        return new Message();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Create }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "create")
    public JAXBElement<Create> createCreate(Create value) {
        return new JAXBElement<Create>(_Create_QNAME, Create.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CreateResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "createResponse")
    public JAXBElement<CreateResponse> createCreateResponse(CreateResponse value) {
        return new JAXBElement<CreateResponse>(_CreateResponse_QNAME, CreateResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessage }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "getMessage")
    public JAXBElement<GetMessage> createGetMessage(GetMessage value) {
        return new JAXBElement<GetMessage>(_GetMessage_QNAME, GetMessage.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Delete }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "delete")
    public JAXBElement<Delete> createDelete(Delete value) {
        return new JAXBElement<Delete>(_Delete_QNAME, Delete.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllMessagesFromResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "getAllMessagesFromResponse")
    public JAXBElement<GetAllMessagesFromResponse> createGetAllMessagesFromResponse(GetAllMessagesFromResponse value) {
        return new JAXBElement<GetAllMessagesFromResponse>(_GetAllMessagesFromResponse_QNAME, GetAllMessagesFromResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetMessageResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "getMessageResponse")
    public JAXBElement<GetMessageResponse> createGetMessageResponse(GetMessageResponse value) {
        return new JAXBElement<GetMessageResponse>(_GetMessageResponse_QNAME, GetMessageResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetAllMessagesFrom }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "getAllMessagesFrom")
    public JAXBElement<GetAllMessagesFrom> createGetAllMessagesFrom(GetAllMessagesFrom value) {
        return new JAXBElement<GetAllMessagesFrom>(_GetAllMessagesFrom_QNAME, GetAllMessagesFrom.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "deleteResponse")
    public JAXBElement<DeleteResponse> createDeleteResponse(DeleteResponse value) {
        return new JAXBElement<DeleteResponse>(_DeleteResponse_QNAME, DeleteResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link LengthExceededException }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://server.messageboard/", name = "LengthExceededException")
    public JAXBElement<LengthExceededException> createLengthExceededException(LengthExceededException value) {
        return new JAXBElement<LengthExceededException>(_LengthExceededException_QNAME, LengthExceededException.class, null, value);
    }

}

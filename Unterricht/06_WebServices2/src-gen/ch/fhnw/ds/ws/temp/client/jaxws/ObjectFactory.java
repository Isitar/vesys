
package ch.fhnw.ds.ws.temp.client.jaxws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ch.fhnw.ds.ws.temp.client.jaxws package. 
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

    private final static QName _CelciusToFahrenheit_QNAME = new QName("http://temp.ws.ds.fhnw.ch/", "celciusToFahrenheit");
    private final static QName _CelciusToFahrenheitResponse_QNAME = new QName("http://temp.ws.ds.fhnw.ch/", "celciusToFahrenheitResponse");
    private final static QName _FahrenheitToCelsius_QNAME = new QName("http://temp.ws.ds.fhnw.ch/", "fahrenheitToCelsius");
    private final static QName _FahrenheitToCelsiusResponse_QNAME = new QName("http://temp.ws.ds.fhnw.ch/", "fahrenheitToCelsiusResponse");
    private final static QName _GetRandomTemperature_QNAME = new QName("http://temp.ws.ds.fhnw.ch/", "getRandomTemperature");
    private final static QName _GetRandomTemperatureResponse_QNAME = new QName("http://temp.ws.ds.fhnw.ch/", "getRandomTemperatureResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ch.fhnw.ds.ws.temp.client.jaxws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FahrenheitToCelsius }
     * 
     */
    public FahrenheitToCelsius createFahrenheitToCelsius() {
        return new FahrenheitToCelsius();
    }

    /**
     * Create an instance of {@link CelciusToFahrenheit }
     * 
     */
    public CelciusToFahrenheit createCelciusToFahrenheit() {
        return new CelciusToFahrenheit();
    }

    /**
     * Create an instance of {@link CelciusToFahrenheitResponse }
     * 
     */
    public CelciusToFahrenheitResponse createCelciusToFahrenheitResponse() {
        return new CelciusToFahrenheitResponse();
    }

    /**
     * Create an instance of {@link GetRandomTemperature }
     * 
     */
    public GetRandomTemperature createGetRandomTemperature() {
        return new GetRandomTemperature();
    }

    /**
     * Create an instance of {@link GetRandomTemperatureResponse }
     * 
     */
    public GetRandomTemperatureResponse createGetRandomTemperatureResponse() {
        return new GetRandomTemperatureResponse();
    }

    /**
     * Create an instance of {@link FahrenheitToCelsiusResponse }
     * 
     */
    public FahrenheitToCelsiusResponse createFahrenheitToCelsiusResponse() {
        return new FahrenheitToCelsiusResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CelciusToFahrenheit }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://temp.ws.ds.fhnw.ch/", name = "celciusToFahrenheit")
    public JAXBElement<CelciusToFahrenheit> createCelciusToFahrenheit(CelciusToFahrenheit value) {
        return new JAXBElement<CelciusToFahrenheit>(_CelciusToFahrenheit_QNAME, CelciusToFahrenheit.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CelciusToFahrenheitResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://temp.ws.ds.fhnw.ch/", name = "celciusToFahrenheitResponse")
    public JAXBElement<CelciusToFahrenheitResponse> createCelciusToFahrenheitResponse(CelciusToFahrenheitResponse value) {
        return new JAXBElement<CelciusToFahrenheitResponse>(_CelciusToFahrenheitResponse_QNAME, CelciusToFahrenheitResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FahrenheitToCelsius }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://temp.ws.ds.fhnw.ch/", name = "fahrenheitToCelsius")
    public JAXBElement<FahrenheitToCelsius> createFahrenheitToCelsius(FahrenheitToCelsius value) {
        return new JAXBElement<FahrenheitToCelsius>(_FahrenheitToCelsius_QNAME, FahrenheitToCelsius.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link FahrenheitToCelsiusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://temp.ws.ds.fhnw.ch/", name = "fahrenheitToCelsiusResponse")
    public JAXBElement<FahrenheitToCelsiusResponse> createFahrenheitToCelsiusResponse(FahrenheitToCelsiusResponse value) {
        return new JAXBElement<FahrenheitToCelsiusResponse>(_FahrenheitToCelsiusResponse_QNAME, FahrenheitToCelsiusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRandomTemperature }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://temp.ws.ds.fhnw.ch/", name = "getRandomTemperature")
    public JAXBElement<GetRandomTemperature> createGetRandomTemperature(GetRandomTemperature value) {
        return new JAXBElement<GetRandomTemperature>(_GetRandomTemperature_QNAME, GetRandomTemperature.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetRandomTemperatureResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://temp.ws.ds.fhnw.ch/", name = "getRandomTemperatureResponse")
    public JAXBElement<GetRandomTemperatureResponse> createGetRandomTemperatureResponse(GetRandomTemperatureResponse value) {
        return new JAXBElement<GetRandomTemperatureResponse>(_GetRandomTemperatureResponse_QNAME, GetRandomTemperatureResponse.class, null, value);
    }

}

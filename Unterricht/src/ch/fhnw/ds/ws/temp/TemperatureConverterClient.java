package ch.fhnw.ds.ws.temp;

import java.math.BigDecimal;

import javax.xml.ws.Holder;

import ch.fhnw.ds.ws.temp.client.jaxws.GetRandomTemperatureResponse;
import ch.fhnw.ds.ws.temp.client.jaxws.TemperatureConversions;
import ch.fhnw.ds.ws.temp.client.jaxws.TemperatureConversionsService;

public class TemperatureConverterClient {
	public static void main(String[] args) {
		TemperatureConversionsService service = new TemperatureConversionsService();
		TemperatureConversions port = service.getTemperatureConversions();
		Holder<BigDecimal> randomCelcius = new Holder<BigDecimal>();
		Holder<BigDecimal> randomFarenheit = new Holder<BigDecimal>();
		port.getRandomTemperature(randomCelcius, randomFarenheit);
		System.out.println("randomCelcius: " + randomCelcius.value.toString());
		System.out.println("randomFarenheit: " + randomFarenheit.value.toString());

		// celciusValue is argument and result
		Holder<BigDecimal> celciusValue = new Holder<BigDecimal>();
		celciusValue.value = new BigDecimal(100);
		port.celciusToFahrenheit(celciusValue);
		System.out.println("Celcius: " + celciusValue.value.toString());

		// celciusValue is argument and result
		port.fahrenheitToCelsius(celciusValue);
		System.out.println("Farenheit: " + celciusValue.value.toString());

	}
}

package edu.uclm.esi.carreful.http;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import edu.uclm.esi.carreful.model.Carrito;

@RestController
@RequestMapping("payments")
public class PaymentsController extends CookiesController {

	// This is your real test secret API key.
	static {
		Stripe.apiKey = "sk_test_51Idbt6HmArDnS3pXZqkxllvllEdJ2Ar22MtcKMBGxsbOpyWsvq2NN3eBWtI3SFZYGWmholpJrrcGfvEYaKnwDjgM003YICHUbA";
	}

	@PostMapping("/solicitarPreautorizacion")
	public String solicitarPreautorizacion(HttpServletRequest request, @RequestBody Map<String, Object> info) {
		try {
			/*
			 * long importe = (long) info.get("importe");
			 * Con eso los clientes podrian cambiar el importe
			 * Mejro usar la sesion del cliente para tener su carrito y usarr getImporte()
			 */
			Carrito carrito = (Carrito) request.getSession().getAttribute("carrito");
			System.out.println((long)carrito.getImporte());
			PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder().setCurrency("eur")
					.setAmount((long) carrito.getImporte() * 100).build();
			// Create a PaymentIntent with the order amount and currency
			PaymentIntent intent = PaymentIntent.create(createParams);
			JSONObject jso = new JSONObject(intent.toJson());
			System.out.println(intent.toJson());
			return jso.getString("client_secret");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}

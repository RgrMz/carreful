package edu.uclm.esi.carreful.http;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
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
			Carrito carrito = (Carrito) request.getSession().getAttribute("carrito");
			double importe = carrito.getImporte() * 100;
			PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder().setCurrency("eur")
					.setAmount((long) importe).build();
			// Create a PaymentIntent with the order amount and currency
			PaymentIntent intent = PaymentIntent.create(createParams);
			JSONObject jso = new JSONObject(intent.toJson());
			return jso.getString("client_secret");
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}

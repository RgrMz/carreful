package edu.uclm.esi.carreful.http;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.carreful.dao.OrderedProductDao;
import edu.uclm.esi.carreful.dao.PedidoDao;
import edu.uclm.esi.carreful.model.Carrito;
import edu.uclm.esi.carreful.model.Estado;
import edu.uclm.esi.carreful.model.OrderedProduct;
import edu.uclm.esi.carreful.model.Pedido;
import edu.uclm.esi.carreful.model.interfaces.GastosDeEnvio;
import edu.uclm.esi.carreful.tokens.Email;


@RestController
@RequestMapping("pedido")
public class PedidosController {

	@Autowired
	private PedidoDao pedidoDao;
	
	@Autowired
	private OrderedProductDao orderedProductDao;
	
	private Email smtp = new Email();
	
	@PostMapping("/guardarPedido")
	public void add(HttpServletRequest request, @RequestBody Map<String, Object> info) {
		Carrito carrito = (Carrito) request.getSession().getAttribute("carrito");
		//String validacionEmail = "^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$";
		//Pattern patron = Pattern.compile(validacionEmail);
		try {
			JSONObject jso = new JSONObject(info);
			String nombre = jso.optString("nombre");
			String apellidos = jso.optString("apellidos");
			String email = jso.optString("email");
			/* Matcher matcher = patron.matcher(email); */
			// Hacer antes del pago en todo caso
			// Checkear validez de campos: opcional
			// Checkear congelados para dom express obligatorio
			// Checkear formulario
			// Checkear el envio de email para ver estado pedido
			String telefonoMovil = jso.optString("telefonoMovil");
			String ciudad = jso.optString("ciudad");
			String provincia = jso.optString("provincia");
			String pais = jso.optString("pais");
			String codigoPostal = jso.optString("codigoPostal");
			String tipoPedido = jso.optString("tipoPedido");
			String direccion = jso.optString("direccion");
			double precioPedido = jso.optDouble("precioPedido");
			Pedido pedido = new Pedido();
			pedido.setDireccion(direccion);
			pedido.setNombre(nombre);
			pedido.setApellidos(apellidos);
			pedido.setEmail(email);
			pedido.setTelefonoMovil(telefonoMovil);
			pedido.setCiudad(ciudad);
			pedido.setProvincia(provincia);
			pedido.setPais(pais);
			pedido.setCodigoPostal(Integer.parseInt(codigoPostal));
			pedido.setTipoPedido(tipoPedido);
			pedido.setEstado(Estado.RECIBIDO);
			pedido.setPrecioPedido(precioPedido);
			
			pedidoDao.save(pedido);
			guardarProductosDelPedido(carrito, pedido);
			String texto = "Para consultar el estado del pedido realizado, pulsa aqui: <br></br>"
					+ "<a href=\"http://localhost:8080?ojr=pedido&idPedido=" + pedido.getIdPedido() + "\">Consultar mi pedido</a>";
			smtp.send(email, "Carreful: Consulta el estado de tu pedido", texto);
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}
	
	public void guardarProductosDelPedido(Carrito carrito, Pedido pedido) {
		for(OrderedProduct op : carrito.getProducts()) {
			op.setPedido(pedido);
			orderedProductDao.save(op);
		}
	}
	
	@GetMapping("/consultarPedido/{idPedido}")
	public Pedido consultarPedido (HttpServletResponse response, @PathVariable String idPedido) {
		Optional<Pedido> optPedido = pedidoDao.findById(idPedido);
		if (optPedido.isPresent()) {
			return optPedido.get();
		} else {
			try {
				response.sendError(404, "El pedido consultado no existe");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}
	
	@GetMapping("/precioGastosEnvio/{tipoPedido}")
	public double precioGastosEnvio(@PathVariable String tipoPedido) {
		switch(tipoPedido) {
		case "domicilio":
			return GastosDeEnvio.getGastosdomicilio();
		case "express":
			return GastosDeEnvio.getGastosexpress();
		default:
			return GastosDeEnvio.getGastosrecogida();
		}
	}
	
}

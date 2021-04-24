package edu.uclm.esi.carreful.http;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.carreful.dao.OrderedProductDao;
import edu.uclm.esi.carreful.dao.PedidoDao;
import edu.uclm.esi.carreful.model.Carrito;
import edu.uclm.esi.carreful.model.OrderedProduct;
import edu.uclm.esi.carreful.model.Pedido;


@RestController
@RequestMapping("pedido")
public class PedidosController {

	@Autowired
	private PedidoDao pedidoDao;
	
	@Autowired
	private OrderedProductDao orderedProductDao;
	
	@PostMapping("/guardarPedido")
	public void add(HttpServletRequest request, @RequestBody Map<String, Object> info) {
		Carrito carrito = (Carrito) request.getSession().getAttribute("carrito");
		String validacionEmail = "^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$";
		Pattern patron = Pattern.compile(validacionEmail);
		try {
			JSONObject jso = new JSONObject(info);
			String nombre = jso.optString("nombre");
			String apellidos = jso.optString("apellidos");
			String email = jso.optString("email");
			Matcher matcher = patron.matcher(email);
			/*if(!matcher.matches()) {
				throw new Exception("Por favor, introduzca un email válido.");
			}*/
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
			Pedido pedido = new Pedido();
			pedido.setNombre(nombre);
			pedido.setApellidos(apellidos);
			pedido.setEmail(email);
			pedido.setTelefonoMovil(telefonoMovil);
			pedido.setCiudad(ciudad);
			pedido.setProvincia(provincia);
			pedido.setPais(pais);
			pedido.setCodigoPostal(Integer.parseInt(codigoPostal));
			pedido.setTipoPedido(tipoPedido);
			pedidoDao.save(pedido);
			guardarProductosDelPedido(carrito, pedido);
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
	
}

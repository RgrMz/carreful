package edu.uclm.esi.carreful.http;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.carreful.dao.CategoriesDao;
import edu.uclm.esi.carreful.dao.ProductDao;
import edu.uclm.esi.carreful.exceptionhandling.CarrefulException;
import edu.uclm.esi.carreful.model.Carrito;
import edu.uclm.esi.carreful.model.Categoria;
import edu.uclm.esi.carreful.model.Product;

@RestController
@RequestMapping("product")
public class ProductController extends CookiesController {

	@Autowired
	private ProductDao productDao;

	@Autowired
	private CategoriesDao categoriesDao;

	private static final String CARRITO_STRING = "carrito";
	
	private Carrito carrito = new Carrito();

	@PostMapping("/add")
	public void add(@RequestBody Product product) {
		try {
			productDao.save(product);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@GetMapping("/getTodos")
	public List<Product> get() {
		try {
			return productDao.findAll();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/getPrecio/{nombre}")
	public double getPrecio(@PathVariable String nombre) {
		try {
			Optional<Product> optProduct = productDao.findById(nombre);
			if (optProduct.isPresent())
				return optProduct.get().getPrecio();
			throw new CarrefulException(HttpStatus.NOT_FOUND, "El producto no existe");
		} catch (CarrefulException e) {
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}
	}

	@GetMapping("/getCarrito")
	public Carrito getCarrito(HttpServletRequest request) {
		try {
			carrito = (Carrito) request.getSession().getAttribute(CARRITO_STRING);
			if (carrito == null) {
				carrito = new Carrito();
				request.getSession().setAttribute(CARRITO_STRING, carrito);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
		return carrito;
	}

	@PostMapping("/eliminarUnidadDelCarrito/{nombre}")
	public Carrito elminarUnidadDelCarrito(HttpServletRequest request, @PathVariable String nombre) {
		carrito = (Carrito) request.getSession().getAttribute(CARRITO_STRING);
		Product producto = new Product();
		if (carrito == null) {
			carrito = new Carrito();
			request.getSession().setAttribute(CARRITO_STRING, carrito);
		}
		Optional<Product> optProducto = productDao.findByNombre(nombre);
		if (optProducto.isPresent()) {
			producto = optProducto.get();
		}
		carrito.eliminarUnidad(producto, 1);
		return carrito;
	}

	@PostMapping("/addUnidadDelCarrito/{nombre}")
	public Carrito addUnidadDelCarrito(HttpServletRequest request, @PathVariable String nombre) {
		carrito = (Carrito) request.getSession().getAttribute(CARRITO_STRING);
		Product producto = new Product();
		if (carrito == null) {
			carrito = new Carrito();
			request.getSession().setAttribute(CARRITO_STRING, carrito);
		}
		Optional<Product> optProducto = productDao.findByNombre(nombre);
		if (optProducto.isPresent()) {
			producto = optProducto.get();
		}
		carrito.add(producto, 1);
		return carrito;
	}

	@PostMapping("/eliminarProductoDelCarrito/{nombre}")
	public Carrito eliminarProductoDelCarrito(HttpServletRequest request, @PathVariable String nombre) {
		carrito = (Carrito) request.getSession().getAttribute(CARRITO_STRING);
		Product producto = new Product();
		if (carrito == null) {
			carrito = new Carrito();
			request.getSession().setAttribute(CARRITO_STRING, carrito);
		}
		Optional<Product> optProducto = productDao.findByNombre(nombre);
		if (optProducto.isPresent()) {
			producto = optProducto.get();
		}
		carrito.eliminarProducto(producto);
		return carrito;
	}
	
	@PutMapping("/vaciarCarrito")
	public void vaciarCarrito(HttpServletRequest request) {
		carrito = (Carrito) request.getSession().getAttribute(CARRITO_STRING);
		try {
			carrito.vaciarCarrito();
			if(carrito.getProducts().size() != 0)
				throw new CarrefulException(HttpStatus.NOT_MODIFIED, "No se pudo vaciar el carrito");
		} catch (CarrefulException e) {
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}
	}

	@DeleteMapping("/borrarProducto/{nombre}")
	public void borrarProducto(@PathVariable String nombre) {
		try {
			Optional<Product> optProduct = productDao.findById(nombre);
			if (optProduct.isPresent())
				productDao.deleteById(nombre);
			else
				throw new CarrefulException(HttpStatus.NOT_FOUND, "El producto no existe");
		} catch (CarrefulException e) {
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}
	}

	@GetMapping("/getImporte")
	public double getMapping(HttpServletRequest request) {
		carrito = (Carrito) request.getSession().getAttribute(CARRITO_STRING);
		if (carrito == null) {
			carrito = new Carrito();
			request.getSession().setAttribute(CARRITO_STRING, carrito);
		}
		BigDecimal bigDecimal = new BigDecimal(Double.toString(carrito.getImporte()));

		bigDecimal = bigDecimal.setScale(2, RoundingMode.HALF_UP);

		return bigDecimal.doubleValue();
	}

	@GetMapping("/getCategorias")
	public List<Categoria> getCategorias() {
		try {
			return categoriesDao.findAll();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}

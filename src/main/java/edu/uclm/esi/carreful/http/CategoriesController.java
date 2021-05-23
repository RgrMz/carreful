package edu.uclm.esi.carreful.http;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.carreful.dao.CategoriesDao;
import edu.uclm.esi.carreful.dao.ProductDao;
import edu.uclm.esi.carreful.exceptionhandling.CarrefulException;
import edu.uclm.esi.carreful.model.Categoria;
import edu.uclm.esi.carreful.model.Product;

@RestController
@RequestMapping("categories")
public class CategoriesController extends CookiesController {
	
	@Autowired
	private CategoriesDao categoriesDao;
	
	@Autowired
	private ProductDao productDao;

	@GetMapping("/getTodas")
	public List<Categoria> get() {
		try {
			return categoriesDao.findAll();
		} catch(Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}
	
	@GetMapping("/{categoria}")
	public List<Product> getProductosPorCategoria(@PathVariable String categoria) {
		List<Product> productosPorCategoria = null;
		try {
			int idCategoria = categoriesDao.codigoCategoria(categoria);
			productosPorCategoria = productDao.productosPorCategoria(idCategoria);
			if(productosPorCategoria == null)
				throw new CarrefulException(HttpStatus.NOT_FOUND, "No existe la categoria " + categoria);
		} catch(CarrefulException e) {
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}
		return productosPorCategoria;
	}
}

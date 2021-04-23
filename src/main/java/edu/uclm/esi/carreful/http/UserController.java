package edu.uclm.esi.carreful.http;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import edu.uclm.esi.carreful.dao.TokenDao;
import edu.uclm.esi.carreful.dao.UserDao;
import edu.uclm.esi.carreful.model.User;
import edu.uclm.esi.carreful.tokens.AlmacenUsuarios;
import edu.uclm.esi.carreful.tokens.Email;
import edu.uclm.esi.carreful.tokens.Token;

@RestController
@RequestMapping("user")
public class UserController extends CookiesController {

	@Autowired
	UserDao userDao;

	@Autowired
	TokenDao tokenDao;

	Email smtp = new Email();

	@GetMapping("usarToken/{tokenId}")
	public void usarToken(HttpServletResponse response, @PathVariable String tokenId) throws IOException {
		Optional<Token> optToken = tokenDao.findById(tokenId);
		if (optToken.isPresent()) {
			Token token = optToken.get();
			if (token.isUsed())
				response.sendError(409, "El token ya se utilizó");
			else {
				response.sendRedirect(
						"http://localhost:8080?ojr=setNewPassword&token=" + tokenId + "&email=" + token.getEmail());
			}
		} else {
			response.sendError(404, "El token no existe");
		}
	}

	@GetMapping("confirmarCorreo/{tokenId}")
	public void confirmarCorreo(HttpServletResponse response, @PathVariable String tokenId) throws IOException {
		Optional<Token> optToken = tokenDao.findById(tokenId);
		if (optToken.isPresent()) {
			Token token = optToken.get();
			if (token.isUsed())
				response.sendError(409, "El token ya se utilizó");
			else {
				User user = userDao.findByEmail(token.getEmail());
				if (user != null) {
					user.setActivado(true);
					userDao.save(user);
					response.sendRedirect("http://localhost:8080?ojr=login");
				} else {
					response.sendError(404, "El usuario no existe");
				}
			}
		} else {
			response.sendError(404, "El token no existe");
		}
	}

	@GetMapping("/recoverPwd")
	public void recoverPwd(@RequestParam String email) {
		try {
			if (email.length() == 0) {
				throw new Exception("Debes introducir un correo válido en el campo \"Correo electrónico\"");
			}
			User user = userDao.findByEmail(email);
			if (user != null) {
				Token token = new Token(email);
				tokenDao.save(token);

				String texto = "Para recuperar tu contraseña, pulsa aquí: "
						+ "<a href='http://localhost:8080/user/usarToken/" + token.getId() + "'>aquí</a>";
				smtp.send(email, "Carreful: recuperación de contraseña", texto);
			}
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}

	@PostMapping("/login")
	public void login(HttpServletRequest request, @RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			String email = jso.optString("email");
			if (email.length() == 0)
				throw new Exception("Debes indicar tu nombre de usuario");
			String pwd = jso.optString("pwd");
			User user = userDao.findByEmailAndPwd(email, DigestUtils.sha512Hex(pwd));
			if (user == null)
				throw new Exception("Credenciales inválidas");
			if (!user.isActivado())
				throw new Exception(
						"No confirmaste tu cuenta de correo electrónico. Por favor, confirmala para poder entrar al sistema.");
			boolean recuerdame = jso.optBoolean("recuerdame");
			AlmacenUsuarios almacen = AlmacenUsuarios.getInstance();
			if (recuerdame) {
				if (!almacen.isRecordado(request.getSession().getId())) {
					almacen.recordarUsuario(request.getSession().getId(), email, pwd);
					request.getSession().setMaxInactiveInterval(0);
				}
			} else {
				almacen.dejarDeRecordarUsuario(request.getSession().getId());
			}
			request.getSession().setAttribute("userEmail", email);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage());
		}
	}

	@GetMapping("/getUsuarioRecordado")
	public String[] getUsuarioRecordado(HttpServletRequest request) {
		AlmacenUsuarios almacen = AlmacenUsuarios.getInstance();
		if (almacen.isRecordado(request.getSession().getId())) {
			String[] datos = almacen.getDatos(request.getSession().getId());
			return datos;
		} else {
			return null;
		}
	}

	@PutMapping("/register")
	public void register(@RequestBody Map<String, Object> info) {
		try {
			JSONObject jso = new JSONObject(info);
			String userName = jso.optString("userName");
			if (userName.length() == 0)
				throw new Exception("Debes indicar tu nombre de usuario");
			String email = jso.optString("email");
			if (email.length() == 0)
				throw new Exception("Debes indicar un email válido");
			String pwd1 = jso.optString("pwd");
			String pwd2 = jso.optString("pwd2");
			if (!pwd1.equals(pwd2))
				throw new Exception("La contraseña no coincide con su confirmación");
			if (pwd1.length() < 8)
				throw new Exception("La contraseña tiene que tener al menos 8 caracteres");
			User user = new User();
			user.setEmail(email);
			user.setPwd(pwd1);
			user.setPicture(jso.optString("picture"));
			Token token = new Token(email);
			tokenDao.save(token);
			String correoConfirmacion = "<p>Para confirmar tu cuenta de correo y finalizar"
					+ " el registro de tu cuenta, clicka aqui: </p><br>"
					+ "<a href=\"http://localhost:8080/user/confirmarCorreo/" + token.getId()
					+ "\"> Activa tu cuenta</a>";
			smtp.send(email, "Carreful: confirmación de cuenta", correoConfirmacion);
			userDao.save(user);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
		}
	}
}

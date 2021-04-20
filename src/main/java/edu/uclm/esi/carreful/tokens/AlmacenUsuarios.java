package edu.uclm.esi.carreful.tokens;

import java.util.HashMap;

public class AlmacenUsuarios {

	private static AlmacenUsuarios yo;

	private HashMap<String, String[]> usuariosRecordados;

	private AlmacenUsuarios() {
		this.usuariosRecordados = new HashMap<String, String[]>();
	}
	
	public static AlmacenUsuarios getInstance() {
		if (yo==null) {
			synchronized (AlmacenUsuarios.class) {
				if (yo==null)
					yo=new AlmacenUsuarios();				
			}
		}
		return yo;
	}
	
	public void recordarUsuario(String idSession, String correo, String pwd) {
		this.usuariosRecordados.put(idSession, new String[] {correo, pwd});
	}
	
	public String[] getDatos(String idSession) {
		return this.usuariosRecordados.get(idSession);
	}
	
	public void dejarDeRecordarUsuario(String idSession) {
		this.usuariosRecordados.remove(idSession);
	}
	
	public boolean isRecordado(String idSession) {
		return this.usuariosRecordados.containsKey(idSession);
	}

}

package edu.uclm.esi.carreful.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import edu.uclm.esi.carreful.model.Categoria;

@Repository
public interface CategoriesDao extends JpaRepository <Categoria, String> {

	Optional<Categoria> findByNombre(String nombre);
	
	@Query(value = "SELECT id FROM categoria WHERE nombre = ?1", nativeQuery = true)
	int codigoCategoria(String nombre);
}

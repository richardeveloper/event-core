package br.com.kafka.repositories;

import br.com.kafka.entities.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  boolean existsByNome(String nome);

  Optional<Usuario> findByNome(String nome);
}

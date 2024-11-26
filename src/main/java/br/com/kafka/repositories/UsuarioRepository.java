package br.com.kafka.repositories;

import br.com.kafka.entities.Usuario;
import br.com.kafka.enums.TipoUsuarioEnum;
import br.com.kafka.services.EventoService;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  boolean existsByNome(String nome);

  Optional<Usuario> findByNome(String nome);

  @Query(value = "SELECT usuario FROM Usuario usuario WHERE UPPER(usuario.nome) LIKE UPPER(CONCAT('%', :nome, '%'))")
  List<Usuario> findByNomeLike(String nome);

  List<Usuario> findByTipoUsuario(TipoUsuarioEnum tipoUsuario);
  
}

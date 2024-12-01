package br.com.event.core.repositories;

import br.com.event.core.entities.Usuario;
import br.com.event.core.enums.TipoUsuarioEnum;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

  boolean existsByNome(String nome);

  boolean existsByCpf(String cpf);

  boolean existsByEmail(String email);

  boolean existsByTelefone(String telefone);

  Optional<Usuario> findByNome(String nome);

  List<Usuario> findByTipoUsuario(TipoUsuarioEnum tipoUsuario);

  @Query(value =
    "SELECT usuario FROM Usuario usuario WHERE UPPER(usuario.nome) LIKE UPPER(CONCAT('%', :nome, '%'))")
  List<Usuario> findByNomeLike(String nome);

  @Query(value = "SELECT nextval('user_code_seq')", nativeQuery = true)
  Long generateNextUserCode();

}

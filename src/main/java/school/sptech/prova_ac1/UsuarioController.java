package school.sptech.prova_ac1;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    private List<Usuario> usuarios = new ArrayList<>();
    private long increment = 1;

    @GetMapping
    public ResponseEntity<List<Usuario>> buscarTodos() {
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario isExists = usuarios.get(i);

            if (isExists.getEmail().equalsIgnoreCase(usuario.getEmail())){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            if (isExists.getCpf().equalsIgnoreCase(usuario.getCpf())){
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
        }
        usuario.setId(increment);
        increment++;
        usuarios.add(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);

            if (usuario.getId().equals(id)) {
                return ResponseEntity.ok(usuario);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);

            if (usuario.getId().equals(id)) {
                usuarios.remove(i);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/filtro-data")
    public ResponseEntity<List<Usuario>> buscarPorDataNascimento(@RequestParam("nascimento") LocalDate nascimento) {
        List<Usuario> maiores = new ArrayList<>();

        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            if (usuario.getDataNascimento().isAfter(nascimento)) {
                maiores.add(usuario);
            }
        }
        return ResponseEntity.ok(maiores);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario
    ) {
        for (int i = 0; i < usuarios.size() ; i++) {
            Usuario current = usuarios.get(i);
            if (current.getId().equals(id)) {
                for (int a = 0; a < usuarios.size(); a++) {
                    Usuario pessoa = usuarios.get(a);
                    if (!pessoa.getId().equals(id)) {
                        if (pessoa.getEmail().equalsIgnoreCase(usuario.getEmail())) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).build();
                        }
                        if (pessoa.getCpf().equalsIgnoreCase(usuario.getCpf())) {
                            return ResponseEntity.status(HttpStatus.CONFLICT).build();
                        }
                    }
                }

                current.setNome(usuario.getNome());
                current.setEmail(usuario.getEmail());
                current.setCpf(usuario.getCpf());
                current.setSenha(usuario.getSenha());
                current.setDataNascimento(usuario.getDataNascimento());

                return ResponseEntity.ok(current);
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}

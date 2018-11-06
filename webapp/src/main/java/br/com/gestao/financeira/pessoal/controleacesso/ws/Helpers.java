package br.com.gestao.financeira.pessoal.controleacesso.ws;

import br.com.gestao.financeira.pessoal.controleacesso.Usuario;
import br.com.gestao.financeira.pessoal.controleacesso.api.UsuarioDTO;

public class Helpers {
    
    public static class UsuarioDTOConverter {
        public UsuarioDTO usuarioDTO(Usuario usuario) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setEmail(usuario.getEmail());
            usuarioDTO.setNome(usuario.getFirstName());
            usuarioDTO.setSobrenome(usuario.getLastName());
            usuarioDTO.setDataCadastro(usuario.getDataCadastro());
            usuarioDTO.setDataUltimoAcesso(usuario.getDataUltimoAcesso());
            return usuarioDTO;
        }
    }
}

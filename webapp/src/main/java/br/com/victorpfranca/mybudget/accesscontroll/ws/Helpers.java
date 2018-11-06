package br.com.victorpfranca.mybudget.accesscontroll.ws;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.controleacesso.api.UsuarioDTO;

public class Helpers {
    
    public static class UsuarioDTOConverter {
        public UsuarioDTO usuarioDTO(User user) {
            UsuarioDTO usuarioDTO = new UsuarioDTO();
            usuarioDTO.setEmail(user.getEmail());
            usuarioDTO.setNome(user.getFirstName());
            usuarioDTO.setSobrenome(user.getLastName());
            usuarioDTO.setDataCadastro(user.getDataCadastro());
            usuarioDTO.setDataUltimoAcesso(user.getDataUltimoAcesso());
            return usuarioDTO;
        }
    }
}

package br.com.victorpfranca.mybudget.accesscontroll.ws;

import br.com.victorpfranca.mybudget.accesscontroll.User;
import br.com.victorpfranca.mybudget.accesscontroll.api.UserDTO;

public class Helpers {

	public static class UserDTOConverter {
		public UserDTO usuarioDTO(User user) {
			UserDTO usuarioDTO = new UserDTO();
			usuarioDTO.setEmail(user.getEmail());
			usuarioDTO.setNome(user.getFirstName());
			usuarioDTO.setSobrenome(user.getLastName());
			usuarioDTO.setDataCadastro(user.getDataCadastro());
			usuarioDTO.setDataUltimoAcesso(user.getDataUltimoAcesso());
			return usuarioDTO;
		}
	}
}

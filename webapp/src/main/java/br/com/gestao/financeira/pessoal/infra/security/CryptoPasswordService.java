package br.com.gestao.financeira.pessoal.infra.security;

import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.authc.credential.PasswordService;

import br.com.gestao.financeira.pessoal.infra.log.LogProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

public class CryptoPasswordService implements PasswordService {

	public CryptoPasswordService() {
	}
	
	@Override
	public String encryptPassword(Object plaintextPassword) throws IllegalArgumentException {
		if (plaintextPassword instanceof String) {
			String password = (String) plaintextPassword;
			return hashpw(password, gensalt(10));
		}
        LogProvider.getLogger(CryptoPasswordService.class).error(plaintextPassword.getClass().getName());
		throw new IllegalArgumentException("encryptPassword only support java.lang.String credential.");
	}

	private String hashpw(String password, byte[] gensalt) {
		return Base64.encodeBase64String(DigestUtils.getSha256Digest().digest(password.getBytes(StandardCharsets.UTF_8)));
	}

	private byte[] gensalt(int saltIterations) {
		// TODO Auto-generated method stub
		return null;
	}

	private boolean checkpw(String password, String encrypted) {
		return encrypted != null && encrypted.equals(hashpw(password, null));
	}

	@Override
	public boolean passwordsMatch(Object credentials, String encrypted) {
		if (credentials instanceof char[]) {
			String password = String.valueOf((char[]) credentials);
			return checkpw(password, encrypted);
		} else if (credentials instanceof Jws) {
		    Jws<Claims> jws=(Jws) credentials;
		    return jws != null;
		}
		throw new IllegalArgumentException("passwordsMatch only support char[] or Jws credential.");
	}

}
package br.com.victorpfranca.mybudget.conta;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface ContaBancoResource {

	@GET
	@Path("/")
	List<ContaDTO> findAll();

	@GET
	@Path("/{uid}")
	public ContaDTO find(@PathParam("uid") String uidConta);
	
	@POST
	@Path("/")
	public void save(ContaDTO conta);

	@DELETE
	@Path("/{uid}")
	public void remove(@PathParam("uid") String uidConta);

}

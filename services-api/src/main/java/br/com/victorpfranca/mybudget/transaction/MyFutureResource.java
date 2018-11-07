package br.com.victorpfranca.mybudget.transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import br.com.victorpfranca.mybudget.account.BalanceDTO;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface MyFutureResource {
    
    @GET
    @Path("/")
    List<BalanceDTO> lancamentos(@QueryParam("de") Date start, @QueryParam("ate") Date end);
    
    @GET
    @Path("ano/{ano:[0-9]+}/mes/{mes:[0-9]+}")
    BigDecimal lancamento(@PathParam("ano") Integer ano,@Min(1) @Max(12) @PathParam("mes") Integer mes, @QueryParam("de") Date start, @QueryParam("ate") Date end);
    
    @GET
    @Path("menor")
    BalanceDTO menor(@QueryParam("de") Date start, @QueryParam("ate") Date end);
    
    @GET
    @Path("maior")
    BalanceDTO maior(@QueryParam("de") Date start, @QueryParam("ate") Date end);
    
    @GET
    @Path("ultimo")
    BalanceDTO ultimo(@QueryParam("de") Date start, @QueryParam("ate") Date end);
}
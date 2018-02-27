package serviceproviderwebserver;

import com.google.gson.Gson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("CurrentWeight")
public class EntryPoint {
    @GET
    @Path("JsonToCurrentWeight")
    @Produces
    public CurrentWeight createCurrentWeight(@QueryParam("json") String json) {
        System.out.println(json);
        Gson gson = new Gson();
        CurrentWeight currentWeight = gson.fromJson(json, CurrentWeight.class);
        System.out.println(currentWeight);
        return currentWeight;
    }
}

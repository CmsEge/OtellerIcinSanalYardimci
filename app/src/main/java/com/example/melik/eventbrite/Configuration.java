package com.example.melik.eventbrite;


import android.util.Log;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;

public class Configuration {
    Client client = ClientBuilder.newClient();
    Response response = client.target("https://www.eventbriteapi.com/v3/events/search/")
            .request(MediaType.APPLICATION_JSON_TYPE)
            .header("Authorization", "Bearer LCJHM625NWALIR3PZJVG")
            .get();

}

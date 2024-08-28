package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.Voznja;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentSimulacije.
 * 
 * @author Denis Matijević
 */
public class RestKlijentSimulacije {
  /**
   * Kontruktor klase.
   */
  public RestKlijentSimulacije() {}

  /**
   * Metoda getVoznjeJSON_od_do - vraća vožnje u intervalu od do.
   *
   * @param odVremena - početak intervala
   * @param doVremena - kraj intervala
   * @return vožnje
   */
  public List<Voznja> getVoznjeJSON_od_do(long odVremena, long doVremena) {
    RestSimulacije rs = new RestSimulacije();
    List<Voznja> voznje = rs.getJSON_od_do(odVremena, doVremena);

    return voznje;
  }

  /**
   * Metoda getVoznjeJSON_vozilo - Vraća vožnje za vozilo.
   *
   * @param id - id vozila
   * @return vožnje
   */
  public List<Voznja> getVoznjeJSON_vozilo(String id) {
    RestSimulacije rs = new RestSimulacije();
    List<Voznja> voznje = rs.getJSONsimulacije_vozilo(id);
    return voznje;
  }

  /**
   * Metoda getVoznjeJSON_vozilo_od_do - vraća vožnje za vozilo u intervalu od do..
   *
   * @param id - id vozila
   * @param odVremena - početak intervala
   * @param doVremena - kraj intervala
   * @return vožnje
   */
  public List<Voznja> getVoznjeJSON_vozilo_od_do(String id, long odVremena, long doVremena) {
    RestSimulacije rs = new RestSimulacije();
    List<Voznja> voznje = rs.getJSONsimulacije_vozilo_od_do(id, odVremena, doVremena);
    return voznje;
  }

  static class RestSimulacije {

    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** konstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestSimulacije() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/simulacije");
    }


    /**
     * Metoda getJSON_od_do - vraća vožnje u intervalu od do.
     *
     * @param odVremena - početak intervala
     * @param doVremena - kraj intervala
     * @return vožnje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Voznja> getJSON_od_do(long odVremena, long doVremena) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznja = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznja));
      }
      return voznje;
    }

    /**
     * Metoda getJSONsimulacije_vozilo - vraća vožnje za vozilo.
     *
     * @param id - id vozila
     * @return vožnje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Voznja> getJSONsimulacije_vozilo(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznja = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznja));
      }

      return voznje;
    }

    /**
     * Metoda getJSONsimulacije_vozilo_od_do - vraća vožnje za vozilo u intervalu od do..
     *
     * @param id - id vozila
     * @param odVremena - početak intervala
     * @param doVremena - kraj intervala
     * @return vožnje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<Voznja> getJSONsimulacije_vozilo_od_do(String id, long odVremena, long doVremena)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      List<Voznja> voznje = new ArrayList<Voznja>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var pvoznja = jb.fromJson(odgovor, Voznja[].class);
        voznje.addAll(Arrays.asList(pvoznja));
      }

      return voznje;
    }
  }
}

package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.model;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PracenaVoznja;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.bind.JsonbBuilder;
import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentVozila.
 * 
 * @author Denis Matijević
 */
public class RestKlijentVozila {

  /**
   * Kontruktor klase.
   */
  public RestKlijentVozila() {}

  /**
   * Metoda getPraceneVoznjeJSON_od_do - vraća praćene vožnje u intervalu od do.
   *
   * @param odVremena - početak intervala
   * @param doVremena - kraj intervala
   * @return praćene vožnje
   */
  public List<PracenaVoznja> getPraceneVoznjeJSON_od_do(long odVremena, long doVremena) {
    RestVozila rv = new RestVozila();
    List<PracenaVoznja> praceneVoznje = rv.getJSON_od_do(odVremena, doVremena);

    return praceneVoznje;
  }

  /**
   * Metoda getPraceneVoznjeJSON_vozilo - Vraća praćene vožnje za vozilo.
   *
   * @param id - id vozila
   * @return praćene vožnje
   */
  public List<PracenaVoznja> getPraceneVoznjeJSON_vozilo(String id) {
    RestVozila rv = new RestVozila();
    List<PracenaVoznja> praceneVoznje = rv.getJSON_vozilo(id);
    return praceneVoznje;
  }

  /**
   * Metoda getPraceneVoznjeJSON_vozilo_od_do - vraća praćene vožnje za vozilo u intervalu od do..
   *
   * @param id - id vozila
   * @param odVremena - početak intervala
   * @param doVremena - kraj intervala
   * @return praćene vožnje
   */
  public List<PracenaVoznja> getPraceneVoznjeJSON_vozilo_od_do(String id, long odVremena,
      long doVremena) {
    RestVozila rv = new RestVozila();
    List<PracenaVoznja> praceneVoznje = rv.getJSON_vozilo_od_do(id, odVremena, doVremena);
    return praceneVoznje;
  }

  /**
   * Metoda getPraceneVoznjeJSON_vozilo_start - pokreće praćenje vožnje za vozilo.
   *
   * @param id - id vozila
   * @return odgovor
   */
  public String getPraceneVoznjeJSON_vozilo_start(String id) {
    RestVozila rv = new RestVozila();
    String odgovor = rv.getJSON_vozilo_start(id);
    return odgovor;
  }

  /**
   * Metoda getPraceneVoznjeJSON_vozilo_stop - prekida praćenje vožnje za vozilo.
   *
   * @param id - id vozila
   * @return odgovor
   */
  public String getPraceneVoznjeJSON_vozilo_stop(String id) {
    RestVozila rv = new RestVozila();
    String odgovor = rv.getJSON_vozilo_stop(id);
    return odgovor;
  }

  /**
   * Klasa RestVozila.
   */
  static class RestVozila {

    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** konstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestVozila() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/vozila");
    }


    /**
     * Metoda getJSON_od_do - vraća praćene vožnje u intervalu od do.
     *
     * @param odVremena - početak intervala
     * @param doVremena - kraj intervala
     * @return praćene vožnje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<PracenaVoznja> getJSON_od_do(long odVremena, long doVremena)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      List<PracenaVoznja> praceneVoznje = new ArrayList<PracenaVoznja>();

      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var ppracenaVoznja = jb.fromJson(odgovor, PracenaVoznja[].class);
        praceneVoznje.addAll(Arrays.asList(ppracenaVoznja));
      }

      return praceneVoznje;
    }

    /**
     * Metoda getJSON_vozilo - vraća praćene vožnje za vozilo.
     *
     * @param id - id vozila
     * @return praćene vožnje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<PracenaVoznja> getJSON_vozilo(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      List<PracenaVoznja> praceneVoznje = new ArrayList<PracenaVoznja>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var ppracenaVoznja = jb.fromJson(odgovor, PracenaVoznja[].class);
        praceneVoznje.addAll(Arrays.asList(ppracenaVoznja));
      }

      return praceneVoznje;
    }

    /**
     * Metoda getJSON_vozilo_od_do - vraća praćene vožnje za vozilo u intervalu od do..
     *
     * @param id - id vozila
     * @param odVremena - početak intervala
     * @param doVremena - kraj intervala
     * @return praćene vožnje
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public List<PracenaVoznja> getJSON_vozilo_od_do(String id, long odVremena, long doVremena)
        throws ClientErrorException {
      WebTarget resource = webTarget;
      List<PracenaVoznja> praceneVoznje = new ArrayList<PracenaVoznja>();

      resource = resource.path(java.text.MessageFormat.format("vozilo/{0}", new Object[] {id}));
      resource = resource.queryParam("od", odVremena);
      resource = resource.queryParam("do", doVremena);
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        var jb = JsonbBuilder.create();
        var ppracenaVoznja = jb.fromJson(odgovor, PracenaVoznja[].class);
        praceneVoznje.addAll(Arrays.asList(ppracenaVoznja));
      }

      return praceneVoznje;
    }

    /**
     * Metoda getJSON_vozilo_start - pokreće praćenje vožnji za vozilo.
     *
     * @param id - id vozila
     * @return odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public String getJSON_vozilo_start(String id) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource =
          resource.path(java.text.MessageFormat.format("vozilo/{0}/start", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        JsonReader jsonCitac = Json.createReader(new StringReader(odgovor));
        JsonObject jsonObjekt = jsonCitac.readObject();

        if ("OK".equals(jsonObjekt.getString("odgovor"))) {
          return "OK - Pokrenuto praćenje vožnje za vozilo s ID-om " + id;
        }
      }

      return "Neuspješan zahtjev GET/vozilo/{id}/start.";
    }

    /**
     * Metoda getJSON_vozilo_stop - prekida praćenje vožnji za vozilo.
     *
     * @param id - id vozila
     * @return odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public String getJSON_vozilo_stop(String id) throws ClientErrorException {
      WebTarget resource = webTarget;

      resource =
          resource.path(java.text.MessageFormat.format("vozilo/{0}/stop", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      Response restOdgovor = resource.request().get();
      if (restOdgovor.getStatus() == 200) {
        String odgovor = restOdgovor.readEntity(String.class);
        JsonReader jsonCitac = Json.createReader(new StringReader(odgovor));
        JsonObject jsonObjekt = jsonCitac.readObject();

        if ("OK".equals(jsonObjekt.getString("odgovor"))) {
          return "OK - Prekinuto praćenje vožnje za vozilo s ID-om " + id;
        }
      }

      return "Neuspješan zahtjev GET/vozilo/{id}/stop.";
    }
  }
}

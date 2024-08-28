package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.model;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Klasa RestKlijentRadari.
 * 
 * @author Denis Matijević
 */
public class RestKlijentRadari {

  /**
   * Kopntruktor klase.
   */
  public RestKlijentRadari() {}

  /**
   * Metoda getRadariJSON - vraća sve radare.
   *
   * @return odgovor
   */
  public Response getRadariJSON() {
    RestRadari rv = new RestRadari();
    return rv.getJSONRadari();
  }

  /**
   * Metoda resetRadarJSON - resetira sve radare.
   *
   * @return odgovor
   */
  public Response resetRadarJSON() {
    RestRadari rv = new RestRadari();
    return rv.resetJSONRadar();
  }

  /**
   * Metoda getRadarJSON - vraća radar.
   *
   * @return odgovor
   */
  public Response getRadarJSON(String id) {
    RestRadari rv = new RestRadari();
    return rv.getJSONRadar(id);
  }

  /**
   * Metoda provjeriRadarJSON - provjerava radar.
   *
   * @return odgovor
   */
  public Response provjeriRadarJSON(String id) {
    RestRadari rv = new RestRadari();
    return rv.provjeriJSONRadar(id);
  }

  /**
   * Metoda deleteRadariJSON - briše sve radare.
   *
   * @return odgovor
   */
  public Response deleteRadariJSON() {
    RestRadari rv = new RestRadari();
    return rv.deleteJSONRadari();
  }

  /**
   * Metoda deleteRadarJSON - briše radar.
   *
   * @return odgovor
   */
  public Response deleteRadarJSON(String id) {
    RestRadari rv = new RestRadari();
    return rv.deleteJSONRadar(id);
  }

  /**
   * Klasa RestRadari.
   */
  static class RestRadari {

    /** web target. */
    private final WebTarget webTarget;

    /** client. */
    private final Client client;

    /** konstanta BASE_URI. */
    private static final String BASE_URI = "http://localhost:9080/";

    /**
     * Konstruktor klase.
     */
    public RestRadari() {
      client = ClientBuilder.newClient();
      webTarget = client.target(BASE_URI).path("nwtis/v1/api/radari");
    }

    /**
     * Metoda getJSONRadari - vraća sve radare.
     *
     * @return odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Response getJSONRadari() throws ClientErrorException {
      WebTarget resource = webTarget;
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      return request.get();
    }

    /**
     * Metoda resetJSONRadar - resetira sve radare.
     *
     * @return odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Response resetJSONRadar() throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path("reset");
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      return request.get();
    }

    /**
     * Metoda getJSONRadar - vraća radar.
     *
     * @return odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Response getJSONRadar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      return request.get();
    }

    /**
     * Metoda provjeriJSONRadar - provjerava radar.
     *
     * @return odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Response provjeriJSONRadar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}/provjeri", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      return request.get();
    }

    /**
     * Metoda deleteJSONRadari - briše sve radare.
     *
     * @return odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Response deleteJSONRadari() throws ClientErrorException {
      WebTarget resource = webTarget;
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      return request.delete();
    }

    /**
     * Metoda deleteJSONRadar - briše radar.
     *
     * @return odgovor
     * @throws ClientErrorException iznimka kod poziva klijenta
     */
    public Response deleteJSONRadar(String id) throws ClientErrorException {
      WebTarget resource = webTarget;
      resource = resource.path(java.text.MessageFormat.format("{0}", new Object[] {id}));
      Invocation.Builder request = resource.request(MediaType.APPLICATION_JSON);
      return request.delete();
    }
  }
}

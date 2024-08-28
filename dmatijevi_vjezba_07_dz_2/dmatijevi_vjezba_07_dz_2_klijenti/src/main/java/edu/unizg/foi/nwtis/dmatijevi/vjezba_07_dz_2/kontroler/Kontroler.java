package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.kontroler;

import jakarta.enterprise.context.RequestScoped;
import jakarta.mvc.Controller;
import jakarta.mvc.View;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

// TODO: Auto-generated Javadoc
/**
 * Klasa Kontroler.
 * 
 * @author Denis Matijević
 */
@Controller
@Path("sadrzaj")
@RequestScoped
public class Kontroler {

  /**
   * Metoda pocetak - prikaz stranice sadržaja.
   */
  @GET
  @View("sadrzaj.jsp")
  public void pocetak() {}
}

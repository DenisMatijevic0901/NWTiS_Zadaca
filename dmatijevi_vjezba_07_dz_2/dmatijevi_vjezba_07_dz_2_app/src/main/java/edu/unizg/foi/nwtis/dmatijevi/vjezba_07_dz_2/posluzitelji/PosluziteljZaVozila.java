package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaVozila;

/**
 * Klasa PosluziteljZaVozila.
 *
 * @author Denis Matijević
 */
public class PosluziteljZaVozila implements Runnable {

  /** Mrežna vrata PosluziteljZaVozila. */
  private int mreznaVrata;

  /** Tvornica dretvi koja služi za kreiranje novih dretvi (virtualnih). */
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /** Centralni sustav. */
  private CentralniSustav centralniSustav;

  /**
   * Inicijalizacija objekta PosluziteljZaVozila.
   *
   * @param mreznaVrata - mrežna vrata PosluziteljZaVozila
   * @param centralniSustav - centralni sustav
   */
  public PosluziteljZaVozila(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }

  /**
   * Metoda run - spajanje na poslužitelja i čekanje zahtjeva te kreiranje radnika.
   */
  @Override
  public void run() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        RadnikZaVozila radnik = new RadnikZaVozila(mreznaUticnica, centralniSustav, this);
        var dretva = tvornicaDretvi.newThread(radnik);
        dretva.start();
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }
}

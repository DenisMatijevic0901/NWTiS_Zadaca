package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.RedPodaciVozila;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa CentralniSustav.
 *
 * @author Denis Matijević
 */
public class CentralniSustav {

  /**
   * Mrežna vrata PosluziteljZaRegistracijuRadara koja su potrebna za pokretanje posluzitelja
   * PosluziteljZaRegistracijuRadara.
   */
  public int mreznaVrataRadara;

  /**
   * Mrežna vrata PosluziteljZaVozila koja su potrebna za pokretanje posluzitelja
   * PosluziteljZaVozila.
   */
  public int mreznaVrataVozila;

  /** Mrežna vrata nadzora (trenutno se nigdje ne koristi). */
  private int mreznaVrataNadzora;

  /** Maksimalni broj vozila (trenutno se nigdje ne koristi). */
  private int maksVozila;

  /** Tvornica dretvi koja služi za kreiranje novih dretvi (virtualnih). */
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /** Mapa svih radara koji se nalaze u sustavu. */
  public ConcurrentHashMap<Integer, PodaciRadara> sviRadari =
      new ConcurrentHashMap<Integer, PodaciRadara>();

  /** Mapa svih vozila koji se nalaze u sustavu. */
  public ConcurrentHashMap<Integer, RedPodaciVozila> svaVozila =
      new ConcurrentHashMap<Integer, RedPodaciVozila>();

  /**
   * Mapa praćenih vozila čiji se podaci o vožnji šalju na RESTful web servis za praćenje odabranih
   * vozila.
   */
  public ConcurrentHashMap<Integer, Integer> pracenaVozila =
      new ConcurrentHashMap<Integer, Integer>();

  /**
   * Inicijalizacija objekta CentralniSustav.
   */
  public CentralniSustav() {}

  /**
   * Metoda main - provjerava unesene argumente, pokreće poslužitelje.
   *
   * @param args - uneseni argumenti (jedan (konfiguracijska datoteka)).
   */
  public static void main(String[] args) {
    if (args.length != 1) {
      System.out.println("Broj argumenata nije 1.");
      return;
    }

    CentralniSustav centralniSustav = new CentralniSustav();
    try {
      centralniSustav.preuzmiPostavke(args);
      centralniSustav.pokreniPosluzitelja(centralniSustav);
    } catch (Exception ex) {
      System.out.println(ex.getMessage());
      return;
    }
  }

  /**
   * Metoda pokreniPosluzitelja - pokreće poslužitelje PosluziteljZaRegistracijuRadara i
   * PosluziteljZaVozila kao dvije zasebne dretve.
   *
   * @param centralniSustav - objekt centralnog sustava
   */
  public void pokreniPosluzitelja(CentralniSustav centralniSustav) {
    PosluziteljZaRegistracijuRadara posluziteljZaRegistracijuRadara =
        new PosluziteljZaRegistracijuRadara(mreznaVrataRadara, centralniSustav);
    PosluziteljZaVozila posluziteljZaVozila =
        new PosluziteljZaVozila(mreznaVrataVozila, centralniSustav);

    var dretvaRadara = tvornicaDretvi.newThread(posluziteljZaRegistracijuRadara);
    var dretvaVozila = tvornicaDretvi.newThread(posluziteljZaVozila);
    dretvaRadara.start();
    dretvaVozila.start();
    try {
      dretvaRadara.join();
      dretvaVozila.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * Metoda preuzmiPostavke - preuzima postavke iz konfiguracijske datoteke.
   *
   * @param args - uneseni argumenti
   * @throws NeispravnaKonfiguracija - neispravna konfiguracija iznimka (iznimka kod problema s
   *         učitavanjem konfiguracijske datoteke)
   */
  public void preuzmiPostavke(String[] args) throws NeispravnaKonfiguracija {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.mreznaVrataRadara = Integer.valueOf(konfig.dajPostavku("mreznaVrataRadara"));
    this.mreznaVrataVozila = Integer.valueOf(konfig.dajPostavku("mreznaVrataVozila"));
    this.mreznaVrataNadzora = Integer.valueOf(konfig.dajPostavku("mreznaVrataNadzora"));
    this.maksVozila = Integer.valueOf(konfig.dajPostavku("maksVozila"));
  }

}

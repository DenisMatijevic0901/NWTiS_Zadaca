package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji;

import java.io.IOException;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.radnici.RadnikZaRadare;
import edu.unizg.foi.nwtis.konfiguracije.Konfiguracija;
import edu.unizg.foi.nwtis.konfiguracije.KonfiguracijaApstraktna;
import edu.unizg.foi.nwtis.konfiguracije.NeispravnaKonfiguracija;

/**
 * Klasa PosluziteljRadara.
 *
 * @author Denis Matijević
 */
public class PosluziteljRadara {

  /** Predložak za provjeru unesenih argumenata (1 argument). */
  private static Pattern predlozakNazivDatoteke =
      Pattern.compile("(?<nazivDatoteke>^[a-zA-Z0-9ćčšđžĆČŠĐŽ\\/_-]+\\.(txt|xml|bin|json))$");

  /** Poklapanje za provjeru unesenih argumenata (1 argument). */
  private static Matcher poklapanjeNazivDatoteke;

  /** Predložak za provjeru unesenih argumenata (3 argumenta, ali za brisanje jednog radara). */
  private static Pattern predlozakObrisiId =
      Pattern.compile("(?<nazivDatoteke>^[a-zA-Z0-9ćčšđžĆČŠĐŽ\\/_-]+\\.(txt|xml|bin|json)) "
          + "(?<obrisi>OBRIŠI) " + "(?<obrisiId>\\d+)$");

  /** Poklapanje za provjeru unesenih argumenata (3 argumenta, ali za brisanje jednog radara) */
  private static Matcher poklapanjeObrisiId;

  /** Predložak za provjeru unesenih argumenata (3 argumenta, ali za brisanje svih radara). */
  private static Pattern predlozakObrisiSve =
      Pattern.compile("(?<nazivDatoteke>^[a-zA-Z0-9ćčšđžĆČŠĐŽ\\/_-]+\\.(txt|xml|bin|json)) "
          + "(?<obrisi>OBRIŠI) " + "(?<sve>SVE)$");

  /** Poklapanje za provjeru unesenih argumenata (3 argumenta, ali za brisanje svih radara). */
  private static Matcher poklapanjeObrisiSve;

  /** Tvornica dretvi koja služi za kreiranje novih dretvi (virtualnih). */
  private ThreadFactory tvornicaDretvi = Thread.ofVirtual().factory();

  /** Podaci radara. */
  private PodaciRadara podaciRadara;

  /** Mapa brzih vozila. */
  private ConcurrentHashMap<Integer, BrzoVozilo> brzaVozila = new ConcurrentHashMap<>();

  /**
   * Metoda main - provjerava unesene argumente, pokreće poslužitelja.
   *
   * @param args - uneseni argumenti (jedan (konfiguracijska datoteka) ili 3(konfiguracijska
   *        datoteka, OBRISI id radara, ili SVE))).
   */
  public static void main(String[] args) {
    if ((args.length != 1) && (args.length != 3)) {
      System.out.println("Broj argumenata nije 1 ili 3.");
      return;
    }

    PosluziteljRadara posluziteljRadara = new PosluziteljRadara();
    try {
      String argumentiSpojeno = String.join(" ", args);
      posluziteljRadara.preuzmiPostavke(args);

      if (args.length == 1) {
        poklapanjeNazivDatoteke = predlozakNazivDatoteke.matcher(argumentiSpojeno);
        var statusNazivDatoteke = poklapanjeNazivDatoteke.matches();
        if (statusNazivDatoteke) {
          posluziteljRadara.registrirajPosluzitelja();
          posluziteljRadara.pokreniPosluzitelja();
        }
      } else if (args.length == 3) {
        poklapanjeObrisiId = predlozakObrisiId.matcher(argumentiSpojeno);
        poklapanjeObrisiSve = predlozakObrisiSve.matcher(argumentiSpojeno);
        var statusObrisiId = poklapanjeObrisiId.matches();
        var statusObrisiSve = poklapanjeObrisiSve.matches();
        if (statusObrisiId) {
          posluziteljRadara.obrisiRadarId(poklapanjeObrisiId);
        } else if (statusObrisiSve) {
          posluziteljRadara.obrisiRadarSve(poklapanjeObrisiSve);
        } else {
          System.out.println("Komanda ne zadovoljava format.");
        }
      }
    } catch (NeispravnaKonfiguracija | NumberFormatException | UnknownHostException e) {
      System.out.println(e.getMessage());
      return;
    }
  }

  /**
   * Metoda pokreniPosluzitelja - pokreće poslužitelj PosluziteljRadra te radi novog radnika tj.
   * dretvu za pojedinog radara.
   */
  public void pokreniPosluzitelja() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja =
        new ServerSocket(this.podaciRadara.mreznaVrataRadara())) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
        RadnikZaRadare radnik = new RadnikZaRadare(mreznaUticnica, podaciRadara, this, brzaVozila);
        var dretva = tvornicaDretvi.newThread(radnik);
        dretva.start();
      }
    } catch (NumberFormatException | IOException e) {
      if (e instanceof BindException) {
        System.err.println("ERROR 19 Greška pri pokretanju servera na portu "
            + this.podaciRadara.mreznaVrataRadara() + " (zauzet port).");
      } else {
        e.printStackTrace();
      }
    }
  }

  /**
   * Metoda preuzmiPostavke - preuzima postavke iz konfiguracijske datoteke.
   *
   * @param args - uneseni argumenti
   * @throws NeispravnaKonfiguracija - neispravna konfiguracija iznimka (iznimka kod problema s
   *         učitavanjem konfiguracijske datoteke)
   * @throws NumberFormatException - number format iznimka (iznimke kod parsiranja)
   * @throws UnknownHostException - unknown host iznimka
   */
  public void preuzmiPostavke(String[] args)
      throws NeispravnaKonfiguracija, NumberFormatException, UnknownHostException {
    Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);

    this.podaciRadara = new PodaciRadara(Integer.parseInt(konfig.dajPostavku("id")),
        InetAddress.getLocalHost().getHostName(),
        Integer.parseInt(konfig.dajPostavku("mreznaVrataRadara")),
        Integer.parseInt(konfig.dajPostavku("maksBrzina")),
        Integer.parseInt(konfig.dajPostavku("maksTrajanje")),
        Integer.parseInt(konfig.dajPostavku("maksUdaljenost")),
        konfig.dajPostavku("adresaRegistracije"),
        Integer.parseInt(konfig.dajPostavku("mreznaVrataRegistracije")),
        konfig.dajPostavku("adresaKazne"), Integer.parseInt(konfig.dajPostavku("mreznaVrataKazne")),
        konfig.dajPostavku("postanskaAdresaRadara"),
        Double.parseDouble(konfig.dajPostavku("gpsSirina")),
        Double.parseDouble(konfig.dajPostavku("gpsDuzina")));
  }

  /**
   * Metoda registrirajPosluzitelja - registriranje novog radara.
   *
   * @return true, ako je uspješna registracija, inače vraća false
   */
  private boolean registrirajPosluzitelja() {
    String komandaZaRegistracijuRadara =
        "RADAR " + this.podaciRadara.id() + " " + this.podaciRadara.adresaRadara() + " "
            + this.podaciRadara.mreznaVrataRadara() + " " + this.podaciRadara.gpsSirina() + " "
            + this.podaciRadara.gpsDuzina() + " " + this.podaciRadara.maksUdaljenost() + "\n";
    String odgovor =
        MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
            this.podaciRadara.mreznaVrataRegistracije(), komandaZaRegistracijuRadara);

    if (odgovor != null) {
      System.out.println(odgovor);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metoda obrisiRadarId - briše radar sa zadanim IDom.
   *
   * @param poklapanje - poklapanje za provjeru unesenih argumenata (3 argumenta, ali za brisanje
   *        jednog radara)
   * @return true, ako je uspješno brisanje, inače vraća false
   */
  private boolean obrisiRadarId(Matcher poklapanje) {
    String komandaZaBrisanjeRadara = "RADAR " + "OBRIŠI " + poklapanje.group("obrisiId") + "\n";
    String odgovor =
        MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
            this.podaciRadara.mreznaVrataRegistracije(), komandaZaBrisanjeRadara);

    if (odgovor != null) {
      System.out.println(odgovor);
      return true;
    } else {
      return false;
    }
  }

  /**
   * Metoda obrisiRadarSve - briše sve registrirane radare.
   *
   * @param poklapanje - poklapanje za provjeru unesenih argumenata (3 argumenta, ali za brisanje
   *        svih radara)
   * @return true, ako je uspješno brisanje, inače vraća false
   */
  private boolean obrisiRadarSve(Matcher poklapanje) {
    String komandaZaBrisanjeSvihRadara = "RADAR " + "OBRIŠI " + "SVE\n";
    String odgovor =
        MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
            this.podaciRadara.mreznaVrataRegistracije(), komandaZaBrisanjeSvihRadara);

    if (odgovor != null) {
      System.out.println(odgovor);
      return true;
    } else {
      return false;
    }
  }
}

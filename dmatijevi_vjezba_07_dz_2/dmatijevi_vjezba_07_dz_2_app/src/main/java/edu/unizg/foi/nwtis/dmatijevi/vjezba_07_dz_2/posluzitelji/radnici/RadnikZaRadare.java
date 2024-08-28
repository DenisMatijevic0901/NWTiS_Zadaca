package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.radnici;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.BrzoVozilo;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji.PosluziteljRadara;

// TODO: Auto-generated Javadoc
/**
 * Klasa RadnikZaRadare.
 *
 * @author Denis Matijević
 */
public class RadnikZaRadare implements Runnable {

  /** Mrežna utičnica poslužitelja radara. */
  private Socket mreznaUticnica;

  /** Podaci radara. */
  private PodaciRadara podaciRadara;

  /** Poslužitelj radara. */
  private PosluziteljRadara posluziteljRadara;

  /** Mapa brzih vozila. */
  private ConcurrentHashMap<Integer, BrzoVozilo> brzaVozila;

  /** Predložak za provjeru ispravnosti komande za praćenje brzine . */
  private Pattern predlozakBrzine = Pattern.compile(
      "^VOZILO (?<id>\\d+) (?<vrijeme>\\d+) (?<brzina>-?\\d+([.]\\d+)?) (?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+)$");

  /** Poklapanje za provjeru ispravnosti komande za praćenje brzine. */
  private Matcher poklapanjeBrzine;

  /** Predložak za provjeru komande radar reset. */
  private Pattern predlozakRadarId = Pattern.compile("^RADAR " + "(?<id>\\d+)$");

  /** Poklapanje za provjeru komande radar reset. */
  private Matcher poklapanjeRadarId;

  /** Predložak za provjeru komande radar reset na tom radaru. */
  private Pattern predlozakRadarReset = Pattern.compile("^RADAR RESET$");

  /** Poklapanje za provjeru komande radar reset na tom radaru. */
  private Matcher poklapanjeRadarReset;

  /**
   * Inicijalizacija objekta RadnikZaRadare.
   *
   * @param mreznaUticnica - mrežna utičnica radara
   * @param podaciRadara - podaci radara
   * @param posluziteljRadara - poslužitelj radara
   * @param brzaVozila - mapa brzih vozila
   */
  public RadnikZaRadare(Socket mreznaUticnica, PodaciRadara podaciRadara,
      PosluziteljRadara posluziteljRadara, ConcurrentHashMap<Integer, BrzoVozilo> brzaVozila) {
    super();
    this.mreznaUticnica = mreznaUticnica;
    this.podaciRadara = podaciRadara;
    this.posluziteljRadara = posluziteljRadara;
    this.brzaVozila = brzaVozila;
  }

  /**
   * Metoda run - obrađivanje dolaznih zahtjeva.
   */
  @Override
  public void run() {
    try {
      BufferedReader citac =
          new BufferedReader(new InputStreamReader(mreznaUticnica.getInputStream(), "utf8"));
      OutputStream out = mreznaUticnica.getOutputStream();
      PrintWriter pisac = new PrintWriter(new OutputStreamWriter(out, "utf8"), true);
      var redak = citac.readLine();
      mreznaUticnica.shutdownInput();
      pisac.println(obradaZahtjeva(redak));
      pisac.flush();
      mreznaUticnica.shutdownOutput();
      mreznaUticnica.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * Metoda obradaZahtjeva - za dolazni zahtjev provjerava ispravnost komande te izvršava komandu.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor
   */
  String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 39 Zahtjev je null.\n";
    }

    poklapanjeBrzine = predlozakBrzine.matcher(zahtjev);
    if (poklapanjeBrzine.matches()) {
      return obradaZahtjevaBrzine(zahtjev);
    }

    poklapanjeRadarId = predlozakRadarId.matcher(zahtjev);
    if (poklapanjeRadarId.matches()) {
      return obradaZahtjevaRadarId(zahtjev);
    }

    poklapanjeRadarReset = predlozakRadarReset.matcher(zahtjev);
    if (poklapanjeRadarReset.matches()) {
      return obradaZahtjevaRadarReset(zahtjev);
    }

    return "ERROR 30 Neispravna sintaksa komande.\n";
  }

  /**
   * Metoda obradaZahtjevaBrzine - slanje zahtjeva PosluziteljKazni ukoliko je došlo do situacije da
   * treba generirati kaznu.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor
   */
  public String obradaZahtjevaBrzine(String zahtjev) {
    int id = Integer.parseInt(poklapanjeBrzine.group("id"));
    long vrijeme = Long.parseLong(poklapanjeBrzine.group("vrijeme"));
    double brzina = Double.parseDouble(poklapanjeBrzine.group("brzina"));
    double gpsSirina = Double.parseDouble(poklapanjeBrzine.group("gpsSirina"));
    double gpsDuzina = Double.parseDouble(poklapanjeBrzine.group("gpsDuzina"));
    BrzoVozilo brzoVozilo = brzaVozila.get(id);

    if (brzina > podaciRadara.maksBrzina()) {
      if (brzoVozilo == null) {
        brzoVozilo = new BrzoVozilo(id, -1, vrijeme, brzina, -1, -1, true);
        brzaVozila.put(id, brzoVozilo);
      } else {
        long trajanje = vrijeme - brzoVozilo.vrijeme();
        if (trajanje > (2 * podaciRadara.maksTrajanje() * 1000)) {
          BrzoVozilo azuriranoBrzoVozilo = brzoVozilo.postaviStatus(false);
          brzaVozila.put(id, azuriranoBrzoVozilo);
        } else if (trajanje > (podaciRadara.maksTrajanje() * 1000) && brzoVozilo.status()) {
          String komandaZaGeneriranjeKazne = "VOZILO " + id + " " + brzoVozilo.vrijeme() + " "
              + vrijeme + " " + brzina + " " + gpsSirina + " " + gpsDuzina + " "
              + podaciRadara.gpsSirina() + " " + podaciRadara.gpsDuzina() + "\n";

          String odgovor =
              MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaKazne(),
                  this.podaciRadara.mreznaVrataKazne(), komandaZaGeneriranjeKazne);

          BrzoVozilo azuriranoBrzoVozilo = brzoVozilo.postaviStatus(false);
          brzaVozila.put(id, azuriranoBrzoVozilo);
          brzaVozila.remove(id);
          if (odgovor == null) {
            return "ERROR 31 PoslužiteljKazni nije aktivan.\n";
          } else {
            System.out.println(odgovor);
            return "OK\n";
          }
        }
      }
    } else {
      if (brzoVozilo != null && brzoVozilo.status()) {
        brzaVozila.remove(id);
        return "OK\n";
      }
    }
    return "OK\n";
  }

  /**
   * Metoda postaviPoklapanjeBrzine - koja služi za postavljanje poklapanja zbog testova.
   *
   * @param poklapanje - poklapanje za generiranje kazne
   */
  public void postaviPoklapanjeBrzine(Matcher poklapanje) {
    this.poklapanjeBrzine = poklapanje;
  }

  /**
   * Metoda obradaZahtjevaRadarId - provjera identifikatora radara i provjera aktivnosti
   * posluzitelja kazne.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor
   */
  private String obradaZahtjevaRadarId(String zahtjev) {
    int id = Integer.valueOf(this.poklapanjeRadarId.group("id"));

    if (id != this.podaciRadara.id()) {
      return "ERROR 33 Radar s ID-om " + id + " ne odgovara identifikatoru radara "
          + this.podaciRadara.id() + "\n";
    } else {
      String komandaTest = "TEST\n";
      var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaKazne(),
          this.podaciRadara.mreznaVrataKazne(), komandaTest);

      if (odgovor == null) {
        return "ERROR 34 PosluziteljKazni nije aktivan.\n";
      } else if (odgovor.equals("OK")) {
        return "OK\n";
      } else {
        return "ERROR 40 Neispravna sintaksa komande.\n";
      }
    }
  }

  /**
   * Metoda obradaZahtjevaRadarReset - resetira radar.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor
   */
  private String obradaZahtjevaRadarReset(String zahtjev) {
    int id = this.podaciRadara.id();

    String komandaRadarId = "RADAR " + id + "\n";
    var odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
        this.podaciRadara.mreznaVrataRegistracije(), komandaRadarId);

    if (odgovor == null) {
      return "ERROR 32 PosluziteljZaRegistracijuRadara nije aktivan.\n";
    } else if (odgovor.equals("OK")) {
      return "OK\n";
    } else if (odgovor.equals("ERROR 12 Radar s ID-om " + id + " ne postoji.")) {

      String komandaZaRegistracijuRadara =
          "RADAR " + this.podaciRadara.id() + " " + this.podaciRadara.adresaRadara() + " "
              + this.podaciRadara.mreznaVrataRadara() + " " + this.podaciRadara.gpsSirina() + " "
              + this.podaciRadara.gpsDuzina() + " " + this.podaciRadara.maksUdaljenost() + "\n";

      var odgovorRegistracije =
          MrezneOperacije.posaljiZahtjevPosluzitelju(this.podaciRadara.adresaRegistracije(),
              this.podaciRadara.mreznaVrataRegistracije(), komandaZaRegistracijuRadara);

      if (odgovorRegistracije.equals("OK")) {
        return "OK\n";
      } else {
        return "ERROR 10 Neispravna sintaksa komande.\n";
      }

    } else {
      return "ERROR 10 Neispravna sintaksa komande.\n";
    }
  }
}


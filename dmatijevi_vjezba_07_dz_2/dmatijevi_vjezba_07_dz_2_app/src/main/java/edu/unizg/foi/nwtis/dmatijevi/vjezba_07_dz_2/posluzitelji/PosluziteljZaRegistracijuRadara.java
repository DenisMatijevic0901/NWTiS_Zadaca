package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.posluzitelji;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci.PodaciRadara;
import edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.pomocnici.MrezneOperacije;

// TODO: Auto-generated Javadoc
/**
 * Klasa PosluziteljZaRegistracijuRadara.
 *
 * @author Denis Matijević
 */
public class PosluziteljZaRegistracijuRadara implements Runnable {

  /** Mrežna vrata PosluziteljZaRegistracijuRadara. */
  private int mreznaVrata;

  /** Centralni sustav. */
  private CentralniSustav centralniSustav;

  /** Predložak za provjeru komande registracije radara. */
  private Pattern predlozakRegistracijaRadara =
      Pattern.compile("^RADAR (?<id>\\d+) (?<adresa>[a-zA-Z0-9ćčšđžĆČŠĐŽ_-]+) (?<mreznaVrata>\\d+) "
          + "(?<gpsSirina>\\d+[.]\\d+) (?<gpsDuzina>\\d+[.]\\d+) (?<maksUdaljenost>-?\\d*)$");

  /** Poklapanje za provjeru komande registracije radara. */
  private Matcher poklapanjeRegistracijaRadara;

  /** Predložak za provjeru komande brisanja jednog radara. */
  private Pattern predlozakZaBrisanjeJednogRadara =
      Pattern.compile("^RADAR " + "(?<obrisi>OBRIŠI) " + "(?<obrisiId>\\d+)$");

  /** Poklapanje za provjeru komande brisanja jednog radara. */
  private Matcher poklapanjeZaBrisanjeJednogRadara;

  /** Predložak za provjeru komande brisanja svih radara. */
  private Pattern predlozakZaBrisanjeSvihRadara =
      Pattern.compile("^RADAR " + "(?<obrisi>OBRIŠI) " + "(?<sve>SVE)$");

  /** Poklapanje za provjeru komande brisanja svih radara. */
  private Matcher poklapanjeZaBrisanjeSvihRadara;

  /** Predložak za provjeru komande ispisa svih radara. */
  private Pattern predlozakSviRadari = Pattern.compile("^RADAR SVI$");

  /** Poklapanje za provjeru komande ispisa svih radara. */
  private Matcher poklapanjeSviRadari;

  /** Predložak za provjeru komande provjeri radar. */
  private Pattern predlozakProvjeriRadar = Pattern.compile("^RADAR " + "(?<id>\\d+)$");

  /** Poklapanje za provjeru komande provjeri radar. */
  private Matcher poklapanjeProvjeriRadar;

  /** Predložak za provjeru komande radar reset. */
  private Pattern predlozakRadarReset = Pattern.compile("^RADAR RESET$");

  /** Poklapanje za provjeru komande radar reset. */
  private Matcher poklapanjeRadarReset;

  /**
   * Inicijalizacija objekta PosluziteljZaRegistracijuRadara.
   *
   * @param mreznaVrata - mrežna vrata PosluziteljZaRegistracijuRadara
   * @param centralniSustav - centralni sustav
   */
  public PosluziteljZaRegistracijuRadara(int mreznaVrata, CentralniSustav centralniSustav) {
    super();
    this.mreznaVrata = mreznaVrata;
    this.centralniSustav = centralniSustav;
  }

  /**
   * Metoda run - spajanje na poslužitelja i čekanje zahtjeva.
   */
  @Override
  public void run() {
    boolean kraj = false;

    try (ServerSocket mreznaUticnicaPosluzitelja = new ServerSocket(this.mreznaVrata)) {
      while (!kraj) {
        var mreznaUticnica = mreznaUticnicaPosluzitelja.accept();
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
      }
    } catch (NumberFormatException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Obrada zahtjeva - za dolazni zahtjev provjerava ispravnost komande te izvršava komandu.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda od klijenta
   * @return odgovor poslužitelja
   */
  public String obradaZahtjeva(String zahtjev) {
    if (zahtjev == null) {
      return "ERROR 19 Zahtjev je null.\n";
    }

    poklapanjeRegistracijaRadara = predlozakRegistracijaRadara.matcher(zahtjev);
    if (poklapanjeRegistracijaRadara.matches()) {
      return obradaZahtjevaRegistracijeRadara(zahtjev);
    }

    poklapanjeZaBrisanjeJednogRadara = predlozakZaBrisanjeJednogRadara.matcher(zahtjev);
    if (poklapanjeZaBrisanjeJednogRadara.matches()) {
      return obradaZahtjevaBrisanjaRadara(zahtjev);
    }

    poklapanjeZaBrisanjeSvihRadara = predlozakZaBrisanjeSvihRadara.matcher(zahtjev);
    if (poklapanjeZaBrisanjeSvihRadara.matches()) {
      return obradaZahtjevaBrisanjaSvihRadara(zahtjev);
    }

    poklapanjeSviRadari = predlozakSviRadari.matcher(zahtjev);
    if (poklapanjeSviRadari.matches()) {
      return obradaZahtjevaSviRadari(zahtjev);
    }

    poklapanjeProvjeriRadar = predlozakProvjeriRadar.matcher(zahtjev);
    if (poklapanjeProvjeriRadar.matches()) {
      return obradaZahtjevaProvjeriRadar(zahtjev);
    }

    poklapanjeRadarReset = predlozakRadarReset.matcher(zahtjev);
    if (poklapanjeRadarReset.matches()) {
      return obradaZahtjevaRadarReset(zahtjev);
    }


    return "ERROR 10 Neispravna sintaksa komande.\n";
  }

  /**
   * Metoda obradaZahtjevaRegistracijeRadara - registracija novog radara.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor
   */
  public String obradaZahtjevaRegistracijeRadara(String zahtjev) {
    int id = Integer.valueOf(this.poklapanjeRegistracijaRadara.group("id"));

    if (this.centralniSustav.sviRadari.containsKey(id)) {
      return "ERROR 11 Radar s ID-om " + id + " već postoji.\n";
    }

    var radar = new PodaciRadara(Integer.valueOf(this.poklapanjeRegistracijaRadara.group("id")),
        this.poklapanjeRegistracijaRadara.group("adresa"),
        Integer.valueOf(this.poklapanjeRegistracijaRadara.group("mreznaVrata")), -1, -1,
        Integer.valueOf(this.poklapanjeRegistracijaRadara.group("maksUdaljenost")), null, -1, null,
        -1, null, Double.valueOf(this.poklapanjeRegistracijaRadara.group("gpsSirina")),
        Double.valueOf(this.poklapanjeRegistracijaRadara.group("gpsDuzina")));

    this.centralniSustav.sviRadari.put(radar.id(), radar);

    return "OK\n";
  }

  /**
   * Metoda obradaZahtjevaBrisanjaRadara - brisanje jednog radara.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor
   */
  private String obradaZahtjevaBrisanjaRadara(String zahtjev) {
    int id = Integer.valueOf(this.poklapanjeZaBrisanjeJednogRadara.group("obrisiId"));

    if (this.centralniSustav.sviRadari.containsKey(id)) {
      this.centralniSustav.sviRadari.remove(id);
      return "OK\n";
    } else {
      return "ERROR 12 Radar s ID-om " + id + " ne postoji.\n";
    }
  }

  /**
   * Metoda obradaZahtjevaBrisanjaSvihRadara - brisanje svih radara.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor
   */
  private String obradaZahtjevaBrisanjaSvihRadara(String zahtjev) {
    if (this.centralniSustav.sviRadari.isEmpty()) {
      return "ERROR 19 Kolekcija radara je već prazna.\n";
    } else {
      this.centralniSustav.sviRadari.clear();
      return "OK\n";
    }
  }

  /**
   * Metoda obradaZahtjevaSviRadari - ispisuje sve radare.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor
   */
  private String obradaZahtjevaSviRadari(String zahtjev) {
    if (this.centralniSustav.sviRadari.isEmpty()) {
      return "OK {}\n";
    }

    StringBuilder odgovorSviRadari = new StringBuilder("OK {");
    for (PodaciRadara radar : this.centralniSustav.sviRadari.values()) {
      if (odgovorSviRadari.length() > 4) {
        odgovorSviRadari.append(", ");
      }
      odgovorSviRadari.append("[").append(radar.id()).append(" ").append(radar.adresaRadara())
          .append(" ").append(radar.mreznaVrataRadara()).append(" ").append(radar.gpsSirina())
          .append(" ").append(radar.gpsDuzina()).append(" ").append(radar.maksUdaljenost())
          .append("]");
    }
    odgovorSviRadari.append("}\n");
    return odgovorSviRadari.toString();
  }

  /**
   * Metoda obradaZahtjevaProvjeriRadar - provjerava postoji li radar u kolekciji .
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor
   */
  private String obradaZahtjevaProvjeriRadar(String zahtjev) {
    int id = Integer.valueOf(this.poklapanjeProvjeriRadar.group("id"));

    if (this.centralniSustav.sviRadari.containsKey(id)) {
      return "OK\n";
    } else {
      return "ERROR 12 Radar s ID-om " + id + " ne postoji.\n";
    }
  }

  /**
   * Metoda obradaZahtjevaRadarReset - resetira radare.
   *
   * @param zahtjev - dolazni zahtjev tj. komanda
   * @return odgovor
   */
  private String obradaZahtjevaRadarReset(String zahtjev) {
    int ukupanBrojRadara = this.centralniSustav.sviRadari.size();
    List<Integer> neaktivniRadari = new ArrayList<>();
    int brojNeaktivnihRadara = 0;

    for (Integer kljuc : this.centralniSustav.sviRadari.keySet()) {
      PodaciRadara radar = this.centralniSustav.sviRadari.get(kljuc);

      String komandaRadarId = "RADAR " + kljuc + "\n";
      String odgovor = MrezneOperacije.posaljiZahtjevPosluzitelju(radar.adresaRadara(),
          radar.mreznaVrataRadara(), komandaRadarId);

      if (odgovor == null || !odgovor.equals("OK")) {
        neaktivniRadari.add(kljuc);
      }
    }

    for (int id : neaktivniRadari) {
      this.centralniSustav.sviRadari.remove(id);
      brojNeaktivnihRadara++;
    }

    return "OK " + ukupanBrojRadara + " " + brojNeaktivnihRadara + "\n";
  }
}

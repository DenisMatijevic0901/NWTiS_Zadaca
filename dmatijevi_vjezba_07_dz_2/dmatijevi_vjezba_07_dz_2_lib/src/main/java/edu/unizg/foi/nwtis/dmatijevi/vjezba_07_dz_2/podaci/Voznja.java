package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci;

// TODO: Auto-generated Javadoc
/**
 * Klasa Voznja.
 * 
 * @author Denis Matijević
 */
public class Voznja {

  /** id. */
  private int id;

  /** broj. */
  private int broj;

  /** vrijeme. */
  private long vrijeme;

  /** brzina. */
  private double brzina;

  /** snaga. */
  private double snaga;

  /** struja. */
  private double struja;

  /** visina. */
  private double visina;

  /** gps brzina. */
  private double gpsBrzina;

  /** temp vozila. */
  private int tempVozila;

  /** postotak baterija. */
  private int postotakBaterija;

  /** napon baterija. */
  private double naponBaterija;

  /** kapacitet baterija. */
  private int kapacitetBaterija;

  /** temp baterija. */
  private int tempBaterija;

  /** preostalo km. */
  private double preostaloKm;

  /** ukupno km. */
  private double ukupnoKm;

  /** gps sirina. */
  private double gpsSirina;

  /** gps duzina. */
  private double gpsDuzina;

  /**
   * Inicijalizacija objekta Voznja.
   */
  public Voznja() {}

  /**
   * Inicijalizacija objekta Voznja.
   *
   * @param id - id
   * @param broj - broj
   * @param vrijeme - vrijeme
   * @param brzina - brzina
   * @param snaga - snaga
   * @param struja - struja
   * @param visina - visina
   * @param gpsBrzina - gps brzina
   * @param tempVozila - temp vozila
   * @param postotakBaterija - postotak baterija
   * @param naponBaterija - napon baterija
   * @param kapacitetBaterija - kapacitet baterija
   * @param tempBaterija - temp baterija
   * @param preostaloKm - preostalo km
   * @param ukupnoKm - ukupno km
   * @param gpsSirina - gps sirina
   * @param gpsDuzina - gps duzina
   */
  public Voznja(int id, int broj, long vrijeme, double brzina, double snaga, double struja,
      double visina, double gpsBrzina, int tempVozila, int postotakBaterija, double naponBaterija,
      int kapacitetBaterija, int tempBaterija, double preostaloKm, double ukupnoKm,
      double gpsSirina, double gpsDuzina) {
    super();
    this.id = id;
    this.broj = broj;
    this.vrijeme = vrijeme;
    this.brzina = brzina;
    this.snaga = snaga;
    this.struja = struja;
    this.visina = visina;
    this.gpsBrzina = gpsBrzina;
    this.tempVozila = tempVozila;
    this.postotakBaterija = postotakBaterija;
    this.naponBaterija = naponBaterija;
    this.kapacitetBaterija = kapacitetBaterija;
    this.tempBaterija = tempBaterija;
    this.preostaloKm = preostaloKm;
    this.ukupnoKm = ukupnoKm;
    this.gpsSirina = gpsSirina;
    this.gpsDuzina = gpsDuzina;
  }

  /**
   * Vraća id.
   *
   * @return id
   */
  public int getId() {
    return id;
  }

  /**
   * Vraća broj.
   *
   * @return broj
   */
  public int getBroj() {
    return broj;
  }

  /**
   * Vraća vrijeme.
   *
   * @return vrijeme
   */
  public long getVrijeme() {
    return vrijeme;
  }

  /**
   * Vraća brzina.
   *
   * @return brzina
   */
  public double getBrzina() {
    return brzina;
  }

  /**
   * Vraća snaga.
   *
   * @return snaga
   */
  public double getSnaga() {
    return snaga;
  }

  /**
   * Vraća struja.
   *
   * @return struja
   */
  public double getStruja() {
    return struja;
  }

  /**
   * Vraća visina.
   *
   * @return visina
   */
  public double getVisina() {
    return visina;
  }

  /**
   * Vraća gps brzina.
   *
   * @return gps brzina
   */
  public double getGpsBrzina() {
    return gpsBrzina;
  }

  /**
   * Vraća temp vozila.
   *
   * @return temp vozila
   */
  public int getTempVozila() {
    return tempVozila;
  }

  /**
   * Vraća postotak baterija.
   *
   * @return postotak baterija
   */
  public int getPostotakBaterija() {
    return postotakBaterija;
  }

  /**
   * Vraća napon baterija.
   *
   * @return napon baterija
   */
  public double getNaponBaterija() {
    return naponBaterija;
  }

  /**
   * Vraća kapacitet baterija.
   *
   * @return kapacitet baterija
   */
  public int getKapacitetBaterija() {
    return kapacitetBaterija;
  }

  /**
   * Vraća temp baterija.
   *
   * @return temp baterija
   */
  public int getTempBaterija() {
    return tempBaterija;
  }

  /**
   * Vraća preostalo km.
   *
   * @return preostalo km
   */
  public double getPreostaloKm() {
    return preostaloKm;
  }

  /**
   * Vraća ukupno km.
   *
   * @return ukupno km
   */
  public double getUkupnoKm() {
    return ukupnoKm;
  }

  /**
   * Vraća gps sirina.
   *
   * @return gps sirina
   */
  public double getGpsSirina() {
    return gpsSirina;
  }

  /**
   * Vraća gps duzina.
   *
   * @return gps duzina
   */
  public double getGpsDuzina() {
    return gpsDuzina;
  }

  /**
   * Postavlja id.
   *
   * @param id id
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Postavlja broj.
   *
   * @param broj broj
   */
  public void setBroj(int broj) {
    this.broj = broj;
  }

  /**
   * Postavlja vrijeme.
   *
   * @param vrijeme vrijeme
   */
  public void setVrijeme(long vrijeme) {
    this.vrijeme = vrijeme;
  }

  /**
   * Postavlja brzina.
   *
   * @param brzina brzina
   */
  public void setBrzina(double brzina) {
    this.brzina = brzina;
  }

  /**
   * Postavlja snaga.
   *
   * @param snaga snaga
   */
  public void setSnaga(double snaga) {
    this.snaga = snaga;
  }

  /**
   * Postavlja struja.
   *
   * @param struja struja
   */
  public void setStruja(double struja) {
    this.struja = struja;
  }

  /**
   * Postavlja visina.
   *
   * @param visina visina
   */
  public void setVisina(double visina) {
    this.visina = visina;
  }

  /**
   * Postavlja gps brzina.
   *
   * @param gpsBrzina gps brzina
   */
  public void setGpsBrzina(double gpsBrzina) {
    this.gpsBrzina = gpsBrzina;
  }

  /**
   * Postavlja temp vozila.
   *
   * @param tempVozila temp vozila
   */
  public void setTempVozila(int tempVozila) {
    this.tempVozila = tempVozila;
  }

  /**
   * Postavlja postotak baterija.
   *
   * @param postotakBaterija postotak baterija
   */
  public void setPostotakBaterija(int postotakBaterija) {
    this.postotakBaterija = postotakBaterija;
  }

  /**
   * Postavlja napon baterija.
   *
   * @param naponBaterija napon baterija
   */
  public void setNaponBaterija(double naponBaterija) {
    this.naponBaterija = naponBaterija;
  }

  /**
   * Postavlja kapacitet baterija.
   *
   * @param kapacitetBaterija kapacitet baterija
   */
  public void setKapacitetBaterija(int kapacitetBaterija) {
    this.kapacitetBaterija = kapacitetBaterija;
  }

  /**
   * Postavlja temp baterija.
   *
   * @param tempBaterija temp baterija
   */
  public void setTempBaterija(int tempBaterija) {
    this.tempBaterija = tempBaterija;
  }

  /**
   * Postavlja preostalo km.
   *
   * @param preostaloKm preostalo km
   */
  public void setPreostaloKm(double preostaloKm) {
    this.preostaloKm = preostaloKm;
  }

  /**
   * Postavlja ukupno km.
   *
   * @param ukupnoKm ukupno km
   */
  public void setUkupnoKm(double ukupnoKm) {
    this.ukupnoKm = ukupnoKm;
  }

  /**
   * Postavlja gps sirina.
   *
   * @param gpsSirina gps sirina
   */
  public void setGpsSirina(double gpsSirina) {
    this.gpsSirina = gpsSirina;
  }

  /**
   * Postavlja gps duzina.
   *
   * @param gpsDuzina gps duzina
   */
  public void setGpsDuzina(double gpsDuzina) {
    this.gpsDuzina = gpsDuzina;
  }
}

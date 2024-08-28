package edu.unizg.foi.nwtis.dmatijevi.vjezba_07_dz_2.podaci;

// TODO: Auto-generated Javadoc
/**
 * Klasa Kazna.
 */
public class Kazna {

  /** id vozila. */
  private int id;

  /** vrijeme pocetak. */
  private long vrijemePocetak;

  /** vrijeme kraj. */
  private long vrijemeKraj;

  /** brzina. */
  private double brzina;

  /** gps sirina. */
  private double gpsSirina;

  /** gps duzina. */
  private double gpsDuzina;

  /** gps sirina radar. */
  private double gpsSirinaRadar;

  /** gps duzina radar. */
  private double gpsDuzinaRadar;

  /**
   * Inicijalizacija objekta Kazna.
   */
  public Kazna() {}

  /**
   * Inicijalizacija objekta Kazna.
   *
   * @param id - id
   * @param vrijemePocetak - vrijeme pocetak
   * @param vrijemeKraj - vrijeme kraj
   * @param brzina - brzina
   * @param gpsSirina - gps sirina
   * @param gpsDuzina - gps duzina
   * @param gpsSirinaRadar - gps sirina radar
   * @param gpsDuzinaRadar - gps duzina radar
   */
  public Kazna(int id, long vrijemePocetak, long vrijemeKraj, double brzina, double gpsSirina,
      double gpsDuzina, double gpsSirinaRadar, double gpsDuzinaRadar) {
    super();
    this.id = id;
    this.vrijemePocetak = vrijemePocetak;
    this.vrijemeKraj = vrijemeKraj;
    this.brzina = brzina;
    this.gpsSirina = gpsSirina;
    this.gpsDuzina = gpsDuzina;
    this.gpsSirinaRadar = gpsSirinaRadar;
    this.gpsDuzinaRadar = gpsDuzinaRadar;
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
   * Vraća vrijeme pocetak.
   *
   * @return vrijeme pocetak
   */
  public long getVrijemePocetak() {
    return vrijemePocetak;
  }

  /**
   * Vraća vrijeme kraj.
   *
   * @return vrijeme kraj
   */
  public long getVrijemeKraj() {
    return vrijemeKraj;
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
   * Vraća gps sirina radar.
   *
   * @return gps sirina radar
   */
  public double getGpsSirinaRadar() {
    return gpsSirinaRadar;
  }

  /**
   * Vraća gps duzina radar.
   *
   * @return gps duzina radar
   */
  public double getGpsDuzinaRadar() {
    return gpsDuzinaRadar;
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
   * Postavlja vrijeme pocetak.
   *
   * @param vrijemePocetak vrijeme pocetak
   */
  public void setVrijemePocetak(long vrijemePocetak) {
    this.vrijemePocetak = vrijemePocetak;
  }

  /**
   * Postavlja vrijeme kraj.
   *
   * @param vrijemeKraj vrijeme kraj
   */
  public void setVrijemeKraj(long vrijemeKraj) {
    this.vrijemeKraj = vrijemeKraj;
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

  /**
   * Postavlja gps sirina radar.
   *
   * @param gpsSirinaRadar gps sirina radar
   */
  public void setGpsSirinaRadar(double gpsSirinaRadar) {
    this.gpsSirinaRadar = gpsSirinaRadar;
  }

  /**
   * Postavlja gps duzina radar.
   *
   * @param gpsDuzinaRadar gps duzina radar
   */
  public void setGpsDuzinaRadar(double gpsDuzinaRadar) {
    this.gpsDuzinaRadar = gpsDuzinaRadar;
  }

}

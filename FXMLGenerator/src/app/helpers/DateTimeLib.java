package app.helpers;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Classe de méthodes statiques permettant des conversions
 * facilitées entre date, heure et chaînes de caractères (String).
 *
 * @author Jean-Claude Stritt
 *
 */
public class DateTimeLib {
  public final static String DATE_FORMAT_SHORT = "dd.MM.yy";
  public final static String DATE_FORMAT_STANDARD = "d.M.yyyy";

  public final static String TIME_FORMAT_SHORT = "HH:mm";
  public final static String TIME_FORMAT_STANDARD = "HH:mm:ss";
  public final static String TIME_BASE ="00";

  public final static String ISO8601_DATE_FORMAT = "yyyy-MM-dd";
  public final static String ISO8601_DATE_TIME_FORMAT1 = "yyyy-MM-dd'T'HH:mm:ss";
  public final static String ISO8601_DATE_TIME_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

  public final static String DATE_TIME_FORMAT_STANDARD = "d.M.yy HH:mm:ss";
  public final static String DATE_TIME_FORMAT_FILENAME = "yyyy_MM_dd_HHmmss_SSS";
  public final static String DATE_TIME_FORMAT_SPECIAL = "dd-MM-yyyy HH:mm";

  public final static long MILLISECONDS_PER_DAY = 1000 * 60 * 60 * 24;
  private static long timeStamp = 0;



  /*
   * LOCALE
   */

  /**
   * Retourne le format local d'une date avec ou sans heure.
   *
   * @param format un format de date et/ou heure
   * @return le format local d'une date
   */
  public static SimpleDateFormat getLocaleFormat(String format) {
    Locale locale = Locale.getDefault();
    return new SimpleDateFormat(format, locale);
  }

  /**
   * Retourne un tableau avec les informations sur le format standard des dates et heures.<br>
   * <br>
   * [0] = séparateur dans une date, exemple: "."<br>
   * [1] = séparateur sous la forme d'une expression regex, exemple: "\."<br>
   * [2] = le format d'une date, exemple: "dd.MM.yy"<br>
   * [3] = séparateur d'un temps, exemple: ":"<br>
   * [4] = séparateur d'un temps sous la forme d'une expression regex, exemple: "\:"<br>
   * [5] = le format d'un temps, exemple: "HH:mm:ss"<br>
   *
   * @return un tableau avec les informations sus-mentionnées
   */
  public static String[] getLocalePatternInfo() {
    String info[] = new String[6];
    Locale loc = Locale.getDefault();
    SimpleDateFormat sdf = (SimpleDateFormat) SimpleDateFormat.getDateInstance(java.text.SimpleDateFormat.SHORT, loc);
    SimpleDateFormat stf = (SimpleDateFormat) SimpleDateFormat.getTimeInstance(java.text.SimpleDateFormat.SHORT, loc);
    String datePattern = sdf.toPattern();
    String timePattern = stf.toPattern();
    info[0] = datePattern.toLowerCase().replaceAll("[a-zA-Z]","").substring(0,1);
    info[1] = "\\" + info[0];
    info[2] = datePattern;

    // infos sur le format des heures
    info[3] = timePattern.toLowerCase().replaceAll("[a-zA-Z]","").substring(0,1);
    info[4] = "\\" + info[3];
    info[5] = timePattern;

    return info;
  }



  /*
   * INFORMATIONS GLOBALES SUR LES DATES ET LES HEURES
   */

  /**
   * Retourne un tableau d'entiers avec le jour, le mois et l'année
   * extraits de la date spécifiée en paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau d'entiers avec le jour, le mois et l'année
   */
  public static int[] extractDateInfo(Date date) {
    int[] info = new int[] {-1, -1, -1};
    if (date != null) {
      Calendar c = new GregorianCalendar();
      c.setTime(date);
      info[0] = c.get(Calendar.DAY_OF_MONTH);
      info[1] = c.get(Calendar.MONTH) + 1;
      info[2] = c.get(Calendar.YEAR);
    }
    return info;
  }

  /**
   * Retourne un tableau d'entiers avec l'heure, les minutes, les secondes
   * et les milisecondes de la date spécifiée en paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau[4] d'entiers avec hh, mm, ss, ms
   */
  public static int[] extractTimeInfo(Date date) {
    int[] info = new int[] {-1, -1, -1, -1};
    if (date != null) {
      Calendar c = new GregorianCalendar();
      c.setTime(date);
      info[0] = c.get(Calendar.HOUR_OF_DAY);
      info[1] = c.get(Calendar.MINUTE);
      info[2] = c.get(Calendar.SECOND);
      info[3] = c.get(Calendar.MILLISECOND);
    }
    return info;
  }

  /**
   * Retourne un tableau d'entiers avec le jour, le mois, l'année, l'heure,
   * les minutes, les secondes et les milisecondes de la date spécifiée
   * en paramètre.
   *
   * @param date une date de type java.util.Date
   * @return un tableau[7] avec JJ, MM, AA, hh, mm, ss, ms
   */
  public static int[] extractDateTimeInfo(Date date) {
    int[] info = new int[] {-1, -1, -1, -1, -1, -1, -1};
    if (date != null) {
      Calendar c = new GregorianCalendar();
      c.setTime(date);
      info[0] = c.get(Calendar.DAY_OF_MONTH);
      info[1] = c.get(Calendar.MONTH) + 1;
      info[2] = c.get(Calendar.YEAR);
      info[3] = c.get(Calendar.HOUR_OF_DAY);
      info[4] = c.get(Calendar.MINUTE);
      info[5] = c.get(Calendar.SECOND);
      info[6] = c.get(Calendar.MILLISECOND);
    }
    return info;
  }



  /*
   * CREATION BASIQUE DE DATES & HEURES
   */

  /**
   * Crée une date en fournissant toutes les données nécessaires.<br>
   *
   * @param day le jour
   * @param month le mois
   * @param year l'année
   * @param hour l'heure
   * @param min les minutes
   * @param sec les secondes
   * @return une date d'après les informations données
   */
  public static Date createDate(int day, int month, int year, int hour, int min, int sec) {

    // récupère un objet calendrier et le remplit avec la date spécifiée
    Calendar cal = new GregorianCalendar();

    // met les infos de temps à zéro
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.MONTH, month-1);
    cal.set(Calendar.YEAR, year);

    // met les infos de temps à zéro
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, min);
    cal.set(Calendar.SECOND, sec);
    cal.set(Calendar.MILLISECOND, 0);

      // retourne la date
    Date date = cal.getTime();
    return date;
  }

  /**
   * Crée une date en fournissant le jour, le mois et l'année.<br>
   * L'heure est mise à zéro.
   *
   * @param day le jour
   * @param month le mois
   * @param year l'année
   * @return une date d'après les informations données
   */
  public static Date createDate(int day, int month, int year) {
    return createDate(day, month, year, 0, 0, 0);
  }

  /**
   * Complète une date donnée en fournissant l'heure, les minutes, les secondes.
   *
   * @param date une date à compléter avec l'heure
   * @param hour l'heure
   * @param min les minutes
   * @param sec les secondes
   * @return un temps construit avec la date spécifiée et les informations fournies
   */
  public static Date setTime(Date date, int hour, int min, int sec) {
    int day = getDay(date);
    int month = getMonth(date);
    int year = getYear(date);
    return createDate(day, month, year, hour, min, sec);
  }

  /**
   * Efface les informations de temps (HH:MM:SS:mm) dans une date donnée.
   *
   * @param date une date quelconque
   * @return une date sans les informations de temps
   */
  public static Date eraseTime(Date date) {
    return setTime(date, 0, 0, 0);
  }

  /**
   * Compare 2 dates en enlevant les informations de temps qui y seraient stockées.
   *
   * @param d1 la première date
   * @param d2 une seconde date à comparer
   * @return true si les 2 dates sont identiques
   */
  public static boolean compareDates(Date d1, Date d2) {
    return eraseTime(d1).getTime() == eraseTime(d2).getTime();
  }



  /*
   * CONVERSION DE STRING CONTENANT DATES ET/OU HEURES
   */

  /**
   * Convertir une date au format ISO-8601 en une date de la classe java.util.Date.
   *
   * @param sDate une date ISO dans une chaîne de caractères (ex: "2016-01-01")
   * @return une date de la classe java.util.Date (ou null si erreur)
   */
  public static Date parseIsoDate(String sDate) {
    Date date;
    String nDate = sDate.trim();

    // format ISO seulement avec la date ou aussi l'heure
    SimpleDateFormat ldf;
    if (nDate.contains(":")) {
      if (nDate.contains("T")) {
        ldf = getLocaleFormat(ISO8601_DATE_TIME_FORMAT1);
      } else {
        ldf = getLocaleFormat(ISO8601_DATE_TIME_FORMAT2);
      }
    } else {
      ldf = getLocaleFormat(ISO8601_DATE_FORMAT);
    }

    // on teste finalement si la date spécifiée peut être convertie
    ldf.setLenient(false);
    try {
      date = ldf.parse(nDate);
    } catch (ParseException ex) {
      date = null;
    }
    return date;
  }

  /**
   * Convertit une chaîne de caractères (String) représentant une date en une date de la
   * classe java.util.Date. Cette version accepte les années avec ou sans le siècle et
   * également les dates sans l'année (cela prend alors l'année en cours).
   *
   * @param sDate la chaîne contenant une date
   * @return une date de la classe java.util.Date (ou null si erreur)
   */
  public static Date parseDate(String sDate) {
    Date date = null;
    String nDate = sDate.trim();
    String info[] = getLocalePatternInfo();
    String t[] = nDate.split(info[1]);

    // s'il manque l'année, on prend l'année en cours
    if (t.length == 2) {
      nDate += info[0] + getYear(getNow());
      t = nDate.split(info[1]);
    }

    // teste si on dispose des 3 parties de la date
    if (t.length == 3) {
      SimpleDateFormat ldf;

      // si l'année fournie est composée de plus de 2 caractères, on change le format
      if (t[2].length() == 2) {
        ldf = getLocaleFormat(info[2]);
      } else {
        ldf = getLocaleFormat(info[2] + "yy");
      }

      // on teste finalement si la date spécifiée peut être convertie
      ldf.setLenient(false);
      try {
        date = ldf.parse(nDate);
      } catch (ParseException ex) {
        date = null;
      }
    }
    return date;
  }

  /**
   * Convertit une chaîne de caractères (String) représentant une date en une date de la
   * classe java.util.Date. Cette version accepte les années avec ou sans le siècle et
   * également les dates sans le jour. Si lastDayOfMonth=false, alors le 1er jour
   * du mois est pris, autrement c'est le dernier jour du mois.
   *
   * @param sDate la chaîne contenant une date
   * @param lastDayOfMonth force la fin de mois si le string contient le mois+l'année
   * @return une date de la classe java.util.Date (ou null si erreur)
   */
  public static Date parseDate(String sDate, boolean lastDayOfMonth) {
    String nDate = sDate.trim();
    Date d = parseDate(nDate);

    String info[] = getLocalePatternInfo();
    String t[] = nDate.split(info[1]);
    if (t.length == 2) {
      if (info[2].substring(0,1).equalsIgnoreCase("m")) {
        nDate = t[0] + info[0] + "1" + info[0] + t[1];
      } else {
        nDate = "1" + info[0] + t[0] + info[0] + t[1];
      }
      d = parseDate(nDate);
      if (lastDayOfMonth) {
        d = createDate(getMonthMaxDay(d), getMonth(d), getYear(d));
      }
    }
    return d;
  }

  /**
   * Convertit un temps dans le format local vers une date de type java.util.Date.
   * Cette version accepte des temps même sans les minutes et/ou les secondes.
   * La date en elle-même sera le 1.1.1970.
   *
   * @param sTime un temps dans le format local (ex: "12:01" ou "12:01:00")
   * @return une date de la classe java.util.Date (ou null si erreur)
   */
  public static Date parseTime(String sTime) {
    Date date = null;
    String info[] = getLocalePatternInfo();

    String nTime = sTime.trim();
    if (!nTime.isEmpty()) {

      // on supprime le dernier séparateur s'il est tout seul
      String lastChar = nTime.substring(nTime.length() - 1);
      if (lastChar.equalsIgnoreCase(info[3])) {
        nTime = nTime.substring(0, nTime.length()-1);
      }

      // om complète si l'heure complète n'est pas spécifiée
      String t[] = nTime.split(info[4]);
      switch (t.length) {
        case 1:
          nTime += info[3] + TIME_BASE + info[3] + TIME_BASE;
          break;
        case 2:
          nTime += info[3] + TIME_BASE;
      }
      t = nTime.split(info[4]);

      // on teste si l'on dispose des 3 parties d'un temps
      if (t.length == 3) {
        SimpleDateFormat ldf;
        ldf = getLocaleFormat("HH" + info[3] + "mm" + info[3] + "ss");

        // on teste finalement si le temps spécifié peut être converti
        ldf.setLenient(false);
        try {
          date = ldf.parse(nTime);
        } catch (ParseException ex) {
          date = null;
        }
      }
    }
    return date;
  }

  /**
   * Convertit un temps au format local en une date Java standard.
   * Le temps est complété avec une date fournie.
   *
   * @param sTime un temps au format local (ex: "12:01:00")
   * @param date une date pour compléter le temps
   * @return une date Java standard
   */
  public static Date parseTime(String sTime, Date date) {
    Date result = null;
    Date time = parseTime(sTime);
    if (date != null && time != null) {
      int info[] =  extractTimeInfo(time);
      result = setTime(date, info[0], info[1], info[2]);
    }
    return result;
  }



  /**
   * VALIDATION DE DATE OU HEURE
   */

  /**
   * Teste si une date au format ISO-8601 est valide.
   *
   * @param sDate une date ISO dans une chaîne de caractères (ex: "2016-01-01")
   * @return true si la date est valide, false autrement
   */
  public static boolean isValidIsoDate(String sDate) {
    return parseIsoDate(sDate) != null;
  }

  /**
   * Teste si une date est valide.
   *
   * @param sDate une date représentée dans le format local dans une chaîne de caractères (String)
   * @return true si la date est valide, false autrement
   */
  public static boolean isValidDate(String sDate) {
    return parseDate(sDate) != null;
  }

  /**
   * Teste si une date est celle attendue.
   *
   * @param date une date fournie
   * @param day le jour attendu
   * @param month le mois attendu
   * @param year l'année attendu
   *
   * @return true si la date est valide, false autrement
   */
  public static boolean isValidDate(Date date, int day, int month, int year) {
    int info[] = extractDateInfo(date);
    return day == info[0] &&  month == info[1] && year == info[2];
  }

  /**
   * Teste si une date au format standard java.util.Date est valide, en testant si l'année
   * est dans une fourchette de +- un nombre d'années spécifié par rapport à l'année
   * courante.
   *
   * @param date une date au format java.util.Date à tester (null est accepté comme ok)
   * @param minusYears un nb d'années en dessous que l'on accepte
   * @param plusYears un nb d'années en dessus que l'on accepte
   *
   * @return true (vrai) si la date est dans la fourchette, false (faux) autrement
   */
  public static boolean isValidDate(Date date, int minusYears, int plusYears) {
    boolean ok = date == null;
    if (!ok) {
      int currentYear = getYear(getToday());
      int dateYear = getYear(date);
      int limits[] = new int[2];
      limits[0] = currentYear - minusYears;
      limits[1] = currentYear + plusYears;
      ok = dateYear >= limits[0] && dateYear <= limits[1];
    }
    return ok;
  }

  /**
   * Teste si une date au format String est valide, en testant encore si l'année est dans
   * une fourchette de +- un nombre d'années spécifié par rapport à l'année courante.
   *
   * @param sDate une date au format String à tester
   * @param minusYears un nb d'années en dessous que l'on accepte
   * @param plusYears un nb d'années en dessus que l'on accepte
   *
   * @return true (vrai) si la date est dans la fourchette, false (faux) autrement
   */
  public static boolean isValidDate(String sDate, int minusYears, int plusYears) {
    Date d = parseDate(sDate);
    return d != null && isValidDate(d, minusYears, plusYears);
  }

  /**
   * Teste si un temps spécifié dans le format local est valide.
   *
   * @param sTime un temps représentée dans le format local (ex: "23:59", "12:00:01")
   * @return true si le temps est valide, false autrement
   */
  public static boolean isValidTime(String sTime) {
    return parseTime(sTime) != null;
  }

  /**
   * Teste si le temps contenu dans une date est celui attendu.
   *
   * @param date une date fournie qui contient le temps à tester
   * @param hour l'heure attendue
   * @param min les minutes attendues
   * @param sec les secondes attendues
   * @return true si le temps contenu dans la date est celui attendu
   */
  public static boolean isValidTime(Date date, int hour, int min, int sec) {
    int info[] = extractTimeInfo(date);
    return hour == info[0] &&  min == info[1] && sec == info[2];
  }



  /*
   * CONVERSION DE DATES ET HEURES EN STRING
   */

  /**
   * Convertit une date (java.util.Date) vers une représentation String construite avec le
   * format demandé.
   *
   * @param date une date de la classe java.util.Date
   * @param format un format de date (ou de temps) sous la forme d'un String
   * @param objects paramètres facultatifs : 1 seul est permis = la valeur par défaut si la date est nulle (ex: "...")
   * @return la même date au format String
   */
  public static String dateToString(Date date, String format, Object...objects) {
    String sDate;
    if (date != null) {
      SimpleDateFormat ldf = getLocaleFormat(format);
      sDate = ldf.format(date);
    } else {
      if (objects.length > 0) {
        sDate = (String) objects[0];
      } else {
        char[] array = new char[format.length()];
        Arrays.fill(array, ' ');
        sDate = new String(array);
      }
    }
    return sDate;
  }

  /**
   * Convertit une date (java.util.Date) vers une représentation String standard.
   *
   * @param date une date de la classe java.util.Date
   * @return la même date au format String
   */
  public static String dateToString(Date date) {
    return dateToString(date, DATE_FORMAT_STANDARD);
  }

  /**
   * Convertit une date avec heure vers une représentation String standard.
   *
   * @param dateTime une date-heure de la classe java.util.Date
   * @return la même date-heure au format String
   */
  public static String dateTimeToString(Date dateTime) {
    return dateToString(dateTime, DATE_TIME_FORMAT_STANDARD);
  }



  /*
   * DEPLACEMENT DE DATE
   */

  /**
   * Retourne une date augmentée ou diminuée d'un certain nombre de jours ou de mois spécifié.
   * Le type doit être spécifié. Toute les informations sur l'heure sont mises à zéro.
   *
   * @param d la date d'origine
   * @param type le type de décalage (Calendar.DAY_OF_MONTH ou Calendar.MONTH)
   * @param nb le nombre de jours ou de mois à décaler
   * @return la nouvelle date
   */
  public static Date moveDate(Date d, int type, int nb) {
    Calendar c = new GregorianCalendar();
    c.setTime(d);
    c.add(type, nb);
    return eraseTime(c.getTime());
  }

  /**
   * Retourne une date augmentée ou diminuée d'un certain nombre de jours spécifié. Toutes
   * les informations sur l'heure sont mises à zéro.
   *
   * @param d la date d'origine
   * @param days le décalage en jours voulu
   * @return la date d'origine décalée d'un certain nombre de jours
   */
  public static Date moveDate(Date d, int days) {
    return moveDate(d, Calendar.DAY_OF_MONTH, days);
  }



  /*
   * DATE DU JOUR, DE LA SEMAINE, D'UN MOIS, D'UNE ANNEE
   */

  /**
   * Retourne la date du jour (avec heure, minutes, secondes, ms).
   *
   * @return la date du jour
   */
  public static Date getNow() {
    Calendar c = new GregorianCalendar();
    return c.getTime();
  }

  /**
   * Retourne la date du jour sans information de temps.
   *
   * @return la date du jour
   */
  public static Date getToday() {
    return moveDate(getNow(), 0);
  }

 /**
   * Retourne la date du jour avec le mois en clair.
   *
   * @return la date avec le mois en claire (ex: 1er janvier 2017)
   */
  public static String getSmartToday() {
    Date d = getToday();
    int day = getDay(d);
    int month = getMonth(d);
    int annee = getYear(d);
    String defLanguage = Locale.getDefault().getLanguage();
    String sDayMonth = "";
    String sMonth = getMonthName(month);
    if (defLanguage.equals(new Locale("fr").getLanguage()) && day==1) {
      sDayMonth = "1er " + sMonth;
    } else if (defLanguage.equals(new Locale("de").getLanguage())) {
      sDayMonth = String.valueOf(day) + ". " + sMonth;
    } else if (defLanguage.equals(new Locale("en").getLanguage())) {
      sDayMonth = sMonth + " " + String.valueOf(day) + ",";
    } else {
      sDayMonth = String.valueOf(day) + " " + sMonth;
    }
    return sDayMonth + " " + String.valueOf(annee);
  }

  /**
   * Retourne la date du jour augmentée ou diminuée d'un certain nombre de jours
   * spécifié. Toutes les informations sur l'heure sont mises à zéro.
   *
   * @param days le décalage en jours voulu
   * @return la date du jour décalé d'un certain nombre de jours
   */
  public static Date getDate(int days) {
    return moveDate(getNow(), days);
  }

  /**
   * Retourne la date du lundi de la semaine pour une date de référence fournie.
   *
   * @param date une date qui identifie la semaine
   * @return la date du lundi
   */
  public static Date getMonday(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int dayInWeek = cal.get(Calendar.DAY_OF_WEEK);
    int offset = (dayInWeek == 1) ? -6 : -dayInWeek + 2;
    return moveDate(date, offset);
  }

  /**
   * Retourne la date du vendredi de la semaine pour une date de référence fournie.
   *
   * @param date une date qui identifie la semaine
   * @return la date du lundi
   */
  public static Date getFriday(Date date) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int dayInWeek = cal.get(Calendar.DAY_OF_WEEK);
    int offset = (dayInWeek == 1) ? -2 : 6 - dayInWeek;
    return moveDate(date, offset);
  }

  /**
   * Retourne un tableau avec les dates du lundi et du vendredi
   * calculées d'après une date de référence fournie.
   *
   * @param date une date de référence
   * @return un tableau avec les deux dates du lundi et du vendredi
   */
  public static Date[] getMondayFriday(Date date) {
    Date[] dates = new Date[2];
    dates[0] = getMonday(date);
    dates[1] = getFriday(date);
    return dates;
  }

  /**
   * Retourne les dates de travail (du LU au VE) du jour en cours (si weekOffset=0).
   * On peut aussi donc avancer ou reculer de 0 à plusieurs semaines.
   *
   * @param weekOffset un décalage positif ou négatif de semaines (0=semaine en cours)
   * @return un tableau de 5 dates avec les dates de travail de la semaine
   */
  public static Date[] getWeekDates(int weekOffset) {
    Date d = getDate(weekOffset * 7); // sans heure !!!
    Date[] dates = new Date[5];
    dates[0] = getMonday(d);
    for (int i = 1; i < dates.length; i++) {
      dates[i] = moveDate(dates[0], i);
    }
    return dates;
  }

  /**
   * Retourne un tableau avec les dates de début et de fin de mois pour la date spécifiée.
   * On peut également fournir un offset en mois pour décaler cette date.
   *
   * @param curDate la date courante
   * @param monthsOffset un offset en nombre de mois (positif ou négatif)
   * @return un tableau avec les deux dates calculées
   */
  public static Date[] getMonthDates(Date curDate, int monthsOffset) {
    Date retDates[] = new Date[2];

    // recalcule une date avec l'offset des mois fourni
    Date newDate = moveDate(curDate, Calendar.MONTH, monthsOffset);

    // extrait les infos de la date calculée
    int info[] = extractDateInfo(newDate);

    // retourne les dates de début et fin de mois
    retDates[0] = createDate(1, info[1], info[2]);
    retDates[1] = createDate(getMonthMaxDay(retDates[0]), info[1], info[2]);
    return retDates;
  }

  /**
   * Retourne un tableau avec les dates de début et de fin de mois par rapport
   * à la date courante du jour.
   *
   * On peut également fournir un offset en mois pour décaler cette date.
   *
   * @param monthsOffset un offset en nombre de mois (positif ou négatif)
   * @return un tableau avec les deux dates calculées
   */
  public static Date[] getMonthDates(int monthsOffset) {
    return getMonthDates(getToday(), monthsOffset);
  }

  /**
   * Retourne un tableau avec 2 dates qui correspondent au 1.1 et au 31.12 de l'année civile
   * extraite de la date courante fournie.
   *
   * @param curDate une date courante spécifiée
   * @return un tableau avec 2 dates
   */
  public static Date[] getYearDates(Date curDate) {
     Date retDates[] = new Date[2];
     int curDateInfo[] = extractDateInfo(curDate);
     retDates[0] = createDate(1,   1, curDateInfo[2]);
     retDates[1] = createDate(31, 12, curDateInfo[2]);
     return retDates;
  }

  /**
   * Retourne un tableau avec 2 dates qui correspondent au 1.1 et au 31.12
   * de l'année civile en cours.
   *
   * @return un tableau avec les 2 dates calculées
   */
  public static Date[] getYearDates() {
    return getYearDates(getToday());
  }

  /**
   * Retourne un tableau avec 2 dates qui correspondent à :<br>
   * date[0] : la date de début du mois d'une année auparavant (-12 mois) par rapport à la date fournie<br>
   * date[1] : la date de fin de mois de la date courante spécifiée<br>
   * <br>
   * Si la date fournie est avant le mois d'avril, les premiers mois de l'année
   * précédente sont également inclus dans date[0]. Donc, au final date[0]
   * correspond à 12 jusqu'à 15 mois antérieurs à date[1].<br>
   * <br>
   * On peut également rajouter un certain nombre de mois pour calculer la date finale
   *
   * @param curDate une date courante spécifiée
   * @param monthsOffset le nombre de mois à prendre en compte pour calculer la date de fin (0=défaut)
   * @return un tableau avec les 2 dates calculées
   */
  public static Date[] getYearWorkingDates(Date curDate, int monthsOffset) {
    Date retDates[] = new Date[2];

    // date courante (spécifiée)
    int newDateInfo[] = extractDateInfo(curDate);
    int nbMonths = (newDateInfo[1] < 4) ? 12 + newDateInfo[1] : 12;

    // date correspondante à 12 jusqu'à 15 mois avant la date spécifiée
    Date oldDate = moveDate(curDate, Calendar.MONTH, -nbMonths + 1);
    int oldDateInfo[] = extractDateInfo(oldDate);

    // éventuellement déplacement de la date de fin vers le futur
    if (monthsOffset < 0) {
      monthsOffset = 0;
    }
    Date newDate = moveDate(curDate, Calendar.MONTH, monthsOffset);
    newDateInfo = extractDateInfo(newDate);

    // préparation des 2 dates
    retDates[0] = createDate(1, oldDateInfo[1], oldDateInfo[2]);
    retDates[1] = createDate(getMonthMaxDay(newDate), newDateInfo[1], newDateInfo[2]);
    return retDates;
  }

  /**
   * Retourne un tableau avec 2 dates qui correspondent à :<br>
   * date[0] : la date de début du mois d'une année auparavant (-12 mois) par rapport à la date courante du jour<br>
   * date[1] : la date de fin de mois pour cette même date courante du jour<br>
   * <br>
   * Si la date est avant le mois d'avril, les premiers mois de l'année précédente sont également inclus dans date[0].
   * Donc, au final date[0] correspond à 12 jusqu'à 15 mois antérieurs à la date finale (date[1]).
   *
   * @param monthsOffset le nombre de mois à prendre en compte pour calculer la date de fin (0=défaut)
   * @return un tableau avec les 2 dates calculées
   */
  public static Date[] getYearWorkingDates(int monthsOffset) {
    return getYearWorkingDates(getToday(), monthsOffset);
  }



  /**
   * EXTRACTION D'INFORMATIONS SUR LES DATES
   */

  /**
   * Extrait l'année d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return l'année extrait de la date donnée
   */
  public static int getYear(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.YEAR);
  }

  /**
   * Extrait le mois d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return le mois extrait de la date donnée
   */
  public static int getMonth(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.MONTH) + 1;
  }

  /**
   * Retourne le nombre de jours maximal dans un mois en tenant compte des années
   * bissextiles.
   *
   * @param date la date dont il faut examiner le mois
   * @return le nombre de jours maximal dans le mois extrait de la date
   */
  public static int getMonthMaxDay(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
  }

  /**
   * Retourne le nom du mois courant dans une chaîne de caractères.
   *
   * @param month le numéro du mois (1...12)
   * @return le nom du mois en clair
   */
  public static String getMonthName(int month) {
    return new DateFormatSymbols().getMonths()[month - 1];
  }

  /**
   * Extrait le jour d'une date donnée.
   *
   * @param date une date de type java.util.Date
   * @return le jour extrait de la date donnée
   */
  public static int getDay(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Extrait le jour de la semaine (1=dimanche à 7 samedi).
   *
   * @param date une date au format java.util.Date
   * @return int le jour dans la semaine de la date spécifiée (1=dimanche à 7 samedi)
   */
  public static int getDayOfWeek(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.DAY_OF_WEEK);
  }

  /**
   * Retourne la position 0..4 (LU..VE) d'une date dans la semaine
   * de travail, -1 autrement.
   *
   * @param date une date spécifiée
   * @return int la position de la date dans la semaine 0..4 (LU..VE)
   */
  public static int getDateIndex(Date date) {
    int idx = -1;
    int dayOfWeek = getDayOfWeek(date);
    if (dayOfWeek >= 2 && dayOfWeek <= 6) {
       idx = dayOfWeek - 2;
    }
    return idx;
  }

  /**
   * Calcule le nombre de jours entre 2 dates en tenant compte de l'heure d'été.
   *
   * @param theLaterDate la date la plus vieille
   * @param theEarlierDate la date la plus récente
   * @return int le nombre de jours entre les deux dates
   */
  public static int getDaysBetweenTwoDates(Date theLaterDate, Date theEarlierDate) {
   long delta = theEarlierDate.getTime() - theLaterDate.getTime();
   return (int) (delta / (MILLISECONDS_PER_DAY));
  }

  /**
   * Teste si une année donnée est une année bissextile.
   *
   * @param year l'année à tester
   * @return true si c'est une année bissextile
   */
  public static boolean isLeapYear(int year) {
    Calendar cal = new GregorianCalendar();
    cal.set(Calendar.YEAR, year);
    return cal.getActualMaximum(Calendar.DAY_OF_YEAR) > 365;
  }

  /**
   * Calcule et retourne l'âge d'une personne d'après sa date de naissance et la date
   * courante.
   *
   * @param birthDate la date de naissance (java.util.Date)
   * @return l'âge sous la forme d'un entier (int)
   */
  public static int getCurrentAge(Date birthDate) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(birthDate);
    GregorianCalendar now = new GregorianCalendar();
    int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
    if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
            || (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH)
            && cal.get(Calendar.DAY_OF_MONTH) > now.get(Calendar.DAY_OF_MONTH))) {
      res--;
    }
    return res;
  }

  /**
   * Retourne les heures d'une date donnée.
   *
   * @param date une date (avec l'heure)
   * @return les heures de cette date
   */
  public static int getHour(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.HOUR_OF_DAY);
  }

  /**
   * Retourne les minutes d'une date donnée (avec l'heure).
   *
   * @param date une date (avec l'heure)
   * @return les minutes de cette date avec heure
   */
  public static int getMinute(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.MINUTE);
  }

  /**
   * Retourne les secondes d'une date donnée (avec l'heure).
   *
   * @param date une date (avec l'heure)
   * @return les minutes de cette date avec heure
   */
  public static int getSecond(Date date) {
    Calendar cal = new GregorianCalendar();
    cal.setTime(date);
    return cal.get(Calendar.SECOND);
  }



  /*
   * GESTION DES SQL-DATES
   */

  /**
   * Convertit une date de type java.sql.Date vers une date de type java.util.Date.
   *
   * @param sqlDate une date au format java.sql.Date
   * @return une date au format java.util.Date
   */
  public static Date sqldateToDate(java.sql.Date sqlDate) {
    return new java.util.Date(sqlDate.getTime());
  }

  /**
   * Convertit une sqldate en sa représentation "chaîne de caractères".
   *
   * @param sqldate une date de la classe java.sql.Date
   * @param format le format pour la date à formater
   * @return une chaîne avec la représentation de la date
   */
  public static String sqldateToString(java.sql.Date sqldate, String format) {
    Date date = sqldateToDate(sqldate);
    return dateToString(date, format);
  }

  /**
   * Convertit une représentation de date (String) en une date
   * de la classe java.sql.Date.
   *
   * @param sDate une chaîne avec une date à l'intérieur
   * @return une date de la class java.sql.Date
   */
  public static java.sql.Date stringToSqldate(String sDate) {
    java.sql.Date sqlDate = null;
    Date d = parseDate(sDate);
    if (d != null) {
      sqlDate = new java.sql.Date(d.getTime());
    }
    return sqlDate;
  }



  /*
   * CONVERSION DES DATES EN LOCALDATE (JAVA 8) ET VCIE-BVERSA
   */

  /**
   * Cette méthode transforme un objet Date en un objet LocalDate (Java 8).
   * On définit la zone horaire de l'objet LocalDate à la zone horaire du système.
   *
   * @param date l'objet Date à transformer en LocalDate
   * @return l'objet LocalDate correspondant à la date de type Date.
   */
  public static LocalDate dateToLocalDate(Date date) {
    if (date != null) {
      return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    } else {
      return null;
    }
  }

  /**
   * Cette méthode transforme un objet LocalDate (Java 8) en un objet Date.
   * L'objet LocalDate ne possédant pas d'heure, on lui donne minuit et la zone horaire du système.
   *
   * @param localDate l'objet LocalDate à transformer en Date
   * @return l'objet Date correspondant à l'objet LocalDate
   */
  public static Date localDateToDate(LocalDate localDate) {
    if (localDate != null) {
      return Date.from(Instant.from(localDate.atStartOfDay(ZoneId.systemDefault())));
    } else {
      return null;
    }
  }



  /*
   * GESTION DU CHRONOMETRE INTERNE
   */

  /**
   * Reset du chnonomètre interne de cette classe.
   */
  public static void chronoReset() {
    GregorianCalendar now = new GregorianCalendar();
    timeStamp = now.getTime().getTime();
  }

  /**
   * Retourne sous forme numérique (float) le temps écoulé depuis la mise à zéro du
   * chronomètre interne.
   *
   * @return un temps universel sous la forme d'un nombre réel.
   */
  public static float chronoElapsedTime() {
    GregorianCalendar now = new GregorianCalendar();
    return (float) (now.getTime().getTime() - timeStamp) / 1000;
  }

  /**
   * Retourne sous forme d'une chaîne de caractères String le temps écoulé depuis la mise
   * à zéro du chronomètre interne.
   *
   * @return le nombre de secondes écoulés jusqu'au 1/1000s
   */
  public static String chronoStringElapsedTime() {
    Locale locale = Locale.getDefault();
    DecimalFormat df = (DecimalFormat) DecimalFormat.getInstance(locale);
    df.applyPattern("#,##0.000");
    return df.format(chronoElapsedTime());
  }

}

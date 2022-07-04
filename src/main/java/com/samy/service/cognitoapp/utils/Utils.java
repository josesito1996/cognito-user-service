package com.samy.service.cognitoapp.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import com.samy.service.cognitoapp.model.ColaboradorTable;
import com.samy.service.cognitoapp.model.ElementosModulo;
import com.samy.service.cognitoapp.model.Modulo;
import com.samy.service.cognitoapp.model.Usuario;
import com.samy.service.cognitoapp.model.request.AccesoRequest;
import com.samy.service.cognitoapp.model.response.ElementosModuloResponse;
import com.samy.service.cognitoapp.model.response.ModuloResponse;

public class Utils {

  private static ZoneId defaultZoneId = ZoneId.systemDefault();

  /**
   * Tiempo de duracion del token.
   */
  private static long hoursOfDurationToken = 8;

  public static String cleanId(String id) {
    return id.replaceAll("\"", "");
  }

  /**
   * Convierte un Numero {@link Long} a {@link LocalDateTime}.
   *
   * @param timeStamp valor en segundos.
   * @return LocalDateTime
   */
  public static LocalDateTime numberToLocalDateTime(Long timeStamp) {
    return Instant.ofEpochSecond(timeStamp).atZone(defaultZoneId).toLocalDateTime();
  }

  /**
   * Convierte {@link LocalDateTime} a {@link Date}
   * 
   * @param localDateTime
   * @return {@link Date}.
   */
  public static Date toDate(LocalDateTime localDateTime) {
    return Date.from(localDateTime.atZone(defaultZoneId).toInstant());
  }

  /**
   * Metodo que convierte {@link Date} a {@link LocalDateTime}.
   * 
   * @param date
   * @return
   */
  public static LocalDateTime toLocalDateTime(Date date) {
    return date.toInstant().atZone(defaultZoneId).toLocalDateTime();
  }

  /**
   * Metodo que añade hora de duracion a la fecha.
   * 
   * @param fecha
   * @return
   */
  public static Date buildDateExpiration(LocalDateTime fecha) {
    return toDate(fecha.plusHours(hoursOfDurationToken));
  }

  public static ModuloResponse transformToModulo(Modulo modulo) {
    return ModuloResponse.builder().name(modulo.getName()).path(modulo.getPath())
        .items(modulo.getItems().stream().map(item -> {
          return new ElementosModuloResponse(item.getKey(), item.getItem(), item.isEstado());
        }).collect(Collectors.toList())).build();
  }

  public static Modulo transformToModulo(AccesoRequest request) {
    return Modulo.builder().name(request.getName()).path(request.getPath())
        .items(request.getItems().stream().map(item -> {
          return new ElementosModulo(item.getKey(), item.getItem(), item.isEstado());
        }).collect(Collectors.toList())).build();
  }

  public static Map<String, Object> buildBodyForToken(Usuario usuario) {
    Map<String, Object> body = new HashMap<String, Object>();
    body.put("id", usuario.getIdUsuario());
    body.put("nombres", usuario.getNombres());
    body.put("apellidos", usuario.getApellidos());
    return body;
  }

  public static Map<String, Object> buildBodyColaboratorForToken(ColaboradorTable colaborador) {
    Map<String, Object> body = new HashMap<String, Object>();
    body.put("id", colaborador.getIdColaborador());
    body.put("nombres", colaborador.getNombres());
    body.put("apellidos", colaborador.getApellidos());
    return body;
  }

  /**
   * Metodo que extrae el nombre del correo
   * 
   * @param correo
   * @return
   */
  public static String extraerNombreUsuario(String correo) {
    return correo.substring(0, correo.indexOf("@"));
  }

  /**
   * 
   * public static void main(String... args) {
   * System.out.println(extraerDominio("castillojose12031996@gmail.com")); . }
   **/

  public static String formatoFecha(LocalDateTime fecha) {
    return fecha.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
  }

  public static String formatoHora(LocalDateTime fecha) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    return fecha.format(formatter);
  }

  public static LocalDateTime convertActualZoneLocalDateTime(LocalDateTime fechaActual) {

    return LocalDateTime.parse(dateZone("America/Lima", fechaActual, "uuuu-MM-dd'T'HH:mm:ss"));
  }

  public static String dateZone(String zoneTime, LocalDateTime fechaActual, String paramDate) {
    TimeZone timeZone = TimeZone.getTimeZone(zoneTime);
    Date fecha = convertToDateViaInstant(fechaActual);
    return fecha.toInstant().atZone(timeZone.toZoneId()).toLocalDateTime()
        .format(DateTimeFormatter.ofPattern(paramDate, new Locale("es", "ES")));
  }

  private static Date convertToDateViaInstant(LocalDateTime dateToConvert) {
    return java.util.Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
  }
}

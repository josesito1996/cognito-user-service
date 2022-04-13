package com.samy.service.cognitoapp.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.samy.service.cognitoapp.model.ColaboradorTable;
import com.samy.service.cognitoapp.model.Usuario;

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

	/**
	 * Metodo que genera el HTML con el mensaje de bienvenida para el usuario.
	 * 
	 * @param usuario
	 * @param jwt
	 * @return
	 */
	public static String messageWelcomeHtmlBuilder(Usuario usuario, String jwt) {
		String urlActivator = "https://79z25zohcj.execute-api.us-east-2.amazonaws.com/dev/token-auth/authenticate?tokenKey="
				+ jwt;
		String htmlText = "<p><strong>Hola : " + usuario.getNombres() + ";</strong></p>\r\n"
				+ "<h4>Bienvenido(a) a Samy.</h4>\r\n"
				+ "<p>Para poder activar tu cuenta de usuario ingresa al siguiente enlace:</p>\r\n"
				+ "<p><a title=\"Samy authenticador\" href=" + urlActivator
				+ " target=\"_blank\">Haz click aqui</a></p>";
		return htmlText;
	}

	public static String messageWelcomeColaboratorHtmlBuilder(ColaboradorTable colaborador, String jwt) {
		String urlActivator = "https://79z25zohcj.execute-api.us-east-2.amazonaws.com/dev/token-auth/authenticateColaborator?tokenKey="
				+ jwt;
		String htmlText = "<p><strong>Hola : " + colaborador.getNombres() + ";</strong></p>\r\n"
				+ "<h4>Bienvenido(a) a Samy.</h4>\r\n" + "<h4>Tu password es : " + colaborador.getPassword()
				+ "</h4>\r\n" + "<p>Para poder activar tu cuenta de usuario ingresa al siguiente enlace:</p>\r\n"
				+ "<p><a title=\"Samy authenticador\" href=" + urlActivator
				+ " target=\"_blank\">Haz click aqui</a></p>";
		return htmlText;
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
	 * System.out.println(extraerDominio("castillojose12031996@gmail.com"));
	 * 
	 * }
	 **/
}

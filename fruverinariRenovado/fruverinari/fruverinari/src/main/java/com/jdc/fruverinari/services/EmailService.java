package com.jdc.fruverinari.services;

import org.springframework.stereotype.Service;

/**
 * Servicio de correo (modo local: solo imprime en consola).
 * Cuando uses PostgreSQL y Gmail, agrega spring-boot-starter-mail al pom.
 */
@Service
public class EmailService {

    public void sendSimpleMessage(String to, String subject, String text) {
        System.out.println("[EMAIL LOCAL] Para: " + to);
        System.out.println("[EMAIL LOCAL] Asunto: " + subject);
        System.out.println("[EMAIL LOCAL] Mensaje: " + text);
    }

    public void sendStockAlert(String productoNombre, int stockActual, int stockMinimo) {
        String subject = "ALERTA DE STOCK BAJO: " + productoNombre;
        String text = String.format(
                "El producto '%s' tiene stock %d (minimo %d).",
                productoNombre, stockActual, stockMinimo
        );
        sendSimpleMessage("admin@local", subject, text);
    }
}

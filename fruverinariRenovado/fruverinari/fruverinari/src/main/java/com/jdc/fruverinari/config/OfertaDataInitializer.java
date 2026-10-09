package com.jdc.fruverinari.config;

import com.jdc.fruverinari.entities.OfertaEntity;
import com.jdc.fruverinari.repository.OfertaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
public class OfertaDataInitializer implements CommandLineRunner {

    private final OfertaRepository ofertaRepository;

    public OfertaDataInitializer(OfertaRepository ofertaRepository) {
        this.ofertaRepository = ofertaRepository;
    }

    @Override
    public void run(String... args) {
        crearSiNoExiste("Descuento fresco 10%",
                "Canjea puntos y recibe 10% de descuento en tu proxima compra en la tienda.",
                40, 10);
        crearSiNoExiste("Descuento 20% en recomendados",
                "20% de descuento en la siguiente compra. Ideal para llevar productos de baja rotacion.",
                80, 20);
        crearSiNoExiste("Bolsa de frutas a elegir",
                "Canjea este premio en caja: una bolsa de frutas de temporada a eleccion.",
                120, 0);
        crearSiNoExiste("Kit verduras de la casa",
                "Premio para recoger en tienda: mix de verduras que necesitan mas rotacion.",
                150, 0);
    }

    private void crearSiNoExiste(String titulo, String descripcion, int puntos, int descuento) {
        if (!ofertaRepository.existsByTitulo(titulo)) {
            OfertaEntity oferta = new OfertaEntity();
            oferta.setTitulo(titulo);
            oferta.setDescripcion(descripcion);
            oferta.setPuntosRequeridos(puntos);
            oferta.setDescuentoPorcentaje(descuento);
            oferta.setActiva(true);
            ofertaRepository.save(oferta);
        }
    }
}

package com.jdc.fruverinari.services;

import com.jdc.fruverinari.entities.PlanNutricionalEntity;
import com.jdc.fruverinari.entities.ProductoEntity;
import com.jdc.fruverinari.model.CatalogoRecetas;
import com.jdc.fruverinari.model.ComidaSugerida;
import com.jdc.fruverinari.model.DiaPlanSemanal;
import com.jdc.fruverinari.model.PlanNutricionalGenerado;
import com.jdc.fruverinari.model.ProductosReceta;
import com.jdc.fruverinari.model.RecetaPlantilla;
import com.jdc.fruverinari.repository.PlanNutricionalRepository;
import com.jdc.fruverinari.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlanNutricionalService {

    private static final String[] DIAS_SEMANA = {
            "Lunes", "Martes", "Miercoles", "Jueves", "Viernes", "Sabado", "Domingo"
    };

    private final ProductoRepository productoRepository;
    private final PlanNutricionalRepository planNutricionalRepository;

    public PlanNutricionalService(ProductoRepository productoRepository,
                                  PlanNutricionalRepository planNutricionalRepository) {
        this.productoRepository = productoRepository;
        this.planNutricionalRepository = planNutricionalRepository;
    }

    public PlanNutricionalGenerado generarPlan(String nombreCliente, String objetivo, String nivelActividad,
                                               String estiloAlimentacion, String preferenciaRecetas,
                                               String intensidad, List<String> restricciones) {
        PlanNutricionalGenerado plan = new PlanNutricionalGenerado();
        plan.setNombreCliente(nombreCliente.trim());
        plan.setObjetivo(objetivo);
        plan.setNivelActividad(nivelActividad);
        plan.setEstiloAlimentacion(estiloAlimentacion);
        plan.setPreferenciaRecetas(preferenciaRecetas);
        plan.setIntensidad(intensidad);
        plan.setRestriccionesTexto(formatearRestricciones(restricciones));
        plan.setObjetivoEtiqueta(etiquetaObjetivo(objetivo));

        InventarioClasificado inv = clasificarInventario();
        plan.setResumen(construirResumen(plan));
        plan.setConsejos(construirConsejos(plan, restricciones));

        int semilla = Math.abs(nombreCliente.hashCode() + objetivo.hashCode());

        armarComidasDelDia(plan, inv, objetivo, estiloAlimentacion, preferenciaRecetas, intensidad, semilla);
        generarPlanSemanal(plan, inv, objetivo, estiloAlimentacion, preferenciaRecetas, semilla);

        Set<ProductoEntity> unicos = new LinkedHashSet<>(plan.getProductosRecomendados());
        plan.getProductosRecomendados().clear();
        plan.getProductosRecomendados().addAll(unicos);

        if (plan.getProductosRecomendados().isEmpty() && !inv.todos().isEmpty()) {
            plan.getProductosRecomendados().addAll(inv.todos().stream().limit(8).toList());
        }

        return plan;
    }

    /** Compatibilidad con llamadas anteriores */
    public PlanNutricionalGenerado generarPlan(String nombreCliente, String objetivo, String nivelActividad) {
        return generarPlan(nombreCliente, objetivo, nivelActividad,
                "CUALQUIERA", "VARIADO", "MODERADO", List.of());
    }

    @Transactional
    public void guardarPlan(PlanNutricionalGenerado plan, String registradoPor) {
        PlanNutricionalEntity entity = new PlanNutricionalEntity();
        entity.setNombreCliente(plan.getNombreCliente());
        entity.setObjetivo(plan.getObjetivoEtiqueta() + " | " + plan.getEstiloAlimentacion()
                + " | " + plan.getPreferenciaRecetas());
        entity.setNivelActividad(plan.getNivelActividad() + " / " + plan.getIntensidad());
        entity.setContenidoPlan(plan.getContenidoParaGuardar());
        entity.setProductosSugeridos(plan.getProductosComoTexto());
        entity.setRegistradoPor(registradoPor);
        planNutricionalRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<PlanNutricionalEntity> obtenerRecientes() {
        return planNutricionalRepository.findTop10ByOrderByFechaCreacionDesc();
    }

    private void armarComidasDelDia(PlanNutricionalGenerado plan, InventarioClasificado inv,
                                    String objetivo, String estilo, String preferencia,
                                    String intensidad, int semilla) {
        agregarReceta(plan, inv, "DESAYUNO", objetivo, estilo, preferencia, semilla);
        agregarReceta(plan, inv, "ALMUERZO", objetivo, estilo, preferencia, semilla + 3);
        agregarReceta(plan, inv, "CENA", objetivo, estilo, preferencia, semilla + 5);

        if ("INTENSO".equals(intensidad) || "MODERADO".equals(intensidad)) {
            agregarReceta(plan, inv, "MERIENDA", objetivo, estilo, preferencia, semilla + 7);
        }
        if ("INTENSO".equals(intensidad)) {
            agregarReceta(plan, inv, "MERIENDA", objetivo, estilo, preferencia, semilla + 11);
        }
    }

    private void generarPlanSemanal(PlanNutricionalGenerado plan, InventarioClasificado inv,
                                    String objetivo, String estilo, String preferencia, int semilla) {
        for (int i = 0; i < DIAS_SEMANA.length; i++) {
            DiaPlanSemanal dia = new DiaPlanSemanal();
            dia.setDia(DIAS_SEMANA[i]);

            RecetaDia des = elegirRecetaCompleta(inv, "DESAYUNO", objetivo, estilo, preferencia, semilla + i);
            RecetaDia alm = elegirRecetaCompleta(inv, "ALMUERZO", objetivo, estilo, preferencia, semilla + i + 10);
            RecetaDia cen = elegirRecetaCompleta(inv, "CENA", objetivo, estilo, preferencia, semilla + i + 20);

            dia.setDesayunoTitulo(des.titulo());
            dia.setDesayuno(des.detalle());
            dia.setAlmuerzoTitulo(alm.titulo());
            dia.setAlmuerzo(alm.detalle());
            dia.setCenaTitulo(cen.titulo());
            dia.setCena(cen.detalle());

            plan.getSemana().add(dia);
        }
    }

    private void agregarReceta(PlanNutricionalGenerado plan, InventarioClasificado inv,
                               String momento, String objetivo, String estilo,
                               String preferencia, int semilla) {
        RecetaDia receta = elegirRecetaCompleta(inv, momento, objetivo, estilo, preferencia, semilla);
        String momentoLabel = etiquetaMomento(momento);
        plan.getComidas().add(new ComidaSugerida(
                momentoLabel, receta.titulo(), receta.detalle(), receta.productos()));
        plan.getProductosRecomendados().addAll(receta.productosEntidades());
    }

    private RecetaDia elegirRecetaCompleta(InventarioClasificado inv, String momento,
                                           String objetivo, String estilo, String preferencia, int semilla) {
        ProductosReceta productos = inv.productosParaSemilla(semilla);
        RecetaPlantilla plantilla = CatalogoRecetas.elegir(momento, objetivo, estilo, preferencia, semilla);
        String detalle = plantilla.aplicar(productos);
        List<ProductoEntity> usados = inv.resolver(productos);
        return new RecetaDia(plantilla.getTitulo(), detalle, productos.productosTexto(), usados);
    }

    private String construirResumen(PlanNutricionalGenerado plan) {
        return "Plan personalizado para " + plan.getNombreCliente()
                + " con objetivo de " + plan.getObjetivoEtiqueta().toLowerCase(Locale.ROOT)
                + ", estilo " + etiquetaEstilo(plan.getEstiloAlimentacion())
                + ", recetas " + etiquetaPreferencia(plan.getPreferenciaRecetas()).toLowerCase(Locale.ROOT)
                + " e intensidad " + plan.getIntensidad().toLowerCase(Locale.ROOT) + ".";
    }

    private String construirConsejos(PlanNutricionalGenerado plan, List<String> restricciones) {
        StringBuilder sb = new StringBuilder();
        sb.append(switch (plan.getObjetivo()) {
            case "PERDER_PESO" -> "Prioriza porciones moderadas, mucha agua y cocina al vapor o a la plancha.";
            case "GANAR_MUSCULO", "DEPORTISTA" -> "Combina estas recetas con proteina magra y hidratacion constante.";
            case "DETOX", "DIGESTION" -> "Evita frituras y ultraprocesados; prefiere alimentos frescos del fruver.";
            case "DIABETES" -> "Distribuye frutas en porciones controladas y evita jugos azucarados.";
            case "VEGANO", "VEGETARIANO" -> "Asegura fuente de proteina vegetal (legumbres, tofu, quinoa).";
            case "NINOS" -> "Presenta colores variados y porciones adaptadas a la edad del nino.";
            case "ADULTO_MAYOR" -> "Cocina bien las verduras y usa texturas suaves para facilitar la digestion.";
            default -> "Come con horarios regulares y varia frutas y verduras de temporada cada semana.";
        });
        if (restricciones != null && !restricciones.isEmpty()) {
            sb.append(" Restricciones consideradas: ").append(formatearRestricciones(restricciones)).append(".");
        }
        return sb.toString();
    }

    private InventarioClasificado clasificarInventario() {
        List<ProductoEntity> todos = productoRepository.findAll().stream()
                .filter(p -> p.isActivo() && p.getCantidad() != null && p.getCantidad() > 0)
                .collect(Collectors.toList());

        List<ProductoEntity> frutas = filtrarPorTipo(todos,
                "fruta", "manzana", "banana", "platano", "naranja", "mango", "papaya", "uva",
                "fresa", "melon", "sandia", "pina", "piña", "maracuya", "guayaba", "lulo", "granadilla");
        List<ProductoEntity> verduras = filtrarPorTipo(todos,
                "verdura", "lechuga", "tomate", "zanahoria", "brocoli", "espinaca", "pepino",
                "cebolla", "papa", "col", "apio", "berenjena", "calabacin", "remolacha", "habichuela");
        List<ProductoEntity> hierbas = filtrarPorTipo(todos, "cilantro", "perejil", "albahaca", "menta", "hierba");
        List<ProductoEntity> citricos = filtrarPorTipo(todos, "limon", "lima", "naranja", "mandarina", "citrico");

        return new InventarioClasificado(todos, frutas, verduras, hierbas, citricos);
    }

    private List<ProductoEntity> filtrarPorTipo(List<ProductoEntity> inventario, String... palabras) {
        List<ProductoEntity> resultado = new ArrayList<>();
        for (ProductoEntity p : inventario) {
            String texto = (p.getNombre() + " " + (p.getDescripcion() != null ? p.getDescripcion() : ""))
                    .toLowerCase(Locale.ROOT);
            for (String palabra : palabras) {
                if (texto.contains(palabra)) {
                    resultado.add(p);
                    break;
                }
            }
        }
        return resultado;
    }

    private String formatearRestricciones(List<String> restricciones) {
        if (restricciones == null || restricciones.isEmpty()) {
            return "Ninguna";
        }
        return restricciones.stream()
                .map(r -> switch (r) {
                    case "SIN_GLUTEN" -> "Sin gluten";
                    case "SIN_LACTEOS" -> "Sin lacteos";
                    case "BAJO_SODIO" -> "Bajo sodio";
                    case "SIN_FRUTOS_SECOS" -> "Sin frutos secos";
                    case "SIN_MARISCOS" -> "Sin mariscos";
                    default -> r;
                })
                .collect(Collectors.joining(", "));
    }

    private String etiquetaMomento(String momento) {
        return switch (momento) {
            case "DESAYUNO" -> "Desayuno";
            case "ALMUERZO" -> "Almuerzo";
            case "CENA" -> "Cena";
            case "MERIENDA" -> "Merienda";
            default -> momento;
        };
    }

    private String etiquetaObjetivo(String objetivo) {
        return switch (objetivo) {
            case "PERDER_PESO" -> "Bajar de peso";
            case "GANAR_MUSCULO" -> "Ganar musculo";
            case "ENERGIA" -> "Mas energia";
            case "DETOX" -> "Detox y depuracion";
            case "DIGESTION" -> "Mejorar digestion";
            case "INMUNIDAD" -> "Fortalecer inmunidad";
            case "ANTIESTRES" -> "Reducir estres";
            case "DEPORTISTA" -> "Rendimiento deportivo";
            case "VEGANO" -> "Alimentacion vegana";
            case "VEGETARIANO" -> "Alimentacion vegetariana";
            case "NINOS" -> "Nutricion infantil";
            case "ADULTO_MAYOR" -> "Adulto mayor";
            case "DIABETES" -> "Control glucosa";
            case "ANTIINFLAMATORIO" -> "Antiinflamatorio";
            case "SALUD_GENERAL" -> "Salud general";
            default -> "Mantener peso";
        };
    }

    private String etiquetaEstilo(String estilo) {
        return switch (estilo) {
            case "VEGANO" -> "Vegano";
            case "VEGETARIANO" -> "Vegetariano";
            default -> "Omnivoro";
        };
    }

    private String etiquetaPreferencia(String pref) {
        return switch (pref) {
            case "RAPIDO" -> "Rapidas";
            case "GOURMET" -> "Gourmet";
            case "ECONOMICO" -> "Economicas";
            case "FAMILIAR" -> "Familiares";
            default -> "Variadas";
        };
    }

    private record RecetaDia(String titulo, String detalle, String productos, List<ProductoEntity> productosEntidades) {
    }

    private record InventarioClasificado(
            List<ProductoEntity> todos,
            List<ProductoEntity> frutas,
            List<ProductoEntity> verduras,
            List<ProductoEntity> hierbas,
            List<ProductoEntity> citricos
    ) {
        ProductosReceta productosParaSemilla(int semilla) {
            return new ProductosReceta(
                    nombre(frutas, semilla, "fruta de temporada"),
                    nombre(frutas, semilla + 1, "fruta alterna"),
                    nombre(frutas, semilla + 2, "fruta fresca"),
                    nombre(verduras, semilla, "verdura fresca"),
                    nombre(verduras, semilla + 1, "verdura de hoja"),
                    nombre(verduras, semilla + 2, "verdura mix"),
                    nombre(hierbas, semilla, "hierbas frescas"),
                    nombre(citricos, semilla, "citrico")
            );
        }

        List<ProductoEntity> resolver(ProductosReceta p) {
            List<ProductoEntity> lista = new ArrayList<>();
            buscarPorNombre(lista, p.f1());
            buscarPorNombre(lista, p.f2());
            buscarPorNombre(lista, p.f3());
            buscarPorNombre(lista, p.v1());
            buscarPorNombre(lista, p.v2());
            buscarPorNombre(lista, p.v3());
            return lista.stream().distinct().toList();
        }

        private void buscarPorNombre(List<ProductoEntity> lista, String nombre) {
            todos.stream()
                    .filter(pr -> pr.getNombre().equalsIgnoreCase(nombre))
                    .findFirst()
                    .ifPresent(lista::add);
        }

        private String nombre(List<ProductoEntity> lista, int idx, String fallback) {
            if (lista == null || lista.isEmpty()) {
                return fallback;
            }
            return lista.get(Math.floorMod(idx, lista.size())).getNombre();
        }
    }
}

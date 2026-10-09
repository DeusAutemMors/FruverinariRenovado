package com.jdc.fruverinari.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class CatalogoRecetas {

    private static final Set<String> TODOS_OBJ = Set.of("TODOS");
    private static final Set<String> EST_NORMAL = Set.of("CUALQUIERA", "VEGETARIANO", "VEGANO");
    private static final Set<String> EST_VEG = Set.of("VEGETARIANO", "VEGANO");
    private static final Set<String> EST_VEGAN = Set.of("VEGANO");
    private static final Set<String> PREF_TODAS = Set.of("VARIADO", "RAPIDO", "GOURMET", "ECONOMICO", "FAMILIAR");

    private static final List<RecetaPlantilla> RECETAS = new ArrayList<>();

    static {
        registrarDesayunos();
        registrarAlmuerzos();
        registrarCenas();
        registrarMeriendas();
    }

    private CatalogoRecetas() {
    }

    public static List<RecetaPlantilla> todas() {
        return List.copyOf(RECETAS);
    }

    public static RecetaPlantilla elegir(String momento, String objetivo, String estilo,
                                        String preferencia, int semilla) {
        List<RecetaPlantilla> filtradas = RECETAS.stream()
                .filter(r -> r.getMomento().equals(momento))
                .filter(r -> r.getObjetivos().contains("TODOS") || r.getObjetivos().contains(objetivo))
                .filter(r -> coincideEstilo(r, estilo))
                .filter(r -> r.getPreferencias().contains("VARIADO")
                        || r.getPreferencias().contains(preferencia)
                        || preferencia.equals("VARIADO"))
                .toList();

        if (filtradas.isEmpty()) {
            filtradas = RECETAS.stream().filter(r -> r.getMomento().equals(momento)).toList();
        }
        if (filtradas.isEmpty()) {
            return new RecetaPlantilla("Opcion del dia", "Plato con {f1}, {v1} y {v2}.",
                    momento, TODOS_OBJ, EST_NORMAL, PREF_TODAS, false);
        }
        return filtradas.get(Math.floorMod(semilla, filtradas.size()));
    }

    private static boolean coincideEstilo(RecetaPlantilla r, String estilo) {
        if ("CUALQUIERA".equals(estilo)) {
            return true;
        }
        if ("VEGANO".equals(estilo)) {
            return r.getEstilos().contains("VEGANO");
        }
        if ("VEGETARIANO".equals(estilo)) {
            return r.getEstilos().contains("VEGETARIANO") || r.getEstilos().contains("VEGANO");
        }
        return true;
    }

    private static void r(String titulo, String desc, String momento, Set<String> objs,
                        Set<String> estilos, Set<String> prefs, boolean coccion) {
        RECETAS.add(new RecetaPlantilla(titulo, desc, momento, objs, estilos, prefs, coccion));
    }

    private static Set<String> obj(String... o) {
        return Set.of(o);
    }

    // ===================== DESAYUNOS =====================
    private static void registrarDesayunos() {
        r("Parfait de yogurt y frutas", "Capas de yogurt natural, {f1}, {f2} y granola. Endulzar con miel al gusto.",
                "DESAYUNO", obj("TODOS"), EST_NORMAL, PREF_TODAS, false);
        r("Bowl tropical de papaya", "Cubos de {f1}, {f2}, semillas de chia y zumo de {c}. Servir frio.",
                "DESAYUNO", obj("TODOS", "ENERGIA", "DETOX"), EST_NORMAL, Set.of("VARIADO", "RAPIDO"), false);
        r("Avena overnight con manzana", "Avena remojada 8h con leche vegetal, {f1} rallada y canela. Lista al despertar.",
                "DESAYUNO", obj("GANAR_MUSCULO", "ENERGIA", "MANTENER"), EST_NORMAL, Set.of("RAPIDO", "ECONOMICO"), false);
        r("Smoothie verde energizante", "Licuar {v1}, {f2}, {f1}, agua de coco y jengibre. Textura cremosa.",
                "DESAYUNO", obj("DETOX", "DIGESTION", "INMUNIDAD"), EST_VEG, Set.of("RAPIDO", "VARIADO"), false);
        r("Tostadas de aguacate y tomate", "Pan integral, aguacate machacado, rodajas de {v1} y semillas.",
                "DESAYUNO", obj("SALUD_GENERAL", "MANTENER"), EST_VEG, Set.of("GOURMET", "FAMILIAR"), false);
        r("Huevos revueltos con espinaca", "2 huevos con {v2} salteada, {v1} y pimenton. Proteina de calidad.",
                "DESAYUNO", obj("GANAR_MUSCULO", "DEPORTISTA"), Set.of("CUALQUIERA"), Set.of("FAMILIAR", "GOURMET"), true);
        r("Panqueques de banano", "Pure de {f1}, harina integral, huevo y canela. Sin azucar anadida.",
                "DESAYUNO", obj("NINOS", "ENERGIA"), EST_NORMAL, Set.of("FAMILIAR"), true);
        r("Ensalada de frutas citricas", "Segmentos de {c}, {f1}, menta y un toque de miel.",
                "DESAYUNO", obj("PERDER_PESO", "INMUNIDAD", "DIABETES"), EST_NORMAL, Set.of("RAPIDO", "VARIADO"), false);
        r("Bowl de quinoa caliente", "Quinoa cocida con {f1}, {v2} al vapor y nueces.",
                "DESAYUNO", obj("VEGANO", "VEGETARIANO", "GANAR_MUSCULO"), EST_VEGAN, Set.of("GOURMET"), true);
        r("Wrap integral de verduras", "Tortilla integral, hummus, {v1}, {v2} y zanahoria rallada.",
                "DESAYUNO", obj("PERDER_PESO", "SALUD_GENERAL"), EST_VEG, Set.of("RAPIDO"), false);
        r("Chia pudding de mango", "Semillas de chia, leche de almendras, cubos de {f1} y coco rallado.",
                "DESAYUNO", obj("VEGANO", "DETOX"), EST_VEGAN, Set.of("GOURMET", "VARIADO"), false);
        r("Desayuno andino", "Papa cocida, huevo, {v1} en rodajas y ají suave al lado.",
                "DESAYUNO", obj("DEPORTISTA", "GANAR_MUSCULO", "MANTENER"), EST_NORMAL, Set.of("FAMILIAR", "ECONOMICO"), true);
        r("Batido proteico de frutos rojos", "Licuado de {f1}, {f2}, proteina en polvo y agua. Post-entreno ideal.",
                "DESAYUNO", obj("GANAR_MUSCULO", "DEPORTISTA"), EST_NORMAL, Set.of("RAPIDO"), false);
        r("Arepa con aguacate", "Arepa integral, aguacate, rodajas de {f1} y queso fresco (opcional).",
                "DESAYUNO", obj("SALUD_GENERAL", "ENERGIA"), EST_NORMAL, Set.of("FAMILIAR", "ECONOMICO"), true);
        r("Porridge de avena y pera", "Avena cocida con canela, {f1} pochada y almendras laminadas.",
                "DESAYUNO", obj("ADULTO_MAYOR", "DIGESTION"), EST_NORMAL, Set.of("FAMILIAR"), true);
        r("Tostada de hummus y pepino", "Base de hummus casero, {v2} en laminas y {h} fresca.",
                "DESAYUNO", obj("VEGANO", "ANTIINFLAMATORIO"), EST_VEGAN, Set.of("RAPIDO", "ECONOMICO"), false);
    }

    // ===================== ALMUERZOS =====================
    private static void registrarAlmuerzos() {
        r("Ensalada mediterranea completa", "Base de {v1}, {v2}, {v3}, aceite de oliva, limon y garbanzos.",
                "ALMUERZO", obj("TODOS"), EST_VEG, PREF_TODAS, false);
        r("Bowl de pollo y vegetales", "Pechuga a la plancha, {v1}, {v2} salteadas y arroz integral.",
                "ALMUERZO", obj("GANAR_MUSCULO", "DEPORTISTA", "PERDER_PESO"), EST_NORMAL, Set.of("FAMILIAR", "GOURMET"), true);
        r("Sopa de verduras casera", "Caldo ligero con {v1}, {v2}, {v3}, apio y hierbas. Reconfortante.",
                "ALMUERZO", obj("DETOX", "DIGESTION", "INMUNIDAD", "ADULTO_MAYOR"), EST_VEG, Set.of("ECONOMICO", "FAMILIAR"), true);
        r("Poke bowl de atun", "Arroz, atun fresco, {v1}, {v2}, edamame y salsa de soya baja en sodio.",
                "ALMUERZO", obj("GANAR_MUSCULO", "DEPORTISTA"), EST_NORMAL, Set.of("GOURMET"), false);
        r("Lasaña de verduras", "Capas de pasta integral, {v1}, {v2}, ricotta y salsa de tomate casera.",
                "ALMUERZO", obj("SALUD_GENERAL", "MANTENER", "NINOS"), EST_VEG, Set.of("FAMILIAR", "GOURMET"), true);
        r("Ceviche de pescado blanco", "Pescado marinado en limon, {v1}, cebolla morada y cilantro.",
                "ALMUERZO", obj("PERDER_PESO", "DEPORTISTA"), EST_NORMAL, Set.of("GOURMET"), false);
        r("Buddha bowl vegano", "Quinoa, {v1} asada, {v2}, {f1}, tahini y semillas de girasol.",
                "ALMUERZO", obj("VEGANO", "VEGETARIANO", "DETOX"), EST_VEGAN, Set.of("GOURMET", "VARIADO"), true);
        r("Fajitas de pollo", "Tiras de pollo, pimientos, {v1}, {v2} en tortilla integral.",
                "ALMUERZO", obj("GANAR_MUSCULO", "ENERGIA", "NINOS"), EST_NORMAL, Set.of("FAMILIAR"), true);
        r("Tabule de quinoa", "Quinoa, perejil, menta, {v1}, {v2} y aderezo de limon.",
                "ALMUERZO", obj("VEGETARIANO", "ANTIINFLAMATORIO"), EST_VEG, Set.of("RAPIDO", "GOURMET"), false);
        r("Arroz salteado oriental", "Arroz integral, {v1}, {v2}, brocoli y salsa tamari.",
                "ALMUERZO", obj("ENERGIA", "MANTENER"), EST_VEG, Set.of("FAMILIAR", "ECONOMICO"), true);
        r("Ensalada Cesar ligera", "{v1} romana, pollo grillado, croutons integrales y parmesano.",
                "ALMUERZO", obj("PERDER_PESO", "DEPORTISTA"), EST_NORMAL, Set.of("GOURMET"), false);
        r("Curry de garbanzos", "Garbanzos, leche de coco, {v1}, {v2} y especias suaves.",
                "ALMUERZO", obj("VEGANO", "VEGETARIANO", "DIABETES"), EST_VEGAN, Set.of("FAMILIAR", "ECONOMICO"), true);
        r("Pasta primavera", "Pasta integral, {v1}, {v2}, {v3} salteados en ajo y oliva.",
                "ALMUERZO", obj("SALUD_GENERAL", "NINOS", "ENERGIA"), EST_VEG, Set.of("FAMILIAR"), true);
        r("Hamburguesa de lentejas", "Hamburguesa casera de lentejas, {v1}, aguacate y pan integral.",
                "ALMUERZO", obj("VEGANO", "VEGETARIANO"), EST_VEGAN, Set.of("FAMILIAR", "GOURMET"), true);
        r("Wok de tofu y verduras", "Tofu dorado, {v1}, {v2}, jengibre y salsa de ostras vegetal.",
                "ALMUERZO", obj("VEGANO", "GANAR_MUSCULO"), EST_VEGAN, Set.of("GOURMET"), true);
        r("Sancocho ligero de verduras", "Caldo con {v1}, {v2}, {v3}, platano verde y cilantro.",
                "ALMUERZO", obj("DIGESTION", "ADULTO_MAYOR", "FAMILIAR"), EST_NORMAL, Set.of("FAMILIAR", "ECONOMICO"), true);
    }

    // ===================== CENAS =====================
    private static void registrarCenas() {
        r("Ensalada verde digestiva", "{v1}, {v2}, {v3}, pepino y aderezo de yogur con limon.",
                "CENA", obj("PERDER_PESO", "DIGESTION", "DETOX"), EST_NORMAL, PREF_TODAS, false);
        r("Salmon al horno con esparragos", "Filete de salmon, {v2} al horno, limon y eneldo.",
                "CENA", obj("GANAR_MUSCULO", "ANTIINFLAMATORIO"), EST_NORMAL, Set.of("GOURMET"), true);
        r("Crema de calabacin", "Calabacin, {v1}, cebolla y caldo vegetal. Textura aterciopelada.",
                "CENA", obj("DETOX", "DIGESTION", "ADULTO_MAYOR"), EST_VEG, Set.of("ECONOMICO", "FAMILIAR"), true);
        r("Tacos de pescado", "Tortillas de maiz, pescado grillado, {v1} y salsa de aguacate.",
                "CENA", obj("SALUD_GENERAL", "MANTENER"), EST_NORMAL, Set.of("FAMILIAR"), true);
        r("Risotto de champiñones", "Arroz arborio, champiñones, {v2} y caldo vegetal. Cremoso.",
                "CENA", obj("VEGETARIANO", "SALUD_GENERAL"), EST_VEG, Set.of("GOURMET"), true);
        r("Revuelto de huevos y champiñones", "Huevos, {v2}, {v1} y cebolla. Cena rapida nutritiva.",
                "CENA", obj("PERDER_PESO", "GANAR_MUSCULO"), EST_NORMAL, Set.of("RAPIDO", "ECONOMICO"), true);
        r("Brochetas de verduras grilladas", "Brochetas con {v1}, {v2}, {v3} y marinada de hierbas.",
                "CENA", obj("VEGANO", "VEGETARIANO", "DETOX"), EST_VEGAN, Set.of("GOURMET", "VARIADO"), true);
        r("Sopa miso con verduras", "Caldo miso, tofu, {v1}, {v2} y alga wakame.",
                "CENA", obj("DIGESTION", "INMUNIDAD", "ANTIINFLAMATORIO"), EST_VEG, Set.of("RAPIDO"), true);
        r("Pechuga rellena de espinacas", "Pechuga rellena de {v2}, queso y hierbas. Horno 25 min.",
                "CENA", obj("GANAR_MUSCULO", "DEPORTISTA"), EST_NORMAL, Set.of("GOURMET"), true);
        r("Gazpacho andaluz", "Tomate, {v1}, pepino, pimiento y aceite de oliva. Servir frio.",
                "CENA", obj("PERDER_PESO", "DETOX", "ENERGIA"), EST_VEG, Set.of("RAPIDO", "VARIADO"), false);
        r("Quiche de verduras", "Masa integral, huevo, {v1}, {v2} y queso. Porcion moderada.",
                "CENA", obj("SALUD_GENERAL", "MANTENER", "NINOS"), EST_VEG, Set.of("FAMILIAR"), true);
        r("Carpaccio de remolacha", "Remolacha fina, {v1}, nueces, queso de cabra y balsamico.",
                "CENA", obj("VEGETARIANO", "GOURMET"), EST_VEG, Set.of("GOURMET"), false);
        r("Ensalada de atun y garbanzos", "Atun, garbanzos, {v1}, {v2} y vinagreta de mostaza.",
                "CENA", obj("PERDER_PESO", "DEPORTISTA"), EST_NORMAL, Set.of("RAPIDO"), false);
        r("Verduras al wok con tempeh", "Tempeh, {v1}, {v2}, salsa de soya y sesamo.",
                "CENA", obj("VEGANO", "GANAR_MUSCULO"), EST_VEGAN, Set.of("GOURMET"), true);
        r("Caldo de pollo con verduras", "Caldo casero, pollo desmenuzado, {v1}, {v2} y zanahoria.",
                "CENA", obj("INMUNIDAD", "ADULTO_MAYOR", "DIGESTION"), EST_NORMAL, Set.of("FAMILIAR", "ECONOMICO"), true);
        r("Portobellos rellenos", "Hongos portobello rellenos de {v2}, tomate y queso.",
                "CENA", obj("VEGETARIANO", "MANTENER"), EST_VEG, Set.of("GOURMET", "FAMILIAR"), true);
    }

    // ===================== MERIENDAS =====================
    private static void registrarMeriendas() {
        r("Mix de frutos secos y fruta", "Puñado de almendras con rodajas de {f1}.",
                "MERIENDA", obj("TODOS"), EST_NORMAL, PREF_TODAS, false);
        r("Palitos de apio con hummus", "{v3} con hummus de garbanzo y pimenton.",
                "MERIENDA", obj("PERDER_PESO", "VEGANO"), EST_VEG, Set.of("RAPIDO"), false);
        r("Smoothie de proteina", "{f1}, {f2}, leche y scoop de proteina.",
                "MERIENDA", obj("GANAR_MUSCULO", "DEPORTISTA"), EST_NORMAL, Set.of("RAPIDO"), false);
        r("Yogurt con granola casera", "Yogurt griego, granola, {f1} y miel.",
                "MERIENDA", obj("SALUD_GENERAL", "ENERGIA", "NINOS"), EST_NORMAL, Set.of("FAMILIAR"), false);
        r("Rodajas de manzana con mantequilla de mani", "{f1} con 1 cda de mantequilla de mani.",
                "MERIENDA", obj("MANTENER", "DEPORTISTA"), EST_NORMAL, Set.of("RAPIDO", "ECONOMICO"), false);
        r("Batido verde ligero", "{v1}, {v2}, {f1} y agua de coco.",
                "MERIENDA", obj("DETOX", "DIGESTION"), EST_VEG, Set.of("RAPIDO"), false);
        r("Gelatina de frutas naturales", "Gelatina sin azucar con trozos de {f1} y {f2}.",
                "MERIENDA", obj("NINOS", "DIABETES"), EST_NORMAL, Set.of("FAMILIAR"), false);
        r("Edamame salteado", "Vainas de edamame con sal marina y limon.",
                "MERIENDA", obj("VEGANO", "VEGETARIANO"), EST_VEGAN, Set.of("RAPIDO"), true);
        r("Infusion y fruta de temporada", "Te de {h} con porcion de {f1}.",
                "MERIENDA", obj("PERDER_PESO", "ANTIESTRES", "ADULTO_MAYOR"), EST_NORMAL, Set.of("ECONOMICO"), false);
        r("Rollitos de jamón y queso", "Jamón bajo en grasa, queso, {v1} enrollado.",
                "MERIENDA", obj("GANAR_MUSCULO"), EST_NORMAL, Set.of("RAPIDO"), false);
    }
}

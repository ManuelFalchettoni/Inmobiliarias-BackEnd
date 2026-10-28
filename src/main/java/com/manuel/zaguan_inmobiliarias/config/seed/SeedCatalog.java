package com.manuel.zaguan_inmobiliarias.config.seed;

import java.util.List;

//Datos fijos que usa el DataSeeder para generar propiedades: zonas reales (con coordenadas
//aproximadas, para que el mapa muestre puntos con sentido) y textos del CRM
public final class SeedCatalog {

    private SeedCatalog() {
    }

    //usdPerM2: precio de venta de referencia de la zona, el seeder lo ajusta por tipo y estado.
    //spread: cuanto se pueden alejar las coordenadas del centro (en grados, 0.01 ~ 1 km)
    public record Zone(String province, String county, String city, double latitude, double longitude,
                       double spread, int usdPerM2, List<String> streets) {
    }

    //Zaguan esta en CABA: mismas province/city que los datos fijos ("CABA" / "Buenos Aires")
    public static final List<Zone> CABA = List.of(
            new Zone("CABA", null, "Buenos Aires", -34.5620, -58.4560, 0.008, 2900,
                    List.of("Av. Cabildo", "Juramento", "Mendoza", "Olazabal", "Echeverria", "Vuelta de Obligado")),
            new Zone("CABA", null, "Buenos Aires", -34.5830, -58.4250, 0.010, 3100,
                    List.of("Av. Santa Fe", "Guemes", "Charcas", "Gorriti", "Thames", "Malabia")),
            new Zone("CABA", null, "Buenos Aires", -34.5450, -58.4630, 0.007, 2600,
                    List.of("Av. del Libertador", "Crisologo Larralde", "Manuela Pedraza", "Grecia", "Arribenos")),
            new Zone("CABA", null, "Buenos Aires", -34.6190, -58.4420, 0.009, 2200,
                    List.of("Av. Rivadavia", "Av. Pedro Goyena", "Rosario", "Hidalgo", "Senillosa", "Acoyte")),
            new Zone("CABA", null, "Buenos Aires", -34.5730, -58.4870, 0.008, 2400,
                    List.of("Av. Triunvirato", "Monroe", "Bauness", "Mendoza", "Pedro Ignacio Rivera")),
            new Zone("CABA", null, "Buenos Aires", -34.5880, -58.3970, 0.006, 3300,
                    List.of("Ayacucho", "Junin", "Arenales", "Pena", "Vicente Lopez", "Av. Callao")),
            new Zone("Buenos Aires", "Vicente Lopez", "Olivos", -34.5080, -58.4890, 0.010, 2500,
                    List.of("Av. Maipu", "Corrientes", "Rawson", "Ugarte", "Juan B. Alberdi"))
    );

    //Del Sur esta en Mar del Plata
    public static final List<Zone> MAR_DEL_PLATA = List.of(
            new Zone("Buenos Aires", "General Pueyrredon", "Mar del Plata", -37.9990, -57.5480, 0.008, 1700,
                    List.of("Av. Colon", "Guemes", "San Martin", "Cordoba", "Santiago del Estero", "Moreno")),
            new Zone("Buenos Aires", "General Pueyrredon", "Mar del Plata", -37.9880, -57.5480, 0.006, 1600,
                    List.of("Av. Patricio Peralta Ramos", "Rawson", "Falucho", "Bolivar", "Brown")),
            new Zone("Buenos Aires", "General Pueyrredon", "Mar del Plata", -38.0300, -57.5350, 0.007, 2100,
                    List.of("Alem", "Almafuerte", "Viamonte", "Saavedra", "Matheu")),
            new Zone("Buenos Aires", "General Pueyrredon", "Mar del Plata", -38.0260, -57.5600, 0.006, 1900,
                    List.of("Lamadrid", "Tucuman", "Dorrego", "Rodriguez Pena", "Garay")),
            new Zone("Buenos Aires", "General Pueyrredon", "Sierra de los Padres", -37.9500, -57.7800, 0.015, 600,
                    List.of("Los Cardos", "Las Lilas", "Del Lago", "Los Aromos"))
    );

    public static final List<String> CALL_NOTES = List.of(
            "Consulta por WhatsApp, pide mas fotos",
            "Llamo por el aviso, quiere saber expensas",
            "Pregunta si acepta credito hipotecario",
            "Consulta desde el portal, pide visita",
            "Pregunta si se puede ingresar con mascotas",
            "Quiere saber si la propiedad sigue disponible"
    );

    public static final List<String> VISIT_NOTES = List.of(
            "Le gusto la luz, duda por el tamaño de la cocina",
            "Vino con la pareja, van a pensarlo",
            "Pregunta por el estado de las cañerias",
            "Le encanto, quiere hacer una oferta",
            "Le parecio cara para la zona",
            "Pidio volver a verla un dia de semana"
    );

    public static final List<String> DISCARD_NOTES = List.of(
            "No le sirve, queda lejos del trabajo",
            "Consiguio otra propiedad",
            "Se le cayo el credito",
            "Busca algo mas grande",
            "No contesta los mensajes"
    );

    public static final List<String> ALERTS = List.of(
            "Llamar para coordinar visita",
            "Mandar fotos y plano",
            "Responder contraoferta",
            "Pedir documentacion del interesado",
            "Confirmar visita del sabado",
            "Avisar al dueño de la oferta",
            "Seguimiento: no respondio el ultimo mensaje"
    );
}

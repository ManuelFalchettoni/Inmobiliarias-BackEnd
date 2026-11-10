package com.manuel.zaguan_inmobiliarias.config;

import com.manuel.zaguan_inmobiliarias.entity.agency.Agency;
import com.manuel.zaguan_inmobiliarias.entity.contractparty.ContractParty;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmHistory;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.entity.crm.Offer;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.entity.property.contract.PropertyContract;
import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import com.manuel.zaguan_inmobiliarias.entity.user.User;
import com.manuel.zaguan_inmobiliarias.enums.Currency;
import com.manuel.zaguan_inmobiliarias.enums.agency.AgencyStatus;
import com.manuel.zaguan_inmobiliarias.enums.contractparty.ContractRole;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmAlertStatus;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmEventType;
import com.manuel.zaguan_inmobiliarias.enums.crm.CrmStage;
import com.manuel.zaguan_inmobiliarias.enums.crm.OfferStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyCondition;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyOccupancy;
import com.manuel.zaguan_inmobiliarias.enums.property.PropertyType;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractStatus;
import com.manuel.zaguan_inmobiliarias.enums.property.contract.ContractType;
import com.manuel.zaguan_inmobiliarias.enums.user.UserRol;
import com.manuel.zaguan_inmobiliarias.repository.agency.JpaAgencyRepository;
import com.manuel.zaguan_inmobiliarias.repository.contractparty.JpaContractPartyRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmHistoryRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaOfferRepository;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.contract.JpaPropertyContractRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.owner.JpaPropertyOwnerRepository;
import com.manuel.zaguan_inmobiliarias.repository.user.JpaUserRepository;
import com.manuel.zaguan_inmobiliarias.config.seed.SeedCatalog;
import com.manuel.zaguan_inmobiliarias.config.seed.SeedPhotos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

//Llena la base con datos de prueba al arrancar. Se apaga con app.seed.enabled=false
//(por defecto prendido) y corre solo si la tabla de usuarios esta vacia, asi que reiniciar la app
//no duplica nada. Para volver a cargar: docker compose down -v y arrancar de nuevo.
//Tiene dos partes:
// - Datos fijos: usuarios para el login y casos puntuales para probar (siempre iguales).
// - Datos generados: app.seed.properties propiedades y app.seed.people personas mas, con
//   dueños, contratos y CRM. Usan una semilla fija (SEED), asi que en todas las compus salen
//   los mismos nombres, direcciones y precios.
//Las fotos salen de resources/seed-photos (ver SeedPhotos) y se suben a MinIO.
//Todos los usuarios de prueba tienen la contraseña SEED_PASSWORD
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private static final String SEED_PASSWORD = "Password123";
    private static final long SEED = 42L;

    private final JpaAgencyRepository jpaAgencyRepository;
    private final JpaUserRepository jpaUserRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final JpaPropertyOwnerRepository jpaPropertyOwnerRepository;
    private final JpaPropertyContractRepository jpaPropertyContractRepository;
    private final JpaContractPartyRepository jpaContractPartyRepository;
    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final JpaCrmHistoryRepository jpaCrmHistoryRepository;
    private final JpaOfferRepository jpaOfferRepository;
    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final PasswordEncoder passwordEncoder;
    private final SeedPhotos seedPhotos;

    @Value("${app.seed.properties:150}")
    private int generatedProperties;

    @Value("${app.seed.people:200}")
    private int generatedPeople;

    @Value("${app.seed.photos-per-property:4}")
    private int photosPerProperty;

    private final Random random = new Random(SEED);
    private final Faker faker = new Faker(Locale.of("es", "AR"), random);
    private final Set<String> usedDni = new HashSet<>();

    @Override
    @Transactional
    public void run(String... args) {
        if (jpaUserRepository.count() > 0) {
            log.info("DataSeeder: la base ya tiene datos, no se carga nada");
            return;
        }

        // ---------- Inmobiliarias ----------
        Agency zaguan = agency("30-71234567-8", "Zaguan Propiedades SRL", "Zaguan Propiedades",
                "contacto@zaguan.test", "1145678901", "Av. Cabildo 2040, CABA",
                "https://zaguan.test", "@zaguanprop", AgencyStatus.VERIFY);
        Agency delSur = agency("30-79876543-2", "Del Sur Inmuebles SA", "Del Sur Inmuebles",
                "info@delsur.test", "2234567890", "Av. Colon 1500, Mar del Plata",
                null, "@delsurinmuebles", AgencyStatus.VERIFY);
        Agency nueva = agency("30-70000111-5", "Nueva Casa SAS", "Nueva Casa",
                "hola@nuevacasa.test", "3514445566", "Bv. San Juan 300, Cordoba",
                null, null, AgencyStatus.PENDING);

        // ---------- Usuarios ----------
        user("Admin", "admin@zaguan.test", "1100000001", null, UserRol.ADMIN, null, null);
        User zaguanOwner = user("Laura Gomez", "laura@zaguan.test", "1100000002", zaguan.getId(),
                UserRol.AGENCY, "27-28111222-3", "CUCICBA 1234");
        User zaguanAgent = user("Martin Perez", "martin@zaguan.test", "1100000003", zaguan.getId(),
                UserRol.AGENT, "20-30111222-4", "CUCICBA 5678");
        User delSurOwner = user("Sofia Rios", "sofia@delsur.test", "2230000004", delSur.getId(),
                UserRol.AGENCY, "27-29333444-5", "CMCPMDP 910");
        User delSurAgent = user("Diego Sosa", "diego@delsur.test", "2230000005", delSur.getId(),
                UserRol.AGENT, null, null);
        user("Pablo Diaz", "pablo@nuevacasa.test", "3510000006", nueva.getId(), UserRol.AGENCY, null, null);
        user("Ana Lopez", "ana@mail.test", "1100000007", null, UserRol.USER, null, null);

        // ---------- Personas ----------
        People carlos = people("Carlos Fernandez", "1155550001", "carlos.f@mail.test",
                "Av. Santa Fe 3200, CABA", "20111222", "20-20111222-1", zaguan.getId());
        People marta = people("Marta Suarez", "1155550002", "marta.s@mail.test",
                "Ciudad de la Paz 1800, CABA", "18333444", null, zaguan.getId());
        People julian = people("Julian Acosta", "1155550003", "julian.a@mail.test",
                null, "35555666", null, zaguan.getId());
        People lucia = people("Lucia Herrera", "1155550004", "lucia.h@mail.test",
                null, null, null, zaguan.getId());
        People roberto = people("Roberto Molina", "2235550005", "roberto.m@mail.test",
                "Guemes 2900, Mar del Plata", "16777888", "20-16777888-9", delSur.getId());
        People valeria = people("Valeria Castro", "2235550006", "valeria.c@mail.test",
                null, "38999000", null, delSur.getId());

        // ---------- Propiedades y precios ----------
        Property depto = property("Av. Cabildo 1850 4B", PropertyType.APARTMENT, "CABA", null, "Buenos Aires",
                -34.5612, -58.4567, zaguan.getId(), 2010, 3, 75, PropertyCondition.GOOD,
                PropertyOccupancy.OWNER_OCCUPIED, 4);
        price(depto, OperationType.SALE, Currency.USD, "145000");
        jpaPropertyRepository.save(depto);

        Property ph = property("Mendoza 2500", PropertyType.PH, "CABA", null, "Buenos Aires",
                -34.5601, -58.4589, zaguan.getId(), 1965, 4, 110, PropertyCondition.NEEDS_REPAIR,
                PropertyOccupancy.VACANT, 0);
        price(ph, OperationType.SALE, Currency.USD, "180000");
        price(ph, OperationType.RENT, Currency.ARS, "850000");
        jpaPropertyRepository.save(ph);

        Property oficina = property("Lavalle 1200 piso 8", PropertyType.OFFICE, "CABA", null, "Buenos Aires",
                -34.6025, -58.3850, zaguan.getId(), 1980, 2, 60, PropertyCondition.GOOD,
                PropertyOccupancy.TENANT_OCCUPIED, 8);
        price(oficina, OperationType.RENT, Currency.ARS, "650000");
        jpaPropertyRepository.save(oficina);

        Property casa = property("Alem 3400", PropertyType.HOUSE, "Buenos Aires", "General Pueyrredon",
                "Mar del Plata", -38.0180, -57.5300, delSur.getId(), 1998, 5, 220, PropertyCondition.GOOD,
                PropertyOccupancy.VACANT, null);
        price(casa, OperationType.SALE, Currency.USD, "260000");
        jpaPropertyRepository.save(casa);

        Property lote = property("Ruta 11 km 520", PropertyType.LAND, "Buenos Aires", "General Pueyrredon",
                "Mar del Plata", -37.9300, -57.5350, delSur.getId(), null, 0, 600, PropertyCondition.BRAND_NEW,
                PropertyOccupancy.VACANT, null);
        price(lote, OperationType.SALE, Currency.USD, "45000");
        jpaPropertyRepository.save(lote);

        //Una dada de baja, para probar el restore
        Property baja = property("Juramento 2100", PropertyType.GARAGE, "CABA", null, "Buenos Aires",
                -34.5608, -58.4542, zaguan.getId(), 1990, 0, 12, PropertyCondition.GOOD,
                PropertyOccupancy.VACANT, -1);
        baja.setActive(false);
        jpaPropertyRepository.save(baja);

        // ---------- Dueños ----------
        owner(depto, carlos, "Titular unico");
        owner(ph, marta, null);
        owner(oficina, carlos, null);
        owner(casa, roberto, "Heredero, tiene poder de la hermana");
        owner(lote, roberto, null);

        // ---------- Contratos ----------
        //Fechas relativas a hoy, asi el activo sigue vigente y el terminado sigue vencido sin importar cuando se cargue
        LocalDate thisMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate oficinaStart = thisMonth.minusMonths(20);
        PropertyContract alquilerOficina = contract(oficina, ContractType.RENT, ContractStatus.ACTIVE,
                Currency.ARS, "600000", oficinaStart, oficinaStart.plusYears(3).minusDays(1));
        party(alquilerOficina, carlos, ContractRole.OWNER, null);
        party(alquilerOficina, julian, ContractRole.TENANT, null);
        party(alquilerOficina, marta, ContractRole.GUARANTOR, "Garantia propietaria");

        PropertyContract alquilerCasa = contract(casa, ContractType.RENT, ContractStatus.FINISHED,
                Currency.ARS, "300000", thisMonth.minusYears(5), thisMonth.minusYears(2).minusDays(1));
        party(alquilerCasa, roberto, ContractRole.OWNER, null);
        party(alquilerCasa, valeria, ContractRole.TENANT, null);

        // ---------- CRM ----------
        CrmProperty leadDepto = crm(depto, lucia, zaguanAgent, CrmStage.NEGOTIATING);
        history(leadDepto, zaguanAgent, CrmEventType.CALL, "Consulta por WhatsApp, pide visita");
        history(leadDepto, zaguanAgent, CrmEventType.VISIT, "Le gusto, pregunta por expensas");
        history(leadDepto, zaguanOwner, CrmEventType.OFFER, "Ofrece USD 135.000");
        history(leadDepto, zaguanAgent, CrmEventType.STAGE_CHANGE, "VISITED -> NEGOTIATING");
        offer(leadDepto, "135000", Currency.USD, OfferStatus.REJECTED);
        offer(leadDepto, "140000", Currency.USD, OfferStatus.PENDING);
        alert(leadDepto, zaguanAgent, "Responder contraoferta de Lucia", LocalDateTime.now().plusDays(1), CrmAlertStatus.PENDING);

        CrmProperty leadPh = crm(ph, julian, zaguanAgent, CrmStage.CONTACTED);
        history(leadPh, zaguanAgent, CrmEventType.NOTE, "Busca PH con patio para alquilar");
        alert(leadPh, zaguanAgent, "Coordinar visita al PH", LocalDateTime.now().plusDays(3), CrmAlertStatus.PENDING);
        alert(leadPh, zaguanAgent, "Mandar fotos del patio", LocalDateTime.now().minusDays(1), CrmAlertStatus.PENDING);

        CrmProperty leadCasa = crm(casa, valeria, delSurAgent, CrmStage.CLOSED);
        history(leadCasa, delSurAgent, CrmEventType.VISIT, null);
        history(leadCasa, delSurOwner, CrmEventType.STAGE_CHANGE, "NEGOTIATING -> CLOSED");
        offer(leadCasa, "250000", Currency.USD, OfferStatus.ACCEPTED);
        alert(leadCasa, delSurOwner, "Pedir documentacion para escritura", LocalDateTime.now().minusDays(2), CrmAlertStatus.DONE);

        CrmProperty leadLote = crm(lote, valeria, delSurAgent, CrmStage.DISCARDED);
        history(leadLote, delSurAgent, CrmEventType.NOTE, "No le sirve, queda lejos");

        crm(depto, julian, zaguanOwner, CrmStage.NEW);

        // ---------- Fotos de los datos fijos ----------
        for (Property property : List.of(depto, ph, oficina, casa, lote)) {
            seedPhotos.attach(property, photosPerProperty, random);
        }

        // ---------- Datos generados ----------
        //Zaguan se lleva el 60% y Del Sur el 40%. Nueva Casa queda vacia: esta PENDING
        int zaguanProperties = generatedProperties * 6 / 10;
        int zaguanPeople = generatedPeople * 6 / 10;
        generate(zaguan, List.of(zaguanOwner, zaguanAgent), SeedCatalog.CABA, "11",
                zaguanProperties, zaguanPeople);
        generate(delSur, List.of(delSurOwner, delSurAgent), SeedCatalog.MAR_DEL_PLATA, "223",
                generatedProperties - zaguanProperties, generatedPeople - zaguanPeople);

        log.info("DataSeeder: datos de prueba cargados");
    }

    //Genera personas y propiedades de una inmobiliaria, con sus dueños, contratos y leads del CRM.
    //staff: los usuarios que atienden los leads
    private void generate(Agency agency, List<User> staff, List<SeedCatalog.Zone> zones, String areaCode,
                          int propertyCount, int peopleCount) {
        if (propertyCount <= 0) {
            return;
        }
        List<People> people = new ArrayList<>();
        for (int i = 0; i < Math.max(peopleCount, 2); i++) {
            people.add(randomPeople(agency, areaCode));
        }

        for (int i = 0; i < propertyCount; i++) {
            SeedCatalog.Zone zone = pick(zones);
            Property property = randomProperty(agency, zone);
            jpaPropertyRepository.save(property);
            seedPhotos.attach(property, photosPerProperty, random);

            //Dueños: uno, a veces dos. Los interesados del CRM no pueden ser los dueños
            List<People> owners = new ArrayList<>();
            owners.add(pick(people));
            if (chance(15)) {
                People coOwner = pick(people);
                if (!owners.contains(coOwner)) {
                    owners.add(coOwner);
                }
            }
            for (People owner : owners) {
                owner(property, owner, null);
            }
            List<People> others = people.stream().filter(p -> !owners.contains(p)).toList();

            randomContracts(property, owners, others);
            if (property.getActive() && chance(60)) {
                randomLeads(property, others, staff);
            }
        }
    }

    private People randomPeople(Agency agency, String areaCode) {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String name = firstName + " " + lastName;
        String email = slug(firstName) + "." + slug(lastName) + random.nextInt(1000) + "@mail.test";
        String phone = areaCode + digits(10 - areaCode.length());

        //Los interesados muchas veces no dejan DNI ni direccion
        String dni = chance(70) ? uniqueDni() : null;
        String cuit = dni != null && chance(40) ? cuitOf(dni) : null;
        String address = chance(50) ? faker.address().streetName() + " " + (100 + random.nextInt(4900)) : null;
        return people(name, phone, email, address, dni, cuit, agency.getId());
    }

    private Property randomProperty(Agency agency, SeedCatalog.Zone zone) {
        PropertyType type = randomType(zone);
        String street = pick(zone.streets());
        String address = street + " " + (100 + random.nextInt(5800));

        //rooms, size y piso segun el tipo
        int rooms;
        int size;
        Integer floor = null;
        switch (type) {
            case APARTMENT -> {
                rooms = 1 + random.nextInt(4);
                size = 25 + rooms * (16 + random.nextInt(12));
                floor = random.nextInt(16);
                address += floor == 0 ? " PB " + letter() : " " + floor + letter();
            }
            case HOUSE -> {
                rooms = 3 + random.nextInt(5);
                size = 80 + rooms * (20 + random.nextInt(25));
            }
            case PH -> {
                rooms = 2 + random.nextInt(4);
                size = 45 + rooms * (15 + random.nextInt(12));
                floor = random.nextInt(3);
            }
            case OFFICE -> {
                rooms = 1 + random.nextInt(5);
                size = 25 + rooms * (15 + random.nextInt(25));
                floor = 1 + random.nextInt(20);
                address += " piso " + floor;
            }
            case COMMERCIAL_SPACE -> {
                rooms = 1 + random.nextInt(2);
                size = 30 + random.nextInt(220);
                floor = 0;
            }
            case WAREHOUSE -> {
                rooms = 1 + random.nextInt(2);
                size = 200 + random.nextInt(1300);
                floor = 0;
            }
            case GARAGE -> {
                rooms = 0;
                size = 12 + random.nextInt(4);
                floor = -random.nextInt(3);
            }
            case LAND -> {
                rooms = 0;
                size = 200 + random.nextInt(1800);
            }
            default -> { //FARM
                rooms = 3 + random.nextInt(4);
                size = 5000 + random.nextInt(45000);
            }
        }

        Integer year = type == PropertyType.LAND ? null : 1940 + random.nextInt(LocalDate.now().getYear() - 1940 + 1);
        PropertyCondition condition = randomCondition(type, year);
        PropertyOccupancy occupancy = type == PropertyType.LAND
                ? PropertyOccupancy.VACANT
                : weighted(PropertyOccupancy.values(), 45, 30, 25);

        //Coordenadas cerca del centro de la zona, asi coinciden con la ciudad y las calles de la propiedad
        double latitude = round(zone.latitude() + (random.nextDouble() * 2 - 1) * zone.spread(), 6);
        double longitude = round(zone.longitude() + (random.nextDouble() * 2 - 1) * zone.spread(), 6);

        Property property = property(address, type, zone.province(), zone.county(), zone.city(),
                latitude, longitude, agency.getId(), year, rooms, size, condition, occupancy, floor);
        //Unas pocas dadas de baja, para probar el restore y los filtros
        property.setActive(!chance(6));
        randomPrices(property, zone);
        return property;
    }

    //Vivienda casi siempre; terrenos y campos solo afuera de la ciudad
    private PropertyType randomType(SeedCatalog.Zone zone) {
        if (zone.usdPerM2() < 1000) {
            return weighted(new PropertyType[]{PropertyType.HOUSE, PropertyType.LAND, PropertyType.FARM}, 55, 30, 15);
        }
        return weighted(new PropertyType[]{PropertyType.APARTMENT, PropertyType.HOUSE, PropertyType.PH,
                        PropertyType.OFFICE, PropertyType.COMMERCIAL_SPACE, PropertyType.GARAGE,
                        PropertyType.LAND, PropertyType.WAREHOUSE},
                42, 16, 14, 9, 6, 5, 4, 4);
    }

    private PropertyCondition randomCondition(PropertyType type, Integer year) {
        if (type == PropertyType.LAND) {
            return PropertyCondition.BRAND_NEW;
        }
        //Construidas en los ultimos dos años: nuevas o todavia en obra
        if (year >= LocalDate.now().getYear() - 1) {
            return chance(50) ? PropertyCondition.UNDER_CONSTRUCTION : PropertyCondition.BRAND_NEW;
        }
        //Cuanto mas vieja, mas chance de necesitar arreglos
        return chance(year < 1980 ? 40 : 15) ? PropertyCondition.NEEDS_REPAIR : PropertyCondition.GOOD;
    }

    //Venta en USD por m2 de la zona, alquiler mensual en ARS. Terrenos y campos solo venta
    private void randomPrices(Property property, SeedCatalog.Zone zone) {
        double factor = switch (property.getType()) {
            case LAND -> 0.35;
            case WAREHOUSE -> 0.5;
            case FARM -> 0.004;
            case HOUSE, PH -> 0.85;
            default -> 1.0;
        };
        factor *= switch (property.getCondition()) {
            case BRAND_NEW -> 1.15;
            case UNDER_CONSTRUCTION -> 0.85;
            case NEEDS_REPAIR -> 0.75;
            default -> 1.0;
        };
        factor *= 0.85 + random.nextDouble() * 0.3;
        long sale = property.getType() == PropertyType.GARAGE
                ? 15000 + random.nextInt(20) * 1000L
                : Math.max(10000, Math.round(property.getSize() * zone.usdPerM2() * factor / 1000) * 1000);
        //Alquiler: aprox. 0,4% del valor de venta por mes, pasado a pesos
        long rent = Math.round(sale * 0.004 * 1200 / 10000) * 10000;

        boolean onlySale = property.getType() == PropertyType.LAND || property.getType() == PropertyType.FARM;
        //Si tiene inquilino, se alquila si o si
        boolean rented = property.getOccupancy() == PropertyOccupancy.TENANT_OCCUPIED;
        int operation = random.nextInt(100);
        if (onlySale) {
            price(property, OperationType.SALE, Currency.USD, String.valueOf(sale));
            return;
        }
        if (rented || operation < 25) {
            price(property, OperationType.RENT, Currency.ARS, String.valueOf(rent));
            if (operation < 10) {
                price(property, OperationType.SALE, Currency.USD, String.valueOf(sale));
            }
            return;
        }
        price(property, OperationType.SALE, Currency.USD, String.valueOf(sale));
        if (operation < 35) {
            price(property, OperationType.RENT, Currency.ARS, String.valueOf(rent));
        }
    }

    //Si tiene inquilino, un alquiler activo. A veces tambien contratos viejos (terminados o cancelados)
    private void randomContracts(Property property, List<People> owners, List<People> others) {
        LocalDate today = LocalDate.now();
        PropertyPrice rentPrice = priceOf(property, OperationType.RENT);

        if (property.getOccupancy() == PropertyOccupancy.TENANT_OCCUPIED && rentPrice != null) {
            LocalDate start = today.minusMonths(random.nextInt(30)).withDayOfMonth(1);
            String amount = rentPrice.getAmount().multiply(BigDecimal.valueOf(0.85)).setScale(0, RoundingMode.HALF_UP).toPlainString();
            PropertyContract contract = contract(property, ContractType.RENT, ContractStatus.ACTIVE, Currency.ARS,
                    amount, start, start.plusYears(3).minusDays(1));
            parties(contract, owners, others, ContractRole.TENANT);
        }

        if (rentPrice != null && chance(25)) {
            LocalDate start = today.minusYears(4 + random.nextInt(3)).withDayOfMonth(1);
            String amount = rentPrice.getAmount().multiply(BigDecimal.valueOf(0.3)).setScale(0, RoundingMode.HALF_UP).toPlainString();
            PropertyContract contract = contract(property, ContractType.RENT, ContractStatus.FINISHED, Currency.ARS,
                    amount, start, start.plusYears(3).minusDays(1));
            parties(contract, owners, others, ContractRole.TENANT);
        }

        PropertyPrice salePrice = priceOf(property, OperationType.SALE);
        if (salePrice != null && chance(5)) {
            LocalDate start = today.minusMonths(2 + random.nextInt(10));
            PropertyContract contract = contract(property, ContractType.SALE, ContractStatus.CANCELLED, Currency.USD,
                    salePrice.getAmount().toPlainString(), start, start.plusMonths(2));
            parties(contract, owners, others, ContractRole.BUYER);
        }
    }

    private void parties(PropertyContract contract, List<People> owners, List<People> others, ContractRole role) {
        for (People owner : owners) {
            party(contract, owner, ContractRole.OWNER, null);
        }
        People counterpart = pick(others);
        party(contract, counterpart, role, null);
        if (role == ContractRole.TENANT && chance(50)) {
            People guarantor = pick(others);
            if (guarantor != counterpart) {
                party(contract, guarantor, ContractRole.GUARANTOR, chance(50) ? "Garantia propietaria" : "Seguro de caucion");
            }
        }
    }

    //1 a 3 interesados, cada uno en una etapa del CRM con la historia, ofertas y alertas que le corresponden
    private void randomLeads(Property property, List<People> others, List<User> staff) {
        List<People> leads = new ArrayList<>(others);
        Collections.shuffle(leads, random);
        int count = 1 + random.nextInt(3);
        PropertyPrice price = property.getPrices().getFirst();

        for (People lead : leads.subList(0, Math.min(count, leads.size()))) {
            User user = pick(staff);
            CrmStage stage = weighted(CrmStage.values(), 18, 20, 12, 15, 12, 8, 15);
            CrmProperty crmProperty = crm(property, lead, user, stage);

            if (stage == CrmStage.NEW) {
                continue;
            }
            history(crmProperty, user, chance(50) ? CrmEventType.CALL : CrmEventType.NOTE, pick(SeedCatalog.CALL_NOTES));
            if (stage == CrmStage.DISCARDED) {
                history(crmProperty, user, CrmEventType.STAGE_CHANGE, "-> DISCARDED: " + pick(SeedCatalog.DISCARD_NOTES));
                continue;
            }
            if (stage.ordinal() >= CrmStage.VISITED.ordinal()) {
                history(crmProperty, user, CrmEventType.VISIT, pick(SeedCatalog.VISIT_NOTES));
            }
            if (stage == CrmStage.NEGOTIATING || stage == CrmStage.CLOSED) {
                //Primero una oferta baja rechazada, despues la que sigue en juego o la aceptada
                BigDecimal amount = price.getAmount();
                if (chance(60)) {
                    offer(crmProperty, percentOf(amount, 82 + random.nextInt(6)), price.getCurrency(), OfferStatus.REJECTED);
                    history(crmProperty, user, CrmEventType.OFFER, "Primera oferta, rechazada por el dueño");
                }
                OfferStatus status = stage == CrmStage.CLOSED ? OfferStatus.ACCEPTED : OfferStatus.PENDING;
                offer(crmProperty, percentOf(amount, 90 + random.nextInt(9)), price.getCurrency(), status);
                history(crmProperty, user, CrmEventType.OFFER, status == OfferStatus.ACCEPTED ? "Oferta aceptada" : "Nueva oferta, esperando respuesta");
            }
            history(crmProperty, user, CrmEventType.STAGE_CHANGE, "-> " + stage);

            //Alertas: las cerradas con una hecha; el resto pendientes, algunas vencidas
            if (stage == CrmStage.CLOSED) {
                alert(crmProperty, user, "Pedir documentacion para escritura", LocalDateTime.now().minusDays(1 + random.nextInt(20)), CrmAlertStatus.DONE);
            } else if (chance(75)) {
                LocalDateTime date = LocalDateTime.now().plusDays(random.nextInt(14) - 4).withHour(9 + random.nextInt(9)).withMinute(0).withSecond(0).withNano(0);
                alert(crmProperty, user, pick(SeedCatalog.ALERTS), date, CrmAlertStatus.PENDING);
            }
        }
    }

    private PropertyPrice priceOf(Property property, OperationType operationType) {
        return property.getPrices().stream()
                .filter(price -> price.getOperationType() == operationType)
                .findFirst()
                .orElse(null);
    }

    private String percentOf(BigDecimal amount, int percent) {
        //Redondeado a miles, como se ofertan en la realidad
        return amount.multiply(BigDecimal.valueOf(percent)).divide(BigDecimal.valueOf(100_000), 0, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(1000)).toPlainString();
    }

    //DNI de 8 digitos que no se repita: People tiene unique (agency_id, dni)
    private String uniqueDni() {
        String dni;
        do {
            dni = String.valueOf(20_000_000 + random.nextInt(25_000_000));
        } while (!usedDni.add(dni));
        return dni;
    }

    //CUIT con el digito verificador bien calculado, asi pasa cualquier validacion
    private String cuitOf(String dni) {
        String prefix = chance(50) ? "20" : "27";
        String base = prefix + dni;
        int[] weights = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += (base.charAt(i) - '0') * weights[i];
        }
        int check = 11 - sum % 11;
        if (check == 10) {
            return null;
        }
        return prefix + "-" + dni + "-" + (check == 11 ? 0 : check);
    }

    private String slug(String text) {
        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "")
                .replaceAll("[^A-Za-z]", "")
                .toLowerCase();
    }

    private String digits(int count) {
        StringBuilder digits = new StringBuilder();
        for (int i = 0; i < count; i++) {
            digits.append(random.nextInt(10));
        }
        return digits.toString();
    }

    private String letter() {
        return String.valueOf((char) ('A' + random.nextInt(6)));
    }

    private double round(double value, int decimals) {
        return BigDecimal.valueOf(value).setScale(decimals, RoundingMode.HALF_UP).doubleValue();
    }

    private boolean chance(int percent) {
        return random.nextInt(100) < percent;
    }

    private <T> T pick(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }

    //Elige un valor segun pesos: weighted(values, 70, 30) devuelve el primero el 70% de las veces
    private <T> T weighted(T[] values, int... weights) {
        int total = 0;
        for (int weight : weights) {
            total += weight;
        }
        int roll = random.nextInt(total);
        for (int i = 0; i < weights.length; i++) {
            roll -= weights[i];
            if (roll < 0) {
                return values[i];
            }
        }
        return values[values.length - 1];
    }

    private Agency agency(String cuit, String companyName, String publicName, String email, String phoneNumber,
                          String address, String webURL, String socials, AgencyStatus status) {
        Agency agency = new Agency();
        agency.setCuit(cuit);
        agency.setCompanyName(companyName);
        agency.setPublicName(publicName);
        agency.setEmail(email);
        agency.setPhoneNumber(phoneNumber);
        agency.setAddress(address);
        agency.setWebURL(webURL);
        agency.setSocials(socials);
        agency.setActive(true);
        agency.setStatus(status);
        return jpaAgencyRepository.save(agency);
    }

    private User user(String name, String email, String phoneNumber, Long agencyId, UserRol rol,
                      String cuit, String license) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(SEED_PASSWORD));
        user.setPhoneNumber(phoneNumber);
        user.setAgencyId(agencyId);
        user.setCuit(cuit);
        user.setLicense(license);
        user.setActive(true);
        user.setRol(rol);
        return jpaUserRepository.save(user);
    }

    private People people(String name, String phone, String email, String address, String dni, String cuit,
                          Long agencyId) {
        People people = new People();
        people.setName(name);
        people.setPhone(phone);
        people.setEmail(email);
        people.setAddress(address);
        people.setDni(dni);
        people.setCuit(cuit);
        people.setAgencyId(agencyId);
        if (dni != null) {
            usedDni.add(dni);
        }
        return jpaPeopleRepository.save(people);
    }

    //No se guarda aca: los precios se agregan antes y se guardan en cascada con la propiedad
    private Property property(String address, PropertyType type, String province, String county, String city,
                              Double latitude, Double longitude, Long agencyId, Integer year, Integer rooms,
                              Integer size, PropertyCondition condition, PropertyOccupancy occupancy,
                              Integer floorNumber) {
        Property property = new Property();
        property.setAddress(address);
        property.setActive(true);
        property.setType(type);
        property.setProvince(province);
        property.setCounty(county);
        property.setCity(city);
        property.setLatitude(latitude);
        property.setLongitude(longitude);
        property.setAgencyId(agencyId);
        property.setYear(year);
        property.setRooms(rooms);
        property.setSize(size);
        property.setCondition(condition);
        property.setOccupancy(occupancy);
        property.setFloorNumber(floorNumber);
        return property;
    }

    private void price(Property property, OperationType operationType, Currency currency, String amount) {
        PropertyPrice price = new PropertyPrice();
        price.setOperationType(operationType);
        price.setCurrency(currency);
        price.setAmount(new BigDecimal(amount));
        price.setProperty(property);
        property.getPrices().add(price);
    }

    private void owner(Property property, People people, String comments) {
        PropertyOwner owner = new PropertyOwner();
        owner.setPropertyId(property.getId());
        owner.setPeopleId(people.getId());
        owner.setComments(comments);
        jpaPropertyOwnerRepository.save(owner);
    }

    private PropertyContract contract(Property property, ContractType type, ContractStatus status,
                                      Currency currency, String amount, LocalDate startDate, LocalDate endDate) {
        PropertyContract contract = new PropertyContract();
        contract.setPropertyId(property.getId());
        contract.setType(type);
        contract.setStatus(status);
        contract.setCurrency(currency);
        contract.setAmount(new BigDecimal(amount));
        contract.setStartDate(startDate);
        contract.setEndDate(endDate);
        contract.setDocumentURL("https://docs.zaguan.test/contratos/" + property.getId() + "-" + startDate + ".pdf");
        return jpaPropertyContractRepository.save(contract);
    }

    private void party(PropertyContract contract, People people, ContractRole role, String comments) {
        ContractParty party = new ContractParty();
        party.setContractId(contract.getId());
        party.setPeopleId(people.getId());
        party.setRole(role);
        party.setComments(comments);
        jpaContractPartyRepository.save(party);
    }

    private CrmProperty crm(Property property, People people, User user, CrmStage stage) {
        CrmProperty crmProperty = new CrmProperty();
        crmProperty.setPropertyId(property.getId());
        crmProperty.setPeopleId(people.getId());
        crmProperty.setUserId(user.getId());
        crmProperty.setStage(stage);
        return jpaCrmPropertyRepository.save(crmProperty);
    }

    private void history(CrmProperty crmProperty, User user, CrmEventType type, String comments) {
        CrmHistory history = new CrmHistory();
        history.setCrmPropertyId(crmProperty.getId());
        history.setUserId(user.getId());
        history.setType(type);
        history.setComments(comments);
        jpaCrmHistoryRepository.save(history);
    }

    private void offer(CrmProperty crmProperty, String amount, Currency currency, OfferStatus status) {
        Offer offer = new Offer();
        offer.setCrmPropertyId(crmProperty.getId());
        offer.setAmount(new BigDecimal(amount));
        offer.setCurrency(currency);
        offer.setStatus(status);
        jpaOfferRepository.save(offer);
    }

    private void alert(CrmProperty crmProperty, User user, String message, LocalDateTime alertDate, CrmAlertStatus status) {
        CrmAlert alert = new CrmAlert();
        alert.setCrmPropertyId(crmProperty.getId());
        alert.setUserId(user.getId());
        alert.setMessage(message);
        alert.setAlertDate(alertDate);
        alert.setStatus(status);
        jpaCrmAlertRepository.save(alert);
    }
}

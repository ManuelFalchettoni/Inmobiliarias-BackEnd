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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

//Llena la base con datos de prueba al arrancar. Se apaga con app.seed.enabled=false
//(por defecto prendido) y corre solo si la tabla de usuarios esta vacia, asi que reiniciar la app
//no duplica nada. Para volver a cargar: borrar la base y arrancar de nuevo.
//No carga fotos: necesitan el archivo en MinIO, se suben por el endpoint.
//Todos los usuarios de prueba tienen la contraseña SEED_PASSWORD
@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true")
public class DataSeeder implements CommandLineRunner {

    private static final String SEED_PASSWORD = "Password123";

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
                "Mar del Plata", null, null, delSur.getId(), null, 0, 600, PropertyCondition.BRAND_NEW,
                PropertyOccupancy.VACANT, null);
        price(lote, OperationType.SALE, Currency.USD, "45000");
        jpaPropertyRepository.save(lote);

        //Una dada de baja, para probar el restore
        Property baja = property("Juramento 2100", PropertyType.GARAGE, "CABA", null, "Buenos Aires",
                null, null, zaguan.getId(), 1990, 0, 12, PropertyCondition.GOOD,
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
        PropertyContract alquilerOficina = contract(oficina, ContractType.RENT, ContractStatus.ACTIVE,
                Currency.ARS, "600000", LocalDate.of(2025, 3, 1), LocalDate.of(2028, 2, 29));
        party(alquilerOficina, carlos, ContractRole.OWNER, null);
        party(alquilerOficina, julian, ContractRole.TENANT, null);
        party(alquilerOficina, marta, ContractRole.GUARANTOR, "Garantia propietaria");

        PropertyContract alquilerCasa = contract(casa, ContractType.RENT, ContractStatus.FINISHED,
                Currency.ARS, "300000", LocalDate.of(2022, 1, 1), LocalDate.of(2024, 12, 31));
        party(alquilerCasa, roberto, ContractRole.OWNER, null);
        party(alquilerCasa, valeria, ContractRole.TENANT, null);

        // ---------- CRM ----------
        CrmProperty leadDepto = crm(depto, lucia, zaguanAgent, CrmStage.NEGOTIATION);
        history(leadDepto, zaguanAgent, CrmEventType.CALL, "Consulta por WhatsApp, pide visita");
        history(leadDepto, zaguanAgent, CrmEventType.VISIT, "Le gusto, pregunta por expensas");
        history(leadDepto, zaguanOwner, CrmEventType.OFFER, "Ofrece USD 135.000");
        history(leadDepto, zaguanAgent, CrmEventType.STAGE_CHANGE, "VISIT -> NEGOTIATION");
        offer(leadDepto, "135000", Currency.USD, OfferStatus.REJECTED);
        offer(leadDepto, "140000", Currency.USD, OfferStatus.PENDING);
        alert(leadDepto, zaguanAgent, "Responder contraoferta de Lucia", LocalDateTime.now().plusDays(1), false);

        CrmProperty leadPh = crm(ph, julian, zaguanAgent, CrmStage.CONTACTED);
        history(leadPh, zaguanAgent, CrmEventType.NOTE, "Busca PH con patio para alquilar");
        alert(leadPh, zaguanAgent, "Coordinar visita al PH", LocalDateTime.now().plusDays(3), false);

        CrmProperty leadCasa = crm(casa, valeria, delSurAgent, CrmStage.WON);
        history(leadCasa, delSurAgent, CrmEventType.VISIT, null);
        history(leadCasa, delSurOwner, CrmEventType.STAGE_CHANGE, "NEGOTIATION -> WON");
        offer(leadCasa, "250000", Currency.USD, OfferStatus.ACCEPTED);
        alert(leadCasa, delSurOwner, "Pedir documentacion para escritura", LocalDateTime.now().minusDays(2), true);

        CrmProperty leadLote = crm(lote, valeria, delSurAgent, CrmStage.LOST);
        history(leadLote, delSurAgent, CrmEventType.NOTE, "No le sirve, queda lejos");

        crm(depto, julian, zaguanOwner, CrmStage.NEW);

        log.info("DataSeeder: datos de prueba cargados");
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

    private void alert(CrmProperty crmProperty, User user, String message, LocalDateTime alertDate, Boolean isRead) {
        CrmAlert alert = new CrmAlert();
        alert.setCrmPropertyId(crmProperty.getId());
        alert.setUserId(user.getId());
        alert.setMessage(message);
        alert.setAlertDate(alertDate);
        alert.setIsRead(isRead);
        jpaCrmAlertRepository.save(alert);
    }
}

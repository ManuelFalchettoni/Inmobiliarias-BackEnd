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
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//Carga datos de prueba al arrancar. Solo corre con la base vacia (sin inmobiliarias):
//para volver a cargarlos hay que borrar la base y reiniciar. Todos los usuarios tienen
//la contraseña de PASSWORD. Las fotos no se cargan porque necesitan el archivo en MinIO
@Component
@AllArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final String PASSWORD = "zaguan123";

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

    //Las 15 propiedades que se cargan por inmobiliaria: tipo, ambientes y metros
    private static final PropertyType[] TYPES = {
            PropertyType.APARTMENT, PropertyType.HOUSE, PropertyType.PH, PropertyType.APARTMENT,
            PropertyType.OFFICE, PropertyType.LAND, PropertyType.APARTMENT, PropertyType.COMMERCIAL_SPACE,
            PropertyType.HOUSE, PropertyType.GARAGE, PropertyType.APARTMENT, PropertyType.WAREHOUSE,
            PropertyType.PH, PropertyType.HOUSE, PropertyType.FARM
    };
    private static final int[] ROOMS = {2, 4, 3, 1, 2, 0, 3, 1, 5, 0, 2, 1, 4, 3, 6};
    private static final int[] SIZES = {55, 180, 90, 35, 60, 300, 75, 120, 220, 15, 48, 400, 110, 160, 5000};

    private static final String[] PEOPLE_NAMES = {
            "Juan Perez", "Maria Lopez", "Carlos Garcia", "Ana Martinez", "Jorge Rodriguez", "Laura Sanchez",
            "Pablo Romero", "Valeria Diaz", "Federico Alvarez", "Camila Torres", "Nicolas Ruiz", "Julieta Flores",
            "Ricardo Acosta", "Florencia Benitez", "Gustavo Medina", "Paula Herrera", "Hernan Suarez", "Agustina Rios",
            "Santiago Molina", "Micaela Castro", "Tomas Ortiz", "Rocio Silva", "Matias Rojas", "Daniela Nunez"
    };

    @Override
    public void run(String... args) {
        if (jpaAgencyRepository.count() > 0) return;

        Agency centro = createAgency("30-71234567-1", "Zaguan Propiedades SRL", "Zaguan Centro",
                "contacto@zaguancentro.com", "1145678901", "Av. Corrientes 1234, CABA");
        Agency lomas = createAgency("30-71876543-5", "Lomas Inmuebles SA", "Lomas Inmuebles",
                "info@lomasinmuebles.com", "1142445566", "Av. Meeks 456, Lomas de Zamora");

        createUser("Admin Zaguan", "admin@zaguan.com", "1100000001", centro.getId(), UserRol.ADMIN, null, null);
        createUser("Zaguan Centro", "centro@zaguan.com", "1100000002", centro.getId(), UserRol.AGENCY, null, null);
        createUser("Lomas Inmuebles", "lomas@zaguan.com", "1100000003", lomas.getId(), UserRol.AGENCY, null, null);
        User lucia = createUser("Lucia Fernandez", "lucia@zaguan.com", "1100000004", centro.getId(), UserRol.AGENT, "CUCICBA 4521", "27-30111222-5");
        User martin = createUser("Martin Gomez", "martin@zaguan.com", "1100000005", centro.getId(), UserRol.AGENT, "CUCICBA 5873", "20-31222333-4");
        User sofia = createUser("Sofia Ramirez", "sofia@zaguan.com", "1100000006", lomas.getId(), UserRol.AGENT, "CMCPLZ 1189", "27-32333444-2");
        User diego = createUser("Diego Torres", "diego@zaguan.com", "1100000007", lomas.getId(), UserRol.AGENT, "CMCPLZ 1342", "20-33444555-1");
        createUser("Usuario Prueba", "usuario@zaguan.com", "1100000008", null, UserRol.USER, null, null);

        List<People> centroPeople = createPeople(centro, 0);
        List<People> lomasPeople = createPeople(lomas, 12);

        List<Property> centroProperties = createProperties(centro, "CABA", null,
                new String[]{"Palermo", "Belgrano", "Caballito", "Almagro", "Recoleta", "Villa Crespo", "Nunez"},
                new String[]{"Gorriti", "Av. Cabildo", "Av. Rivadavia", "Billinghurst", "Arenales", "Thames", "Juramento"},
                -34.60, -58.43);
        List<Property> lomasProperties = createProperties(lomas, "Buenos Aires", "Lomas de Zamora",
                new String[]{"Lomas de Zamora", "Banfield", "Temperley", "Turdera"},
                new String[]{"Av. Meeks", "Laprida", "Alem", "Boedo", "Colombres", "Sarmiento", "Av. Hipolito Yrigoyen"},
                -34.76, -58.40);

        createOwners(centroProperties, centroPeople);
        createOwners(lomasProperties, lomasPeople);

        createContracts(centroProperties, centroPeople);
        createContracts(lomasProperties, lomasPeople);

        createCrm(centroProperties, centroPeople, lucia, martin, 0);
        createCrm(lomasProperties, lomasPeople, sofia, diego, 1);
    }

    private Agency createAgency(String cuit, String companyName, String publicName, String email,
                                String phone, String address) {
        Agency agency = new Agency();
        agency.setCuit(cuit);
        agency.setCompanyName(companyName);
        agency.setPublicName(publicName);
        agency.setEmail(email);
        agency.setPhoneNumber(phone);
        agency.setAddress(address);
        agency.setWebURL("https://www." + email.substring(email.indexOf('@') + 1));
        agency.setSocials("@" + publicName.toLowerCase().replace(" ", ""));
        agency.setActive(true);
        agency.setStatus(AgencyStatus.VERIFY);
        return jpaAgencyRepository.save(agency);
    }

    private User createUser(String name, String email, String phone, Long agencyId, UserRol rol,
                            String license, String cuit) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(PASSWORD));
        user.setPhoneNumber(phone);
        user.setAgencyId(agencyId);
        user.setRol(rol);
        user.setLicense(license);
        user.setCuit(cuit);
        user.setActive(true);
        return jpaUserRepository.save(user);
    }

    //12 personas por inmobiliaria: las primeras 6 son dueños (con DNI y CUIT), las otras
    //6 son interesados e inquilinos (solo algunos con DNI)
    private List<People> createPeople(Agency agency, int firstName) {
        List<People> people = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            int n = firstName + i;
            String dni = String.valueOf(20000000 + n * 731911);

            People person = new People();
            person.setName(PEOPLE_NAMES[n]);
            person.setPhone("11" + (50000000 + n * 12345));
            person.setEmail(PEOPLE_NAMES[n].toLowerCase().replace(" ", ".") + "@mail.com");
            person.setAgencyId(agency.getId());
            if (i < 6) {
                person.setDni(dni);
                person.setCuit(buildCuit("20", dni));
                person.setAddress("Calle " + (100 + n) + " N° " + (1000 + n * 37));
            } else if (i % 2 == 0) {
                person.setDni(dni);
            }
            people.add(jpaPeopleRepository.save(person));
        }
        return people;
    }

    //Arma un CUIT valido calculando el digito verificador de AFIP. Si la cuenta da 10 no hay
    //digito posible y AFIP cambia el prefijo a 23
    private String buildCuit(String prefix, String dni) {
        String digits = prefix + dni;
        int[] weights = {5, 4, 3, 2, 7, 6, 5, 4, 3, 2};
        int sum = 0;
        for (int i = 0; i < weights.length; i++) {
            sum += Character.getNumericValue(digits.charAt(i)) * weights[i];
        }
        int check = 11 - sum % 11;
        if (check == 11) check = 0;
        if (check == 10) return buildCuit("23", dni);
        return prefix + "-" + dni + "-" + check;
    }

    private List<Property> createProperties(Agency agency, String province, String county, String[] cities,
                                            String[] streets, double latitude, double longitude) {
        List<Property> properties = new ArrayList<>();
        for (int i = 0; i < TYPES.length; i++) {
            PropertyType type = TYPES[i];

            Property property = new Property();
            property.setAddress(streets[i % streets.length] + " " + (500 + i * 113));
            property.setType(type);
            property.setProvince(province);
            property.setCounty(county);
            property.setCity(cities[i % cities.length]);
            property.setLatitude(latitude + i * 0.003);
            property.setLongitude(longitude - i * 0.002);
            property.setAgencyId(agency.getId());
            property.setYear(1965 + i * 4);
            property.setRooms(ROOMS[i]);
            property.setSize(SIZES[i]);
            property.setCondition(PropertyCondition.values()[i % PropertyCondition.values().length]);
            property.setOccupancy(PropertyOccupancy.values()[i % PropertyOccupancy.values().length]);
            if (type == PropertyType.APARTMENT || type == PropertyType.OFFICE) {
                property.setFloorNumber(i % 9);
            }
            //La ultima queda dada de baja, para probar la baja logica
            property.setActive(i != TYPES.length - 1);

            //Casi todas en venta, una de cada tres tambien en alquiler y algunas solo en alquiler
            if (i % 5 != 4) {
                addPrice(property, OperationType.SALE, Currency.USD, SIZES[i] * 1800);
            }
            if (i % 3 == 0 || i % 5 == 4) {
                addPrice(property, OperationType.RENT, Currency.ARS, SIZES[i] * 9000);
            }
            properties.add(jpaPropertyRepository.save(property));
        }
        return properties;
    }

    //Se guarda junto con la propiedad por el cascade de Property.prices
    private void addPrice(Property property, OperationType operationType, Currency currency, long amount) {
        PropertyPrice price = new PropertyPrice();
        price.setOperationType(operationType);
        price.setCurrency(currency);
        price.setAmount(BigDecimal.valueOf(amount));
        price.setProperty(property);
        property.getPrices().add(price);
    }

    //Cada propiedad tiene uno de los 6 dueños. Cada tercera tiene ademas un co-titular
    private void createOwners(List<Property> properties, List<People> people) {
        for (int i = 0; i < properties.size(); i++) {
            createOwner(properties.get(i), people.get(i % 6), null);
            if (i % 3 == 1) {
                createOwner(properties.get(i), people.get((i + 1) % 6), "Co-titular");
            }
        }
    }

    private void createOwner(Property property, People person, String comments) {
        PropertyOwner owner = new PropertyOwner();
        owner.setPropertyId(property.getId());
        owner.setPeopleId(person.getId());
        owner.setComments(comments);
        jpaPropertyOwnerRepository.save(owner);
    }

    //Tres alquileres (dos vigentes y uno terminado) y una venta terminada
    private void createContracts(List<Property> properties, List<People> people) {
        int[] rentProperties = {0, 3, 6};
        for (int k = 0; k < rentProperties.length; k++) {
            int i = rentProperties[k];
            ContractStatus status = k < 2 ? ContractStatus.ACTIVE : ContractStatus.FINISHED;
            LocalDate start = status == ContractStatus.ACTIVE ? LocalDate.of(2025, 3 + k * 3, 1) : LocalDate.of(2022, 4, 1);

            PropertyContract contract = createContract(properties.get(i), ContractType.RENT, status,
                    Currency.ARS, SIZES[i] * 9000L, start, start.plusYears(2));
            createParty(contract, people.get(i % 6), ContractRole.OWNER);
            createParty(contract, people.get(6 + k), ContractRole.TENANT);
            createParty(contract, people.get(9 + k), ContractRole.GUARANTOR);
        }

        PropertyContract sale = createContract(properties.get(1), ContractType.SALE, ContractStatus.FINISHED,
                Currency.USD, SIZES[1] * 1700L, LocalDate.of(2025, 11, 15), null);
        createParty(sale, people.get(1), ContractRole.OWNER);
        createParty(sale, people.get(11), ContractRole.BUYER);
    }

    private PropertyContract createContract(Property property, ContractType type, ContractStatus status,
                                            Currency currency, long amount, LocalDate start, LocalDate end) {
        PropertyContract contract = new PropertyContract();
        contract.setPropertyId(property.getId());
        contract.setType(type);
        contract.setStatus(status);
        contract.setCurrency(currency);
        contract.setAmount(BigDecimal.valueOf(amount));
        contract.setStartDate(start);
        contract.setEndDate(end);
        contract.setDocumentURL("https://example.com/contratos/propiedad-" + property.getId() + ".pdf");
        return jpaPropertyContractRepository.save(contract);
    }

    private void createParty(PropertyContract contract, People person, ContractRole role) {
        ContractParty party = new ContractParty();
        party.setContractId(contract.getId());
        party.setPeopleId(person.getId());
        party.setRole(role);
        jpaContractPartyRepository.save(party);
    }

    //Cinco leads por inmobiliaria, cada uno en una etapa distinta. Con stageOffset 1 la
    //segunda inmobiliaria arranca en CONTACTED y llega hasta LOST
    private void createCrm(List<Property> properties, List<People> people, User agent1, User agent2,
                           int stageOffset) {
        int[] leadProperties = {1, 2, 5, 7, 8};
        for (int k = 0; k < leadProperties.length; k++) {
            int i = leadProperties[k];
            CrmStage stage = CrmStage.values()[k + stageOffset];
            User agent = k % 2 == 0 ? agent1 : agent2;

            CrmProperty lead = new CrmProperty();
            lead.setPropertyId(properties.get(i).getId());
            lead.setPeopleId(people.get(6 + k).getId());
            lead.setUserId(agent.getId());
            lead.setStage(stage);
            lead = jpaCrmPropertyRepository.save(lead);

            //El historial acompaña a la etapa: cuanto mas avanzado el lead, mas eventos tiene
            createHistory(lead, agent, CrmEventType.NOTE, "Consulta recibida por la web");
            if (stage.ordinal() >= CrmStage.CONTACTED.ordinal()) {
                createHistory(lead, agent, CrmEventType.CALL, "Se lo llamo para coordinar una visita");
            }
            if (stage.ordinal() >= CrmStage.VISIT.ordinal()) {
                createHistory(lead, agent, CrmEventType.VISIT, "Visito la propiedad, le gusto la ubicacion");
            }
            if (stage.ordinal() >= CrmStage.NEGOTIATION.ordinal()) {
                createHistory(lead, agent, CrmEventType.OFFER, "Hizo una oferta por debajo del precio publicado");
            }
            createHistory(lead, agent, CrmEventType.STAGE_CHANGE, "Paso a " + stage);

            long salePrice = SIZES[i] * 1800L;
            if (stage == CrmStage.NEGOTIATION) {
                createOffer(lead, salePrice * 85 / 100, OfferStatus.REJECTED);
                createOffer(lead, salePrice * 92 / 100, OfferStatus.PENDING);
            } else if (stage == CrmStage.WON) {
                createOffer(lead, salePrice * 95 / 100, OfferStatus.ACCEPTED);
            } else if (stage == CrmStage.LOST) {
                createOffer(lead, salePrice * 70 / 100, OfferStatus.REJECTED);
            }

            //Alertas solo para los leads abiertos: una vencida y ya leida, otra pendiente
            if (stage != CrmStage.WON && stage != CrmStage.LOST) {
                createAlert(lead, agent, "Volver a llamar al interesado", LocalDateTime.now().minusDays(2), true);
                createAlert(lead, agent, "Enviar fotos y precio actualizado", LocalDateTime.now().plusDays(k + 1), false);
            }
        }
    }

    private void createHistory(CrmProperty lead, User agent, CrmEventType type, String comments) {
        CrmHistory history = new CrmHistory();
        history.setCrmPropertyId(lead.getId());
        history.setUserId(agent.getId());
        history.setType(type);
        history.setComments(comments);
        jpaCrmHistoryRepository.save(history);
    }

    private void createOffer(CrmProperty lead, long amount, OfferStatus status) {
        Offer offer = new Offer();
        offer.setCrmPropertyId(lead.getId());
        offer.setAmount(BigDecimal.valueOf(amount));
        offer.setCurrency(Currency.USD);
        offer.setStatus(status);
        jpaOfferRepository.save(offer);
    }

    private void createAlert(CrmProperty lead, User agent, String message, LocalDateTime date, boolean isRead) {
        CrmAlert alert = new CrmAlert();
        alert.setCrmPropertyId(lead.getId());
        alert.setUserId(agent.getId());
        alert.setMessage(message);
        alert.setAlertDate(date);
        alert.setIsRead(isRead);
        jpaCrmAlertRepository.save(alert);
    }
}

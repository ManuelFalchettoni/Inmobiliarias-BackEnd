package com.manuel.zaguan_inmobiliarias.service.crm;

import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAgendaResponse;
import com.manuel.zaguan_inmobiliarias.dto.response.crm.CrmAgendaSummaryResponse;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmAlert;
import com.manuel.zaguan_inmobiliarias.entity.crm.CrmProperty;
import com.manuel.zaguan_inmobiliarias.entity.people.People;
import com.manuel.zaguan_inmobiliarias.entity.property.Property;
import com.manuel.zaguan_inmobiliarias.exception.property.InvalidSearchFilterException;
import com.manuel.zaguan_inmobiliarias.exception.user.UserNotFoundException;
import com.manuel.zaguan_inmobiliarias.mapper.crm.CrmAlertMapper;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmAlertRepository;
import com.manuel.zaguan_inmobiliarias.repository.crm.JpaCrmPropertyRepository;
import com.manuel.zaguan_inmobiliarias.repository.people.JpaPeopleRepository;
import com.manuel.zaguan_inmobiliarias.repository.property.JpaPropertyRepository;
import com.manuel.zaguan_inmobiliarias.service.auth.AgencyScopeService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//La agenda de avisos: una fila por lead con el aviso que le toca.
//Se arma en memoria: se traen los leads y los avisos PENDING de la inmobiliaria (dos consultas),
//se ordena y se corta la pagina. Nombre y direccion se buscan solo para los de la pagina
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class CrmAgendaService {
    private static final List<String> BUCKETS = List.of("overdue", "hour", "day", "week", "later");

    private final JpaCrmPropertyRepository jpaCrmPropertyRepository;
    private final JpaCrmAlertRepository jpaCrmAlertRepository;
    private final JpaPeopleRepository jpaPeopleRepository;
    private final JpaPropertyRepository jpaPropertyRepository;
    private final CrmAlertMapper crmAlertMapper;
    private final AgencyScopeService agencyScopeService;

    //Vencidos primero (el mas atrasado arriba), despues el resto segun sort y al final los sin aviso
    public Page<CrmAgendaResponse> findAgenda(Long userId, boolean includeOverdue, boolean includeWithoutAlerts,
                                              String bucket, String sort, int page, int size){
        if (bucket != null && !BUCKETS.contains(bucket)) {
            throw new InvalidSearchFilterException("Invalid bucket: " + bucket
                    + ". Allowed values: overdue, hour, day, week, later");
        }
        if (!sort.equals("proximo") && !sort.equals("lejano")) {
            throw new InvalidSearchFilterException("Invalid sort: " + sort + ". Allowed values: proximo, lejano");
        }

        List<CrmAgendaResponse> overdueRows = new ArrayList<>();
        List<CrmAgendaResponse> upcomingRows = new ArrayList<>();
        List<CrmAgendaResponse> withoutAlertRows = new ArrayList<>();

        for (CrmAgendaResponse row : buildRows(userId, includeOverdue)) {
            if (row.getNextAlert() == null) {
                //Con bucket se pide un tramo: los sin aviso no tienen ninguno
                if (includeWithoutAlerts && bucket == null) {
                    withoutAlertRows.add(row);
                }
            } else if (bucket == null || bucket.equals(row.getBucket())) {
                if (row.getOverdue()) {
                    overdueRows.add(row);
                } else {
                    upcomingRows.add(row);
                }
            }
        }

        //A igual fecha desempata el id del lead, asi las paginas no cambian entre pedidos
        Comparator<CrmAgendaResponse> byAlertDate = Comparator
                .comparing((CrmAgendaResponse row) -> row.getNextAlert().getAlertDate())
                .thenComparing(CrmAgendaResponse::getId);
        overdueRows.sort(byAlertDate);
        if (sort.equals("lejano")) {
            upcomingRows.sort(byAlertDate.reversed());
        } else {
            upcomingRows.sort(byAlertDate);
        }
        withoutAlertRows.sort(Comparator.comparing(CrmAgendaResponse::getId).reversed());

        List<CrmAgendaResponse> rows = new ArrayList<>();
        rows.addAll(overdueRows);
        rows.addAll(upcomingRows);
        rows.addAll(withoutAlertRows);

        PageRequest pageRequest = PageRequest.of(page, size);
        int from = (int) Math.min(pageRequest.getOffset(), rows.size());
        int to = Math.min(from + size, rows.size());
        List<CrmAgendaResponse> pageRows = rows.subList(from, to);
        addPersonAndProperty(pageRows);

        return new PageImpl<>(pageRows, pageRequest, rows.size());
    }

    //withoutAlerts cuenta los leads sin ningun aviso pendiente, se pidan o no en la agenda
    public CrmAgendaSummaryResponse summary(Long userId, boolean includeOverdue){
        CrmAgendaSummaryResponse summary = new CrmAgendaSummaryResponse();
        for (CrmAgendaResponse row : buildRows(userId, includeOverdue)) {
            if (row.getBucket() == null) {
                summary.setWithoutAlerts(summary.getWithoutAlerts() + 1);
                continue;
            }
            switch (row.getBucket()) {
                case "overdue" -> summary.setOverdue(summary.getOverdue() + 1);
                case "hour" -> summary.setHour(summary.getHour() + 1);
                case "day" -> summary.setDay(summary.getDay() + 1);
                case "week" -> summary.setWeek(summary.getWeek() + 1);
                default -> summary.setLater(summary.getLater() + 1);
            }
        }
        return summary;
    }

    //Una fila por lead, sin ordenar. Un lead que solo tiene vencidos, con includeOverdue en false,
    //no sale: no es un lead "sin avisos"
    private List<CrmAgendaResponse> buildRows(Long userId, boolean includeOverdue){
        if (userId != null && !agencyScopeService.canAccessActiveUser(userId)) {
            throw new UserNotFoundException(userId);
        }
        Long scopedAgencyId = agencyScopeService.resolveAgencyId(null);
        LocalDateTime now = LocalDateTime.now();

        //Los PENDING de la inmobiliaria agrupados por lead, ya ordenados por fecha
        Map<Long, List<CrmAlert>> alertsByLead = new HashMap<>();
        for (CrmAlert alert : jpaCrmAlertRepository.findUpcoming(scopedAgencyId, null, null)) {
            alertsByLead.computeIfAbsent(alert.getCrmPropertyId(), id -> new ArrayList<>()).add(alert);
        }

        List<CrmAgendaResponse> rows = new ArrayList<>();
        for (CrmProperty lead : jpaCrmPropertyRepository.findAllForAnalytics(scopedAgencyId)) {
            if (userId != null && !userId.equals(lead.getUserId())) {
                continue;
            }
            List<CrmAlert> alerts = alertsByLead.getOrDefault(lead.getId(), List.of());

            //Como vienen ordenadas, la primera vencida es la mas atrasada y la primera no vencida la proxima
            CrmAlert firstOverdue = null;
            CrmAlert firstUpcoming = null;
            for (CrmAlert alert : alerts) {
                boolean isOverdue = alert.getAlertDate().isBefore(now);
                if (isOverdue && firstOverdue == null) {
                    firstOverdue = alert;
                }
                if (!isOverdue && firstUpcoming == null) {
                    firstUpcoming = alert;
                }
            }

            CrmAlert nextAlert = (includeOverdue && firstOverdue != null) ? firstOverdue : firstUpcoming;
            if (nextAlert == null && !alerts.isEmpty()) {
                continue;
            }

            CrmAgendaResponse row = new CrmAgendaResponse();
            row.setId(lead.getId());
            row.setPeopleId(lead.getPeopleId());
            row.setPropertyId(lead.getPropertyId());
            row.setUserId(lead.getUserId());
            row.setStage(lead.getStage());
            row.setPendingCount(alerts.size());
            row.setOverdue(nextAlert != null && nextAlert == firstOverdue);
            if (nextAlert != null) {
                row.setNextAlert(crmAlertMapper.toResponse(nextAlert));
                row.setBucket(bucketOf(nextAlert.getAlertDate(), now));
            }
            rows.add(row);
        }
        return rows;
    }

    //Los tramos no se pisan: el limite de arriba de cada uno es el de abajo del siguiente
    private String bucketOf(LocalDateTime alertDate, LocalDateTime now){
        if (alertDate.isBefore(now)) {
            return "overdue";
        }
        if (alertDate.isBefore(now.plusHours(1))) {
            return "hour";
        }
        if (alertDate.isBefore(now.plusHours(24))) {
            return "day";
        }
        if (alertDate.isBefore(now.plusDays(7))) {
            return "week";
        }
        return "later";
    }

    //Nombre y telefono de la persona y direccion de la propiedad, en dos consultas para toda la pagina
    private void addPersonAndProperty(List<CrmAgendaResponse> rows){
        List<Long> peopleIds = rows.stream().map(CrmAgendaResponse::getPeopleId).distinct().toList();
        List<Long> propertyIds = rows.stream().map(CrmAgendaResponse::getPropertyId).distinct().toList();

        Map<Long, People> peopleById = new HashMap<>();
        for (People people : jpaPeopleRepository.findAllById(peopleIds)) {
            peopleById.put(people.getId(), people);
        }
        Map<Long, Property> propertyById = new HashMap<>();
        for (Property property : jpaPropertyRepository.findAllById(propertyIds)) {
            propertyById.put(property.getId(), property);
        }

        for (CrmAgendaResponse row : rows) {
            People people = peopleById.get(row.getPeopleId());
            if (people != null) {
                row.setPersonName(people.getName());
                row.setPersonPhone(people.getPhone());
            }
            Property property = propertyById.get(row.getPropertyId());
            if (property != null) {
                row.setPropertyAddress(property.getAddress());
            }
        }
    }
}

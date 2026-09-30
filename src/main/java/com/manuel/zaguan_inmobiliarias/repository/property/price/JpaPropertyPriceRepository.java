package com.manuel.zaguan_inmobiliarias.repository.property.price;

import com.manuel.zaguan_inmobiliarias.entity.property.price.PropertyPrice;
import com.manuel.zaguan_inmobiliarias.enums.property.OperationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPropertyPriceRepository extends JpaRepository<PropertyPrice, Long> {

    List<PropertyPrice> findByPropertyIdOrderByOperationTypeAsc(Long propertyId);

    Optional<PropertyPrice> findByIdAndPropertyId(Long priceId, Long propertyId);

    boolean existsByPropertyIdAndOperationType(Long propertyId, OperationType operationType);

    //Para editar: busca la operacion en otros precios de la propiedad, sin contar al que se edita
    boolean existsByPropertyIdAndOperationTypeAndIdNot(Long propertyId, OperationType operationType, Long id);
}

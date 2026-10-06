package com.manuel.zaguan_inmobiliarias.repository.property.owner;

import com.manuel.zaguan_inmobiliarias.entity.property.Owner.PropertyOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPropertyOwnerRepository extends JpaRepository<PropertyOwner, Long>{
    boolean existsByPropertyIdAndPeopleId(Long propertyId, Long peopleId);
}

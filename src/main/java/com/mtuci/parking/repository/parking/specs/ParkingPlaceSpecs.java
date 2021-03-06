package com.mtuci.parking.repository.parking.specs;

import com.mtuci.parking.model.entity.parking.ParkingPlace;
import com.mtuci.parking.model.dto.parking.ParkingPlaceSearchParams;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParkingPlaceSpecs {

    public static Specification<ParkingPlace> createSpecification(ParkingPlaceSearchParams params) {
        return (root, query, builder) -> {
            List<Predicate> list = new ArrayList<>();
            if (params.getId() != null) {
                list.add(builder.equal(root.get("id"), params.getId()));
            }
            if (params.getAddress() != null) {
                list.add(builder.equal(root.get("address"), params.getAddress()));
            }
            if (params.getZone() != null) {
                list.add(builder.equal(root.get("zone"), params.getZone()));
            }
            if (params.getFloor() != null) {
                list.add(builder.equal(root.get("floor"), params.getFloor()));
            }
            if (params.isForDisabled()) {
                list.add(builder.equal(root.get("forDisabled"), params.isForDisabled()));
            }
            return builder.and(list.toArray(Predicate[]::new));
        };
    }
}

package com.example.demo.specification;

import com.example.demo.DTO.filter.CarFilterRequest;
import com.example.demo.model.Brand;
import com.example.demo.model.BrandType;
import com.example.demo.model.Car;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification {
    public static Specification<Car> buildSpecification(CarFilterRequest request) {
        Specification<Car> spec = Specification.where(null);
        if (!Boolean.TRUE.equals(request.getIsAdmin()) && request.getOwner() == null) {
            throw new IllegalArgumentException("Owner is required for non-admin access");
        }

        if (request.getBrandId() != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("brand").get("id"), request.getBrandId()));
        }

        if (request.getMfg() != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("mfg"), request.getMfg()));
        }

        if (request.getPrice() != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("price"), request.getPrice()));
        }

        if (request.getOwner() != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("owner"), request.getOwner()));
        }


        return spec;
    }


    public static Specification<Car> specialCondition() {
        return (root, query, cb) -> {
            Join<Car, Brand> brandJoin = root.join("brand");
            var highPriceAndBrandStartsWithS = cb.and(
                    cb.greaterThan(root.get("price"), 10000000L),
                    cb.like(cb.upper(brandJoin.get("name")), "S%")
            );
            var lowPriceAndBusType = cb.and(
                    cb.lessThanOrEqualTo(root.get("price"), 10000000L),
                    cb.equal(brandJoin.get("carType"), BrandType.BUS)
            );

            return cb.or(highPriceAndBrandStartsWithS, lowPriceAndBusType);
        };
    }
}

package com.example.demo.specification;

import com.example.demo.model.Brand;
import com.example.demo.model.BrandType;
import com.example.demo.model.Car;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class CarSpecification {
    public static Specification<Car> hasBrandId(Long brandId) {
        return (root, query, builder) ->
                brandId == null ? null : builder.equal(root.get("brand").get("id"), brandId);
    }

    public static Specification<Car> hasMfg(String mfg) {
        return (root, query, builder) ->
                mfg == null ? null : builder.equal(root.get("mfg"), mfg);
    }

    public static Specification<Car> hasPrice(Long price) {
        return (root, query, builder) ->
                price == null ? null : builder.equal(root.get("price"), price);
    }

    public static Specification<Car> hasOwner(String owner) {
        return (root, query, builder) ->
                owner == null ? null : builder.equal(root.get("owner"), owner);
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

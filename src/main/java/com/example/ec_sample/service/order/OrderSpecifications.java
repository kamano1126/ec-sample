package com.example.ec_sample.service.order;

import com.example.ec_sample.domain.order.Order;
import com.example.ec_sample.domain.order.Status;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpecifications {

    public static Specification<Order> hasStatus(Status status){
        return (root,query,cb) ->
                status == null ? null : cb.equal(root.get("status"),status);

    }
}

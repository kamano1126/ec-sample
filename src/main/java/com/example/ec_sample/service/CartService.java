package com.example.ec_sample.service;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CartService {

    public int addItem(int currentQuantity,int addQuantity){
        int totalQuantity = currentQuantity + addQuantity;
        return totalQuantity;
    }

    public int changeItem(int currentQuantity,int changeQuantity){
        int totalQuantity = currentQuantity + changeQuantity;
        return totalQuantity;
    }

    public int totalAmount(Map<Integer, Integer> priceAndQuantity){

        int total = 0;
        for(Map.Entry<Integer,Integer> entry : priceAndQuantity.entrySet()){
            int price = entry.getKey();
            int quantity = entry.getValue();
            total += price * quantity;
        }

        return total;
    }

}

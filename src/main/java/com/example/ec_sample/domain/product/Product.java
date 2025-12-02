package com.example.ec_sample.domain.product;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.antlr.v4.runtime.misc.NotNull;
import org.aspectj.bridge.IMessage;


import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
//ポスグレ側も"P"から”p”へ変更する
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//＠NotBlank、＠NotNullを使えるようにする
//メッセージ”商品名は必須です”等を表示させる
    @Column(nullable = false)
    private String name;

    @Column(name = "image_path")
    private String imagePath;

    private Integer price;

    private String description;

    private Integer stock;

    private String size;

    //"category","period"をenumに変更する(絞り込みできるようにするため)
    private String category;

    private String period;

    private String brand;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "delete_date")
    private LocalDate deleteDate;
}

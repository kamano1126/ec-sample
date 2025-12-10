package com.example.ec_sample.domain.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
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
    @NotBlank(message = "商品名は必須です")
    private String name;

    @Column(name = "image_path")
    private String imagePath;


    private Integer price;

    private String description;

    private Integer stock;

    private String size;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "カテゴリーは必須です")
    private Category category;

    @Enumerated(EnumType.STRING)
    private Period period;

    @Enumerated(EnumType.STRING)
    private Brand brand;

    @Column(name = "join_date")
    private LocalDate joinDate;

    @Column(name = "delete_date")
    private LocalDate deleteDate;
}

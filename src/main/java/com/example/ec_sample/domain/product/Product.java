package com.example.ec_sample.domain.product;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.bridge.IMessage;


import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "商品名は必須です")
    @Size(max = 100)
    private String name;

    @Column(name = "image_path")
    private String imagePath;

    @NotNull(message="価格は必須です")
    @Min(value = 0,message="価格は0以上で入力してください")
    private Integer price;

    @NotBlank(message = "説明は必須です")
    @Size(max = 300,message = "説明は300文字以内で入力してください")
    private String description;

    @NotNull(message = "在庫は必須です")
    @Min(value = 0,message = "在庫は0以上で入力してください")
    private Integer stock;

    @NotBlank(message = "サイズは必須です")
    @Size(max = 50)
    private String size;


    @Enumerated(EnumType.STRING)
    @NotNull(message = "カテゴリーは必須です")
    private Category category;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "年代は必須です")
    private Period period;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "ブランドは必須です")
    private Brand brand;

    @Column(name = "join_date")
    @NotNull(message = "登録日は必須です")
    private LocalDate joinDate;

    @Column(name = "delete_date")
    private LocalDate deleteDate;
}

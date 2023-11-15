package org.project.dev.category;

import org.hibernate.annotations.ColumnDefault;
import org.project.dev.utils.BaseEntity;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "category")
public class CategoryEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private String categoryTitle;

    @ColumnDefault("0")
    private int isDisplay; //기본 값 0 = 비노출
}

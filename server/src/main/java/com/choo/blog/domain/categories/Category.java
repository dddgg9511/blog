package com.choo.blog.domain.categories;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {
    @Id @GeneratedValue
    private Long id;

    private String title;

    private int depth;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_parent")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> childrens;
}

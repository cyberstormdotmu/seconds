package com.ishoal.core.persistence.entity;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity(name = "Category")
@Table(name = "CATEGORIES")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CATEGORY_NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID", referencedColumnName = "ID")
    private CategoryEntity parent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    private List<CategoryEntity> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    public List<CategoryEntity> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryEntity> children) {
        this.children = children;
    }
    public  CategoryEntity(CategoryEntity builder) {
        name = builder.name;
        parent = builder.parent;
        id=builder.id;
    }
    public  CategoryEntity() {
 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof CategoryEntity)) {
            return false;
        }

        CategoryEntity that = (CategoryEntity) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .isEquals();
    }

    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .toHashCode();
    }
    private CategoryEntity(Builder builder) {
        name = builder.name;
        parent = builder.parent;
        id=builder.id;
    }

    public static Builder aCategory() {
        return new Builder();
    }

   
    public static final class Builder {
        private String name;
        private CategoryEntity parent;
        private Long id;
        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder parent(CategoryEntity val) {
            parent = val;
            return this;
        }
       

        public CategoryEntity build() {
            return new CategoryEntity(this);
        }

    }



}

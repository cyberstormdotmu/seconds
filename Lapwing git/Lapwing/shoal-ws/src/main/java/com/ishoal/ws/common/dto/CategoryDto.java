package com.ishoal.ws.common.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
    private String name;
    private List<String> parents;
    private List<String> children;

    private CategoryDto(Builder builder) {
        name = builder.name;
        parents = builder.parents;
        children = builder.children;
    }

    public static Builder aCategory() {
        return new Builder();
    }

    public String getName() {
        return name;
    }

    public List<String> getParents() {
        return parents;
    }

    public List<String> getChildren() {
        return children;
    }

    public static final class Builder {
        private String name;
        private List<String> parents = new ArrayList<>();
        private List<String> children = new ArrayList<>();

        private Builder() {
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder parent(String val) {
            parents.add(0, val);
            return this;
        }

        public Builder child(String val) {
            children.add(val);
            return this;
        }

        public CategoryDto build() {
            return new CategoryDto(this);
        }
    }
}

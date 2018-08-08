package com.tatvasoft.entity;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author TatvaSoft This is entity (Model) class for Category.
 */
@Entity
@Table(name = "category", catalog = "orgnizational_forum")
public class CategoryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "category_id")
	private long categoryid;

	@Column(name = "name")
	private String categoryName;

	@Column(name = "avtar")
	private Blob avtar;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
	private Set<PostEntity> UserPost = new HashSet<PostEntity>(0);

	// Default constructor
	public CategoryEntity() {}

	// Getter and Setter methods
	public long getCategoryid() {
		return categoryid;
	}

	public void setCategoryid(long categoryid) {
		this.categoryid = categoryid;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Blob getAvtar() {
		return avtar;
	}

	public void setAvtar(Blob avtar) {
		this.avtar = avtar;
	}

	public Set<PostEntity> getUserPost() {
		return UserPost;
	}

	public void setUserPost(Set<PostEntity> userPost) {
		UserPost = userPost;
	}

}

package org.androidpn.server.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "userPhoto")
public class UserPhoto {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@Column(name = "user_id")
    private int userId;

	@Column(name = "photo", columnDefinition = "TEXT")
	private String photo;
	
	public Long getId() {
		return id;
	}

	public void setId(
			Long id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(
			int userId) {
		this.userId = userId;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(
			String photo) {
		this.photo = photo;
	}
}

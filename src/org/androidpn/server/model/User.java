package org.androidpn.server.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

@Entity
@Table(name = "user")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "username", nullable = false, length = 64, unique = true)
    private String username;

    @Column(name = "password", length = 64)
    private String password;

    @Column(name = "email", length = 64)
    private String email;

    @Column(name = "name", length = 64)
    private String name;

    @Column(name = "created_date", updatable = false)
    private Date createdDate = new Date();

    @Column(name = "updated_date")
    private Date updatedDate;
    
    @Column(name = "birthday")
    private long birthday;
    
    @Column(name = "partner")
    private int partner;
    
    @Column(name = "gender")
    private boolean gender;
    
    @Transient
    private boolean online;
    
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<OnlineRecord> onlineRecords;
    
	@OneToMany(mappedBy="user",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<PushRecord> pushRecords;
    
    public List<OnlineRecord> getOnlineRecords() {
		return onlineRecords;
	}

	public void setOnlineRecords(List<OnlineRecord> onlineRecords) {
		this.onlineRecords = onlineRecords;
	}


    public User() {
    }

    public User(final String username) {
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }
    
    

    public long getBirthday() {
		return birthday;
	}

	public void setBirthday(
			long birthday) {
		this.birthday = birthday;
	}

	public int getPartner() {
		return partner;
	}

	public void setPartner(
			int partner) {
		this.partner = partner;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(
			boolean gender) {
		this.gender = gender;
	}

	@Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) {
            return false;
        }

        final User obj = (User) o;
        if (username != null ? !username.equals(obj.username)
                : obj.username != null) {
            return false;
        }
        if (!(createdDate.getTime() == obj.createdDate.getTime())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 29 * result + (username != null ? username.hashCode() : 0);
        result = 29 * result
                + (createdDate != null ? createdDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

}

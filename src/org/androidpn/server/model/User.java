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

/** 
 * This class represents the basic user object.
 *
 */
@Entity
@Table(name = "apn_user")
public class User implements Serializable {

    private static final long serialVersionUID = 4733464888738356502L;

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

    @Transient
    private boolean online;
    
    /**
     * Add extra column for user
     * @author ade
     */
    @Column( name = "applist", length=5000 )
    private String appList;
    @Column( name = "isRoot", length=20 )
    private String isRoot;
    @Column( name = "adminPolicy", length=2000 )
    private String adminPolicy;
    @Column( name = "simInfo", length=1000 )
    private String simInfo;
    @Column( name = "sysList", length=1000 )
    private String sysList;
    @Column( name = "locationGPS", length=1000 )
    private String locationGPS;
    @Column( name = "locationWIFI", length=1000 )
    private String locationWIFI;
    @Column( name = "androidID", length=100 )
    private String androidID;
    @Column( name = "cpuID", length=1000 )
    private String cpuID;
    @Column( name = "telID", length=100 )
    private String telID;
    
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

	public String getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(String isRoot) {
		this.isRoot = isRoot;
	}

	public String getAdminPolicy() {
		return adminPolicy;
	}

	public void setAdminPolicy(String adminPolicy) {
		this.adminPolicy = adminPolicy;
	}

	public String getSimInfo() {
		return simInfo;
	}

	public void setSimInfo(String simInfo) {
		this.simInfo = simInfo;
	}

	public String getSysList() {
		return sysList;
	}

	public void setSysList(String sysList) {
		this.sysList = sysList;
	}

	public String getLocationGPS() {
		return locationGPS;
	}

	public void setLocationGPS(String locationGPS) {
		this.locationGPS = locationGPS;
	}

	public String getLocationWIFI() {
		return locationWIFI;
	}

	public void setLocationWIFI(String locationWIFI) {
		this.locationWIFI = locationWIFI;
	}

	public String getAndroidID() {
		return androidID;
	}

	public void setAndroidID(String androidID) {
		this.androidID = androidID;
	}

	public String getCpuID() {
		return cpuID;
	}

	public void setCpuID(String cpuID) {
		this.cpuID = cpuID;
	}

	public String getTelID() {
		return telID;
	}

	public void setTelID(String telID) {
		this.telID = telID;
	}

	public String getAppList() {
		return appList;
	}

	public void setAppList(String appList) {
		this.appList = appList;
	}

	/**
     * end extra column
     */

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

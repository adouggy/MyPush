package org.androidpn.server.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlTransient;

import me.promenade.xmpp.bean.PushStatus;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/** 
 * This class represents the pushRecord object.
 *
 */
@Entity
@Table(name = "push_record")
public class PushRecord implements Serializable {
	private static final long serialVersionUID = 265880172087619111L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@ManyToOne
	@JoinColumn(name="user")
	@XmlTransient
	private User user;

	@ManyToOne
	@JoinColumn(name="message")
	@XmlTransient
	private Message message;
	
	@Enumerated(EnumType.STRING)
    @Column(name = "status", unique = false)
    private PushStatus status;

	@Column(name = "update_date", updatable = false)
    private Date updateDate = new Date();
        
    public PushRecord() {
    }

    public PushRecord(Message message, User user, PushStatus status) {
        this.user = user;
        this.message = message;
        this.status = status;
    }

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public PushStatus getStatus() {
		return status;
	}

	public void setStatus(PushStatus status) {
		this.status = status;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PushRecord)) {
            return false;
        }

        final PushRecord obj = (PushRecord) o;
        if (message != null ? !message.equals(obj.message)
                : obj.message != null) {
            return false;
        }
        if (user != null ? !user.equals(obj.user)
                : obj.user != null) {
            return false;
        }
        if (status != null ? !status.equals(obj.status)
                : obj.status != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 0;
        result = 29 * result + (message != null ? message.hashCode() : 0);
        result = 29 * result + (user != null ? user.hashCode() : 0);
        result = 29 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }

}

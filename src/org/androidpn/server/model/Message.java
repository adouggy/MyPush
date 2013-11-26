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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/** 
 * This class represents the message object.
 *
 */
@Entity
@Table(name = "message")
public class Message implements Serializable {

	private static final long serialVersionUID = 2888579151723537808L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	@Column(name = "message", nullable = false, unique = false, length=2000)
    private String message;

    @Column(name = "created_date", updatable = false)
    private Date createdDate = new Date();

    @OneToMany(mappedBy="message",cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<PushRecord> pushRecords;
    
    public Message() {
    }

    public Message(String message, Date createdDate) {
        this.message = message;
        this.createdDate = createdDate;
    }


    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public List<PushRecord> getPushRecords() {
		return pushRecords;
	}

	public void setPushRecords(List<PushRecord> pushRecords) {
		this.pushRecords = pushRecords;
	}


	@Override
    public boolean equals(Object o) {
        if (!(o instanceof Message)) {
            return false;
        }

        final Message obj = (Message) o;
        if (message != null ? !message.equals(obj.message)
                : obj.message != null) {
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
        result = 29 * result + (message != null ? message.hashCode() : 0);
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

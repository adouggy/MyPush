package org.androidpn.server.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * 
 * A bean for synchronize demo
 * 
 * @author ade
 *
 */
@Entity
@Table(name = "demo_table")
public class KeyValueBean implements Serializable {

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	private static final long serialVersionUID = 4733464888738356502L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "demo_key", nullable = false, length = 64, unique = false)
    private String key;

    @Column(name = "demo_value", length = 64)
    private String value;
   

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof KeyValueBean)) {
            return false;
        }

        final KeyValueBean obj = (KeyValueBean) o;
        if (key != null ? !key.equals(obj.key)
                : obj.key != null) {
            return false;
        }
        
        if (value != null ? !value.equals(obj.value)
                : obj.value != null) {
            return false;
        }
        
        return true;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
//                ToStringStyle.MULTI_LINE_STYLE);
        		ToStringStyle.SIMPLE_STYLE);
    }

}

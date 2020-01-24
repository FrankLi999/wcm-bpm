package com.bpwizard.config.domain;

import java.io.Serializable;

import javax.persistence.Entity;
// import javax.persistence.GeneratedValue;
// import javax.persistence.GenerationType;
import javax.persistence.Id;
// import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.IdClass;
// import lombok.Data;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * @author TTDKOC
 *
 */
@Entity
@Table(name="properties")
// @Data
@IdClass(PropertyId.class)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Properties implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6747531716778688077L;
	
	// @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PROPERTIES")
	// @SequenceGenerator(name="SEQ_PROPERTIES", schema="CONFIG", sequenceName="SEQ_PROPERTIES_ID", allocationSize=1)
	
	// @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	// @GenericGenerator(name = "native", strategy = "native")
	// @Id
	// @GeneratedValue(strategy = GenerationType.IDENTITY)
	// @Column(name = "id", updatable = false, nullable = false)
	// private Long id;
    @Id
	@Column(name = "APPLICATION")
	private String application;

	@Id
	@Column(name = "PROFILE", updatable = false, nullable = false)
	private String profile;

	@Id
	@Column(name = "LABEL", updatable = false, nullable = false)
	private String label;

	@Id
	@Column(name = "PROP_KEY", updatable = false, nullable = false)
	private String key;
	
	@Column(name = "VALUE", updatable = false, nullable = false)
	private String value;

	@Column(name = "CREATED_ON")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdOn;
}
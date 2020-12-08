package com.bpwizard.spring.boot.commons.domain;

import java.io.Serializable;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.AuditorAware;

import com.bpwizard.spring.boot.commons.security.UserDto;

/**
 * Needed for auto-filling of the
 * AbstractAuditable columns of AbstractUser
 */
public abstract class AbstractAuditorAware<ID extends Serializable>
implements AuditorAware<ID> {

	private static Logger logger = LoggerFactory.getLogger(AbstractAuditorAware.class);
    
    private IdConverter<ID> idConverter;
    
    @Autowired
	public void setIdConverter(IdConverter<ID> idConverter) {
		
		this.idConverter = idConverter;
		logger.info("Created");
	}

	protected abstract UserDto currentUser();
	
	@Override
	public Optional<ID> getCurrentAuditor() {
		
		UserDto user = currentUser();
		
		if (user == null)
			return Optional.empty();
		
		return Optional.of(idConverter.toId(user.getId()));
	}	
}

package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;

import org.camunda.bpm.engine.identity.UserQuery;
import org.camunda.bpm.engine.impl.QueryOrderingProperty;
import org.camunda.bpm.engine.impl.UserQueryProperty;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.bpwizard.camunda.identity.boot.plugin.SpringUserQuery;

@Repository
public class CustomeSpringUserRepositoryImpl extends CustomeSpringIdentityRepositoryImpl<SpringUser, UserQuery>
		implements CustomeSpringUserRepository {

	@Override
	public SpringUser findById(String id) {
		return entityManager.find(SpringUser.class, id);
	}

	protected Query buildSearchQuery(SpringUserQuery q) {
		StringBuilder jpql = new StringBuilder("SELECT u FROM SpringUser u");
		Map<String, Object> parameters = this.addSearchCriteria(jpql, q);

		Query query = this.entityManager.createQuery(jpql.toString(), SpringUser.class);
		for (String param : parameters.keySet()) {
			query.setParameter(param, parameters.get(param));
		}
		if (q.getMaxResults() > 0) {
			query.setMaxResults(q.getMaxResults());
		}

		if (q.getFirstResult() > 0) {
			query.setFirstResult(q.getMaxResults());
		}
		return query;
	}

	protected Query buildCountQuery(SpringUserQuery q) {
		StringBuilder jpql = new StringBuilder("SELECT count(u.id) FROM  usr u ");
		Map<String, Object> parameters = this.addSearchCriteria(jpql, q);
		Query query = this.entityManager.createQuery(jpql.toString(), SpringUser.class);
		for (String param : parameters.keySet()) {
			query.setParameter(param, parameters.get(param));
		}
		return query;
	}

	private Map<String, Object> addSearchCriteria(StringBuilder jpql, SpringUserQuery q) {
		// Inner Joins
		if (StringUtils.hasText(q.getGroupId())) {
			jpql.append(" INNER JOIN u.groups g");
		}

		if (StringUtils.hasText(q.getTenantId())) {
			jpql.append(" INNER JOIN u.tenants t");
		}

		// Where clause
		boolean useAnd = false;
		Map<String, Object> parameters = new HashMap<>();
		StringBuilder jpqlWhere = new StringBuilder("");
		if (StringUtils.hasText(q.getId())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.id = :id");
			parameters.put("id", q.getId());
		}

		if (q.getIds() != null && q.getIds().length > 0) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.id IN (:ids)");
			parameters.put("ids", q.getIds());
		}

		if (StringUtils.hasText(q.getFirstName())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.firstName = :firstName");
			parameters.put("firstName", q.getFirstName());
		}

		if (StringUtils.hasText(q.getFirstNameLike())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.firstName Like %:firstNameLike%");
			parameters.put("firstNameLike", q.getFirstNameLike());
		}

		if (StringUtils.hasText(q.getLastName())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.lastName = :lastName");
			parameters.put("lastName", q.getLastName());
		}

		if (StringUtils.hasText(q.getLastNameLike())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.lastName Like %:lastNameLike%");
			parameters.put("lastNameLike", q.getLastNameLike());
		}

		if (StringUtils.hasText(q.getEmail())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.email = :email");
			parameters.put("email", q.getEmail());
		}

		if (StringUtils.hasText(q.getEmailLike())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.email Like %:emailLike%");
			parameters.put("emailLike", q.getEmailLike());
		}

		if (StringUtils.hasText(q.getGroupId())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" g.id = :gid");
			parameters.put("gid", q.getGroupId());
		}

		if (StringUtils.hasText(q.getTenantId())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" t.id = :tid");
			parameters.put("tid", q.getTenantId());
		}

		if (jpqlWhere.length() > 0) {
			jpql.append(" WHERE").append(jpqlWhere);
		}

		// Order By
		StringBuilder orderBy = new StringBuilder("");
		int orderByCount = 0;
		for (QueryOrderingProperty orderByProp : q.getOrderingProperties()) {
			if (orderByCount > 0) {
				orderBy.append(",");
			}
			if (orderByProp.getQueryProperty().equals(UserQueryProperty.USER_ID)) {
				orderBy.append(" u.id");
			} else if (orderByProp.getQueryProperty().equals(UserQueryProperty.EMAIL)) {
				orderBy.append(" u.email");
			} else if (orderByProp.getQueryProperty().equals(UserQueryProperty.FIRST_NAME)) {
				orderBy.append(" u.firstName");
			} else if (orderByProp.getQueryProperty().equals(UserQueryProperty.LAST_NAME)) {
				orderBy.append(" u.lastName");
			} else {
				orderBy.append(" u.").append(orderByProp.getQueryProperty().getName());
			}
			if (orderByProp.getDirection() != null) {
				orderBy.append(" ").append(orderByProp.getDirection().getName());
			}
			orderByCount++;
		}
		if (orderBy.length() > 0) {
			jpql.append(" ORDER BY").append(jpqlWhere);
		}
		return parameters;

	}
}

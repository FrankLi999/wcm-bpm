package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;

import org.camunda.bpm.engine.identity.TenantQuery;
import org.camunda.bpm.engine.impl.QueryOrderingProperty;
import org.camunda.bpm.engine.impl.TenantQueryProperty;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.bpwizard.camunda.identity.boot.plugin.SpringTenantQuery;

//https://github.com/eugenp/tutorials/tree/master/persistence-modules/spring-data-jpa/src/main/java/com/baeldung/boot/daos/impl
@Repository
public class CustomeSpringTenantRepositoryImpl extends CustomeSpringIdentityRepositoryImpl<SpringTenant, TenantQuery>
		implements CustomeSpringTenantRepository {
	@Override
	public SpringTenant findById(String id) {
		return entityManager.find(SpringTenant.class, id);
	}

	protected Query buildSearchQuery(SpringTenantQuery q) {
		StringBuilder jpql = new StringBuilder("SELECT t FROM SpringTenant t");
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

	protected Query buildCountQuery(SpringTenantQuery q) {
		StringBuilder jpql = new StringBuilder("SELECT count(distinct t.id) FROM SpringTenant t");
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

	private Map<String, Object> addSearchCriteria(StringBuilder jpql, SpringTenantQuery q) {
		// Inner Joins
		if (StringUtils.hasText(q.getUserId())) {
			jpql.append(" INNER JOIN t.users u");
		}

		if (StringUtils.hasText(q.getGroupId())) {
			jpql.append(" INNER JOIN t.groups g");
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
			jpqlWhere.append(" t.id = :id");
			parameters.put("id", q.getId());
		}

		if (q.getIds() != null && q.getIds().length > 0) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" t.id IN (:ids)");
			parameters.put("ids", q.getIds());
		}

		if (StringUtils.hasText(q.getName())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" t.name = :name");
			parameters.put("name", q.getName());
		}

		if (StringUtils.hasText(q.getNameLike())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" t.nameLike Like %:nameLike%");
			parameters.put("nameLike", q.getNameLike());
		}

		if (StringUtils.hasText(q.getUserId())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.id = :uid");
			parameters.put("uid", q.getUserId());
		}

		if (StringUtils.hasText(q.getGroupId())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" g.id = :tid");
			parameters.put("gid", q.getGroupId());
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
			if (orderByProp.getQueryProperty().equals(TenantQueryProperty.GROUP_ID)) {
				orderBy.append(" t.id");
			} else if (orderByProp.getQueryProperty().equals(TenantQueryProperty.NAME)) {
				orderBy.append(" t.name");
			} else {
				orderBy.append(" t.").append(orderByProp.getQueryProperty().getName());
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

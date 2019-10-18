package com.bpwizard.wcm.repo.camunda.identity.domain;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Query;

import org.camunda.bpm.engine.identity.GroupQuery;
import org.camunda.bpm.engine.impl.GroupQueryProperty;
import org.camunda.bpm.engine.impl.QueryOrderingProperty;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.bpwizard.camunda.identity.boot.plugin.SpringGroupQuery;

@Repository
public class CustomeSpringGroupRepositoryImpl extends CustomeSpringIdentityRepositoryImpl<SpringGroup, GroupQuery>
		implements CustomeSpringGroupRepository {

	@Override
	public SpringGroup findById(String id) {
		return entityManager.find(SpringGroup.class, id);
	}

	protected Query buildSearchQuery(SpringGroupQuery q) {
		StringBuilder jpql = new StringBuilder("SELECT g FROM SpringGroup g");
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

	protected Query buildCountQuery(SpringGroupQuery q) {
		StringBuilder jpql = new StringBuilder("SELECT count(distinct g.id) FROM SpringGroup g");
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

	private Map<String, Object> addSearchCriteria(StringBuilder jpql, SpringGroupQuery q) {
		// Inner Joins
		if (StringUtils.hasText(q.getUserId())) {
			jpql.append(" INNER JOIN g.users u");
		}

		if (StringUtils.hasText(q.getTenantId())) {
			jpql.append(" INNER JOIN g.tenants t");
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
			jpqlWhere.append(" g.id = :id");
			parameters.put("id", q.getId());
		}

		if (q.getIds() != null && q.getIds().length > 0) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" g.id IN (:ids)");
			parameters.put("ids", q.getIds());
		}

		if (StringUtils.hasText(q.getName())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" g.name = :name");
			parameters.put("name", q.getName());
		}

		if (StringUtils.hasText(q.getNameLike())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" g.nameLike Like %:nameLike%");
			parameters.put("nameLike", q.getNameLike());
		}

		if (StringUtils.hasText(q.getType())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" g.type = :type");
			parameters.put("type", q.getType());
		}
		
		if (StringUtils.hasText(q.getUserId())) {
			if (useAnd) {
				jpqlWhere.append(" AND");
			}
			useAnd = true;
			jpqlWhere.append(" u.id = :uid");
			parameters.put("uid", q.getUserId());
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
			if (orderByProp.getQueryProperty().equals(GroupQueryProperty.GROUP_ID)) {
				orderBy.append(" g.id");
			} else if (orderByProp.getQueryProperty().equals(GroupQueryProperty.NAME)) {
				orderBy.append(" g.name");
			} else if (orderByProp.getQueryProperty().equals(GroupQueryProperty.TYPE)) {
				orderBy.append(" g.type");
			} else {
				orderBy.append(" g.").append(orderByProp.getQueryProperty().getName());
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
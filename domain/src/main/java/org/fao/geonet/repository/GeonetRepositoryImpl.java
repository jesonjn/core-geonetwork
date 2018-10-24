/*
 * Copyright (C) 2001-2016 Food and Agriculture Organization of the
 * United Nations (FAO-UN), United Nations World Food Programme (WFP)
 * and United Nations Environment Programme (UNEP)
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301, USA
 *
 * Contact: Jeroen Ticheler - FAO - Viale delle Terme di Caracalla 2,
 * Rome - Italy. email: geonetwork@osgeo.org
 */

package org.fao.geonet.repository;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.fao.geonet.domain.GeonetEntity;
import org.jdom.Element;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

/**
 * Abstract super class of Geonetwork repositories that contains extra useful implementations.
 *
 * @param <T>  The entity type
 * @param <ID> The entity id type
 *             <p/>
 *             User: jeichar Date: 9/5/13 Time: 11:26 AM
 */
public abstract class GeonetRepositoryImpl<T extends GeonetEntity, ID extends Serializable> 
	implements GeonetRepository<T, ID> {
    
	@PersistenceContext
    private EntityManager _entityManager;
	private final Class<T> _entityClass = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass()).getActualTypeArguments()[0];

    protected static <T extends GeonetEntity> Element findAllAsXml(EntityManager entityManager, Class<T> entityClass,
                                                                   Specification<T> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = cb.createQuery(entityClass);
        Root<T> root = query.from(entityClass);

        if (specification != null) {
            final Predicate predicate = specification.toPredicate(root, query, cb);
            query.where(predicate);
        }

        if (pageable != null) {
            if (pageable.getSort() != null) {
                List<Order> orders = SortUtils.sortToJpaOrders(cb, pageable.getSort(), root);
                query.orderBy(orders);
            }
        }

        Element rootEl = new Element(entityClass.getSimpleName().toLowerCase());

        final TypedQuery<T> typedQuery = entityManager.createQuery(query);
        if (pageable != null) {
            typedQuery.setFirstResult((int) pageable.getOffset());
            typedQuery.setMaxResults(pageable.getPageSize());
        }
        for (T t : typedQuery.getResultList()) {
            rootEl.addContent(t.asXml());
        }
        return rootEl;
    }

    @Transactional
    public T update(ID id, Updater<T> updater) {
        final T entity = _entityManager.find(this._entityClass, id);

        if (entity == null) {
            throw new EntityNotFoundException("No entity found with id: " + id);
        }

        updater.apply(entity);

        _entityManager.persist(entity);
        _entityManager.flush();

        return entity;
    }

    @Override
    public <V> BatchUpdateQuery<T> createBatchUpdateQuery(PathSpec<T, V> pathToUpdate, V newValue) {
        return new BatchUpdateQuery<T>(_entityClass, _entityManager, pathToUpdate, newValue);
    }

    @Override
    public <V> BatchUpdateQuery<T> createBatchUpdateQuery(PathSpec<T, V> pathToUpdate, V newValue, Specification<T> spec) {
        final BatchUpdateQuery<T> updateQuery = new BatchUpdateQuery<T>(_entityClass, _entityManager, pathToUpdate, newValue);
        updateQuery.setSpecification(spec);
        return updateQuery;
    }

    @Nonnull
    @Override
    public Element findAllAsXml() {
        return findAllAsXml(null, (Pageable) null);
    }

    @Nonnull
    @Override
    public Element findAllAsXml(final Specification<T> specification) {
        return findAllAsXml(specification, (Pageable) null);
    }

    @Nonnull
    @Override
    public Element findAllAsXml(@Nullable Pageable pageable) {
        return findAllAsXml(null, pageable);
    }

    @Nonnull
    @Override
    public Element findAllAsXml(@Nullable Specification<T> specification, @Nullable Pageable pageable) {
        return findAllAsXml(_entityManager, _entityClass, specification, pageable);
    }

    @Nonnull
    @Override
    public Element findAllAsXml(final Sort sort) {
        return findAllAsXml(null, sort);
    }

    @Override
    @Transactional
    public int deleteAll(@Nonnull Specification<T> specification) {
        final CriteriaBuilder cb = _entityManager.getCriteriaBuilder();
        final CriteriaDelete<T> criteriaDelete = cb.createCriteriaDelete(_entityClass);
        final Root<T> from = criteriaDelete.from(_entityClass);
        final Predicate predicate = specification.toPredicate(from, null, cb);// TODO pass in delete when spring-JPA is updated
        criteriaDelete.where(predicate);

        final Query query = _entityManager.createQuery(criteriaDelete);
        final int deleted = query.executeUpdate();

        _entityManager.clear();
        return deleted;
    }

    @Nonnull
    @Override
    @Transactional
    public Element findAllAsXml(final Specification<T> specification, final Sort sort) {
        PageRequest request = new PageRequest(0, Integer.MAX_VALUE, sort);
        return findAllAsXml(_entityManager, _entityClass, specification, request);
    }
    
    @Override
    public T findOne(ID id) {
    	return _entityManager.find(_entityClass, id);
    }

	@Override
	public boolean exists(ID id) {
		return findOne(id) != null;
	}

	@Override
	public void delete(ID id) {
		T elementToRemove = findOne(id);
		if (elementToRemove != null) {
			_entityManager.remove(elementToRemove);
		}
	}
}

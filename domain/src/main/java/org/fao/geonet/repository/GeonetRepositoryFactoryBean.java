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

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;


/**
 * The factory that allows all Geonetwork repositories to automatically get the functionality from
 * GeonetRepository if they extend that interface.
 *
 * @param <R> The repository type
 * @param <T> The entity type the repository returns
 * @param <I> the id type of the entity
 */
public class GeonetRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, I> {

    public GeonetRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
	}
//
//	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
//
//        return new GeonetRepositoryFactory(entityManager);
//    }

//    private static class GeonetRepositoryFactory<T, I extends Serializable> extends JpaRepositoryFactory {
//
//        private EntityManager entityManager;
//
//        public GeonetRepositoryFactory(EntityManager entityManager) {
//            super(entityManager);
//
//            this.entityManager = entityManager;
//        }
//
//    	protected final JpaRepositoryImplementation<?, ?>  getTargetRepository(RepositoryInformation metadata, EntityManager em) {
//    		
//
//            Class<?> repositoryInterface = metadata.getRepositoryInterface();
//            
//            if(metadata.getRepositoryInterface().toString().equals("interface org.fao.geonet.repository.MetadataRepository"))
//            {
//            	System.out.println("lskdjflsjflsdkj");
//            }
//
//            if (isQueryDslExecutor(repositoryInterface)) {
//                return super.getTargetRepository(metadata, em);
//            } else if (metadata.getDomainType().equals(HarvesterSetting.class)) {
//                return new HarvesterSettingRepositoryOverridesImpl((Class<HarvesterSetting>) metadata.getDomainType(), em);
//            } else {
//                return new GeonetRepositoryImpl((Class<T>) metadata.getDomainType(), em);
//            }
//        }
//
//        protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
//
//            if (isQueryDslExecutor(metadata.getRepositoryInterface())) {
//                return super.getRepositoryBaseClass(metadata);
//            } else if (metadata.getDomainType().equals(HarvesterSetting.class)) {
//                return HarvesterSettingRepositoryOverridesImpl.class;
//            } else {
//                return GeonetRepositoryImpl.class;
//            }
//        }
//
//        /**
//         * Returns whether the given repository interface requires a QueryDsl specific
//         * implementation to be chosen.
//         */
//        private boolean isQueryDslExecutor(Class<?> repositoryInterface) {
//
//            return QUERY_DSL_PRESENT && QuerydslPredicateExecutor.class.isAssignableFrom(repositoryInterface);
//        }
//
//    }
}

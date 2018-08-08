/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cubeia.backoffice.operator.service.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

public class AbstractDAO {

    @PersistenceContext(unitName = "operatorPersistenceUnit")
    protected EntityManager em;
    
    @Transactional
    public <T> T save(T o) {
        em.persist(o);
        return o;
    }

    @Transactional
    public <T> T update(T o) {
        return em.merge(o);
    }
    
    public <T> T findById(Class<T> clazz, Long id) {
        return em.find(clazz, id);
    }
    
}

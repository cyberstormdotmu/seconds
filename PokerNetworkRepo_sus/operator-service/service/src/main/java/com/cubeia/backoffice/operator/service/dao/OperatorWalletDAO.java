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

import static org.hibernate.criterion.Restrictions.eq;

import java.util.Collection;
import java.util.List;

import javax.persistence.Query;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.operator.api.OperatorWalletRequestOrder;
import com.cubeia.backoffice.operator.service.entity.OperatorWalletRequestLog;
import com.cubeia.backoffice.operator.service.entity.RequestStatus;

@Component
public class OperatorWalletDAO extends AbstractDAO {

//    @SuppressWarnings("unchecked")
//    public List<OperatorWalletRequestLog> findAllByOperatorId(Long operatorId) {
//         Query query = em.createQuery("SELECT x FROM OperatorWalletRequestLog x WHERE x.operatorId = ?1")
//                 .setParameter(1, operatorId);
//        return query.getResultList();
//    }

    public OperatorWalletRequestLog getWalletRequestByRequestId(String uuid) {
        Query query = em.createQuery("SELECT x FROM OperatorWalletRequestLog x WHERE x.requestId = ?1")
                .setParameter(1, uuid);
       return (OperatorWalletRequestLog)query.getSingleResult();
    }
    
    public int countLogEntries(Long operatorId, Collection<RequestStatus> includedStatuses) {
        Criteria c = findLogEntriesCriteria(operatorId, includedStatuses, 0, 0, null, true);
        c.setProjection(Projections.rowCount());
        return ((Number) c.uniqueResult()).intValue();
    }
    
    @SuppressWarnings("unchecked")
    public List<OperatorWalletRequestLog> findLogEntries(Long operatorId, Collection<RequestStatus> includedStatuses, 
        int offset, int limit, OperatorWalletRequestOrder order, boolean ascending) {
        
        Criteria c = findLogEntriesCriteria(operatorId, includedStatuses, offset, limit, order, ascending);
        // always add ascending id order as fallback (or sub ordering)
        c.addOrder(Order.asc("id"));
        return c.list();
    }
    
    private Criteria findLogEntriesCriteria(Long operatorId, Collection<RequestStatus> includedStatuses, int offset, int limit, OperatorWalletRequestOrder order, boolean ascending) {
            Session hbSession = getHibernateSession();
            Criteria c = hbSession.createCriteria(OperatorWalletRequestLog.class);
            c.setFetchMode("events", FetchMode.SELECT);
            
//            CriteriaQuery<OperatorWalletRequestLog> criteriaQuery = em.getCriteriaBuilder().createQuery(OperatorWalletRequestLog.class);
            
            if (operatorId != null) {
                c.add(eq("operatorId", operatorId));
            }
            
            if (includedStatuses != null && !includedStatuses.isEmpty()) {
                c.add(Restrictions.in("status", includedStatuses));
            }
            
            if (order != null) {
                if (ascending) {
                    c.addOrder(Order.asc(order.getColumnName()));
                } else {
                    c.addOrder(Order.desc(order.getColumnName()));
                }
            }
            
            c.setFirstResult(offset);
            c.setMaxResults(limit);
            return c;
        }
    
    private Session getHibernateSession() {
        return (Session) em.getDelegate();
    }
}

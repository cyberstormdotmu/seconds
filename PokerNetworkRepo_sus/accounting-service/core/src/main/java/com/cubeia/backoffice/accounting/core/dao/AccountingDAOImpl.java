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

package com.cubeia.backoffice.accounting.core.dao;

import static com.cubeia.backoffice.accounting.core.domain.TransactionParticipant.Direction.BOTH;
import static com.cubeia.backoffice.accounting.core.domain.TransactionParticipant.Direction.CREDITED;
import static com.cubeia.backoffice.accounting.core.domain.TransactionParticipant.IdentifiactionType.USER_ID;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.cubeia.backoffice.accounting.api.NoSuchAccountException;
import com.cubeia.backoffice.accounting.api.request.AccountQueryRequest;
import com.cubeia.backoffice.accounting.core.domain.AccountsOrder;
import com.cubeia.backoffice.accounting.core.domain.TransactionParticipant;
import com.cubeia.backoffice.accounting.core.domain.TransactionsOrder;
import com.cubeia.backoffice.accounting.core.entity.Account;
import com.cubeia.backoffice.accounting.core.entity.AccountAttribute;
import com.cubeia.backoffice.accounting.core.entity.AccountStatus;
import com.cubeia.backoffice.accounting.core.entity.BalanceCheckpoint;
import com.cubeia.backoffice.accounting.core.entity.CurrencyRate;
import com.cubeia.backoffice.accounting.core.entity.Entry;
import com.cubeia.backoffice.accounting.core.entity.Transaction;
import com.cubeia.backoffice.accounting.core.entity.TransactionAttribute;

@Component("accounting.accountingDAO")
public class AccountingDAOImpl implements AccountingDAO {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @PersistenceContext(unitName = "accountingPersistenceUnit")
    protected EntityManager em;
    
    @Override
    public Account getAccount(Long accountId) {
        return em.find(Account.class, accountId);
    }
    
    @Override
    public BigDecimal getBalanceNaive(Long accountId) {
        return (BigDecimal) em.createQuery("select sum(e.amount) from Entry e where e.account.id = ? ").getResultList().iterator().next();
    }

    @Override
    public void saveOrUpdate(Object object) {
        em.persist(object);
    }

    @Override
    public BalanceCheckpoint getLatestBalanceCheckpoint(final Long accountId, final Long entryId) {
        String tmp = "select bc from BalanceCheckpoint bc where bc.entry.account.id = :accountId";
        if(entryId != null) {
        	tmp += " and bc.entry.id <= :entryId";
        }
        tmp += " order by bc.entry.id desc";
        Query q = em.createQuery(tmp);
    	q.setParameter("accountId", accountId);
    	if(entryId != null) {
    		q.setParameter("entryId", entryId);
    	}
    	q.setMaxResults(1);
    	try {
    		return (BalanceCheckpoint) q.getSingleResult();
    	} catch(NoResultException e) {
    		return null;
    	}
    	
    	/*
    	 * The query below uses Long.MAX_VALUE if entry ID is unspecified, however, this
    	 * is counter productive as it will force a range check on the query optimiser which
    	 * should not be needed. /LJN
    	 */
    	
        /*List<BalanceCheckpoint> resultSet = (List<BalanceCheckpoint>) getJpaTemplate().execute(new JpaCallback() {
            @Override
            public Object doInJpa(EntityManager em) throws PersistenceException {
                Query q = em.createQuery(
                    "select bc from BalanceCheckpoint bc " +
                    "where bc.entry.account.id = :accountId " +
                    "  and bc.entry.id <= :entryId " + 
                    "order by bc.entry.id desc");
                q.setParameter("accountId", accountId);
                q.setParameter("entryId", entryId == null ? Long.MAX_VALUE : entryId);
                q.setMaxResults(1);
                
                return q.getResultList();
            }
        });
        
        return resultSet.size() == 0 ? null : resultSet.get(0);*/
    }
    
	@Override
    public EntrySumAndCount getEntrySumAndCount(Long accountId, Long firstEntryId, Long lastEntryId) {
        /*
         * This particular query is not optimised; the account ID will be looked
         * up via index but the entry ID will be scanned. Which means that the query
         * will scan several thousand rows each time. In order to avoid this a compound
         * index should be used, for example (in MySQL):
         * 
         *     create index `account_entry_idx` on Entry (account_id, id);
         *     
         * This will cause the index storage size to grow and the insertion speed to
         * suffer, but lookups will become very fast. Use if needed. 
         */
    	String tmp = "select sum(e.amount), count(*), max(e.id) from Entry e where e.account.id = :accountId ";
    	if(lastEntryId != null) {
    		tmp += "and e.id between :firstEntryId and :lastEntryId";
    	} else {
    		tmp += "and e.id >= :firstEntryId";
    	}
    	Query q = em.createQuery(tmp);
    	q.setParameter("accountId", accountId);
        q.setParameter("firstEntryId", firstEntryId);
        if(lastEntryId != null) {
        	q.setParameter("lastEntryId", lastEntryId);
        }
    	
    	/*Map<String,Object> params = new HashMap<String, Object>();
    	params.put("accountId", accountId);
        params.put("firstEntryId", firstEntryId);
        params.put("lastEntryId", lastEntryId);
        Object[] result = (Object[]) getJpaTemplate().
            findByNamedParams(
                "select sum(e.amount), count(*), max(e.id) from Entry e " +
                    "where e.account.id = :accountId " +
                    "and e.id between :firstEntryId and :lastEntryId ",
                params
            ).iterator().next();*/
        
        Object[] arr = (Object[]) q.getSingleResult();
        BigDecimal sum = (BigDecimal) arr[0];
        Long count = (Long) arr[1];
        Long maxEntryId = (Long) arr[2];
        
        sum = sum == null ? BigDecimal.ZERO : sum;
        count = count == null ? 0 : count;
        
        return new EntrySumAndCount(sum, count, maxEntryId);
    }

    @Override
    public Entry getEntry(Long entryId) {
        return em.find(Entry.class, entryId);
    }

    @Override
    public Transaction getTransaction(Long txId) {
        return em.find(Transaction.class, txId);
    }
    
    @Override
	public Transaction getTransactionByExternalId(String extId) {
    	Query q = em.createQuery("from Transaction t where t.externalId = :extId");
    	q.setParameter("extId", extId);
    	try {
    		return (Transaction) q.getSingleResult();
    	} catch(NoResultException e) {
    		return null;
    	}
    }

    public List<Transaction> getTransactions(
        Long fromAccountId,
        Long toAccountId) {
        
        TypedQuery<Transaction> q = em.createQuery("select tx from Transaction tx, Entry e1, Entry e2 where " +
            "tx = e1.transaction and " +
            "tx = e2.transaction and " +
            "e1.amount < 0 and " +
            "e2.amount >= 0 and " +
            "e1 != e2 and " +
            "e1.account.id = :fromAccountId and " +
            "e2.account.id = :toAccountId " +
            "order by tx.id ", Transaction.class);
        
        q.setParameter("fromAccountId", fromAccountId);
        q.setParameter("toAccountId", toAccountId);
        
        return q.getResultList();
    }

    @Override
	@SuppressWarnings("unchecked")
	public Collection<Account> findAccountsByUserId(Long userId) {
	    Query q = em.createQuery("from Account a where a.userId = :xId");
	    q.setParameter("xId", userId);
	    q.setHint("org.hibernate.cacheable", true);
	    return q.getResultList();
    }
    
    @Override
	@SuppressWarnings("unchecked")
	public Collection<Account> findAccountsByUserAndWalletId(Long userId, Long walletId) {
	    Query q = em.createQuery("from Account a where a.userId = :uId and a.walletId = :wId");
	    q.setParameter("uId", userId);
	    q.setParameter("wId", walletId);
	    q.setHint("org.hibernate.cacheable", true);
	    return q.getResultList();
	}

    @SuppressWarnings("unchecked")
    @Override
    public List<Account> listAccounts(Long accountId, Long userId, Long walletId, String currencyCode, Collection<AccountStatus> includedStatuses, 
        Collection<String> includeTypes, int offset, int limit, AccountsOrder order, boolean ascending) {
        
        if (limit == 0) {
            return new ArrayList<Account>();
        }
        
        Query q = createFindAccountsQuery(
            accountId, userId, walletId,
            currencyCode, includedStatuses, includeTypes, 
            offset, limit, 
            order, ascending, false);
        
        return q.getResultList();
    }

    @Override
    public long countAccounts(Long accountId, Long userId, Long walletId, String currencyCode, 
        Collection<AccountStatus> includedStatuses, Collection<String> includeTypes) {

        Query q = createFindAccountsQuery(
            accountId, userId, walletId, 
            currencyCode, includedStatuses, includeTypes, 
            0, Integer.MAX_VALUE, null, true, true);
        
        return ((Number) q.getSingleResult()).longValue();
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Entry> listEntries(Long accountId, int offset,
        int limit, boolean ascending) {
        
        if (limit == 0) {
            return new ArrayList<Entry>();
        }
        
        String sql = "from Entry ";
        if(accountId != null) {
        	sql += "where account.id = :accountId ";
        }
        
        if (ascending) {
        	sql += "order by id asc";
        } else {
        	sql += "order by id desc";
        }
        
        Query query = em.createQuery(sql);
        if (accountId != null) {
        	query.setParameter("accountId", accountId);
        }
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        
        return query.getResultList();
    }
    
    @Override
    public long countEntries(Long accountId) {
        String hql = "select count(*) from Entry where account.id = :accountId";
        Query query = em.createQuery(hql);
        query.setParameter("accountId", accountId);
        query.setMaxResults(0);
        Number count = (Number) query.getSingleResult();
        return count == null ? 0L : count.longValue();
    }
    
    private Query createFindAccountsQuery(Long accountId, Long userId, Long walletId, String currencyCode, 
        Collection<AccountStatus> includeStatuses,
        Collection<String> includeTypes, int offset, int limit, AccountsOrder order, boolean ascending, boolean countProjection) {

        StringBuilder qs = new StringBuilder((countProjection ? "select count(*) " : "") + "from Account a where 1 = 1 ");
        
        if (accountId != null) {
            qs.append("and a.id = :accountId ");
        }
        
        if (userId != null) {
            qs.append("and a.userId = :userId ");
        }
        
        if (walletId != null) {
            qs.append("and a.walletId = :walletId ");
        }
        
        if (currencyCode != null) {
            qs.append("and a.currencyCode = :currencyCode ");
        }
        
        if (includeStatuses != null && includeStatuses.size() > 0) {
            qs.append("and a.status in (:includeStatuses) ");
        }
        
        if (includeTypes != null && includeTypes.size() > 0) {
            qs.append("and a.type in (:includeTypes) ");
        }
        
        if (!countProjection) {
            order = (order == null ? AccountsOrder.ID : order);
            qs.append("order by " + order.getColumnName() + " " + (ascending ? "asc" : "desc"));
        }
        
        Query q = em.createQuery(qs.toString());
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        
        if (accountId != null) {
            q.setParameter("accountId", accountId);
        }
        
        if (userId != null) {
            q.setParameter("userId", userId);
        }
        
        if (walletId != null) {
            q.setParameter("walletId", walletId);
        }
        
        if (currencyCode != null) {
            q.setParameter("currencyCode", currencyCode);
        }
        
        if (includeStatuses != null && includeStatuses.size() > 0) {
            q.setParameter("includeStatuses", includeStatuses);
        }
        
        if (includeTypes != null && includeTypes.size() > 0) {
            q.setParameter("includeTypes", includeTypes);
        }
        
        return q;
    }
    
    @Override
    public Account merge(Account account) {
        return em.merge(account);
    }

    @SuppressWarnings({ "unchecked" })
    @Override
    public List<Transaction> listTransactions(
        final TransactionParticipant part1,
        final TransactionParticipant part2, 
        final Date startDate, final Date endDate,
        final int offset, final int limit, 
        final TransactionsOrder order, final boolean ascending) {
        
        if (limit == 0) {
            return new ArrayList<Transaction>();
        }
        
        Query q = createListTxQuery(part1, part2, startDate, endDate, offset, limit, order, ascending, em, false);       
        return q.getResultList();
    }
    
    @Override
    public int countTransactions(
        final TransactionParticipant part1, 
        final TransactionParticipant part2, 
        final Date startDate, final Date endDate) {

        Query q = createListTxQuery(part1, part2, startDate, endDate, 0, Integer.MAX_VALUE, null, true, em, true);       
        return ((Number) q.getSingleResult()).intValue();
    }
    
    /**
     * Build the query by string concatenation, this is really ugly, but it is impossible to 
     * do it with a criteria as as it doesn't support distinct projections properly.
     */
    private Query createListTxQuery(
        TransactionParticipant part1,
        TransactionParticipant part2,
        final Date startDate, final Date endDate, 
        final int offset, final int limit,
        TransactionsOrder order, boolean ascending, EntityManager em, boolean count) {
        
        // order = order == null ? TransactionsOrder.ID : order;
        
        part1 = (part1 == null ? new TransactionParticipant(null, null, BOTH) : part1);
        part2 = (part2 == null ? new TransactionParticipant(null, null, BOTH) : part2);
        
        String aId1sub = "";
        String aId2sub = "";
        
        if (part1.getId() != null) {
            String a1creditStr = "";
            if (part1.getDirection() == BOTH) {
                // do nothing
            } else if (part1.getDirection() == CREDITED) {
                a1creditStr = "e1.amount >= 0 and ";
            } else {
                a1creditStr = "e1.amount < 0 and ";
            }
            
            String id1Str = part1.getIdType() != null && part1.getIdType() == USER_ID  ? " e1.account.userId = :id1 " : " e1.account.id = :id1 ";
            aId1sub = id1Str + " and e1.transaction = t and " + a1creditStr;
        }
        
        if (part2.getId() != null) {
            String a2creditStr = "";
            if (part2.getDirection() == BOTH) {
                // do nothing
            } else if (part2.getDirection() == CREDITED) {
                a2creditStr = "e2.amount >= 0 and ";
            } else {
                a2creditStr = "e2.amount < 0 and ";
            }
            
            String id2Str = part2.getIdType() != null && part2.getIdType() == USER_ID ? " e2.account.userId = :id2 " : " e2.account.id = :id2 ";
            aId2sub = id2Str + " and e2.transaction = t and " + a2creditStr;
        }
            
        String projection = count ? "count(t.id)" : "t";
        
        String queryString = "select " + projection + " from Transaction t " + 
            (part1.getId() == null ? "" : ", Entry e1 ") +
            (part2.getId() == null ? "" : ", Entry e2 ") +
            " where " + 
            (part1.getId() == null ? "" : aId1sub) + 
            (part2.getId() == null ? "" : aId2sub) + 
            " t.timestampLong >= :startDate and t.timestampLong < :endDate " + 
            (count || order == null ? "" : " order by " + order.getColumnName() + (ascending ? " asc" : " desc"));

        // log.debug("query string: " + queryString);
        
        Query q = em.createQuery(queryString);
        
        if (part1.getId() != null) {
            // log.debug("Id one of type: " + id1.getClass().getName() + " - " + id1.toString());
        	q.setParameter("id1", part1.getId());
        }
        if (part2.getId() != null) {
            q.setParameter("id2", part2.getId());
        }
        
        q.setParameter("startDate", startDate == null ? 0 : startDate.getTime());
        q.setParameter("endDate", endDate == null ? Long.MAX_VALUE : endDate.getTime());
        q.setFirstResult(offset);       
        q.setMaxResults(limit);
        
        return q;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Transaction> listTransactionsByAttribute(Long accountId, Long userId, String key, String value,
        int offset, int limit, TransactionsOrder order, boolean ascending) {
        
        if (limit == 0) {
            return new ArrayList<Transaction>();
        }
        
        CriteriaQuery<Transaction> cq = createListTransactionsByAttributeQuery(accountId, userId, key, value, order, ascending, false);
        TypedQuery<Transaction> q = em.createQuery(cq);
        
        q.setFirstResult(offset);
        q.setMaxResults(limit);
        
        return q.getResultList();
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public int countTransactionsByAttribute(Long accountId, Long userId, String key, String value) {
        CriteriaQuery cq = createListTransactionsByAttributeQuery(accountId, userId, key, value, null, true, true);
        TypedQuery<Long> q = em.createQuery(cq);
        
        return q.getSingleResult().intValue();
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private CriteriaQuery createListTransactionsByAttributeQuery(Long accountId, Long userId, String key,
        String value, TransactionsOrder order, boolean ascending, boolean count) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery cq = cb.createQuery(Transaction.class);
        Root t = cq.from(Transaction.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        Join<Transaction, TransactionAttribute> ta = t.join("attributes");
        
        predicates.add(cb.equal(ta.get("key"), key));
        
        if (value != null) {
            predicates.add(cb.equal(ta.get("value"), value));
        }
        
        if (userId != null) {
            Join<Transaction, Entry> e = t.join("entries");
            predicates.add(cb.equal(e.get("account").get("userId"), userId));
        }
        
        if (accountId != null) {
            Join<Transaction, Entry> e = t.join("entries");
            predicates.add(cb.equal(e.get("account").get("id"), accountId));
        }
        
        if (order != null) {
            if (ascending) {
                cq.orderBy(cb.asc(t.get("id")));
            } else {
                cq.orderBy(cb.desc(t.get("id")));
            }
        }
        
        cq.where(cb.and(predicates.toArray(new Predicate[] {})));
        
        if (count) {
            cq.select(cb.count(t));
        }
        
        return cq;
    }
    
    @Override
    public Collection<Account> findAccountsByUserId(Long userId, String type, String currency) {
    	return findAccountsByUserAndWalletId(userId, null, type, currency);
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Account> findAccountsByUserAndWalletId(Long userId, Long walletId, String type, String currency) {
    	String tmp = "from Account a where a.userId = :userId";
    	if(walletId != null) {
    		tmp += " and a.walletId = :walletId";
    	}
    	if(type != null) {
    		tmp += " and a.type = :type";
    	}
    	if(currency != null) {
    		tmp += " and a.currencyCode = :currency";
    	}
    	Query q = em.createQuery(tmp);
    	q.setParameter("userId", userId);
    	if(walletId != null) {
    		q.setParameter("walletId", walletId);
    	}
    	if(type != null) {
    		q.setParameter("type", type);
    	}
    	if(currency != null) {
    		q.setParameter("currency", currency);
    	}
    	
    	q.setHint("org.hibernate.cacheable", true);
    	return q.getResultList();
    }
    
    @Override
    @SuppressWarnings("unchecked")
	public Collection<Account> findAccountsByTypeAndCurrency(String type, String currency) {
    	String tmp = "from Account a where a.type = :type and a.currencyCode = :currency";
    	Query q = em.createQuery(tmp);
    	q.setParameter("type", type);
    	q.setParameter("currency", currency);
    	q.setHint("org.hibernate.cacheable", true);
    	return q.getResultList();
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Collection<Account> findAccountsByOperatorIdAndTypeAndCurrency(String type, Long operatorId, String currency) {
        String tmp = "from Account a where a.type = :type and a.operatorId = :operatorId and a.currencyCode = :currency";
        Query q = em.createQuery(tmp);
        q.setParameter("type", type);
        q.setParameter("operatorId", operatorId);
        q.setParameter("currency", currency);
        q.setHint("org.hibernate.cacheable", true);
        return q.getResultList();
    }
    
    public Account findUniqueAccount(AccountQueryRequest request) {
        List<Account> accounts = findAccountsByQuery(request);
        if (accounts.size() > 1) {
            String output = "More than one account for unique query: "+accounts.size()+". ";
            for (Account a : accounts) {
                output += "AccountID: "+a.getId()+", ";
            }
            log.warn(output);
            
            throw new NonUniqueResultException("More than one account found ("+accounts.size()+") for unique query: "+request);
        } if (accounts.size() < 1) {
            throw new NoSuchAccountException("No account found for unique query: "+request);
        } else {
            return accounts.get(0);
        }
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private List<Account> findAccountsByQuery(AccountQueryRequest request) {
        Map<String, String> attributes = request.getAttributes();
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root a = cq.from(Account.class);
        List<Predicate> predicates = new ArrayList<Predicate>();
        Join<Account, AccountAttribute> ta = a.join("attributes", JoinType.LEFT);
        
        for (String key : attributes.keySet()) {
            predicates.add(cb.equal(ta.get("key"), key));
            String value = attributes.get(key);
            if (value != null) {
                predicates.add(cb.equal(ta.get("value"), value));
            }
        }
        
        if (request.getUserId() != null) {
            predicates.add(cb.equal(a.get("userId"), request.getUserId()));
        }
        
        if (request.getOperatorId() != null) {
            predicates.add(cb.equal(a.get("operatorId"), request.getOperatorId()));
        }
        
        if (request.getType() != null) {
            predicates.add(cb.equal(a.get("type"), request.getType()));
        }
        
        if (request.getCurrency() != null) {
            predicates.add(cb.equal(a.get("currencyCode"), request.getCurrency()));
        }
        
        cq.where(cb.and(predicates.toArray(new Predicate[] {})));
        cq.distinct(true);
        TypedQuery<Account> q = em.createQuery(cq);
        q.setMaxResults(3); // just something more than 1 really
        return q.getResultList();
    }
    
    
    @Override
    @SuppressWarnings("unchecked")
    public List<CurrencyRate> listCurrencyRates(String currencyCode, Date start, Date end) {
    	String tmp = "from CurrencyRate where timestamp between :start and :end ";
    	if(currencyCode != null) {
    		tmp += "and (sourceCurrencyCode = :cc or targetCurrencyCode = :cc) ";
    	}
    	tmp += "order by timestamp asc, id asc";
    	
    	Query q = em.createQuery(tmp);
        q.setParameter("start", start);
        q.setParameter("end", end);
        if(currencyCode != null) {
        	q.setParameter("cc", currencyCode);
        }
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public CurrencyRate getCurrencyRate(String sourceCurrencyCode, String targetCurrencyCode, Date start, Date end) {
        Query q = em.createQuery("from CurrencyRate where timestamp between :start and :end " +
            "and sourceCurrencyCode = :scc and targetCurrencyCode = :tcc " +
            "order by timestamp desc, id desc");
        q.setParameter("start", start);
        q.setParameter("end", end);
        q.setParameter("scc", sourceCurrencyCode);
        q.setParameter("tcc", targetCurrencyCode);
        q.setMaxResults(1);
        List<CurrencyRate> result = q.getResultList();
        if (result.isEmpty()) {
            return null;
        } else {
            return result.iterator().next();
        }
    }

    /**
     * This implementation will only search for first level chains (2 rates).
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<CurrencyRate> getCurrencyRateChain(String targetCC, String sourceCC, Date start, Date end) {
        Query q = em.createQuery(
            "from CurrencyRate r0, CurrencyRate r1 " +
            "  where r0.timestamp between :start and :end  and  r1.timestamp between :start and :end " +
            "    and ( " +
            "      r0.sourceCurrencyCode = :scc  and  r1.targetCurrencyCode = :tcc  and r0.targetCurrencyCode = r1.sourceCurrencyCode  or " +
            "      r1.sourceCurrencyCode = :scc  and  r0.targetCurrencyCode = :tcc  and r1.targetCurrencyCode = r0.sourceCurrencyCode  or " +
            "      r0.targetCurrencyCode = :scc  and  r1.targetCurrencyCode = :tcc  and r0.sourceCurrencyCode = r1.sourceCurrencyCode  or " +
            "      r0.targetCurrencyCode = :tcc  and  r1.targetCurrencyCode = :scc  and r0.sourceCurrencyCode = r1.sourceCurrencyCode  or " +
            "      r0.sourceCurrencyCode = :scc  and  r1.sourceCurrencyCode = :tcc  and r0.targetCurrencyCode = r1.targetCurrencyCode  or " +
            "      r0.sourceCurrencyCode = :tcc  and  r1.sourceCurrencyCode = :scc  and r0.targetCurrencyCode = r1.targetCurrencyCode " +
            "    ) " +
            "  order by r0.timestamp desc, r1.timestamp desc, r0.id desc, r1.id desc");
        q.setParameter("start", start);
        q.setParameter("end", end);
        q.setParameter("scc", sourceCC);
        q.setParameter("tcc", targetCC);
        q.setMaxResults(1);
        
        Iterator iter = q.getResultList().iterator();
        if (iter.hasNext()) {
            return new ArrayList(asList((Object[]) iter.next()));
        } else {
            return emptyList();
        }
    }


}

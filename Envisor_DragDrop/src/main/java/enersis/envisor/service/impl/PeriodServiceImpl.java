package enersis.envisor.service.impl;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.Bill;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.Period;
import enersis.envisor.entity.Project;
import enersis.envisor.service.PeriodService;

@Service("periodService")
@Transactional
public class PeriodServiceImpl implements PeriodService {

	public static final String getPeriodsQuery = "from Period u where u.status=0";
	public static final String findByDistributionLine = "from Period u where u.distributionLine =:distributionLine and u.status =0";
	public static final String findByBill = "from Period u where u.bill =:bill and u.status = 0";
	public static final String findByDistLine_Bill = "from Period u where u.bill =:bill and u.distributionLine =:distributionLine and u.status = 0";
	public static final String findByDistLine_Date = "from Period u where u.date =:date and u.distributionLine =:distributionLine and u.status = 0";
	public static final String findByProjectAndDate = "from Period p where p.date =:date and p.project =:project and p.status = 0";
	public static final String findByProject = "from Period p where p.project =:project and p.status = 0";

	@Autowired
	private SessionFactory sessionFactory;
	@Override
	public List<Period> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getPeriodsQuery).list();
	}

	@Override
	public void save(Period period) {
		sessionFactory.getCurrentSession().saveOrUpdate(period);

	}

	@Override
	public void delete(Period period) {
		period.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(period);

	}

	@Override
	public List<Period> findbyDistributionLine(DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public List<Period> findByBill(Bill bill) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByBill);
		query.setParameter("bill", bill);
		return query.list();
	}

	@Override
	public List<Period> findByDistLine_Bill(DistributionLine distributionLine,
			Bill bill) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistLine_Bill);
		query.setParameter("distributionLine", distributionLine);
		query.setParameter("bill", bill);
		return query.list();
	}

	@Override
	public Period findByDistLine_Date(DistributionLine distributionLine,
			Date date) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistLine_Date);
		query.setParameter("distributionLine", distributionLine);
		query.setDate("date", date);
		return (Period) query.list().get(0);
	}

	@Override
	public Period findByProject_Date(Project project,Date date) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProjectAndDate);
		query.setParameter("project", project);
		query.setDate("date", date);
		return (Period) query.list().get(0);
	}

	@Override
	public List<Period> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProject);
		query.setParameter("project", project);
		return query.list();
	}

}

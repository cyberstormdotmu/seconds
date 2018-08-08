package enersis.envisor.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import enersis.envisor.entity.Bill;
import enersis.envisor.entity.DistributionLine;
import enersis.envisor.entity.DistributionLineBill;
import enersis.envisor.entity.Project;
import enersis.envisor.service.DistributionLineBillService;

@Service("distributionLineBillService")
@Transactional
public class DistributionLineBillServiceImpl implements
		DistributionLineBillService {
	
	public static final String getDistributionLineBillQuery = "from DistributionLineBill u where u.status=0 ";
	public static final String findByDistributionLine = "from DistributionLineBill u where u.distributionLine =:distributionLine and u.status =0";
	public static final String findByBill = "from DistributionLineBill u where u.bill =:bill and u.status =0";
	public static final String findByProject ="select distinct dlb from DistributionLineBill dlb where dlb.distributionLine in ("
			+ "from DistributionLine dl where dl.project=:project) and dlb.status =0";
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Override
	public List<DistributionLineBill> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getDistributionLineBillQuery).list();
	}

	@Override
	public void save(DistributionLineBill distributionLineBill) {
		sessionFactory.getCurrentSession().saveOrUpdate(
				distributionLineBill);

	}

	@Override
	public void delete(DistributionLineBill distributionLineBill) {
		distributionLineBill.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(
				distributionLineBill);

	}

	@Override
	public List<DistributionLineBill> findbyDistributionLine(
			DistributionLine distributionLine) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByDistributionLine);
		query.setParameter("distributionLine", distributionLine);
		return query.list();
	}

	@Override
	public List<DistributionLineBill> findByBill(Bill bill) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByBill);
		query.setParameter("bill", bill);
		return query.list();
	}

	@Override
	public List<DistributionLineBill> findByProject(Project project) {
		Query query = sessionFactory.getCurrentSession().createQuery(
				findByProject);
		query.setParameter("project", project);
		return query.list();
	}

}

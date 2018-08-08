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
import enersis.envisor.service.BillService;

@Service("billService")
@Transactional
public class BillServiceImpl implements BillService {

	public static final String getBillsQuery = "from Bill u where u.status=0";
	public static final String findByDistributionLine = "from Bill u where u.distributionLine =:distributionLine and u.status =0";
	public static final String findByPeriod = "from Bill b where b.period=:period and b.status =0";
	public static final String findByPeriodAndMonth ="from Bill b where b.period: and date between startdate and endDate and u.status =0";
	public static final String findByFileName = "from Bill u where u.fileName =:fileName and u.status = 0";
	public static final String findByDate = "from Bill u where u.date =:date and u.status = 0";
	
	public static final String findUnbindedBills = "from Bill u where periodId is null and status = 0";
	
//	public static final String findByChargeBetween = "from Bill u where u.charge is between =:start and =:finish and status =0";
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public List<Bill> findAll() {
		return sessionFactory.getCurrentSession()
				.createQuery(getBillsQuery).list();
	}

	@Override
	public void save(Bill bill) {
		sessionFactory.getCurrentSession().saveOrUpdate(bill);

	}

	@Override
	public void delete(Bill bill) {
		bill.setStatus((byte) 1);
		sessionFactory.getCurrentSession().saveOrUpdate(bill);

	}

	@Override
	public List<Bill> findbyDistributionLine(DistributionLine distributionLine) {
			Query query = sessionFactory.getCurrentSession().createQuery(
					findByDistributionLine);
			query.setParameter("distributionLine", distributionLine);
			return query.list();
	}

	@Override
	public List<Bill> findByFileName(String fileName) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByFileName);
		query.setParameter("fileName", fileName);
		return query.list();
	}

	@Override
	public List<Bill> findByDate(Date date) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByDate);
//		query.setParameter("date", date);
		query.setDate("date", date);
		System.out.println(date);
		return query.list();
	}

	@Override
	public List<Bill> findUnbindedBills() {
		Query query = sessionFactory.getCurrentSession().createQuery(findUnbindedBills);
		return query.list();
	}

	@Override
	public List<Bill> findByPeriod(Period period) {
		Query query = sessionFactory.getCurrentSession().createQuery(findByPeriod);
		query.setParameter("period", period);
		return query.list();
	}

//	@Override
//	public List<Bill> findByChargeBetween(Integer start, Integer finish) {
//		Query query = sessionFactory.getCurrentSession().createQuery(findByChargeBetween);
//		query.setParameter("start", start);
//		query.setParameter("finish", finish);
////		System.out.println(date);
//		return query.list();
//	}

}

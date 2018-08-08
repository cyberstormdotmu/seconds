package enersis.envisor.service;

import java.util.List;

import enersis.envisor.entity.Flat;
import enersis.envisor.entity.Installation;
import enersis.envisor.entity.Subscriber;

public interface SubscriberService {

	public List<Subscriber> findAll();

	public void save(Subscriber subscriber);

	public void delete(Subscriber subscriber);
	
	public List<Subscriber> findbyInstallation(Installation installation);
}

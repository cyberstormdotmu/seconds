package com.ishoal.core.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.ishoal.common.util.Streamable;

public class Addresses implements Streamable<Address> {

	private final List<Address> addresses;

	private Addresses(Builder builder) {
		this.addresses = new ArrayList<>(builder.addresses);
	}

	private Addresses(List<Address> addresses) {
		this.addresses = Collections.unmodifiableList(addresses);
	}

	public static Builder someAddresses() {
		return new Builder();
	}

	public static Addresses emptyAddresses() {
		return new Builder().build();
	}

	public int size() {
		return addresses.size();
	}

	public static Addresses over(List<Address> add) {
		return new Addresses(add);
	}

	@Override
	public Iterator<Address> iterator() {
		return addresses.iterator();
	}

	public Address primary() {
		return addresses.get(0);
	}

	public boolean isEmpty() {
		return addresses.isEmpty();
	}

	public static class Builder {
		private List<Address> addresses = new ArrayList<>();

		private Builder() {
		}

		public Builder address(Address add) {
			this.addresses.add(add);
			return this;
		}

		public Addresses build() {
			//addresses.sort((Address a1, Address a2) -> a1.getOrganisationName().compareTo(a2.getOrganisationName()));
			return new Addresses(this);
		}
	}
}

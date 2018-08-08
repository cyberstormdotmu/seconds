package com.ishoal.ws.buyer.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RegistrationSummariesDto {
    private final List<RegistrationSummaryDto> registrations;

    private RegistrationSummariesDto(List<RegistrationSummaryDto> users) {

        this.registrations = Collections.unmodifiableList(new ArrayList<>(users));
    }

    private RegistrationSummariesDto(Builder builder) {
        registrations = builder.registrations;
    }

    public int count() {
        return registrations.size();
    }

    public List<RegistrationSummaryDto> getRegistrations() {

        return registrations;
    }

    public static RegistrationSummariesDto over(List<RegistrationSummaryDto> registrations) {
        return new RegistrationSummariesDto(registrations);
    }

    public static RegistrationSummariesDto over(RegistrationSummaryDto... registrations) {
        return new RegistrationSummariesDto(Arrays.asList(registrations));
    }

    public static Builder someRegistrations() {

        return new Builder();
    }

    public static final class Builder {
        private final List<RegistrationSummaryDto> registrations;

        public Builder() {
            this.registrations = new ArrayList<>();
        }

        public Builder(RegistrationSummariesDto copy) {
            this.registrations = copy.registrations;
        }

        public Builder addRegistration(RegistrationSummaryDto val) {

            registrations.add(val);
            return this;
        }

        public RegistrationSummariesDto build() {

            return new RegistrationSummariesDto(this);
        }
    }
}

package com.ishoal.core.persistence.converter;

import org.joda.time.DateTime;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Date;

@Converter(autoApply = true)
public class DateTimeConverter implements AttributeConverter<DateTime, Date> {

    @Override
    public Date convertToDatabaseColumn(DateTime dateTime) {
        if(dateTime == null) {
            return null;
        }
        return dateTime.toDate();
    }

    @Override
    public DateTime convertToEntityAttribute(Date date) {
        if(date == null) {
            return null;
        }
        return new DateTime(date.getTime());
    }
}

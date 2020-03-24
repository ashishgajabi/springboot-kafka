package au.com.easynebula.clienttransactionmanagement.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.parse;
import static java.time.format.DateTimeFormatter.ofPattern;
import static org.springframework.util.ReflectionUtils.makeAccessible;
import static org.springframework.util.ReflectionUtils.setField;
import static org.springframework.util.StringUtils.isEmpty;

public class FixedWidthFileParser<T> {

	private static final Logger LOG = LoggerFactory.getLogger(FixedWidthFileParser.class);

	private final List<FieldDescriptor> fieldDescriptorMap;

	private final int fixedWidthMinimumLength;

	public FixedWidthFileParser(final Class<T> aClass,
	                            final int fixedWidthMinimumLength) {
		this.fixedWidthMinimumLength = fixedWidthMinimumLength;

		fieldDescriptorMap = new ArrayList<>();
		for (Field field : aClass.getDeclaredFields()) {
			final FixedWidthColumn fixedWidthColumn = field.getAnnotation(FixedWidthColumn.class);
			final FieldDescriptor fieldDescriptor = new FieldDescriptor();
			if(fixedWidthColumn!= null && !fixedWidthColumn.exclude()) {
				fieldDescriptor.setField(field);
				fieldDescriptor.setStartAt(fixedWidthColumn.startAt());
				fieldDescriptor.setWidth(fixedWidthColumn.width());
				fieldDescriptor.setDate(fixedWidthColumn.isDate());
				fieldDescriptorMap.add(fieldDescriptor);
			}
		}
	}

	public Optional<T> parseLine(final String fixedWidthLine, final Class<T> aClass) {
		if(isEmpty(fixedWidthLine) || fixedWidthLine.length() < fixedWidthMinimumLength) {
			LOG.warn("message with incorrect format received: {}", fixedWidthLine);
			return Optional.empty();
		}
		if(fieldDescriptorMap.isEmpty()) {
			LOG.warn("Cannot parse message to class without annotations {}", aClass.getName());
		}
		try {
			Constructor<T> declaredConstructor = aClass.getDeclaredConstructor();
			declaredConstructor.setAccessible(true);
			final T instance = declaredConstructor.newInstance();

			for (FieldDescriptor fieldDescriptor : fieldDescriptorMap) {
				final String token = fixedWidthLine.substring(fieldDescriptor.getStartAt(), fieldDescriptor.getStartAt() + fieldDescriptor.getWidth());
				makeAccessible(fieldDescriptor.getField());
				if(fieldDescriptor.isDate()) {
					LocalDate date = parse(token.trim(), ofPattern("yyyyMMdd"));
					setField(fieldDescriptor.getField(), instance, date);
				} else {
					setField(fieldDescriptor.getField(), instance, token.trim());
				}
			}
			return Optional.of(instance);
		} catch (Exception e) {
			LOG.error("Exception in parsing record {}", fixedWidthLine, e);
			throw new IllegalArgumentException("Error parsing line: " + fixedWidthLine);
		}
	}
}
package io.github.cooperlyt.commons.cloud.serialize;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.DecimalUtils;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializerBase;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_WITH_CONTEXT_TIME_ZONE;

@Slf4j
public class ZonedLocalDateTimeSerializer extends InstantSerializerBase<LocalDateTime> {



  private static final long serialVersionUID = 1L;

  public static final ZonedLocalDateTimeSerializer INSTANCE = new ZonedLocalDateTimeSerializer();

  private final ZoneId sourceZoneId;

  /**
   * Flag for <code>JsonFormat.Feature.WRITE_DATES_WITH_ZONE_ID</code>
   *
   * @since 2.8
   */
  protected final Boolean _writeZoneId;



  public ZonedLocalDateTimeSerializer() {
    this(DateTimeFormatter.ISO_OFFSET_DATE_TIME, ZoneId.systemDefault());
  }

  public ZonedLocalDateTimeSerializer(String sourceZoneId) {
    this(DateTimeFormatter.ISO_OFFSET_DATE_TIME, ZoneId.of(sourceZoneId));
  }

  public ZonedLocalDateTimeSerializer(ZoneId sourceZoneId) {
    this(DateTimeFormatter.ISO_OFFSET_DATE_TIME, sourceZoneId);
  }

  public ZonedLocalDateTimeSerializer(DateTimeFormatter formatter) {
    this(formatter, ZoneId.systemDefault());
  }

  public ZonedLocalDateTimeSerializer(DateTimeFormatter formatter, String sourceZoneId) {
    this(formatter, ZoneId.of(sourceZoneId));
  }

  public ZonedLocalDateTimeSerializer(DateTimeFormatter formatter, ZoneId sourceZoneId) {
    super(LocalDateTime.class,
        dt -> dt.atZone(sourceZoneId).toInstant().toEpochMilli(),
        dt -> dt.atZone(sourceZoneId).toInstant().toEpochMilli(),
        dt -> dt.atZone(sourceZoneId).getNano(),
        formatter);
    this.sourceZoneId = sourceZoneId;
    _writeZoneId = null;
  }

  protected ZonedLocalDateTimeSerializer(ZonedLocalDateTimeSerializer base,
                                    Boolean useTimestamp, DateTimeFormatter formatter, Boolean writeZoneId, ZoneId sourceZoneId) {
    this(base, useTimestamp, base._useNanoseconds, formatter, base._shape, writeZoneId, sourceZoneId);
  }

  protected ZonedLocalDateTimeSerializer(ZonedLocalDateTimeSerializer base,
                                    Boolean useTimestamp, Boolean useNanoseconds, DateTimeFormatter formatter,
                                    JsonFormat.Shape shape, Boolean writeZoneId, ZoneId sourceZoneId) {
    super(base, useTimestamp, useNanoseconds, formatter, shape);
    _writeZoneId = writeZoneId;
    this.sourceZoneId = sourceZoneId;
  }

  @Override
  protected ZonedLocalDateTimeSerializer withFormat(Boolean useTimestamp, DateTimeFormatter f, JsonFormat.Shape shape) {
    return new ZonedLocalDateTimeSerializer(this, useTimestamp, _useNanoseconds, f, shape,_writeZoneId , sourceZoneId);
  }

  @Override
  protected ZonedLocalDateTimeSerializer withFeatures(Boolean writeZoneId, Boolean writeNanoseconds) {
    return new ZonedLocalDateTimeSerializer(this, _useTimestamp, writeNanoseconds, _formatter, _shape, writeZoneId , sourceZoneId);
  }


  @Override
  public void serialize(LocalDateTime value, JsonGenerator g, SerializerProvider provider)
      throws IOException
  {

    ZonedDateTime _value = value.atZone(sourceZoneId).withZoneSameInstant(provider.getTimeZone().toZoneId());

    //throw new RuntimeException("not support 3");

    if (!useTimestamp(provider)) {
      if (shouldWriteWithZoneId(provider)) {

        // write with zone
        g.writeString(DateTimeFormatter.ISO_ZONED_DATE_TIME.format(_value));
        return;
      }
    }

    if (useTimestamp(provider)) {
      if (useNanoseconds(provider)) {
        g.writeNumber(DecimalUtils.toBigDecimal(
            _value.toEpochSecond(), _value.getNano()
        ));
        return;
      }
      g.writeNumber(_value.toInstant().toEpochMilli());
      return;
    }

    g.writeString(formatValue(_value, provider));

    log.trace("serialize LocalDateTime: {} to {}", value, _value);

  }

//  @Override
//  public void serializeWithType(LocalDateTime value, JsonGenerator g, SerializerProvider provider,
//                                TypeSerializer typeSer) throws IOException
//  {
//    log.debug("serialize with type LocalDateTime: {} to {}", value, convert(value));
//
//    super.serializeWithType(convert(value), g, provider, typeSer);
//  }


  /**
   * @since 2.8
   */
  public boolean shouldWriteWithZoneId(SerializerProvider ctxt) {
    return (_writeZoneId != null) ? _writeZoneId :
        ctxt.isEnabled(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
  }

  @Override // since 2.9
  protected JsonToken serializationShape(SerializerProvider provider) {
    if (!useTimestamp(provider) && shouldWriteWithZoneId(provider)) {
      return JsonToken.VALUE_STRING;
    }
    return super.serializationShape(provider);
  }


  protected String formatValue(ZonedDateTime value, SerializerProvider provider)
  {
    DateTimeFormatter formatter = (_formatter != null) ? _formatter : DateTimeFormatter.ISO_OFFSET_DATE_TIME;

      if (formatter.getZone() == null) { // timezone set if annotated on property
        // If the user specified to use the context TimeZone explicitly, and the formatter provided doesn't contain a TZ
        // Then we use the TZ specified in the objectMapper
        if (provider.getConfig().hasExplicitTimeZone() && provider.isEnabled(WRITE_DATES_WITH_CONTEXT_TIME_ZONE)) {
          formatter = formatter.withZone(provider.getTimeZone().toZoneId());
        }

      return formatter.format(value);
    }

    return value.toString();
  }

}

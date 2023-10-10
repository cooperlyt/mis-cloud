package io.github.cooperlyt.commons.cloud.serialize;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;


@Slf4j
public class ZonedLocalDateTimeDeserializer extends InstantDeserializer<LocalDateTime> {

  private final ZoneId targetZoneId;
//
//  public static final InstantDeserializer<ZonedDateTime> ZONED_DATE_TIME = new InstantDeserializer<>(
//      LocalDateTime.class, DateTimeFormatter.ISO_ZONED_DATE_TIME,
//      ZonedDateTime::from,
//      a -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(a.value), a.zoneId),
//      a -> ZonedDateTime.ofInstant(Instant.ofEpochSecond(a.integer, a.fraction), a.zoneId),
//      ZonedDateTime::withZoneSameInstant,
//      false // keep zero offset and Z separate since zones explicitly supported
//  );


  public ZonedLocalDateTimeDeserializer(){
    this(DateTimeFormatter.ISO_OFFSET_DATE_TIME, ZoneId.systemDefault());
  }

  public ZonedLocalDateTimeDeserializer(ZoneId targetZoneId){
    this(DateTimeFormatter.ISO_OFFSET_DATE_TIME, targetZoneId);
  }

  public ZonedLocalDateTimeDeserializer(DateTimeFormatter formatter){
    this(formatter, ZoneId.systemDefault());
  }

  public ZonedLocalDateTimeDeserializer(DateTimeFormatter formatter, String targetZoneId){
    this(formatter, ZoneId.of(targetZoneId));
  }
  public ZonedLocalDateTimeDeserializer(DateTimeFormatter formatter, ZoneId targetZoneId) {
    super(LocalDateTime.class, formatter,
        dt -> ZonedDateTime.from(dt).withZoneSameInstant(targetZoneId).toLocalDateTime(),
        a -> ZonedDateTime.ofInstant(Instant.ofEpochMilli(a.value), a.zoneId).withZoneSameInstant(targetZoneId).toLocalDateTime(),
        a -> ZonedDateTime.ofInstant(Instant.ofEpochSecond(a.integer, a.fraction), a.zoneId).withZoneSameInstant(targetZoneId).toLocalDateTime(),
        (a,b) -> a.atZone(targetZoneId).withZoneSameInstant(b).toLocalDateTime(),
        false
        );
    this.targetZoneId = targetZoneId;
  }

  protected ZonedLocalDateTimeDeserializer(ZonedLocalDateTimeDeserializer base, Boolean leniency, ZoneId targetZoneId) {
    super(base, leniency);
    this.targetZoneId = targetZoneId;
  }


  @Override
  protected ZonedLocalDateTimeDeserializer withDateFormat(DateTimeFormatter formatter) {
    return new ZonedLocalDateTimeDeserializer(formatter, targetZoneId);
  }

  @Override
  protected ZonedLocalDateTimeDeserializer withLeniency(Boolean leniency) {
    return new ZonedLocalDateTimeDeserializer(this, leniency, targetZoneId);
  }

  @Override
  protected ZonedLocalDateTimeDeserializer withShape(JsonFormat.Shape shape) { return this; }

  @Override
  public LocalDateTime deserialize(JsonParser parser, DeserializationContext context) throws IOException
  {
    //context.getTimeZone()
    log.debug("deserialize LocalDateTime: {} to {}", parser.getText(), targetZoneId);
    return super.deserialize(parser, context);
  }

}

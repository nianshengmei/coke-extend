package org.needcoke.coke.kafka;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@Accessors(chain = true)
public abstract class CokeEvent {

    protected String topic;

    protected Long offset;

    protected Long timestamp;

    protected Integer partition;
}

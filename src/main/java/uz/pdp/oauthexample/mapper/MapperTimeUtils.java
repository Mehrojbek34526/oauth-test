package uz.pdp.oauthexample.mapper;

import java.sql.Timestamp;
import java.util.Objects;

/**
 Created by: Mehrojbek
 DateTime: 17/02/25 21:29
 **/
public class MapperTimeUtils {

    public static Long toMillis(Timestamp timestamp) {
        return Objects.nonNull(timestamp) ? timestamp.getTime() : null;
    }

    public static Timestamp toMillis(Long millis) {
        return Objects.nonNull(millis) ? new Timestamp(millis) : null;
    }

}

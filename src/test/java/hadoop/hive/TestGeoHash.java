package hadoop.hive;


import geohash.GeoHash;
import org.junit.Test;

public class TestGeoHash {

    @Test
    public void generateGeoHashDemo() {
        System.out.println(geoConvert(11D, -49D));
    }

    public String geoConvert(Double lat, Double Lon) {
        GeoHash geoHash = GeoHash.withCharacterPrecision(lat, Lon, 5);
        return geoHash.toBase32();
    }
}

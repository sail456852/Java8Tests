package hadoop.hive;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by IntelliJ IDEA.<br/>
 *
 * @author: Eugene_Wang<br />
 * Date: 6/12/2020<br/>
 * Time: 4:55 PM<br/>
 * To change this template use File | Settings | File Templates.
 */
public class HiveConnectorDemoTest {
    final Logger logger = LogManager.getLogger(HiveConnectorDemo.class);
    private String driverName = "org.apache.hive.jdbc.HiveDriver";
    private Connection con;
    private Statement statement;
    private HiveConnectorDemo demoObj;

    @Before
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName(driverName);
//        con = DriverManager.getConnection("jdbc:hive2://sandbox-hdp.hortonworks.com:2181/;serviceDiscoveryMode=zooKeeper;zooKeeperNamespace=hiveserver2", "hive", "Gbhnjmk,");
        con = DriverManager.getConnection("jdbc:hive2://localhost:2181/;serviceDiscoveryMode=zooKeeper;");
        statement = con.createStatement();
        demoObj = new HiveConnectorDemo();
    }

    @Test
    public void readHive() throws SQLException {
        demoObj.readHive(con, "select * from hotels", 6, 7, 10);
    }

    @Test
    public void geoConvert() {
    }

    @Test
    public void alterHiveTable() throws SQLException, ClassNotFoundException {
        demoObj.alterHiveTable(con);
    }
}
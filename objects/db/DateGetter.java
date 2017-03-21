package edu.ucla.library.libservices.voyager.notices.objects.db;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DateGetter
{
  private static final String QUERY =
    "SELECT to_char(current_due_date,'MM/DD/YYYY HH:MI AM') AS current_due_date FROM ucladb.circ_transactions WHERE" 
    + " patron_id = ? AND item_id = (SELECT item_id FROM ucladb.item_barcode WHERE item_barcode = ?)";
  private Properties props;
  private DriverManagerDataSource ds;

  public DateGetter()
  {
    super();
  }

  public DateGetter( Properties props )
  {
    //loadProperties( propsFile );
    this.props = props;
    makeConnection();
  }

  /*private void loadProperties( String propFile )
  {
    props = new Properties();
    try
    {
      props.load( new FileInputStream( new File( propFile ) ) );
    }
    catch ( IOException ioe )
    {
      ioe.printStackTrace();
    }
  }*/

  private void makeConnection()
  {
    ds = new DriverManagerDataSource();
    ds.setDriverClassName( props.getProperty( "DRIVER_CLASSNAME" ) );
    ds.setUrl( props.getProperty( "DB_URL" ) );
    ds.setUsername( props.getProperty( "DB_USERNAME" ) );
    ds.setPassword( props.getProperty( "DB_PASSWORD" ) );
  }

  public String getOverdueDate( String patronID, String barcode )
  {
    String overdueDate;
    overdueDate = null;
    overdueDate = ( String ) new JdbcTemplate(ds).queryForObject( QUERY, new Object[]{patronID,barcode}, String.class );
    return overdueDate;
  }
}

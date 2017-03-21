package edu.ucla.library.libservices.voyager.notices.objects.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class NoticeLogger
{
  private Properties props;
  private DriverManagerDataSource ds;
  private String lib_name;
  private int cancellations;
  private int courtesydues;
  private int itemavailables;
  private int overdues;
  private int recalls;
  private int recalloverdues;

  public NoticeLogger()
  {
  }

  public NoticeLogger( String propsFile )
  {
    loadProperties( propsFile );
    makeConnection();
  }

  private void loadProperties( String propFile )
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
  }

  private void makeConnection()
  {
    ds = new DriverManagerDataSource();
    ds.setDriverClassName( props.getProperty( "DRIVER_CLASSNAME" ) );
    ds.setUrl( props.getProperty( "DB_URL" ) );
    ds.setUsername( props.getProperty( "DB_USERNAME" ) );
    ds.setPassword( props.getProperty( "DB_PASSWORD" ) );
  }

  public void logNotices()
  {
    JdbcTemplate updater;
    updater = new JdbcTemplate( ds );

    updater.update( props.getProperty( "LOG_SQL" ), 
                    new Object[] { getLib_name(), getCancellations(), 
                                   getCourtesydues(), getItemavailables(), 
                                   getOverdues(), getRecalls(), 
                                   getRecalloverdues() } );
  }

  public void setLib_name( String lib_name )
  {
    this.lib_name = lib_name;
  }

  public String getLib_name()
  {
    return lib_name;
  }

  public void setCancellations( int cancellations )
  {
    this.cancellations = cancellations;
  }

  public int getCancellations()
  {
    return cancellations;
  }

  public void setCourtesydues( int courtesydues )
  {
    this.courtesydues = courtesydues;
  }

  public int getCourtesydues()
  {
    return courtesydues;
  }

  public void setItemavailables( int itemavailables )
  {
    this.itemavailables = itemavailables;
  }

  public int getItemavailables()
  {
    return itemavailables;
  }

  public void setOverdues( int overdues )
  {
    this.overdues = overdues;
  }

  public int getOverdues()
  {
    return overdues;
  }

  public void setRecalls( int recalls )
  {
    this.recalls = recalls;
  }

  public int getRecalls()
  {
    return recalls;
  }

  public void setRecalloverdues( int recalloverdues )
  {
    this.recalloverdues = recalloverdues;
  }

  public int getRecalloverdues()
  {
    return recalloverdues;
  }
}

package edu.ucla.library.libservices.voyager.notices.objects.beans;

import edu.ucla.library.libservices.voyager.notices.objects.db.DateGetter;

import java.util.Properties;

public class Item
{
  private Properties props;
  private String patronID = null;
  private String title = null;
  private String author = null;
  private String barcode = null;
  private String callNumber = null;
  private String enumChron = null;
  private String expirationDate = null;
  private String dueDate = null;
  private String sequence = null;
  private String proxyLastName = null;
  private String proxyFirstName = null;
  private String proxyTitle = null;

  public Item()
  {
  }

  public Item( String[] tokens, Properties props )
  {
    patronID = tokens[ 3 ].trim();
    title = tokens[ 28 ].trim();
    author = tokens[ 29 ].trim();
    barcode = tokens[ 30 ].trim();
    callNumber = tokens[ 31 ].trim();
    enumChron = tokens[ 32 ].trim();
    this.props = props;
    
    setOtherFields( tokens );
  }

  public String getTitle()
  {
    return title;
  }

  public String getAuthor()
  {
    return author;
  }

  public String getBarcode()
  {
    return barcode;
  }

  public String getCallNumber()
  {
    return callNumber;
  }

  public String getEnumChron()
  {
    return enumChron;
  }

  public String getExpirationDate()
  {
    return expirationDate;
  }

  public String getDueDate()
  {
    return dueDate;
  }

  public String getSequence()
  {
    return sequence;
  }

  public String getProxyLastName()
  {
    return proxyLastName;
  }

  public String getProxyFirstName()
  {
    return proxyFirstName;
  }

  public String getProxyTitle()
  {
    return proxyTitle;
  }

  private void setOtherFields( String[] tokens )
  {
    switch ( new Integer( tokens[ 0 ] ).intValue() )
    {
      case 0: //cancellation notice--no extra fields;
        break;
      case 1: //item available notice--optional due date field
        if ( tokens.length >= 34 )
          expirationDate = tokens[ 33 ].trim();
        break;
      case 2: //overdue notice----required due date, optional sequence number and proxy patron info
        setOverDueFields( tokens );
        break;
      case 3: //recall notice--required due date, optional proxy patron info
        setDueOrRecallFields( tokens );
        break;
      case 4: //recall overdue notice--required due date, optional sequence number and proxy patron info
        setOverDueFields( tokens );
        break;
      case 7: //courtesy (due) notice--required due date, optional proxy patron info
        setDueOrRecallFields( tokens );
        break;
    }
  }

  private void setDueOrRecallFields( String[] tokens )
  {
    dueDate = new DateGetter( props ).getOverdueDate( patronID, barcode ); // 
    if ( dueDate == null ) 
      dueDate = tokens[ 33 ].trim();

    if ( tokens.length >= 35 )
      proxyLastName = tokens[ 34 ].trim();
    if ( tokens.length >= 36 )
      proxyFirstName = tokens[ 35 ].trim();
    if ( tokens.length >= 37 )
      proxyTitle = tokens[ 36 ].trim();
  }

  private void setOverDueFields( String[] tokens )
  {
    dueDate = new DateGetter( props ).getOverdueDate( patronID, barcode ); // tokens[ 33 ].trim();

    if ( tokens.length >= 35 )
      sequence = tokens[ 34 ].trim();
    if ( tokens.length >= 36 )
      proxyLastName = tokens[ 35 ].trim();
    if ( tokens.length >= 37 )
      proxyFirstName = tokens[ 36 ].trim();
    if ( tokens.length >= 38 )
      proxyTitle = tokens[ 37 ].trim();
  }

}

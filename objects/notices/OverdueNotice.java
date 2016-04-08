package edu.ucla.library.libservices.voyager.notices.objects.notices;

import edu.ucla.library.libservices.voyager.notices.objects.beans.Item;

import java.util.Properties;

public class OverdueNotice
  extends CircNotice
{
  public OverdueNotice()
  {
  }

  public OverdueNotice( String[] tokens, Properties props, 
                        String location )
  {
    super( tokens, props, location );
    setSubject( props.getProperty( "overdue.subject" ) );
  }

  public String generateNotice()
  {
    content = new StringBuffer( 2000 );

    getSalutation();

    content.append( getProps().getProperty( "overdue.header1" ) );
    content.append( getProps().getProperty( "overdue.header2" ) );
    content.append( getProps().getProperty( "overdue.header3" ) );

    for ( Item theItem: getItems() )
    {
      content.append( "Location:\t" + getLibrary().getLibraryName() );
      content.append( "\n" );
      content.append( "Notification Number:\t\t" + theItem.getSequence() );
      content.append( "\n" );
      content.append( "Title:\t\t" + theItem.getTitle() );
      content.append( "\n" );
      content.append( "Author:\t\t" + theItem.getAuthor() );
      content.append( "\n" );
      content.append( "Item ID:\t" + theItem.getBarcode() );
      content.append( "\n" );
      content.append( "Call #:\t\t" + theItem.getCallNumber() + " " + 
                      theItem.getEnumChron() );
      content.append( "\n\n" );
      content.append( "Due Date:\t\t" + theItem.getDueDate() );
      content.append( "\n\n" );
    }

    getFooter();
    return content.toString();
  }
}

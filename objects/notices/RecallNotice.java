package edu.ucla.library.libservices.voyager.notices.objects.notices;

import edu.ucla.library.libservices.voyager.notices.objects.beans.Item;

import java.util.Properties;

public class RecallNotice
  extends CircNotice
{
  public RecallNotice()
  {
  }

  public RecallNotice( String[] tokens, Properties props, String location )
  {
    super( tokens, props, location );
    setSubject( props.getProperty( "recall.subject" ) );
  }

  public String generateNotice()
  {
    content = new StringBuffer( 2000 );

    getSalutation();

    content.append( getProps().getProperty( "recall.header1" ) );

    for ( Item theItem: getItems() )
    {
      content.append( "Location:\t" + getLibrary().getLibraryName() );
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
      content.append( "Due Date: " + theItem.getDueDate() );
      content.append( "\n\n" );
    }

    getFooter();
    return content.toString();
  }
}

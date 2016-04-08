package edu.ucla.library.libservices.voyager.notices.objects.notices;

import edu.ucla.library.libservices.voyager.notices.objects.beans.Item;

import java.util.Properties;

public class CancellationNotice
  extends CircNotice
{
  public CancellationNotice()
  {
  }

  public CancellationNotice( String[] tokens, Properties props, 
                             String location )
  {
    super( tokens, props, location );
    setSubject( props.getProperty("cancellation.subject") );
  }

  public String generateNotice()
  {
    content = new StringBuffer( 2000 );
    
    getSalutation();
    
    content.append( getProps().getProperty("cancellation.header1") );
    content.append( getProps().getProperty("cancellation.header2") );

    for ( Item theItem: getItems() )
    {
      content.append( "Location:\t" + 
                      getLibrary().getLibraryName() ); 
      content.append( "Title:\t\t" + theItem.getTitle() );
      content.append( "\n" );
      content.append( "Author:\t\t" + theItem.getAuthor() );
      content.append( "\n" );
      content.append( "Item ID:\t" + theItem.getBarcode() );
      content.append( "\n" );
      content.append( "Call #:\t\t" + theItem.getCallNumber() + " " + 
                      theItem.getEnumChron() );
      content.append( "\n\n" );
    }
    
    getFooter();
    return content.toString();
  }
}

package edu.ucla.library.libservices.voyager.notices.objects.notices;

import edu.ucla.library.libservices.voyager.notices.objects.beans.Item;

import edu.ucla.library.libservices.voyager.notices.objects.beans.Library;

import edu.ucla.library.libservices.voyager.notices.objects.beans.Patron;

import java.util.Properties;
import java.util.Vector;

public abstract class CircNotice
{
  private Library library = null;
  private Patron patron = null;
  private Properties props = null;
  private String noticeDate = null;
  private String subject = null;
  private Vector<Item> items = null;
  protected StringBuffer content = null;

  public CircNotice()
  {
  }

  public CircNotice( String[] tokens, Properties props, String location )
  {
    this.props = props;
    patron = new Patron( tokens );
    library = 
        new Library( tokens, props.getProperty( "contact.".concat( location ).concat( ".email" ) ) );
    items = new Vector<Item>();
    noticeDate = tokens[ 17 ].trim();
  }

  public abstract String generateNotice();

  public void addItem( Item toAdd )
  {
    items.addElement( toAdd );
  }

  public Library getLibrary()
  {
    return library;
  }

  public Patron getPatron()
  {
    return patron;
  }

  public String getNoticeDate()
  {
    return noticeDate;
  }

  public String getSubject()
  {
    return subject;
  }

  public Vector<Item> getItems()
  {
    return items;
  }

  public Properties getProps()
  {
    return props;
  }

  protected void getSalutation()
  {
    content.append( getNoticeDate() );
    content.append( "\n\n" );
    content.append( getLibrary().getInstitutionName() + "\n" );
    content.append( getLibrary().getLibraryName() );
    content.append( "\n\n" );
    content.append( "Dear " + getPatron().getFirstName() + " " + 
                    getPatron().getLastName() + ":" );
    content.append( "\n\n" );
  }

  protected void getFooter()
  {
    content.append( "If you have questions or need assistance contact us at:" );
    content.append( "\n" );
    content.append( "\tLocation:\t" + getLibrary().getLibraryName() );
    content.append( "\n" );
    content.append( "\tPhone:\t" + getLibrary().getLibraryPhone() );
    content.append( "\n" );
  }

  public void setSubject( String subject )
  {
    this.subject = subject;
  }
}

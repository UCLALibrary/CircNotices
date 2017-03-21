package edu.ucla.library.libservices.voyager.notices.objects.mail;

import edu.ucla.library.libservices.voyager.notices.objects.notices.CircNotice;

import java.util.Properties;
import java.util.Date;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class NoticeMailer
{
  private Properties props = null;

  public NoticeMailer()
  {
  }

  public NoticeMailer( Properties props )
  {
    this.props = props;
  }

  public void sendNotice( CircNotice theNotice )
  {
    Properties sysProps;
    Session session;
    MimeMessage msg;
    InternetAddress toAddress;

    try
    {
      sysProps = System.getProperties();
      sysProps.put( "mail.smtp.host", 
                    props.getProperty( "voyager.mailhost" ) );
      sysProps.put( "mail.mime.charset", "WINDOWS-1252" );
      session = Session.getInstance( sysProps, null );
      msg = new MimeMessage( session );
      msg.setFrom( new InternetAddress( theNotice.getLibrary().getLibraryEmail() ) );
      toAddress = new InternetAddress( theNotice.getPatron().getEmail() );
      //toAddress = new InternetAddress( "davidr615@hotmail.com" );
      msg.setRecipient( Message.RecipientType.TO, toAddress );
      msg.setSubject( theNotice.getSubject() );
      msg.setHeader( "X-Mailer", 
                     props.getProperty( "noticemailer.mailer" ) );
      msg.setSentDate( new Date() );
      msg.setText( theNotice.generateNotice(), "WINDOWS-1252" );
      Transport.send( msg );
    }
    catch ( Exception e )
    {
      // TODO: better logging of errors
      System.err.println( "Error sending message to: " + 
                          theNotice.getPatron().getEmail() );
      e.printStackTrace();
      //System.err.println( theNotice.generateNotice() );
    }
  }
}

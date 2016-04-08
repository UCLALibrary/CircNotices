package edu.ucla.library.libservices.voyager.notices.main;

import edu.ucla.library.digital.dlcs.util.StringUtil;

import edu.ucla.library.libservices.voyager.notices.objects.beans.Item;
import edu.ucla.library.libservices.voyager.notices.objects.db.NoticeLogger;
import edu.ucla.library.libservices.voyager.notices.objects.mail.NoticeMailer;
import edu.ucla.library.libservices.voyager.notices.objects.notices.CancellationNotice;
import edu.ucla.library.libservices.voyager.notices.objects.notices.CircNotice;
import edu.ucla.library.libservices.voyager.notices.objects.notices.CourtesyDueNotice;
import edu.ucla.library.libservices.voyager.notices.objects.notices.ItemAvailableNotice;
import edu.ucla.library.libservices.voyager.notices.objects.notices.OverdueNotice;
import edu.ucla.library.libservices.voyager.notices.objects.notices.RecallNotice;
import edu.ucla.library.libservices.voyager.notices.objects.notices.RecallOverdueNotice;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import java.util.Iterator;
import java.util.Properties;
import java.util.TreeMap;

public class NoticeGenerator
{
  private static BufferedReader reader;
  private static Properties props;
  private static String inputFilename;
  //= "C:\\unzipped\\crcnotes\\crcnotes.srloan.inp";
  private static String printLocation = null;
  private static String propsFilename;
  //= "C:\\Documents and Settings\\drickard1967\\My Documents\\projects\\circ\\notices.properties";
  private static TreeMap<Integer, CircNotice> cancels;
  private static TreeMap<Integer, CircNotice> renews;
  private static TreeMap<Integer, CircNotice> availables;
  private static TreeMap<Integer, CircNotice> overdues;
  private static TreeMap<Integer, CircNotice> recalls;
  private static TreeMap<Integer, CircNotice> recallOverdues;

  public NoticeGenerator()
  {
  }

  public static void main( String[] args )
  {
    switch ( args.length )
    {
      case 2:
        inputFilename = args[ 0 ];
        propsFilename = args[ 1 ];
        break;
      default:
        System.err.println( "Usage: NoticeGenerator inputfilename propsfilename" );
        System.exit( 1 );
    }

    initializeMaps();
    loadProperties();

    openFile();
    setPrintLocation();
    readData();

    sendNotices( cancels, "Cancellation" );
    sendNotices( renews, "Renewal" );
    sendNotices( availables, "Item Available" );
    sendNotices( overdues, "Overdue" );
    sendNotices( recalls, "Recall" );
    sendNotices( recallOverdues, "Recall Overdue" );

    logNotices();
  }

  private static void readData()
  {
    String input;
    input = null;
    try
    {
      while ( ( input = reader.readLine() ) != null )
      {
        String[] tokens;
        CircNotice theNotice;

        tokens = input.split( "\\|" );

        if ( tokens[ 0 ].equals( "05" ) || tokens[ 0 ].equals( "06" ) )
        {
          // log as a skip
          System.out.println( "skipping a fee/fine notice: " + input );
        }
        else if ( tokens[ 2 ].trim().equals( "" ) || tokens[ 2 ] == null ||
                  tokens[ 2 ].trim().length() == 0 )
        {
          //log as a skip
          System.out.println( "skipping a notice without email: " +
                              input );
        }
        else if ( ! StringUtil.isValidEmailAddress( tokens[ 2 ].trim() ) )
        {
		  //log as a skip
          System.out.println( "skipping a notice with invalid email address: " +
                              input );
		}
        else
        {
          Integer patronId;

          patronId = Integer.valueOf( tokens[ 3 ] );
          switch ( new Integer( tokens[ 0 ] ).intValue() )
          {
            case 0:
              if ( cancels.containsKey( patronId ) )
              {
                theNotice = cancels.get( patronId );
              }
              else
              {
                theNotice =
                    new CancellationNotice( tokens, props, printLocation );
                cancels.put( patronId, theNotice );
              }
              break;
            case 1:
              if ( availables.containsKey( patronId ) )
              {
                theNotice = availables.get( patronId );
              }
              else
              {
                theNotice =
                    new ItemAvailableNotice( tokens, props, printLocation );
                availables.put( patronId, theNotice );
              }
              break;
            case 2:
              if ( overdues.containsKey( patronId ) )
              {
                theNotice = overdues.get( patronId );
              }
              else
              {
                theNotice =
                    new OverdueNotice( tokens, props, printLocation );
                overdues.put( patronId, theNotice );
              }
              break;
            case 3:
              if ( recalls.containsKey( patronId ) )
              {
                theNotice = recalls.get( patronId );
              }
              else
              {
                theNotice =
                    new RecallNotice( tokens, props, printLocation );
                recalls.put( patronId, theNotice );
              }
              break;
            case 4:
              if ( recallOverdues.containsKey( patronId ) )
              {
                theNotice = recallOverdues.get( patronId );
              }
              else
              {
                theNotice =
                    new RecallOverdueNotice( tokens, props, printLocation );
                recallOverdues.put( patronId, theNotice );
              }
              break;
            case 7:
              if ( renews.containsKey( patronId ) )
              {
                theNotice = renews.get( patronId );
              }
              else
              {
                theNotice =
                    new CourtesyDueNotice( tokens, props, printLocation );
                renews.put( patronId, theNotice );
              }
              break;
            default:
              theNotice =
                  new CourtesyDueNotice( tokens, props, printLocation );
          }
          theNotice.addItem( new Item( tokens ) );
        }
      }
    }
    catch ( IOException ioe )
    {
      System.err.println( "IO Error: " + ioe );
    }
  }

  private static void sendNotices( TreeMap<Integer, CircNotice> notices,
                                   String type )
  {
    Iterator iter;
    NoticeMailer theMailer;
    int noticeCount;

    noticeCount = 0;
    theMailer = new NoticeMailer( props );
    iter = notices.keySet().iterator();
    while ( iter.hasNext() )
    {
      theMailer.sendNotice( notices.get( iter.next() ) );
      noticeCount++;
    }
    //System.out.println( type + " Notices sent: " + noticeCount );
  }

  private static void loadProperties()
  {
    props = new Properties();
    try
    {
      props.load( new FileInputStream( propsFilename ) );
    }
    catch ( IOException ioe )
    {
      System.err.println( "Unable to open properties file: " +
                          propsFilename );
      ioe.printStackTrace();
      System.exit( 1 );
    }
  }

  private static void initializeMaps()
  {
    cancels = new TreeMap<Integer, CircNotice>();
    renews = new TreeMap<Integer, CircNotice>();
    availables = new TreeMap<Integer, CircNotice>();
    overdues = new TreeMap<Integer, CircNotice>();
    recalls = new TreeMap<Integer, CircNotice>();
    recallOverdues = new TreeMap<Integer, CircNotice>();
  }

  private static void openFile()
  {
    try
    {
      reader = new BufferedReader( new FileReader( inputFilename ) );
    }
    catch ( IOException ioe )
    {
      System.err.println( "IO Error: " + ioe );
      System.exit( 1 );
    }
  }

  private static void setPrintLocation()
  {
    // input filenames look like this: crcnotes.yrloan.inp
    // the second element (yrloan, arloan, biloan, etc.) is needed to determine which library address to use
    try
    {
      printLocation = inputFilename.split( "\\." )[ 1 ];
    }
    catch ( ArrayIndexOutOfBoundsException e )
    {
      System.err.println( "Error parsing filename - cannot determine print location - using default 'yrloan'" );
      printLocation = "yrloan";
    }
  }

  private static void logNotices()
  {
    NoticeLogger logger;

    logger = new NoticeLogger( propsFilename );

    logger.setLib_name( printLocation );
    logger.setCancellations( cancels.size() );
    logger.setCourtesydues( renews.size() );
    logger.setItemavailables( availables.size() );
    logger.setOverdues( overdues.size() );
    logger.setRecalloverdues( recallOverdues.size() );
    logger.setRecalls( recalls.size() );

    logger.logNotices();
  }
}

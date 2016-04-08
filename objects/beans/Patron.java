package edu.ucla.library.libservices.voyager.notices.objects.beans;

public class Patron
{
  private String email = null;
  private String patronId = null;
  private String lastName = null;
  private String firstName = null;

  public Patron()
  {
  }

  public Patron( String[] tokens )
  {
    email = tokens[ 2 ].trim();
    patronId = tokens[ 3 ].trim();
    lastName = tokens[ 4 ].trim();
    firstName = tokens[ 5 ].trim();
    cleanEmail();
  }

  private void cleanEmail()
  {
    // cleans up some common errors
    String newEmail = email.replaceAll( " ", "" );
    // more?

    if ( !newEmail.equalsIgnoreCase( email ) )
    {
      System.out.println( "Check email address for patron " + patronId + 
                          " : current email " + email + " corrected to " + 
                          newEmail );
    }
    email = newEmail;
  }

  public String getEmail()
  {
    return email;
  }

  public String getPatronId()
  {
    return patronId;
  }

  public String getLastName()
  {
    return lastName;
  }

  public String getFirstName()
  {
    return firstName;
  }

}

package edu.ucla.library.libservices.voyager.notices.objects.beans;

public class Library
{
  private String institutionName = null;
  private String libraryName = null;
  private String libraryEmail = null;
  private String libraryPhone = null;

  public Library()
  {
  }

  public Library( String[] tokens, String email )
  {
    institutionName = tokens[ 18 ].trim();
    libraryName = tokens[ 19 ].trim();
    libraryPhone = tokens[ 27 ].trim();
    libraryEmail = email;
  }

  public String getInstitutionName()
  {
    return institutionName;
  }

  public String getLibraryName()
  {
    return libraryName;
  }

  public String getLibraryEmail()
  {
    return libraryEmail;
  }

  public String getLibraryPhone()
  {
    return libraryPhone;
  }
}

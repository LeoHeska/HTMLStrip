There are two ways to use HTMLStrip:  as-is, or custom.

* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*
* Using HTMLStrip as-is, without customization
*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

HTMLStrip version 1.0 is a Java command-line program.
If you don't already have Java installed on your system,
you will need to install at least the Java Runtime Environment,
or JRE.  This is freely available; you may start at

   http://java.sun.com
   
or, as of this writing (March 20, 2006), the JRE is available at

   http://javashoplm.sun.com/ECom/docs/Welcome.jsp?StoreId=22&PartDetailId=jre-1.5.0_06-oth-JPR

Installing Java is beyond the scope of this document.

Once you have Java installed, all you need to do to run HTMLStrip is type the following at the command prompt:

   java -cp HTMLStrip.jar HTMLStrip [InFileName] [OutFileName]
   
for example:

   java -cp HTMLStrip.jar HTMLStrip c:\ToDo\CancunRates.html c:\travel\brochures\CancunRates.html
   
   
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
*
* Customizing the "ReplacementExpressions" file
*
* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

The format of the file is simple:

{expression to search for}{tab character (ASCII 9)}{replacement expression}

If you look at the replacment expression file distributed with HTMLStrip
(named RegularExpressions.lst), you may notice that the first non-comment line is:

<!DOCTYPE[^>]+>	

and if you examine that line carefully, you'll notice that the last character
of the line is a tab character.  That means that HTMLStrip will replace all
strings matching the regular expression, with nothing.

A little farther down in the file, you may notice

</p>	<br><br>

which simply tells HTMLStrip "find all the </p> tags and replace 'em with
two <br> tags".  The "searched-for" expression and the "replacement expression"
are of course separated by a tab character.

Though the format of the file is simple, its contents are not.  If you don't
already know how to write (POSIX-style) regular expressions, then to customize
the way HTMLStrip works, you could try getting your regular expressions 
from someone else (not too difficult, actually; there are quite a few sources
of "HTML cleanup" regular expressions on the internet).  You could also learn
to write them, but note that this is NOT a simple topic, and learning to write
regular expressions is not easy.

# Yuu-Tube
## FOP Group Project
### Comments:

Clear Screen function fails

Haven't done 2FA function

Login And Registration uses PostgreSQL.
Problem here is can't connect to database remotely unless in the same local network. (Since we're all all over Malaysia)

Steps to get the program running (because cannot connect to database remotely sia, basically need to install the database in your PC)

1.Download and install postgreSQL(https://www.youtube.com/watch?v=fZQI7nBu32M), pgAdmin4, and postgreSQL JDBC driver(https://www.youtube.com/watch?v=nBZDK8PpR4U&t=226s).

2.Add the postgresql JDBC driver to project library in your IDE

3.Create database name 'yuutubedb', in that database

    create table named 'userdata'
    
    create column named 'useremail', data type is text
    
    create column named 'nameuser', also text
    
    create column named 'password', also text
    
    (https://www.youtube.com/watch?v=fZQI7nBu32M) < to see how to create all above using pgAdmin. It's fairly simple.

No need learn the SQL(because in my code I already handle that). FYI, pgAdmin4 is the GUI for the SQL so no need to use the CLI, just refer to the tutorial from the Youtube link I give.

Other than that, in the connectionDB.java class, make sure change your username and password according to what you put when you first installed postgreSQL. Should be all okay after that.

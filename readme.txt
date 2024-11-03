# REQUIREMENTS :
    - Wildfly 21 
    - JDK >= 19
    - Oracle 11g


Step 1: 
    - Change all apj.properties files to match to your database credentials (gestion-station DB)

Step 2: 
    - modify PATH in .bat files
        + start.bat (galana-ejb and galana-war)
    - modify PATH in _config.conf file (galana-ejb and galana-war)
        + change PATH to match to your directory PATH folder

Step 3: 
    - modify Database.java class
        + modify this class to match to your station-perso DB credentials

Step 4:
    - modify .xml files
        + build.xml from /gestion-station/build.xml
        + send.xml from /gestion-station/send.xml
            + modify PATH 

Step 5: (Optionnal, if you want to test .NET boutique project)
    - modify Wildfly PORT in .NET Views and Controllers
        + located in \boutique\Views and \boutique\Controllers 
        + IF your Wildfly PORT is not 8080
            + Change 8080 to your Wildfly PORT value
            + IF you are using Vs Code
                + click : CTRL + SHIFT + F
                    + find :`locahost:8080`
                    + replace by: `locahost:YOUR_WILDFLY_PORT`  

Step 6: (APP MOBILE WITH REACT-NATIVE)
    - modify conf.ts file 
        + located in /lubrifiant/config/conf.ts
        + check your current IPV4 address by:
            + opening a cmd
            + enter the command ipconfig
            + find IPV4
            + copy it
            + then, past it in the variable `const IP = "PAST_IT_HERE"`
        + change your Wildfly PORT if it is not 8080 
            + in this line: API_BASE_URL = `http://${IP}:8080/${backendProjectName}/api`;   

Step 7: (DATABASE)
    - DATABASE CREATION ORDER: 
        1 - create all of the tables in:
            + /galana-war/sql/table.sql
        2 - create all the sequences in:
            + /galana-war/sql/sequence.sql
        3 - create the function in:
            + /galana-war/sql/function.sql
        4 - create all the view in:
            + /galana-war/sql/view.sql
        5 - insert all the data from : /galana-war/sql/table.sql
            + if there is an error inserting the data
                + error like : "parent constraint key ..."
                    + verify the FK that you have inserted 

Step 8: (Wildfly CONFIGURATION)
    - find the file standalone.xml in you Wildfly folder 
        + example:
            + D:\Application\wildfly-26.1.2.Final\standalone\configuration\standalone.xml
        + open this file
        + in this file: 
            + use CTRL + F (if you are using VS Code)
            + search this line and then change this line with this: 
                <interfaces>
                    <interface name="management">
                        <inet-address value="${jboss.bind.address.management:0.0.0.0}"/>
                    </interface>
                    <interface name="public">
                        <inet-address value="${jboss.bind.address:0.0.0.0}"/>
                    </interface>
                </interfaces>

Step 9:
    - COMPILATION ORDER:
        1 - In gestion-station
            + ant -f build.xml
            + ant -f send.xml

        2 - In galana-ejb
            + _run.bat
        
        3 - In galana-war
            + _deploy.bat

        4 - Start Wildfly
            + start.bat
        
        5 - If you want to test .NET boutique
            + cd /boutique
            + dotnet run

        6 - If you want to test REACT-NATIVE Mobile APP    
            + cd /lubrifiant
            + npx expo start
                + QR Code will appear 
            + Download the Expo App in your Mobile
                + playstore
                + AppStore
            + Open the App
            + Scan the QR Code with the App
            + Wait for the app bundling to be finished

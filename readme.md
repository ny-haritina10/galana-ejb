# Project Setup Guide

## Requirements
- Wildfly 21
- JDK >= 19
- Oracle 11g

## Steps

### Step 1: Update Database Credentials
- Update all `apj.properties` files to match your database credentials for the `gestion-station` database.

### Step 2: Update Paths
- Modify the `PATH` in `.bat` files:
    - `start.bat` in `galana-ejb` and `galana-war`
- Modify the `PATH` in `_config.conf` files in `galana-ejb` and `galana-war`:
    - Change the `PATH` to match your directory path.

### Step 3: Update Database Configuration
- In the `Database.java` class, update the credentials to match your `station-perso` database configuration.

### Step 4: Update XML Files
- Modify XML files:
    - `build.xml` located in `/gestion-station/build.xml`
    - `send.xml` located in `/gestion-station/send.xml`
        - Update the `PATH` as needed.

### Step 5: Configure Wildfly Port for .NET Project (Optional)
- If testing the .NET `boutique` project:
    - Update the Wildfly port in the .NET Views and Controllers located in `\boutique\Views` and `\boutique\Controllers`.
    - If your Wildfly port is not 8080:
        - Change all instances of `8080` to your Wildfly port value.
        - If using VS Code, press `CTRL + SHIFT + F` to:
            - Find `localhost:8080`
            - Replace with `localhost:YOUR_WILDFLY_PORT`

### Step 6: Configure Mobile App with React Native
- Update the `conf.ts` file located in `/lubrifiant/config/conf.ts`:
    - Check your current IPv4 address:
        - Open a command prompt and enter `ipconfig`
        - Copy your IPv4 address and paste it into `const IP = "PASTE_IT_HERE"`
    - If your Wildfly port is not 8080, change the port in this line:
        ```javascript
        API_BASE_URL = `http://${IP}:8080/${backendProjectName}/api`;
        ```

### Step 7: Database Setup
- **Database Creation Order**:
    1. Create all tables from `/galana-war/sql/table.sql`
    2. Create all sequences from `/galana-war/sql/sequence.sql`
    3. Create functions from `/galana-war/sql/function.sql`
    4. Create views from `/galana-war/sql/view.sql`
    5. Insert data from `/galana-war/sql/table.sql`:
        - If errors like "parent constraint key" occur, verify the FK.

### Step 8: Configure Wildfly
- Locate `standalone.xml` in your Wildfly folder:
    - Example: `D:\Application\wildfly-26.1.2.Final\standalone\configuration\standalone.xml`
    - Open the file and update the `<interfaces>` section as follows:
      ```xml
      <interfaces>
          <interface name="management">
              <inet-address value="${jboss.bind.address.management:0.0.0.0}"/>
          </interface>
          <interface name="public">
              <inet-address value="${jboss.bind.address:0.0.0.0}"/>
          </interface>
      </interfaces>
      ```

### Step 9: Compilation and Running
1. **In gestion-station**:
   - Run `ant -f build.xml`
   - Run `ant -f send.xml`
2. **In galana-ejb**:
   - Run `_run.bat`
3. **In galana-war**:
   - Run `_deploy.bat`
4. **Start Wildfly**:
   - Run `start.bat`
5. **To test the .NET boutique project**:
   - Navigate to `/boutique`
   - Run `dotnet run`
6. **To test the React Native Mobile App**:
   - Navigate to `/lubrifiant`
   - Run `npx expo start`:
       - A QR code will appear
   - Download the Expo App:
       - Available on Play Store or App Store
   - Open the Expo App and scan the QR code
   - Wait for the app bundling to complete
Twasi-Currency is the official currency API for Twasi that allows streamers to create their own chat currency, and developers to use it in their own plugins as a dependency.
This way it is not necessary to build a whole currency system into every single plugin because every plugin can use the same system and database at once.

**Integration**

To get started with Twasi-Currency you first need to add the currency plugin to your plugin's classpath as a library.
Just add the Twasi-Artifactory to your pom.xml file and then add the latest version of my plugin as dependency:
```
    <repositories>
         <repository>
             <snapshots>
                 <enabled>false</enabled>
             </snapshots>
             <id>central</id>
             <name>libs-release</name>
             <url>https://artifactory.twasi.net/artifactory/libs-release</url>
         </repository>
         <repository>
             <snapshots />
             <id>snapshots</id>
             <name>libs-snapshot</name>
             <url>https://artifactory.twasi.net/artifactory/libs-snapshot</url>
         </repository>
     </repositories>
     <dependencies>
         <dependency>
             <groupId>de.merlinw.twasi.currency</groupId>
             <artifactId>Twasi-Currency-Plugin</artifactId>
             <version></version>
         </dependency>
     </dependencies>
```

Don't forget to enter the latest version number into the version tags!
You can find all version's [here](https://artifactory.twasi.net/artifactory/webapp/#/artifacts/browse/tree/General/libs-snapshot-local/de/merlinw/twasi/currency/Twasi-Currency-Plugin/1.0-SNAPSHOT) in the official Twasi Artifactory.

If you don't want to use maven you could download the latest version's jar file from the artifactory instead.

**How the currency system works**

Every streamer that installs the currency plugin creates it's own "bank" that stores all the base informations about the streamer's currency such as the name of the currency (what is "Punkte" by default) or the command that is used to query or manipulate account values.

Every chatter has it's own "bank account" that stores the individual account value for each bank (or streamer).

To work with these banks and bank accounts you need to get the CurrencyService using the core's ServiceRegistry:

```CurrencyService currencyService = ServiceRegistry.get(CurrencyService.class);```

This class is your interface to query or change account values for every account of every bank.
Every function of this class is very self-explanatory:

![functions of currencyService](https://i.imgur.com/4P0ojv9.png)

Now you can in-/decrease or set account values easily. Please keep in mind that account values are not allowed to be below zero.
To prevent this the "setBankAccountValue()" void and the "removeFromBankAccount()" void throw an Exception (NegativeBankAccountValueException) when you try to set any account value to a value below zero.

Another exception (TransactionIsNotPositiveException) is thrown when you try to call addToBankAccount() or removeFromBankAccount() with a negative point value.
This intends to prevent confusion about account transactions because this way you can't add or remove a negative value to/from a bank account.

Please catch both of these exceptions to prevent any issues.

**Testing**

Every plugin should be tested on a local Twasi-Core instance before we decide to add it to the official plugin store. If your plugin uses the Twasi-Currency API don't forget to put the plugin's jar file that is used as library too into your local core's plugin folder.

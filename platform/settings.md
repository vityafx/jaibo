# JAIBO platform settings file help

## login settings
JAIBO allows users to modify login settings. It is possible to change nicknames and identities, using *SPACE* as separator.

User may provide any number of nicknames and identities but its size must be equal.
Correct example (3 nicknames and 3 identities):

> login.nicknames = jaibo jaibo` jaibo``

> login.identities = jaibo_i jaibo_i` jaibo_i``

___

## IRC connection settings
### Server settings
Provide server list using *SPACE* as separator and port to connect. User can set any number of servers but only one port.

Correct example (3 servers and 1 port):
> ircconnection.host = irc.quakenet.org dreamhack.se.quakenet.org portlane.se.quakenet.org

> ircconnection.port = 6667

### Authentication
Provide your authentication settings such as quakenet username and password to login using *Q bot* in *authentication* section.

Before using authentication set *authentication.authentication* to *yes*. Correct example (username='username123' and password='password123'):

> authentication.authentication = no

> authentication.username = username123

> authentication.password = password123

It is possible to set hidden host to hide your personal host name or ip address by setting *ircconnection.hide_host* to *yes*:

> ircconnection.hide_host = yes

___

## Bot settings

### Channels

Specify channels to make bot join them and work on them by using *aibo.channels* variable.

It is possible to set any number of channels. Split channel names by *SPACE*. Provide *#* before channel name. Correct example:

> aibo.channels = #ircbottest

### Database

If you need extensions or options which are uses database you have to provide connection settings and credentials.

For *sqlite*:

 1. Set database provider name (sqlite) in *aibo.database.provider* field.
 2. Set database file name in *aibo.database.name* field.

 Correct example:

> aibo.database.provider = sqlite

> aibo.database.name = aibo.db

For *mysql*:

 1. Set database provider name (mysql) in *aibo.database.provider* field.
 2. Set database name in *aibo.database.name* field.
 3. Set database host, port, user name and user password in appropriate fields.

 Correct example(host='localhost', port='3306', user name='aibo_user', user password='aibo_password'):

> aibo.database.provider = mysql

> aibo.database.name = aibo

> aibo.database.host = localhost:3306

> aibo.database.username = aibo_user

> aibo.database.password = aibo_password

### Extensions
Provide list of extensions to load in *aibo.extensions* field or leave this field blank. Use *SPACE* as separator.

Note that bot administrator can load and unload extensions dynamically by sending appropriate commands to the bot using data server or irc chat.

Correct example (3 extensions):

> aibo.extensions = games.pickupbot games.livestreams other.advertisement

### Whois information
A lot of irc users often requests *whois* about another irc users. User can change whois information of the bot by changing *aibo.whois_information* field.

Any data can be set in this field, it always interprets as simple string.

Correct example:

> aibo.whois_information = my whois information

### Root admin
However bot administrators can be set dynamically by root admin, root admin can be set only in the *aibo.root_admin_host* field.

To set root admin you need to set his hostname.

Correct example:

> aibo.root_admin_host = jaibo.users.quakenet.org

***Notes:***

 1. Root admin can't be deleted from anywhere.
 2. Root admin can do everything with the bot.

Without root administrator **it is not possible** to control the bot if he did not set another administrators earlier.

### Reconnect attempts
The bot has a number of maximum attempts to connect to the irc server.
When it is not possible to connect bot will perform few attempts to reconnect and after it will try to connect to another(next) irc server.

User can set this number of attempts by specifying *aibo.max_reconnect_attempts* field.

Correct example (maximum reconnect attempts = 3):

> aibo.max_reconnect_attempts = 3

### Channel topic
The bot can be used on any channels. If user of the bot is trust the community of channels where bot is using it is possible to allow any user of these channels to modify the topic.

To use that set *aibo.topic_for_everyone* field to *yes*:

> aibo.topic_for_everyone = yes

### Changing bot contact information
If the source code been modified or this bot was forked with minor changes and distributing as another application it is possible to change contact information such as:

 * help page
 * about page
 * bugs page

Here is default configuration:

> aibo.helppage = http://bb.jaibo.org/jaibo

> aibo.about = JAIBO v0.4 [http://bb.jaibo.org/jaibo/downloads]

> aibo.bugs = http://bug.jaibo.org:8080 and http://goo.gl/O82tnR

### Network interface
By default the bot uses default gateway and route to connect. To set custom interface set *network.custom_interface* to *yes* and specify interface name in *network.interface* field.

Correct example:

> network.custom_interface = yes

> network.interface = wlan0

### Network connection timeout
Specify network connection timeout by setting *network.connection_timeout* field with a number in seconds:

> network.connection_timeout = 10

### Debug information
Some debug information needs to have set *network.debug* flag to *yes*:

> network.debug = yes

___

## Data server
To have an opportunity to retrieve data from the bot and control it remotely (by web-access) user can turn on *data server* by setting *data_server* field to *yes* and port which data server will listen by setting *data_server.listen_port* field.
Also user need to set ip address (network interface) by setting *data_server.listen_ip* field:

> data_server = yes

> data_server.listen_port = 65500

> data_server.listen_ip = 127.0.0.1

___
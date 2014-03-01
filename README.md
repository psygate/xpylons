# XPylons
=======
## Description
XPylons is a plugin that enables player to build certain structures that will "leech" xp from growing plants and store those xp in the pylon.

=======
## Commands
### Players

**xpylonsshowxp**: Allows users to see the xp stored in a pylon.
* *aliases*: xpyxp
* *default permission*: pylons.xp.show
* *default enabled for player*: true

**xpylonsretrievexp**: Allows users to retrieve xp from a pylon.
* *aliases*: xpyrxp
* *default permission*: pylons.xp.retrieve
* *default enabled for player*: true

**xpylonsstore**: Allows users to store their xp inside a pylon.
* *aliases*: xpsxp
* *default permission*: pylons.xp.store
* *default enabled for player*: true

### Ops

**xpylonslist**: Allows admins to list all pylons.
* *aliases*: xpyl
* *default permission*: pylons.admin.list
* *command-line compatible*: yes
* *caveats*: none

**xpylonspurge**: Allows admins to clear the complete XPylons database.
* *aliases*: xppurge
* *default permission*: pylons.admin.purge
* *command-line compatible*: yes
* *caveats*: This command must be issued twice to make sure you really want to delete all data. Once without argument, and once with the argument "ALL". If these conditions are not met, the database will not be cleared.

**xpylonstemplate**: Allows admins to set the template for pylons ingame.
* *aliases*: xpst
* *default permission*: pylons.admin.settemplate
* *command-line compatible*: no
* *caveats*: Templates are set in a way, that the block the admin is currently looking at will become the new base for a new pylon. (aka, players must hit this block to activate the pylon). The template must have 2 blocks of air around it on all sides to be parsed correctly. Maximum size of a template is 100 blocks currently.

**xpylonssetxp**: Allows admins to alter the xp stored in a pylon ingame.
* *aliases*: xpset
* *default permission*: pylons.admin.setxp
* *command-line compatible*: yes (Coordiantes of the pylon must be specified if issued from a command line.)
* *caveats*: Simply setting the parameter without prefix will cause the command to set the xp. A "+" prefix (/xpylonssetxp +100) will ADD those xp to the pylons. A "-" prefix (/xpylonssetxp -100) will take those xp from the pylon. There is no limit on how much an admin can remove, pylon xp can become negative. In this state, a user cannot retrieve any xp from the pylon, until the pylon has >= 0 xp again.

===========

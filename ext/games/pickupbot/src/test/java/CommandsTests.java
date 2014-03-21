import games.pickupbot.ExtensionObject;
import games.pickupbot.Game;
import org.jaibo.api.tests.TestExtensionMessenger;
import org.jaibo.api.IrcEvent;
import org.jaibo.api.IrcMessage;
import junit.framework.TestCase;

/**
 * games.pickupbot.commands test class
 * Copyright (C) 2014  Victor Polevoy (vityatheboss@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

public final class CommandsTests extends TestCase {
    private final TestExtensionMessenger messenger = new TestExtensionMessenger();

    private final ExtensionObject extensionObject = new ExtensionObject();

    private String[] games = {
            "ctf/8",
            "tdm2v2/4",
            "test/2",
            "test2/4"
    };

    private String testGameAccount1 = "_test_aibo_game_account_11_";
    private String testGameAccount2 = "_test_aibo_game_account_22_";

    private String testIam1 =
            String.format(":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!iam %s",
                    this.testGameAccount1);

    private String testIam2 =
            String.format(":testNickName2!testUser2@test2.users.quakenet.org PRIVMSG #test-channel :!iam %s",
                    this.testGameAccount2);

    private String testAdd1Message =
    ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!add test";

    private String testAdd2Message = ":testNickName2!testUser2@test2.users.quakenet.org PRIVMSG #test-channel :!add test";
    private String testAdd3Message = ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!a";

    private String testWrongAddMessage =    ":testNickName1!testUser1@test1.users.quakenet.org " +
                                            "PRIVMSG #test-channel :!add 5492dsgw53";

    private String testPlayerNickChangeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org NICK :testNickName2";

    private String testPromoteDefaultGameTypeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!p";

    private String testPromoteTestGameTypeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!p test";

    private String testPromoteWrongGameTypeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!p sdafsf3245fgh421";

    private String testRemovePlayerMessageFromDefaultGameType =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!r";

    private String testRemovePlayerMessageFromTestGameType =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!r test";

    private String testWrongRemovePlayerMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!r sdafsf3245fgh421";

    private String testWhoDefaultMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!w";

    private String testWhoTestMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!w";

    private String testWrongWhoMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG #test-channel :!w sdafsf3245fgh421";

    private String testPlayer1KickMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org KICK #test-channel testNickName1 :awesome reason";

    private String testPlayer1NickCaseChangeMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org NICK :TeStNIcKNAME1";

    private String testNotAdminResetMessage =
            ":testNickName1!testUser1@test1.users.quakenet.org PRIVMSG fgd :!reset";

    private String testAdminResetMessage =
            String.format(":testNickName1!testUser1@%s PRIVMSG fgd :!reset",
                    this.extensionObject.getRootAdmin());


    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.extensionObject.clearGames();

        for (String gameType : this.games) {
            Game game = Game.tryParse(gameType);

            this.extensionObject.addGame(game);
        }

        extensionObject.setExtensionMessenger(this.messenger);

        this.extensionObject.processTask(IrcMessage.tryParse(testIam1));
        this.extensionObject.processTask(IrcMessage.tryParse(testIam2));
    }

    public void testPickupBotAddPlayer() {
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd1Message));

        assertEquals(this.messenger.isSetTopicEvent(), true);
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd1Message));

        assertEquals(this.messenger.isSetTopicEvent(), false);

        this.extensionObject.processTask(IrcMessage.tryParse(this.testWrongAddMessage));
        assertEquals(this.messenger.isSendNoticeEvent(), true);
        assertEquals(this.messenger.isSetTopicEvent(), false);
    }

    public void testPickupBotAddToDefaultGameType() {
        this.extensionObject.processTask(IrcMessage.tryParse(this.testAdd3Message));

        assertEquals(this.messenger.isSetTopicEvent(), true);
    }

    public void testPickupBotGatherPickupGame() {
        this.extensionObject.processTask(IrcMessage.tryParse(this.testAdd1Message));
        this.extensionObject.processTask(IrcMessage.tryParse(this.testAdd2Message));

        assertEquals(this.messenger.isSetTopicEvent(), true);
        assertEquals(this.messenger.isSendBroadcastMessageEvent(), true);
        assertEquals(this.messenger.isSendPrivateMessageEvent(), true);
    }

    public void testPromote() {
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd3Message));
        this.messenger.isSendBroadcastMessageEvent();
        this.extensionObject.processTask(IrcMessage.tryParse(this.testPromoteDefaultGameTypeMessage));

        assertEquals(this.messenger.isSendBroadcastMessageEvent(), true);


        this.extensionObject.processTask(IrcMessage.tryParse(testAdd1Message));
        this.messenger.isSendBroadcastMessageEvent();
        this.extensionObject.processTask(IrcMessage.tryParse(this.testPromoteTestGameTypeMessage));

        assertEquals(this.messenger.isSendBroadcastMessageEvent(), true);

        this.extensionObject.processTask(IrcMessage.tryParse(this.testPromoteWrongGameTypeMessage));
        assertEquals(this.messenger.isSendBroadcastMessageEvent(), false);
        assertEquals(this.messenger.isSendNoticeEvent(), true);
    }

    public void testRemovePlayer() {
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd3Message));

        this.messenger.isSetTopicEvent();


        this.extensionObject.processTask(IrcMessage.tryParse(testRemovePlayerMessageFromDefaultGameType));
        assertEquals(this.messenger.isSetTopicEvent(), true);

        this.extensionObject.processTask(IrcMessage.tryParse(testRemovePlayerMessageFromDefaultGameType));
        assertEquals(this.messenger.isSetTopicEvent(), false);
    }

    public void testGetPlayerList() {
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd1Message));
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd3Message));

        assertEquals(this.messenger.isSetTopicEvent(), true);

        this.extensionObject.processTask(IrcMessage.tryParse(testWhoDefaultMessage));
        assertEquals(this.messenger.isSendBroadcastMessageEvent(), true);

        this.extensionObject.processTask(IrcMessage.tryParse(testWhoTestMessage));
        assertEquals(this.messenger.isSendBroadcastMessageEvent(), true);

        this.extensionObject.processTask(IrcMessage.tryParse(testWrongWhoMessage));
        assertEquals(this.messenger.isSendBroadcastMessageEvent(), false);
        assertEquals(this.messenger.isSendNoticeEvent(), true);
    }

    public void testPlayerRemoveOnKickPartQuit() {
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd1Message));
        assertEquals(this.messenger.isSetTopicEvent(), true);

        this.extensionObject.processTask(IrcEvent.tryParse(testPlayer1KickMessage));
        assertEquals(this.messenger.isSetTopicEvent(), true);
    }

    public void testPlayerNickChange() {
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd3Message));
        assertEquals(this.messenger.isSetTopicEvent(), true);

        this.extensionObject.processTask(IrcEvent.tryParse(testPlayerNickChangeMessage));
        assertEquals(this.messenger.isSetTopicEvent(), false);
    }

    public void testPlayerCaseNickChange() {
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd3Message));
        assertEquals(this.messenger.isSetTopicEvent(), true);

        this.extensionObject.processTask(IrcEvent.tryParse(testPlayer1NickCaseChangeMessage));
        assertEquals(this.messenger.isSetTopicEvent(), false);
    }

    public void testPickupBotReset() {
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd3Message));
        this.extensionObject.processTask(IrcMessage.tryParse(testAdd1Message));

        this.extensionObject.processTask(IrcMessage.tryParse(testNotAdminResetMessage));
        assertFalse(this.extensionObject.getPlayers(null, false).length() == 0);
        this.extensionObject.processTask(IrcMessage.tryParse(testAdminResetMessage));
        assertEquals(this.extensionObject.getPlayers(null, false).length(), 0);
        assertEquals(this.extensionObject.getPlayers(Game.tryParse(this.games[2]).getGameType(), false).length(), 0);
    }
}

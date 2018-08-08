/**
 * Copyright (C) 2010 Cubeia Ltd <info@cubeia.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.cubeia.firebase.poker.pokerbots.server.translation;
/**
 * Maps AI-Implementation codes to actual classes.
 * TODO: Improve this, it is ugly and hard-encoded.
 * 
 * Deprecated - Use the fully qualified java class in the html directly instead
 */
@Deprecated
public class AIImplementations {

    static final String[] implementations = new String[]{
            "com.game.bot.ai.impl.RandomAI", // <--- We can replace this AI, but keep indexes intact ffs!
            "com.game.bot.ai.impl.lobby.LobbyAI",
            "com.game.bot.ai.impl.testgame.TestGameBot",
            "com.game.bot.ai.impl.tank.TankBot",
            "com.cubeia.games.mahjong.bot.MahjongBot",
            "com.game.bot.ai.impl.probe.ProbeBot",
            "com.game.bot.ai.impl.dummybot.DummyBot",
            "com.game.bot.ai.MttAI",
            "com.cubeia.game.bingo.bot.BingoBot",
            "se.jadestone.dicearena.game.stm.bot.ShootTheMoonsTableBot",
            "com.cubeia.game.poker.bot.PokerBot",
            "com.cubeia.game.poker.bot.TournamentPokerBot",
            "se.jadestone.dicearena.game.stm.bot.ShootTheMoonsTournamentBot"
    };
    
    
    public static String getAI(int code){
        return implementations[code];
    }

}

package com.cubeia.firebase.api.util;

import java.util.LinkedList;
import java.util.List;

import com.cubeia.firebase.api.game.table.Table;
import com.cubeia.firebase.api.game.table.TableWaiting;

public class TableWaitingUtils 
{
	public static TableWaiting getWaitingPlayer( Table table,
												List<TableWaiting> waitingListLobby)
	{
		//List<TableWaiting> linkedWaitingListLobby = (LinkedList<TableWaiting>)waitingListLobby;
				
		for(TableWaiting waiting : waitingListLobby)
		{
			if(compareTables(table, waiting.getTable()))
			{
				waitingListLobby.remove(waiting);
				return waiting;				
			}
		}
		return null;
	}
	
	private static boolean compareTables (Table sourceTable,
								   Table destinationTable)
	{
		if(sourceTable.getId() == destinationTable.getId() &&
				sourceTable.getMetaData().getGameId() == destinationTable.getMetaData().getGameId() &&
				sourceTable.getMetaData().getMttId() == destinationTable.getMetaData().getMttId())
		{
			return true;
		}
		else
		{
			return false;
		}
				
	}
}

package com.cubeia.firebase.service.pubclientreg;

import static com.cubeia.firebase.api.service.clientregistry.ClientSessionState.CONNECTED;
import static com.cubeia.firebase.api.service.clientregistry.ClientSessionState.NOT_CONNECTED;
import static com.cubeia.firebase.api.service.clientregistry.ClientSessionState.WAIT_REJOIN;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.cubeia.firebase.service.clientreg.ClientRegistry;

public class PublicClientRegistryImplTest {

	private PublicClientRegistryImpl pubCliReg;
	
	@Mock
	private ClientRegistry cliReg;
	
	private int tableId = 100;
	private int playerId = 200;

	@Before
	public void setup() {
		initMocks(this);
		pubCliReg = new PublicClientRegistryImpl();
		pubCliReg.clientRegistry = cliReg;
	}
	
	@Test
	public void testRegisterWatcherToTable() {
		when(cliReg.getClientStatus(playerId)).thenReturn(CONNECTED);
		pubCliReg.registerWatcherToTable(tableId, playerId, false);
		verify(cliReg).addWatchingTable(playerId, tableId);
		verify(cliReg, never()).removeWatchingTable(playerId, tableId);
	}
	
	@Test
	public void testRegisterWatcherToTableWaitingRejoin() {
		when(cliReg.getClientStatus(playerId)).thenReturn(WAIT_REJOIN);
		pubCliReg.registerWatcherToTable(tableId, playerId, false);
		verify(cliReg).addWatchingTable(playerId, tableId);
		verify(cliReg, never()).removeWatchingTable(playerId, tableId);
	}
	
	@Test
	public void testRegisterWatcherToTableWaitingNotConnected() {
		when(cliReg.getClientStatus(playerId)).thenReturn(NOT_CONNECTED);
		pubCliReg.registerWatcherToTable(tableId, playerId, false);
		verify(cliReg, never()).addWatchingTable(playerId, tableId);
		verify(cliReg, never()).removeWatchingTable(playerId, tableId);
	}

	@Test
	public void testRegisterWatcherToTableRemove() {
		when(cliReg.getClientStatus(playerId)).thenReturn(CONNECTED);
		pubCliReg.registerWatcherToTable(tableId, playerId, true);
		verify(cliReg, never()).addWatchingTable(playerId, tableId);
		verify(cliReg).removeWatchingTable(playerId, tableId);
	}
}
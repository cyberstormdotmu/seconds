package com.ishoal.core.repository;

import com.ishoal.core.domain.BuyerProfile;
import com.ishoal.core.domain.User;
import com.ishoal.core.persistence.entity.AddressEntity;
import com.ishoal.core.persistence.entity.BankAccountEntity;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.ContactEntity;
import com.ishoal.core.persistence.entity.OrganisationEntity;
import com.ishoal.core.persistence.entity.UserEntity;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static com.ishoal.core.buyer.BuyerProfileServiceTestData.AN_EDITED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.AN_UNEDITED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.A_COMPLETED_USER_PROFILE;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.ORGANISATION_NAME;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildAUser;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildCompleteProfile;
import static com.ishoal.core.buyer.BuyerProfileServiceTestData.buildIncompleteProfile;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@RunWith(MockitoJUnitRunner.class)
public class BuyerProfileRepositoryTest {
    private static final long USER_ID = 1L;
    private static final long ORGANISATION_ID = 101L;
    private static final long CONTACT_ID = 102L;
    private static final long ADDRESS_ID = 103L;
    private static final long BANK_ACCOUNT_ID = 104L;

    @Mock
    private BuyerProfileEntityRepository buyerProfileEntityRepository;

    private BuyerProfileEntity anUneditedProfileEntity;

    private BuyerProfileEntity anEditedProfileEntity;

    private BuyerProfileEntity aCompletedProfileEntity;

    private BuyerProfileRepository buyerProfileRepository;


    private ArgumentCaptor<BuyerProfileEntity> profileSavedToRepository = ArgumentCaptor.forClass(BuyerProfileEntity
        .class);

    private static final long PROFILE_ID = 1000L;

    @Before
    public void before() {

        anUneditedProfileEntity = buildAnUneditedProfileEntity();
        anEditedProfileEntity = buildAnEditedProfileEntityWithIdsForAttributes();
        aCompletedProfileEntity = buildACompletedProfileEntityWithIdsForAttributes();

        buyerProfileRepository = new BuyerProfileRepository(buyerProfileEntityRepository);

        when(buyerProfileEntityRepository.findByUsername(AN_UNEDITED_USER_PROFILE)).thenReturn(anUneditedProfileEntity);
        when(buyerProfileEntityRepository.findByUsername(AN_EDITED_USER_PROFILE)).thenReturn(anEditedProfileEntity);
        when(buyerProfileEntityRepository.save(profileSavedToRepository.capture())).thenAnswer(returnTheSameBuyerProfileThatWasPassed());
        when(buyerProfileEntityRepository.findByUsername(A_COMPLETED_USER_PROFILE)).thenReturn(aCompletedProfileEntity);
    }

    @Test
    public void shouldSaveBuyerProfileEntity_whenCreateProfileRequestIsMade() {

        buyerProfileRepository.createBuyerProfile(uneditedProfileUser(), buildIncompleteProfile().build());

        assertThat(profileSavedToRepository.getValue().getUser().getUsername(), is(AN_UNEDITED_USER_PROFILE));
        assertThat(profileSavedToRepository.getValue().getOrganisation().getName(), is(notNullValue()));
    }

    @Test
    public void shouldSaveAnyBuyerProfileAttributes_whenCreateProfileRequestIsMade() {

        buyerProfileRepository.createBuyerProfile(editedProfileUser(), buildIncompleteProfile().build());

        assertThat(profileSavedToRepository.getValue().getOrganisation().getName(), is(ORGANISATION_NAME));
    }

    @Test
    public void shouldFetchBuyerProfile_whenReadRequestIsMade() {

        buyerProfileRepository.fetchBuyerProfile(editedProfileUser());

        verify(buyerProfileEntityRepository).findByUsername(AN_EDITED_USER_PROFILE);
    }


    @Test
    public void shouldReturnABuyerProfile_whenReadRequestIsMade() {

        BuyerProfile buyerProfile = buyerProfileRepository.fetchBuyerProfile(editedProfileUser());

        assertThat(buyerProfile.getId(), is(PROFILE_ID));
    }

    @Test
    public void shouldFetchTheProfileCompletionStatus() {

        BuyerProfile buyerProfile = buyerProfileRepository.fetchBuyerProfile(completedProfileUser());

        assertThat(buyerProfile.isCompleted(), is(true));
    }

    @Test
    public void shouldCreateNewBuyerProfileAttributes_whenTheyDoNotYetExist() {

        buyerProfileRepository.updateBuyerProfile(uneditedProfileUser(), buildIncompleteProfile().build());

        assertThat(profileSavedToRepository.getValue().getUser().getId(), is(USER_ID));
        assertThatAttributeIsANewEntity(profileSavedToRepository.getValue().getOrganisation().getId());
        assertThatAttributeIsANewEntity(profileSavedToRepository.getValue().getContact().getId());
    //    assertThatAttributeIsANewEntity(profileSavedToRepository.getValue().getDeliveryAddress().getId());
        assertThatAttributeIsANewEntity(profileSavedToRepository.getValue().getBankAccount().getId());
    }

    @Test
    public void shouldCreateANewProfileWithIncompleteStatusAndReturnSame() {

        BuyerProfile createdProfile = buyerProfileRepository.createBuyerProfile(uneditedProfileUser(),
            buildIncompleteProfile().build());

        assertThat(profileSavedToRepository.getValue().isCompleted(), is(false));
        assertThat(createdProfile.isCompleted(), is(false));
    }

    @Test
    public void shouldUpdateBuyerProfileAttributes_whenTheyAlreadyExist() {

        buyerProfileRepository.updateBuyerProfile(editedProfileUser(), buildIncompleteProfile().build());

        assertThat(profileSavedToRepository.getValue().getUser().getId(), is(USER_ID));
        assertThat(profileSavedToRepository.getValue().getOrganisation().getId(), is(ORGANISATION_ID));
        assertThat(profileSavedToRepository.getValue().getContact().getId(), is(CONTACT_ID));
      //  assertThat(profileSavedToRepository.getValue().getDeliveryAddress().getId(),
      //      is(ADDRESS_ID));
        assertThat(profileSavedToRepository.getValue().getBankAccount().getId(), is(BANK_ACCOUNT_ID));
    }

    @Test
    public void shouldUpdateTheProfileCompletionStatusAndReturnSame() {

        BuyerProfile updatedProfile = buyerProfileRepository.updateBuyerProfile(editedProfileUser(),
            buildIncompleteProfile().build());

        assertThat(profileSavedToRepository.getValue().isCompleted(), is(false));
        assertThat(updatedProfile.isCompleted(), is(false));

        updatedProfile = buyerProfileRepository.updateBuyerProfile(editedProfileUser(),
            buildCompleteProfile().build());

        assertThat(profileSavedToRepository.getValue().isCompleted(), is(true));
        assertThat(updatedProfile.isCompleted(), is(true));
    }

    private Answer<Object> returnTheSameBuyerProfileThatWasPassed() {

        return invocation -> {
            Object[] args = invocation.getArguments();
            if (args.length == 0) {
                return null;
            }
            return args[0];
        };
    }

    private void assertThatAttributeIsANewEntity(Long id) {

        assertThat(id, is(nullValue()));
    }

    private User uneditedProfileUser() {

        return buildAUser().username(AN_UNEDITED_USER_PROFILE).build();
    }

    private User editedProfileUser() {

        return buildAUser().username(AN_EDITED_USER_PROFILE).build();
    }

    private User completedProfileUser() {
        return buildAUser().username(A_COMPLETED_USER_PROFILE).build();
    }

    private BuyerProfileEntity buildAnUneditedProfileEntity() {

        BuyerProfileEntity entity = new BuyerProfileEntity();
        entity.setId(PROFILE_ID);
        entity.setUser(buildUserWhoHasAnUneditedProfile());
        return entity;
    }

    private BuyerProfileEntity buildAnEditedProfileEntityWithIdsForAttributes() {

        BuyerProfileEntity entity = buildAnUneditedProfileEntity();
        entity.setId(1000L);
        entity.setUser(buildUserWhoHasEditedHisProfile());
        addOrganisation(entity);
        addContact(entity);
        addDeliveryAddress(entity);
        addBankDetails(entity);
        return entity;
    }

    private BuyerProfileEntity buildACompletedProfileEntityWithIdsForAttributes() {
        BuyerProfileEntity entity = buildAnEditedProfileEntityWithIdsForAttributes();
        entity.setCompleted(true);
        return entity;
    }

    private void addOrganisation(BuyerProfileEntity entity) {

        OrganisationEntity organisation = new OrganisationEntity();
        organisation.setId(ORGANISATION_ID);
        entity.setOrganisation(organisation);
    }

    private void addContact(BuyerProfileEntity entity) {

        ContactEntity contact = new ContactEntity();
        contact.setId(CONTACT_ID);
        entity.setContact(contact);
    }

    private void addDeliveryAddress(BuyerProfileEntity entity) {

        AddressEntity address = new AddressEntity();
        address.setId(ADDRESS_ID);
    //    entity.setDeliveryAddress(address);
    }

    private void addBankDetails(BuyerProfileEntity entity) {

        BankAccountEntity bankAccount = new BankAccountEntity();
        bankAccount.setId(BANK_ACCOUNT_ID);
        entity.setBankAccount(bankAccount);
    }

    private UserEntity buildUserWhoHasAnUneditedProfile() {

        UserEntity entity = new UserEntity();
        entity.setId(USER_ID);
        entity.setUsername(AN_UNEDITED_USER_PROFILE);
        entity.setPassword("$2a$10$hP4rRoLXqgK3BOcyue8hAu7PorIdEmtGHkqrUqxgO3AkRQ8ChCS/.");
        entity.setForename("Roger");
        entity.setSurname("Watkins");
        //entity.addRo(Collections.emptyList());
        return entity;
    }

    private UserEntity buildUserWhoHasEditedHisProfile() {

        UserEntity entity = buildUserWhoHasAnUneditedProfile();
        entity.setUsername(AN_EDITED_USER_PROFILE);
        return entity;
    }
}
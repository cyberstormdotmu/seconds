package com.ishoal.core.repository;

import java.util.ArrayList;
import java.util.List;

import com.ishoal.common.util.IterableUtils;
import com.ishoal.core.domain.User;
import com.ishoal.core.domain.Users;
import com.ishoal.core.persistence.adapter.UserEntityAdapter;
import com.ishoal.core.persistence.adapter.UserRoleEntityAdapter;
import com.ishoal.core.persistence.entity.BuyerProfileEntity;
import com.ishoal.core.persistence.entity.UserEntity;
import com.ishoal.core.persistence.entity.UserRoleEntity;
import com.ishoal.core.persistence.entity.VendorEntity;
import com.ishoal.core.persistence.repository.BuyerProfileEntityRepository;
import com.ishoal.core.persistence.repository.UserEntityRepository;
import com.ishoal.core.persistence.repository.UserRoleEntityRepository;
import com.ishoal.core.persistence.repository.VendorEntityRepository;

public class UserRepository {
    
    private final UserEntityRepository userEntityRepository;
    private final UserRoleEntityRepository roleEntityRepository;
    private final UserEntityAdapter userAdapter = new UserEntityAdapter();
    private final UserRoleEntityAdapter rolesAdapter = new UserRoleEntityAdapter();
    private final VendorEntityRepository vendorEntityRepository;
    private final BuyerProfileEntityRepository buyerProfileEntityRepository;

    public UserRepository(UserEntityRepository entityRepository, UserRoleEntityRepository roleEntityRepository,
            VendorEntityRepository vendorEntityRepository,BuyerProfileEntityRepository buyerProfileEntityRepository) {
        this.userEntityRepository = entityRepository;
        this.roleEntityRepository = roleEntityRepository;
        this.vendorEntityRepository = vendorEntityRepository;
        this.buyerProfileEntityRepository = buyerProfileEntityRepository;
    }

    public User findByUsernameIgnoreCase(String username) {
        UserEntity entity = userEntityRepository.findByUsernameIgnoreCase(username);
        return userAdapter.adaptWithPassword(entity);
    }

    public User findByRegistrationTokenIgnoreCase(String token) {
        UserEntity entity = userEntityRepository.findByRegistrationTokenIgnoreCase(token);
        		return userAdapter.adapt(entity);
    }
    
    public User save(User user) {
        // capture any roles, then clear the roles from the user so we can save
        // it separately
        // it will not save in hibernate with the roles attached.
        UserEntity savedUserEntity = new UserEntity();
        List<UserRoleEntity> roleEntities = rolesAdapter.adapt(user.getRoles());
        System.out.println(user.getRoles());
        user.clearRoles();
        if(roleEntities.iterator().next().getName().equals("SUPPLIER")){
            BuyerProfileEntity buyerProfile = buyerProfileEntityRepository.findByUsername(user.getUsername());      
            VendorEntity vendorEntity = new VendorEntity();
            vendorEntity.setName(buyerProfile.getOrganisation().getName());
            VendorEntity vendorEntityResponse = vendorEntityRepository.save(vendorEntity);   
            UserEntity userEntity = userAdapter.adaptWithVendor(user,vendorEntityResponse);
            savedUserEntity = userEntityRepository.save(userEntity);
        } 
        if(roleEntities.iterator().next().getName().equals("BUYER")){
            UserEntity userEntity = userAdapter.adaptWithPassword(user);
            savedUserEntity = userEntityRepository.save(userEntity);
        }
        
        // put the roles on the user (to ensure ids are updated) and save them.
        if (!roleEntities.isEmpty()) {
            savedUserEntity.addRoles(roleEntities);
            roleEntityRepository.save(roleEntities);
        }

        // pull out the user again with the roles attached
        UserEntity userWithRoles = userEntityRepository.findOne(savedUserEntity.getId());
        return userAdapter.adaptWithPassword(userWithRoles);
    }    
 
    public void saveConfirmedUser(User user) {
    	UserEntity userEntity = userEntityRepository.findByUsernameIgnoreCase(user.getEmailAddress());
    	userEntity.setRegistrationToken("CONFIRM");
        userEntityRepository.save(userEntity);
    }
 
    
    public void savePendingRegistration(User user) {
    	UserEntity userEntity = userAdapter.adaptWithPassword(user);
        userEntityRepository.save(userEntity);
    }
    public Users findAllUsersWithoutARole() {
        return IterableUtils.mapToCollection(userEntityRepository.findAllWithoutARole(),
                entity -> userAdapter.adapt(entity), users -> Users.over(users));
    }
    
    public Users findUsersByConfirmationStatus(String confirm){
    	return IterableUtils.mapToCollection(userEntityRepository.findUsersByConfirmationStatus(confirm),
                entity -> userAdapter.adapt(entity), users -> Users.over(users));
    }

    public Users findUsersWithPendingAuthentication(){
    	return IterableUtils.mapToCollection(userEntityRepository.findUsersWithPendingAuthentication(),
                entity -> userAdapter.adapt(entity), users -> Users.over(users));
    }

    public List<User> getUserListByCriteria(String forename, String surname, String username) {
        
        List<UserEntity> userEntities = userEntityRepository.getUserListByCriteria(forename, surname, username);
        List<User> userList = new ArrayList<>();
        
        if(userEntities != null && userEntities.size() > 0){
            for(int i = 0; i < userEntities.size(); i++){
                User user = userAdapter.adapt(userEntities.get(i));
                userList.add(user);
            }
        }
        return userList;
    }

	public User saveNewAdmin(User user) {
		UserEntity userEntity = userAdapter.adaptWithPassword(user);
		return userAdapter.adapt(userEntityRepository.save(userEntity));
	}
	
	public User saveAdminRole(User user) {
        // capture any roles, then clear the roles from the user so we can save
        // it separately
        // it will not save in hibernate with the roles attached.
        List<UserRoleEntity> roleEntities = rolesAdapter.adapt(user.getRoles());
        user.clearRoles();

        // adapt and save the user entity without saving its roles
        UserEntity savedUserEntity = userEntityRepository.findByUsernameIgnoreCase(user.getUsername());

        // put the roles on the user (to ensure ids are updated) and save them.
        if (!roleEntities.isEmpty()) {
            savedUserEntity.addRoles(roleEntities);
            roleEntityRepository.save(roleEntities);
        }

        // pull out the user again with the roles attached
        UserEntity userWithRoles = userEntityRepository.findOne(savedUserEntity.getId());
        return userAdapter.adaptWithPassword(userWithRoles);
    }
}
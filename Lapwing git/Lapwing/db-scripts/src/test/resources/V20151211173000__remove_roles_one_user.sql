/* Remove permissions for peter as this is the state a newly registered user will be in before he has been authorised */
DELETE FROM USER_ROLES WHERE user_id = ((SELECT ID FROM USERS WHERE USER_NAME = 'peter@ppb.com'));



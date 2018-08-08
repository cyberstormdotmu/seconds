ALTER TABLE users ADD COLUMN buyer_referral_code character varying(20);
ALTER TABLE users ADD COLUMN applied_referral_code character varying(20);
ALTER TABLE users ADD COLUMN applied_for character varying(20);
ALTER TABLE users ADD COLUMN vendor_id integer;
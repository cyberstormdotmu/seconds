UPDATE offers SET current_volume = 1 WHERE id = (SELECT o.id FROM offers o JOIN products p ON o.product_id = p.id WHERE p.product_code = 'HPLASERJETM527DN');
UPDATE offers SET current_volume = 197 WHERE id = (SELECT o.id FROM offers o JOIN products p ON o.product_id = p.id WHERE p.product_code = 'HPELITE840');


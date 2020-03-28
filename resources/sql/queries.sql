-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
    (first_name, last_name, email, pass)
VALUES (:first_name, :last_name, :email, :pass);

-- :name update-user! :! :n
-- :doc updates an existing user record
UPDATE users
SET first_name = :first_name,
    last_name  = :last_name,
    email      = :email
WHERE id = :id;

-- :name get-user :? :1
-- :doc retrieves a user record given the id
SELECT id,
       first_name,
       last_name,
       email,
       admin,
       last_login,
       is_active,
       pass
FROM users
WHERE id = :id;

-- :name delete-user! :! :n
-- :doc deletes a user record given the id
DELETE
FROM users
WHERE id = :id;

-- :name get-token :? :1
-- :doc retrieves the most recent access token
SELECT id, token_type, token, expiry
FROM td_auth
WHERE token_type = :token_type;

-- :name update-token! :! :n
-- :doc update or insert a token
INSERT INTO td_auth (token_type, token, expiry)
VALUES (:token_type, :token, :expiry)
ON CONFLICT (token_type)
    DO UPDATE SET token  = excluded.token,
                  expiry = excluded.expiry
